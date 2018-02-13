package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.TenderDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.ocds.Enquiry;
import com.procurement.notice.model.ocds.ReleaseExt;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
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
        final Enquiry enquiry = jsonUtil.toObject(Enquiry.class, data.get("enquiry").asText());
        final ReleaseExt release = jsonUtil.toObject(ReleaseExt.class, entity.getJsonData());
        addEnquiry(release, enquiry);
        addParty(release, enquiry);
        tenderDao.saveTender(getEntity(cpid, ocid, stage, release));
        return getResponseDto(cpid, ocid);
    }

    @Override
    public ResponseDto updateEnquiry(final String cpid, final String ocid, final String stage, final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(TENDER_NOT_FOUND_ERROR));
        final Enquiry enquiry = jsonUtil.toObject(Enquiry.class, data.get("enquiry").asText());
        final ReleaseExt release = jsonUtil.toObject(ReleaseExt.class, entity.getJsonData());
        addAnswer(release, enquiry);
        tenderDao.saveTender(getEntity(cpid, ocid, stage, release));
        return getResponseDto(cpid, ocid);
    }

    private void addEnquiry(final ReleaseExt release, final Enquiry enquiry) {
        release.getTender().getEnquiries().add(enquiry);
    }

    private void addParty(final ReleaseExt release, final Enquiry enquiry) {
    }

    private void addAnswer(final ReleaseExt release, final Enquiry enquiry) {
        release.getTender().getEnquiries().stream()
                .filter(e -> e.getId().equals(enquiry.getId()))
                .findFirst()
                .get()
                .setAnswer(enquiry.getAnswer());
    }

    private TenderEntity getEntity(final String cpId,
                                   final String ocId,
                                   final String stage,
                                   final ReleaseExt releaseExt) {
        final TenderEntity releaseEntity = new TenderEntity();
        releaseEntity.setCpId(cpId);
        releaseEntity.setOcId(ocId);
        releaseEntity.setReleaseDate(dateUtil.localToDate(releaseExt.getDate()));
        releaseEntity.setReleaseId(getReleaseId(ocId));
        releaseEntity.setStage(stage);
        releaseEntity.setJsonData(jsonUtil.toJson(releaseExt));
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
