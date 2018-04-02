package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.ReleaseDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.exception.ErrorType;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.model.ocds.*;
import com.procurement.notice.model.tender.dto.*;
import com.procurement.notice.model.tender.ms.Ms;
import com.procurement.notice.model.tender.pspq.PsPq;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private static final String SEPARATOR = "-";
    private final ReleaseDao releaseDao;
    private final BudgetService budgetService;
    private final OrganizationService organizationService;
    private final RelatedProcessService relatedProcessService;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public ReleaseServiceImpl(final ReleaseDao releaseDao,
                              final BudgetService budgetService,
                              final OrganizationService organizationService,
                              final RelatedProcessService relatedProcessService,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.releaseDao = releaseDao;
        this.budgetService = budgetService;
        this.organizationService = organizationService;
        this.relatedProcessService = relatedProcessService;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ReleaseEntity getReleaseEntity(final String cpId,
                                          final String stage,
                                          final PsPq release) {
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
                                final LocalDateTime releaseDate,
                                final JsonNode data) {
        final CheckFsDto checkFs = jsonUtil.toObject(CheckFsDto.class, data.toString());
        final Ms ms = jsonUtil.toObject(Ms.class, data.toString());
        ms.setOcid(cpid);
        ms.setDate(releaseDate);
        ms.setId(getReleaseId(cpid));
        ms.setTag(Arrays.asList(Tag.COMPILED));
        ms.setInitiationType(InitiationType.TENDER);
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        organizationService.processMsParties(ms, checkFs);
        final PsPq record = jsonUtil.toObject(PsPq.class, data.toString());
        record.setDate(releaseDate);
        record.setOcid(getOcId(cpid, stage));
        record.setId(getReleaseId(record.getOcid()));
        record.setTag(Arrays.asList(Tag.COMPILED));
        record.setInitiationType(InitiationType.TENDER);
        record.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        relatedProcessService.addRelatedProcessToMs(ms, checkFs, record.getOcid(), RelatedProcessType.X_PRESELECTION);
        relatedProcessService.addMsRelatedProcess(record, ms.getOcid());
        releaseDao.saveTender(getMSEntity(ms.getOcid(), ms));
        releaseDao.saveTender(getReleaseEntity(ms.getOcid(), stage, record));
        budgetService.createEiByMs(checkFs.getEi(), cpid, releaseDate);
        budgetService.createFsByMs(ms.getPlanning().getBudget().getBudgetBreakdown(), cpid, releaseDate);
        return getResponseDto(ms.getOcid(), record.getOcid());
    }


    @Override
    public ResponseDto tenderPeriodEnd(final String cpid,
                                       final String stage,
                                       final LocalDateTime releaseDate,
                                       final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final PsPq release = jsonUtil.toObject(PsPq.class, entity.getJsonData());
        final TenderPeriodEndDto dto = jsonUtil.toObject(TenderPeriodEndDto.class, data.toString());
        release.setId(getReleaseId(release.getOcid()));
        release.setDate(releaseDate);
        release.setTag(Arrays.asList(Tag.AWARD));

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
                                     final LocalDateTime releaseDate,
                                     final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final PsPq release = jsonUtil.toObject(PsPq.class, entity.getJsonData());
        final SuspendTenderDto dto = jsonUtil.toObject(SuspendTenderDto.class, jsonUtil.toJson(data));
        release.setDate(releaseDate);
        release.setId(getReleaseId(release.getOcid()));
        release.getTender().setStatusDetails(dto.getTender().getStatusDetails());
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto awardByBid(final String cpid,
                                  final String stage,
                                  final LocalDateTime releaseDate,
                                  final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final PsPq release = jsonUtil.toObject(PsPq.class, entity.getJsonData());
        final AwardByBidDto dto = jsonUtil.toObject(AwardByBidDto.class, jsonUtil.toJson(data));
        release.setTag(Arrays.asList(Tag.AWARD_UPDATE));
        release.setDate(releaseDate);
        release.setId(getReleaseId(release.getOcid()));
        updateAward(release, dto.getAward());
        updateBid(release, dto.getBid());
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto awardPeriodEnd(final String cpid,
                                      final String stage,
                                      final LocalDateTime releaseDate,
                                      final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final PsPq release = jsonUtil.toObject(PsPq.class, entity.getJsonData());
        final AwardPeriodEndDto dto = jsonUtil.toObject(AwardPeriodEndDto.class, data.toString());
        release.setId(getReleaseId(release.getOcid()));
        release.setDate(releaseDate);
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
    public ResponseDto standstillPeriodEnd(final String cpid,
                                           final String stage,
                                           final LocalDateTime releaseDate,
                                           final JsonNode data) {
        final StandstillPeriodEndDto dto = jsonUtil.toObject(StandstillPeriodEndDto.class, jsonUtil.toJson(data));
        /*MS*/
        final ReleaseEntity msEntity = Optional.ofNullable(releaseDao.getByCpIdAndOcId(cpid, cpid))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Ms ms = jsonUtil.toObject(Ms.class, msEntity.getJsonData());
        ms.setDate(releaseDate);
        ms.setId(getReleaseId(ms.getOcid()));
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTED);
        releaseDao.saveTender(getMSEntity(ms.getOcid(), ms));
        /*PS-PQ*/
        final ReleaseEntity releaseEntity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final PsPq tender = jsonUtil.toObject(PsPq.class, releaseEntity.getJsonData());
        tender.setDate(dto.getStandstillPeriod().getEndDate());
        tender.setId(getReleaseId(tender.getOcid()));
        tender.getTender().setStatusDetails(TenderStatusDetails.PRESELECTED);
        tender.getTender().setStandstillPeriod(dto.getStandstillPeriod());
        updateLots(tender, dto.getLots());
        releaseDao.saveTender(getReleaseEntity(tender.getOcid(), stage, tender));
        return getResponseDto(cpid, tender.getOcid());
    }

    @Override
    public ResponseDto startNewStage(final String cpid,
                                     final String stage,
                                     final String previousStage,
                                     final LocalDateTime releaseDate,
                                     final JsonNode data) {
        final StartNewStageDto dto = jsonUtil.toObject(StartNewStageDto.class, jsonUtil.toJson(data));
        /*MS*/
        final ReleaseEntity msEntity = Optional.ofNullable(releaseDao.getByCpIdAndOcId(cpid, cpid))
                                               .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Ms ms = jsonUtil.toObject(Ms.class, msEntity.getJsonData());
        ms.setDate(releaseDate);
        ms.setId(getReleaseId(ms.getOcid()));
        ms.getTender()
          .setStatusDetails(TenderStatusDetails.PREQUALIFICATION);
        releaseDao.saveTender(getMSEntity(cpid, ms));
        /*PS*/
        final ReleaseEntity releaseEntity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, previousStage))
                                                    .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final PsPq prevRelease = jsonUtil.toObject(PsPq.class, releaseEntity.getJsonData());
        prevRelease.setDate(releaseDate);
        prevRelease.setId(getReleaseId(prevRelease.getOcid()));
        prevRelease.getTender()
                   .setStatusDetails(TenderStatusDetails.COMPLETE);
        releaseDao.saveTender(getReleaseEntity(cpid, stage, prevRelease));
        /*PQ*/
        final PsPq release = prevRelease;

        release.setDate(releaseDate);
        release.setTag(Arrays.asList(Tag.COMPILED));
        release.setId(getReleaseId(prevRelease.getOcid()));

        release.getTender()
               .setStatusDetails(TenderStatusDetails.PREQUALIFICATION);

        release.setRelatedProcesses(new LinkedHashSet<>());

        relatedProcessService.addRelatedProcessToPq(release, ms);

        relatedProcessService.addMsRelatedProcess(release, ms.getOcid());
        releaseDao.saveTender(getReleaseEntity(cpid, stage, release));

        return getResponseDto(cpid, release.getOcid());
    }

    private String getOcId(final String cpId, final String stage) {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + dateUtil.milliNowUTC();
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.milliNowUTC();
    }

    private ReleaseEntity getMSEntity(final String cpId,
                                      final Ms ms) {
        final ReleaseEntity msEntity = new ReleaseEntity();
        msEntity.setCpId(cpId);
        msEntity.setStage("MS");
        msEntity.setOcId(ms.getOcid());
        msEntity.setJsonData(jsonUtil.toJson(ms));
        msEntity.setReleaseId(ms.getId());
        msEntity.setReleaseDate(dateUtil.localToDate(ms.getDate()));
        return msEntity;
    }

    private void updateAward(final PsPq release, final Award award) {
        final Set<Award> awards = release.getAwards();
        final Optional<Award> awardOptional = awards.stream()
                                                    .filter(a -> a.getId()
                                                                  .equals(award.getId()))
                                                    .findFirst();
        if (awardOptional.isPresent()) {
            final Award updatableAward = awardOptional.get();
            if (Objects.nonNull(award.getDate()))
                updatableAward.setDate(award.getDate());
            if (Objects.nonNull(award.getDescription()))
                updatableAward.setDescription(award.getDescription());
            if (Objects.nonNull(award.getStatusDetails()))
                updatableAward.setStatusDetails(award.getStatusDetails());
            if (Objects.nonNull(award.getDocuments()) && !award.getDocuments()
                                                               .isEmpty())
                updatableAward.setDocuments(award.getDocuments());
            release.setAwards(awards);
        } else {
            throw new ErrorException(ErrorType.DATA_NOT_FOUND);
        }
    }

    private void updateBid(final PsPq release, final Bid bid) {
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
            throw new ErrorException(ErrorType.DATA_NOT_FOUND);
        }
    }

    private void updateLots(final PsPq release, final List<Lot> lotsDto) {
        final List<Lot> lots = release.getTender().getLots();
        if (lots.isEmpty()) throw new ErrorException(ErrorType.DATA_NOT_FOUND);
        final Map<String, Lot> updatableLots = new HashMap<>();
        lots.forEach(lot -> updatableLots.put(lot.getId(), lot));
        lotsDto.forEach(lotDto -> updatableLots.get(lotDto.getId()).setStatusDetails(lotDto.getStatusDetails()));
        release.getTender().setLots(new ArrayList<>(updatableLots.values()));
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
    }

}
