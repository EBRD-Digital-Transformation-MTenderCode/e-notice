package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.TenderDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.ocds.*;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private static final String SEPARATOR = "-";
    private static final String TENDER_NOT_FOUND_ERROR = "Tender not found.";

    private final TenderDao tenderDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public EnquiryServiceImpl(final TenderDao tenderDao,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.tenderDao = tenderDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto createEnquiry(final String cpid,
                                     final String ocid,
                                     final String stage,
                                     final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(TENDER_NOT_FOUND_ERROR));
        final Enquiry enquiry = jsonUtil.toObject(Enquiry.class, jsonUtil.toJson(data.get("enquiry")));
        final ReleaseExt release = jsonUtil.toObject(ReleaseExt.class, entity.getJsonData());
        updateEnquiry(release, enquiry);
        addParty(release, enquiry);
        tenderDao.saveTender(getEntity(cpid, ocid, stage, release));
        return getResponseDto(cpid, ocid);
    }

    @Override
    public ResponseDto addAnswer(final String cpid, final String ocid, final String stage, final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(TENDER_NOT_FOUND_ERROR));
        final Enquiry enquiry = jsonUtil.toObject(Enquiry.class, jsonUtil.toJson(data.get("enquiry")));
        final ReleaseExt release = jsonUtil.toObject(ReleaseExt.class, entity.getJsonData());
        updateEnquiry(release, enquiry);
        tenderDao.saveTender(getEntity(cpid, ocid, stage, release));
        return getResponseDto(cpid, ocid);
    }

    @Override
    public ResponseDto enquiryUnsuspendTender(final String cpid, 
                                              final String ocid,
                                              final String stage,
                                              final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(TENDER_NOT_FOUND_ERROR));
        final Enquiry enquiry = jsonUtil.toObject(Enquiry.class, jsonUtil.toJson(data.get("enquiry")));
        final ReleaseExt release = jsonUtil.toObject(ReleaseExt.class, entity.getJsonData());
        updateEnquiry(release, enquiry);
        final JsonNode tenderNode = data.get("tender");
        final String tenderStatusString = tenderNode.get("status").asText();
        if ((tenderStatusString != null && !"".equals(tenderStatusString.trim()))) {
            release.getTender().setStatus(TenderStatus.fromValue(tenderStatusString));
        }
        final String tenderStatusDetailsString = tenderNode.get("statusDetails").asText();
        if ((tenderStatusDetailsString != null && !"".equals(tenderStatusDetailsString.trim()))) {
            release.getTender().setStatusDetails(TenderStatusDetails.fromValue(tenderStatusDetailsString));
        }
        final Period tenderPeriod = jsonUtil.toObject(Period.class, jsonUtil.toJson(tenderNode.get("tenderPeriod")));
        release.getTender().setTenderPeriod(tenderPeriod);
        final Period enquiryPeriod = jsonUtil.toObject(Period.class, jsonUtil.toJson(tenderNode.get("enquiryPeriod")));
        release.getTender().setEnquiryPeriod(enquiryPeriod);
        tenderDao.saveTender(getEntity(cpid, ocid, stage, release));
        return getResponseDto(cpid, ocid);
    }

    private void updateEnquiry(final ReleaseExt release, final Enquiry enquiry) {
        List<Enquiry> enquiries = release.getTender().getEnquiries();
        if (Objects.isNull(enquiries)) {
            enquiries = new ArrayList<>();
        }
        Optional<Enquiry> enquiryOptional = enquiries.stream()
                .filter(e -> e.getId().equals(enquiry.getId()))
                .findFirst();
        if (enquiryOptional.isPresent()) {
            Enquiry updatableEnquiry = enquiryOptional.get();
            updatableEnquiry.setAnswer(enquiry.getAnswer());
        } else {
            enquiries.add(enquiry);
        }
        release.getTender().setEnquiries(enquiries);
    }


    private void addParty(final ReleaseExt release, final Enquiry enquiry) {
    }

    private TenderEntity getEntity(final String cpId,
                                   final String ocId,
                                   final String stage,
                                   final ReleaseExt release) {
        final TenderEntity releaseEntity = new TenderEntity();
        releaseEntity.setCpId(cpId);
        releaseEntity.setOcId(ocId);
        releaseEntity.setReleaseDate(dateUtil.localToDate(dateUtil.getNowUTC()));
        releaseEntity.setReleaseId(getReleaseId(ocId));
        releaseEntity.setStage(stage);
        releaseEntity.setJsonData(jsonUtil.toJson(release));
        return releaseEntity;
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
    }
}
