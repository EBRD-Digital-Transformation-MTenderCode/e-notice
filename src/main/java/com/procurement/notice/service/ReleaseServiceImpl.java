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
import com.procurement.notice.model.tender.record.Record;
import com.procurement.notice.model.tender.record.TenderDescription;
import com.procurement.notice.model.tender.record.TenderTitle;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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
    public ReleaseEntity getReleaseEntity(final String cpId, final String stage, final Record record) {
        return getEntity(
                cpId,
                record.getOcid(),
                record.getId(),
                stage,
                dateUtil.localToDate(record.getDate()),
                jsonUtil.toJson(record)
        );
    }

    @Override
    public ResponseDto createCnPnPin(final String cpid,
                                     final String stage,
                                     final LocalDateTime releaseDate,
                                     final JsonNode data) {
        final CheckFsDto checkFs = jsonUtil.toObject(CheckFsDto.class, data.toString());
        final Ms ms = jsonUtil.toObject(Ms.class, data.toString());
        ms.setOcid(cpid);
        ms.setDate(releaseDate);
        ms.setId(getReleaseId(cpid));
        ms.setTag(Collections.singletonList(Tag.COMPILED));
        ms.setInitiationType(InitiationType.TENDER);
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        organizationService.processMsParties(ms, checkFs);
        final Record record = jsonUtil.toObject(Record.class, data.toString());
        record.setDate(releaseDate);
        record.setOcid(getOcId(cpid, stage));
        record.setId(getReleaseId(record.getOcid()));
        record.setTag(Collections.singletonList(Tag.TENDER));
        record.setInitiationType(InitiationType.TENDER);
        record.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        record.getTender().setTitle(TenderTitle.valueOf(stage.toUpperCase()).getText());
        record.getTender().setDescription(TenderDescription.valueOf(stage.toUpperCase()).getText());
        record.getPurposeOfNotice().setIsACallForCompetition(true);
        switch (Stage.fromValue(stage.toUpperCase())) {
            case PS:
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.getOcid(),
                        RelatedProcessType.X_PRESELECTION);
                break;
            case PQ:
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.getOcid(),
                        RelatedProcessType.X_PRESELECTION);
                break;
            case PN:
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.getOcid(),
                        RelatedProcessType.PLANNING);
                break;
            case PIN:
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.getOcid(),
                        RelatedProcessType.PRIOR);
                break;
        }
        relatedProcessService.addMsRelatedProcessToRecord(record, ms.getOcid());
        releaseDao.saveRelease(getMSEntity(ms.getOcid(), stage, ms));
        releaseDao.saveRelease(getReleaseEntity(ms.getOcid(), stage, record));
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
        final Record record = jsonUtil.toObject(Record.class, entity.getJsonData());
        final TenderPeriodEndDto dto = jsonUtil.toObject(TenderPeriodEndDto.class, data.toString());
        record.setId(getReleaseId(record.getOcid()));
        record.setDate(releaseDate);
        record.setTag(Collections.singletonList(Tag.AWARD));
        if (Objects.nonNull(dto.getAwardPeriod())) {
            record.getTender().setAwardPeriod(dto.getAwardPeriod());
            record.setDate(dto.getAwardPeriod().getStartDate());
        }
        if (Objects.nonNull(dto.getAwards()) && !dto.getAwards().isEmpty())
            record.setAwards(new LinkedHashSet<>(dto.getAwards()));
        if (Objects.nonNull(dto.getLots()) && !dto.getLots().isEmpty())
            record.getTender().setLots(dto.getLots());
        if (Objects.nonNull(dto.getBids()) && !dto.getBids().isEmpty())
            record.setBids(new Bids(null, dto.getBids()));
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record));
        return getResponseDto(cpid, record.getOcid());
    }

    @Override
    public ResponseDto suspendTender(final String cpid,
                                     final String stage,
                                     final LocalDateTime releaseDate,
                                     final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Record record = jsonUtil.toObject(Record.class, entity.getJsonData());
        final SuspendTenderDto dto = jsonUtil.toObject(SuspendTenderDto.class, jsonUtil.toJson(data));
        record.setDate(releaseDate);
        record.setId(getReleaseId(record.getOcid()));
        record.getTender().setStatusDetails(dto.getTender().getStatusDetails());
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record));
        return getResponseDto(cpid, record.getOcid());
    }

    @Override
    public ResponseDto awardByBid(final String cpid,
                                  final String stage,
                                  final LocalDateTime releaseDate,
                                  final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Record record = jsonUtil.toObject(Record.class, entity.getJsonData());
        final AwardByBidDto dto = jsonUtil.toObject(AwardByBidDto.class, jsonUtil.toJson(data));
        record.setTag(Collections.singletonList(Tag.AWARD_UPDATE));
        record.setDate(releaseDate);
        record.setId(getReleaseId(record.getOcid()));
        updateAward(record, dto.getAward());
        updateBid(record, dto.getBid());
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record));
        return getResponseDto(cpid, record.getOcid());
    }

    @Override
    public ResponseDto awardPeriodEnd(final String cpid,
                                      final String stage,
                                      final LocalDateTime releaseDate,
                                      final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Record record = jsonUtil.toObject(Record.class, entity.getJsonData());
        final AwardPeriodEndDto dto = jsonUtil.toObject(AwardPeriodEndDto.class, data.toString());
        record.setId(getReleaseId(record.getOcid()));
        record.setDate(releaseDate);
        record.getTender().setStatusDetails(TenderStatusDetails.COMPLETE);
        if (Objects.nonNull(dto.getAwardPeriod()))
            record.getTender().setAwardPeriod(dto.getAwardPeriod());
        if (Objects.nonNull(dto.getAwards()) && !dto.getAwards().isEmpty())
            record.setAwards(new LinkedHashSet<>(dto.getAwards()));
        if (Objects.nonNull(dto.getLots()) && !dto.getLots().isEmpty())
            record.getTender().setLots(dto.getLots());
        if (Objects.nonNull(dto.getBids()) && !dto.getBids().isEmpty())
            record.setBids(new Bids(null, dto.getBids()));
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record));
        return getResponseDto(cpid, record.getOcid());
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
        releaseDao.saveRelease(getMSEntity(ms.getOcid(), stage, ms));
        /*Record*/
        final ReleaseEntity releaseEntity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Record record = jsonUtil.toObject(Record.class, releaseEntity.getJsonData());
        record.setDate(dto.getStandstillPeriod().getEndDate());
        record.setId(getReleaseId(record.getOcid()));
        record.getTender().setStatusDetails(TenderStatusDetails.PRESELECTED);
        record.getTender().setStandstillPeriod(dto.getStandstillPeriod());
        updateLots(record, dto.getLots());
        releaseDao.saveRelease(getReleaseEntity(record.getOcid(), stage, record));
        return getResponseDto(cpid, record.getOcid());
    }

    @Override
    public ResponseDto startNewStage(final String cpid,
                                     final String stage,
                                     final String previousStage,
                                     final LocalDateTime releaseDate,
                                     final JsonNode data) {

        final StartNewStageDto dto = jsonUtil.toObject(StartNewStageDto.class, jsonUtil.toJson(data));

        TenderStatusDetails statusDetails = null;
        RelatedProcessType relatedProcessType = null;
        switch (Stage.fromValue(stage)) {
            case PQ: {
                statusDetails = TenderStatusDetails.PREQUALIFICATION;
                relatedProcessType = RelatedProcessType.X_PREQUALIFICATION;
                break;
            }
            case EV: {
                statusDetails = TenderStatusDetails.EVALUATION;
                relatedProcessType = RelatedProcessType.X_EVALUATION;
                break;
            }
        }
        /*Multi stage*/
        final ReleaseEntity msEntity = Optional.ofNullable(releaseDao.getByCpIdAndOcId(cpid, cpid))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Ms ms = jsonUtil.toObject(Ms.class, msEntity.getJsonData());
        ms.setDate(releaseDate);
        ms.setId(getReleaseId(ms.getOcid()));
        ms.setTag(Collections.singletonList(Tag.COMPILED));
        ms.getTender().setStatusDetails(statusDetails);
        /* previous record*/
        final ReleaseEntity releaseEntity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, previousStage))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final Record record = jsonUtil.toObject(Record.class, releaseEntity.getJsonData());
        record.setDate(releaseDate);
        record.setId(getReleaseId(record.getOcid()));
        record.getTender().setStatusDetails(TenderStatusDetails.COMPLETE);
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record));
        /*new record*/
        final String prevRecordOcId = record.getOcid();
        record.setDate(releaseDate);
        record.setTag(Collections.singletonList(Tag.COMPILED));
        record.setOcid(getOcId(cpid, stage));
        record.setId(getReleaseId(record.getOcid()));
        record.setTender(dto.getTender());
        record.setBids(dto.getBids());
        record.getTender().setStatusDetails(statusDetails);
        processDocuments(record, dto);
        organizationService.processPartiesFromBids(record, dto.getBids());
        relatedProcessService.addRecordRelatedProcessToMs(record, ms.getOcid(), relatedProcessType);
        relatedProcessService.addMsRelatedProcessToRecord(record, ms.getOcid());
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prevRecordOcId, ms.getOcid());
        releaseDao.saveRelease(getMSEntity(cpid, stage, ms));
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record));
        return getResponseDto(cpid, record.getOcid());
    }

    private void processDocuments(final Record record, final StartNewStageDto dto) {
        final Set<String> docIds = dto.getTender().getDocuments().stream().map(Document::getId).collect(toSet());
        final List<Document> validDocuments =
                record.getTender().getDocuments()
                        .stream()
                        .filter(d -> docIds.contains(d.getId())).collect(toList());
        record.getTender().setDocuments(validDocuments);
    }

    private ReleaseEntity getMSEntity(final String cpId, final String stage, final Ms ms) {
        return getEntity(
                cpId,
                ms.getOcid(),
                ms.getId(),
                stage,
                dateUtil.localToDate(ms.getDate()),
                jsonUtil.toJson(ms)
        );
    }

    private ReleaseEntity getEntity(final String cpId,
                                    final String ocID,
                                    final String releaseId,
                                    final String stage,
                                    final Date releaseDate,
                                    final String json) {
        final ReleaseEntity entity = new ReleaseEntity();
        entity.setCpId(cpId);
        entity.setOcId(ocID);
        entity.setReleaseId(releaseId);
        entity.setStage(stage);
        entity.setReleaseDate(releaseDate);
        entity.setJsonData(json);
        return entity;
    }

    private String getOcId(final String cpId, final String stage) {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + dateUtil.milliNowUTC();
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.milliNowUTC();
    }

    private void updateAward(final Record release, final Award award) {
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

    private void updateBid(final Record release, final Bid bid) {
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

    private void updateLots(final Record release, final List<Lot> lotsDto) {
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
