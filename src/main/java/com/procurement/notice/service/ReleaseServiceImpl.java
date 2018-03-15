package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.ReleaseDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.model.ocds.*;
import com.procurement.notice.model.tender.dto.*;
import com.procurement.notice.model.tender.ms.MsRelease;
import com.procurement.notice.model.tender.pspq.PsPqRelease;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private static final String SEPARATOR = "-";
    private static final String RELEASE_NOT_FOUND_ERROR = "Release not found by stage: ";
    private static final String AWARD_NOT_FOUND_ERROR = "Award not found by stage: ";
    private static final String BID_NOT_FOUND_ERROR = "Bid not found by stage: ";
    private static final String LOTS_NOT_FOUND_ERROR = "Lots not found by stage: ";
    private final ReleaseDao releaseDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public ReleaseServiceImpl(final ReleaseDao releaseDao,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.releaseDao = releaseDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ReleaseEntity getReleaseEntity(final String cpId,
                                          final String stage,
                                          final PsPqRelease release) {
        final ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setCpId(cpId);
        releaseEntity.setStage(stage);
        releaseEntity.setOcId(release.getOcid());
        releaseEntity.setJsonData(jsonUtil.toJson(release));
        releaseEntity.setReleaseId(release.getId());
        releaseEntity.setReleaseDate(dateUtil.localToDate(release.getDate()));
        return releaseEntity;
    }

    @Override
    public ResponseDto createCn(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final MsRelease ms = jsonUtil.toObject(MsRelease.class, data.toString());
        ms.setOcid(cpid);
        ms.setId(getReleaseId(cpid));
        ms.setTag(Collections.singletonList(Tag.COMPILED));
        ms.setInitiationType(InitiationType.TENDER);
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, data.toString());
        release.setOcid(getOcId(cpid, stage));
        release.setId(getReleaseId(release.getOcid()));
        release.setDate(ms.getDate());
        release.setTag(Arrays.asList(Tag.COMPILED));
        release.setInitiationType(InitiationType.TENDER);
        release.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);

        addRelatedProcessToMs(ms, release.getOcid(), RelatedProcess.RelatedProcessType.X_PRESELECTION);
        addMsToRelatedProcess(release, ms.getOcid());

        releaseDao.saveTender(getMSEntity(ms.getOcid(), ms));
        releaseDao.saveTender(getReleaseEntity(ms.getOcid(), stage, release));
        return getResponseDto(ms.getOcid(), release.getOcid());
    }

    @Override
    public ResponseDto tenderPeriodEnd(final String cpid, final String stage, final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final TenderPeriodEndDto dto = jsonUtil.toObject(TenderPeriodEndDto.class, data.toString());
        release.setId(getReleaseId(release.getOcid()));
        release.setTag(Collections.singletonList(Tag.AWARD));
        if (Objects.nonNull(dto.getAwardPeriod())) {
            release.getTender().setAwardPeriod(dto.getAwardPeriod());
            release.setDate(dto.getAwardPeriod().getStartDate());
        }
        if (Objects.nonNull(dto.getAwards()) && !dto.getAwards().isEmpty())
            release.setAwards(new LinkedHashSet<>(dto.getAwards()));
        if (Objects.nonNull(dto.getLots()) && !dto.getLots().isEmpty())
            release.getTender().setLots(dto.getLots());
        if (Objects.nonNull(dto.getBids()) && !dto.getBids().isEmpty())
            release.setBids(new Bids(null, dto.getBids()));
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto suspendTender(final String cpid,
                                     final String stage,
                                     final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final SuspendTenderDto dto = jsonUtil.toObject(SuspendTenderDto.class, jsonUtil.toJson(data));
        release.setDate(dateUtil.getNowUTC());
        release.setId(getReleaseId(release.getOcid()));
        release.getTender().setStatusDetails(dto.getTender().getStatusDetails());
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto awardByBid(final String cpid,
                                  final String stage,
                                  final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final AwardByBidDto dto = jsonUtil.toObject(AwardByBidDto.class, jsonUtil.toJson(data));
        release.setTag(Collections.singletonList(Tag.AWARD_UPDATE));
        release.setDate(dto.getAward().getDate());
        release.setId(getReleaseId(release.getOcid()));
        updateAward(release, dto.getAward());
        updateBid(release, dto.getBid());
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto awardPeriodEnd(final String cpid, final String stage, final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final AwardPeriodEndDto dto = jsonUtil.toObject(AwardPeriodEndDto.class, data.toString());
        release.setId(getReleaseId(release.getOcid()));
        release.setDate(dateUtil.getNowUTC());
        release.getTender().setStatusDetails(TenderStatusDetails.COMPLETE);
        if (Objects.nonNull(dto.getAwardPeriod()))
            release.getTender().setAwardPeriod(dto.getAwardPeriod());
        if (Objects.nonNull(dto.getAwards()) && !dto.getAwards().isEmpty())
            release.setAwards(new LinkedHashSet<>(dto.getAwards()));
        if (Objects.nonNull(dto.getLots()) && !dto.getLots().isEmpty())
            release.getTender().setLots(dto.getLots());
        if (Objects.nonNull(dto.getBids()) && !dto.getBids().isEmpty())
            release.setBids(new Bids(null, dto.getBids()));
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto standstillPeriodEnd(final String cpid, final String stage, final JsonNode data) {
        final StandstillPeriodEndDto dto = jsonUtil.toObject(StandstillPeriodEndDto.class, jsonUtil.toJson(data));
        /*MS*/
        final ReleaseEntity msEntity = Optional.ofNullable(releaseDao.getByCpIdAndOcId(cpid, cpid))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final MsRelease ms = jsonUtil.toObject(MsRelease.class, msEntity.getJsonData());
        ms.setDate(dto.getStandstillPeriod().getEndDate());
        ms.setId(getReleaseId(ms.getOcid()));
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTED);
        releaseDao.saveTender(getMSEntity(ms.getOcid(), ms));
        /*PS-PQ*/
        final ReleaseEntity releaseEntity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease tender = jsonUtil.toObject(PsPqRelease.class, releaseEntity.getJsonData());
        tender.setDate(dto.getStandstillPeriod().getEndDate());
        tender.setId(getReleaseId(tender.getOcid()));
        tender.getTender().setStatusDetails(TenderStatusDetails.PRESELECTED);
        tender.getTender().setStandstillPeriod(dto.getStandstillPeriod());
        updateLots(tender, dto.getLots());
        releaseDao.saveTender(getReleaseEntity(tender.getOcid(), stage, tender));
        return getResponseDto(cpid, tender.getOcid());
    }

    @Override
    public ResponseDto startNewStage(final String cpid, final String stage, final String previousStage, final JsonNode data) {
        final StartNewStageDto dto = jsonUtil.toObject(StartNewStageDto.class, jsonUtil.toJson(data));
        final LocalDateTime startDate = dto.getTender().getTenderPeriod().getStartDate();
        /*MS*/
        final ReleaseEntity msEntity = Optional.ofNullable(releaseDao.getByCpIdAndOcId(cpid, cpid))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final MsRelease ms = jsonUtil.toObject(MsRelease.class, msEntity.getJsonData());
        ms.setDate(startDate);
        ms.setId(getReleaseId(ms.getOcid()));
        ms.getTender().setStatusDetails(TenderStatusDetails.PREQUALIFICATION);
        releaseDao.saveTender(getMSEntity(cpid, ms));
        /*PS*/
        final ReleaseEntity releaseEntity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, previousStage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease prevRelease = jsonUtil.toObject(PsPqRelease.class, releaseEntity.getJsonData());
        prevRelease.setDate(startDate);
        prevRelease.setId(getReleaseId(prevRelease.getOcid()));
        prevRelease.getTender().setStatusDetails(TenderStatusDetails.COMPLETE);
        releaseDao.saveTender(getReleaseEntity(cpid, stage, prevRelease));
        /*PQ*/
        final PsPqRelease release = prevRelease;
        release.setOcid(getOcId(cpid, stage));
        release.setId(getReleaseId(prevRelease.getOcid()));
        release.setDate(startDate);
        release.setTag(Collections.singletonList(Tag.COMPILED));
        release.setInitiationType(InitiationType.TENDER);
        release.getTender().setStatusDetails(TenderStatusDetails.PREQUALIFICATION);
        release.setAwards(new LinkedHashSet<>());
        release.getTender().setAwardPeriod(null);
        release.getTender().setStandstillPeriod(null);
        release.getTender().setEnquiries(new ArrayList<>());
        release.setRelatedProcesses(new LinkedHashSet<>());
        if (Objects.nonNull(dto.getTender().getTenderPeriod()))
            release.getTender().setTenderPeriod(dto.getTender().getTenderPeriod());
        if (Objects.nonNull(dto.getTender().getEnquiryPeriod()))
            release.getTender().setEnquiryPeriod(dto.getTender().getEnquiryPeriod());
        if (Objects.nonNull(dto.getBids()) && !dto.getBids().isEmpty())
            release.setBids(new Bids(null, dto.getBids()));
        addRelatedProcessToMs(ms, release.getOcid(), RelatedProcess.RelatedProcessType.X_PREQUALIFICATION);
        addMsToRelatedProcess(release, ms.getOcid());
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));

        return getResponseDto(cpid, release.getOcid());
    }

    private String getOcId(final String cpId, final String stage) {
        return cpId + SEPARATOR + stage + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private ReleaseEntity getMSEntity(final String cpId,
                                      final MsRelease ms) {
        final ReleaseEntity msEntity = new ReleaseEntity();
        msEntity.setCpId(cpId);
        msEntity.setStage("MS");
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
            if (Objects.nonNull(award.getDate()))
                updatableAward.setDate(award.getDate());
            if (Objects.nonNull(award.getDescription()))
                updatableAward.setDescription(award.getDescription());
            if (Objects.nonNull(award.getStatusDetails()))
                updatableAward.setStatusDetails(award.getStatusDetails());
            if (Objects.nonNull(award.getDocuments()) && !award.getDocuments().isEmpty())
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
        release.getTender().setLots(new ArrayList<>(updatableLots.values()));
    }

    private void addRelatedProcessToMs(final MsRelease ms,
                                       final String ocId,
                                       final RelatedProcess.RelatedProcessType processType) {
        ms.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(processType),
                        RelatedProcess.RelatedProcessScheme.OCID,
                        ocId,
                        "")
        );
    }

    private void addMsToRelatedProcess(final PsPqRelease release,
                                       final String cpId) {
        release.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcess.RelatedProcessType.PARENT),
                        RelatedProcess.RelatedProcessScheme.OCID,
                        cpId,
                        "")
        );
    }


    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
    }

}
