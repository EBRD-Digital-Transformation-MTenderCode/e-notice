package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.TenderDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.ocds.Enquiry;
import com.procurement.notice.model.tender.ReleaseTender;
import com.procurement.notice.model.tender.UnsuspendTenderDto;
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
    private static final String RELEASE_NOT_FOUND_ERROR = "Release not found by stage: ";
    private static final String ENQUIRY_NOT_FOUND_ERROR = "Enquiry not found.";
    private static final String ENQUIRY_JSON = "enquiry";
    private final TenderService tenderService;
    private final TenderDao tenderDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public EnquiryServiceImpl(final TenderService tenderService,
                              final TenderDao tenderDao,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.tenderService = tenderService;
        this.tenderDao = tenderDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto createEnquiry(final String cpid,
                                     final String stage,
                                     final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final Enquiry enquiry = jsonUtil.toObject(Enquiry.class, jsonUtil.toJson(data.get(ENQUIRY_JSON)));
        final ReleaseTender release = jsonUtil.toObject(ReleaseTender.class, entity.getJsonData());
        addEnquiryToTender(release, enquiry);
        release.setDate(enquiry.getDate());
        release.setId(getReleaseId(release.getOcid()));
        tenderDao.saveTender(tenderService.getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto addAnswer(final String cpid,
                                 final String stage,
                                 final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final Enquiry enquiry = jsonUtil.toObject(Enquiry.class, jsonUtil.toJson(data.get(ENQUIRY_JSON)));
        final ReleaseTender release = jsonUtil.toObject(ReleaseTender.class, entity.getJsonData());
        addAnswerToEnquiry(release, enquiry);
        release.setDate(enquiry.getDate());
        release.setId(getReleaseId(release.getOcid()));
        tenderDao.saveTender(tenderService.getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto unsuspendTender(final String cpid,
                                       final String stage,
                                       final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final ReleaseTender release = jsonUtil.toObject(ReleaseTender.class, entity.getJsonData());
        final UnsuspendTenderDto dto = jsonUtil.toObject(UnsuspendTenderDto.class, jsonUtil.toJson(data));
        final Enquiry enquiry = dto.getEnquiry();
        addAnswerToEnquiry(release, enquiry);
        release.setDate(enquiry.getDate());
        release.setId(getReleaseId(release.getOcid()));
        release.getTender().setStatusDetails(dto.getTender().getStatusDetails());
        release.getTender().setTenderPeriod(dto.getTenderPeriod());
        release.getTender().setEnquiryPeriod(dto.getEnquiryPeriod());
        tenderDao.saveTender(tenderService.getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    private void addEnquiryToTender(final ReleaseTender release, final Enquiry enquiry) {
        List<Enquiry> enquiries = release.getTender().getEnquiries();
        if (Objects.isNull(enquiries)) {
            enquiries = new ArrayList<>();
        }
        final Optional<Enquiry> enquiryOptional = enquiries.stream()
                .filter(e -> e.getId().equals(enquiry.getId()))
                .findFirst();
        if (!enquiryOptional.isPresent()) {
            enquiries.add(enquiry);
            release.getTender().setEnquiries(enquiries);
        }
    }

    private void addAnswerToEnquiry(final ReleaseTender release, final Enquiry enquiry) {
        final List<Enquiry> enquiries = release.getTender().getEnquiries();
        final Optional<Enquiry> enquiryOptional = enquiries.stream()
                .filter(e -> e.getId().equals(enquiry.getId()))
                .findFirst();
        if (enquiryOptional.isPresent()) {
            final Enquiry updatableEnquiry = enquiryOptional.get();
            updatableEnquiry.setAnswer(enquiry.getAnswer());
            release.getTender().setEnquiries(enquiries);
        } else {
            throw new ErrorException(ENQUIRY_NOT_FOUND_ERROR);
        }
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
