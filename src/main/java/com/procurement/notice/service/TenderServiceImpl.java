package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.TenderDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.ocds.*;
import com.procurement.notice.model.tender.dto.*;
import com.procurement.notice.model.tender.ms.MsRelease;
import com.procurement.notice.model.tender.pspq.PsPqRelease;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TenderServiceImpl implements TenderService {

    private static final String SEPARATOR = "-";
    private static final String RELEASE_NOT_FOUND_ERROR = "Release not found by stage: ";
    private static final String AWARD_NOT_FOUND_ERROR = "Award not found by stage: ";
    private static final String BID_NOT_FOUND_ERROR = "Bid not found by stage: ";
    private static final String LOTS_NOT_FOUND_ERROR = "Lots not found by stage: ";
    private final TenderDao tenderDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public TenderServiceImpl(final TenderDao tenderDao,
                             final JsonUtil jsonUtil,
                             final DateUtil dateUtil) {
        this.tenderDao = tenderDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public TenderEntity getTenderEntity(final String cpId,
                                        final String stage,
                                        final PsPqRelease tender) {
        final TenderEntity tenderEntity = new TenderEntity();
        tenderEntity.setCpId(cpId);
        tenderEntity.setStage(stage);
        tenderEntity.setOcId(tender.getOcid());
        tenderEntity.setJsonData(jsonUtil.toJson(tender));
        tenderEntity.setReleaseId(tender.getId());
        tenderEntity.setReleaseDate(dateUtil.localToDate(tender.getDate()));
        return tenderEntity;
    }

    @Override
    public ResponseDto createCn(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final MsRelease ms = jsonUtil.toObject(MsRelease.class, data.toString());
        ms.setOcid(cpid);
        ms.setId(getReleaseId(cpid));
        ms.setTag(Arrays.asList(Tag.COMPILED));
        ms.setInitiationType(InitiationType.TENDER);
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        final PsPqRelease tender = jsonUtil.toObject(PsPqRelease.class, data.toString());
        tender.setOcid(getOcId(cpid, stage));
        tender.setId(getReleaseId(tender.getOcid()));
        tender.setDate(ms.getDate());
        tender.setTag(Arrays.asList(Tag.COMPILED));
        tender.setInitiationType(InitiationType.TENDER);
        tender.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);

        ms.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcess.RelatedProcessType.X_PRESELECTION),
                        RelatedProcess.RelatedProcessScheme.OCID,
                        tender.getOcid(),
                        "")
        );

        tender.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcess.RelatedProcessType.PARENT),
                        RelatedProcess.RelatedProcessScheme.OCID,
                        ms.getOcid(),
                        "")
        );
        tenderDao.saveTender(getMSEntity(ms.getOcid(), ms));
        tenderDao.saveTender(getTenderEntity(ms.getOcid(), stage, tender));
        return getResponseDto(ms.getOcid(), tender.getOcid());
    }

    @Override
    public ResponseDto tenderPeriodEnd(final String cpid, final String stage, final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final TenderPeriodEndDto dto = jsonUtil.toObject(TenderPeriodEndDto.class, data.toString());
        release.setId(getReleaseId(release.getOcid()));
        release.setDate(dto.getAwardPeriod().getStartDate());
        release.setTag(Arrays.asList(Tag.AWARD));
        release.setAwards(new LinkedHashSet<>(dto.getAwards()));
        release.setBids(new Bids(null, dto.getBids()));
        tenderDao.saveTender(getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto suspendTender(final String cpid,
                                     final String stage,
                                     final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final SuspendTenderDto dto = jsonUtil.toObject(SuspendTenderDto.class, jsonUtil.toJson(data));
        release.setDate(dateUtil.getNowUTC());
        release.setId(getReleaseId(release.getOcid()));
        release.getTender().setStatusDetails(dto.getTender().getStatusDetails());
        tenderDao.saveTender(getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto awardByBid(final String cpid,
                                  final String stage,
                                  final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final AwardByBidDto dto = jsonUtil.toObject(AwardByBidDto.class, jsonUtil.toJson(data));
        release.setDate(dateUtil.getNowUTC());
        release.setId(getReleaseId(release.getOcid()));
        updateAward(release, dto.getAward());
        updateBid(release, dto.getBid());
        tenderDao.saveTender(getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto endAwarding(final String cpid, final String stage, final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final EndAwardingDto dto = jsonUtil.toObject(EndAwardingDto.class, jsonUtil.toJson(data));
        release.setDate(dto.getStandstillPeriod().getStartDate());
        release.setId(getReleaseId(release.getOcid()));
        release.getTender().setStandstillPeriod(dto.getStandstillPeriod());
        updateLots(release, dto.getLots());
        tenderDao.saveTender(getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto standstillPeriodEnd(final String cpid, final String stage, final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final StandstillPeriodEndDto dto = jsonUtil.toObject(StandstillPeriodEndDto.class, data.toString());
        release.setId(getReleaseId(release.getOcid()));
        release.setDate(dateUtil.getNowUTC());
        release.getTender().setStatusDetails(TenderStatusDetails.COMPLETE);
        release.setAwards(new LinkedHashSet<>(dto.getAwards()));
        release.setBids(new Bids(null, dto.getBids()));
        tenderDao.saveTender(getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    private String getOcId(final String cpId, final String stage) {
        return cpId + SEPARATOR + stage + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private TenderEntity getMSEntity(final String cpId,
                                     final MsRelease ms) {
        final TenderEntity msEntity = new TenderEntity();
        msEntity.setCpId(cpId);
        msEntity.setStage("ms");
        msEntity.setOcId(ms.getOcid());
        msEntity.setJsonData(jsonUtil.toJson(ms));
        msEntity.setReleaseId(ms.getId());
        msEntity.setReleaseDate(dateUtil.localToDate(ms.getDate()));
        return msEntity;
    }

    private void updateAward(final PsPqRelease release, final Award award) {
        final Set<Award> awards = release.getAwards();
        final Optional<Award> awardOptional = awards.stream()
                .filter(a -> a.getId().equals(award.getId()))
                .findFirst();
        if (awardOptional.isPresent()) {
            final Award updatableAward = awardOptional.get();
            if (Objects.nonNull(award.getDescription()))
                updatableAward.setDescription(award.getDescription());
            if (Objects.nonNull(award.getStatusDetails()))
                updatableAward.setStatusDetails(award.getStatusDetails());
            if (Objects.nonNull(award.getDocuments()))
                updatableAward.setDocuments(award.getDocuments());
            release.setAwards(awards);
        } else {
            throw new ErrorException(AWARD_NOT_FOUND_ERROR);
        }
    }

    private void updateBid(final PsPqRelease release, final Bid bid) {
        final List<Bid> bids = release.getBids().getDetails();
        final Optional<Bid> bidOptional = bids.stream()
                .filter(b -> b.getId().equals(bid.getId()))
                .findFirst();
        if (bidOptional.isPresent()) {
            final Bid updatableBid = bidOptional.get();
            if (Objects.nonNull(bid.getDate()))
                updatableBid.setDate(bid.getDate());
            if (Objects.nonNull(bid.getStatusDetails()))
                updatableBid.setStatusDetails(bid.getStatusDetails());
            release.getBids().setDetails(bids);
        } else {
            throw new ErrorException(BID_NOT_FOUND_ERROR);
        }
    }

    private void updateLots(final PsPqRelease release, final List<Lot> lotsDto) {
        final List<Lot> lots = release.getTender().getLots();
        if (lots.isEmpty()) throw new ErrorException(LOTS_NOT_FOUND_ERROR);
        final Map<String, Lot> updatableLots = new HashMap<>();
        lots.forEach(lot -> updatableLots.put(lot.getId(), lot));
        lotsDto.forEach(lotDto -> updatableLots.get(lotDto.getId()).setStatusDetails(lotDto.getStatusDetails()));
        final List<Lot> updatedLots = updatableLots.values().stream().collect(Collectors.toList());
        release.getTender().setLots(updatedLots);
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
    }

}
