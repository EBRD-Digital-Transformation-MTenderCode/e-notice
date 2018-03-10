package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.ReleaseDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.model.tender.dto.UnsuspendTenderDto;
import com.procurement.notice.model.tender.enquiry.PsPqEnquiry;
import com.procurement.notice.model.tender.pspq.PsPqRelease;
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
    private static final String ENQUIRY_NOT_FOUND_ERROR = "PsPqEnquiry not found.";
    private static final String ENQUIRY_JSON = "enquiry";
    private final ReleaseService releaseService;
    private final ReleaseDao releaseDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public EnquiryServiceImpl(final ReleaseService releaseService,
                              final ReleaseDao releaseDao,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.releaseService = releaseService;
        this.releaseDao = releaseDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto createEnquiry(final String cpid,
                                     final String stage,
                                     final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqEnquiry enquiry = jsonUtil.toObject(PsPqEnquiry.class, jsonUtil.toJson(data.get(ENQUIRY_JSON)));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        addEnquiryToTender(release, enquiry);
        release.setDate(enquiry.getDate());
        release.setId(getReleaseId(release.getOcid()));
        releaseDao.saveTender(releaseService.getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto addAnswer(final String cpid,
                                 final String stage,
                                 final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqEnquiry enquiry = jsonUtil.toObject(PsPqEnquiry.class, jsonUtil.toJson(data.get(ENQUIRY_JSON)));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        addAnswerToEnquiry(release, enquiry);
        release.setDate(enquiry.getDate());
        release.setId(getReleaseId(release.getOcid()));
        releaseDao.saveTender(releaseService.getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    @Override
    public ResponseDto unsuspendTender(final String cpid,
                                       final String stage,
                                       final JsonNode data) {
        final ReleaseEntity entity = Optional.ofNullable(releaseDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final PsPqRelease release = jsonUtil.toObject(PsPqRelease.class, entity.getJsonData());
        final UnsuspendTenderDto dto = jsonUtil.toObject(UnsuspendTenderDto.class, jsonUtil.toJson(data));
        final PsPqEnquiry enquiry = dto.getEnquiry();
        addAnswerToEnquiry(release, enquiry);
        release.setDate(enquiry.getDate());
        release.setId(getReleaseId(release.getOcid()));
        release.getTender().setStatusDetails(dto.getTender().getStatusDetails());
        release.getTender().setTenderPeriod(dto.getTenderPeriod());
        release.getTender().setEnquiryPeriod(dto.getEnquiryPeriod());
        releaseDao.saveTender(releaseService.getReleaseEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    private void addEnquiryToTender(final PsPqRelease release, final PsPqEnquiry enquiry) {
        List<PsPqEnquiry> enquiries = release.getTender().getEnquiries();
        if (Objects.isNull(enquiries)) {
            enquiries = new ArrayList<>();
        }
        final Optional<PsPqEnquiry> enquiryOptional = enquiries.stream()
                .filter(e -> e.getId().equals(enquiry.getId()))
                .findFirst();
        if (!enquiryOptional.isPresent()) {
            enquiries.add(enquiry);
            release.getTender().setEnquiries(enquiries);
        }
    }

    private void addAnswerToEnquiry(final PsPqRelease release, final PsPqEnquiry enquiry) {
        final List<PsPqEnquiry> enquiries = release.getTender().getEnquiries();
        final Optional<PsPqEnquiry> enquiryOptional = enquiries.stream()
                .filter(e -> e.getId().equals(enquiry.getId()))
                .findFirst();
        if (enquiryOptional.isPresent()) {
            final PsPqEnquiry updatableEnquiry = enquiryOptional.get();
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
