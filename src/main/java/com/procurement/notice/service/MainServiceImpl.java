package com.procurement.notice.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    private static final String PARAM_ERROR = " should not be empty for this type of operation";
    private static final String IMPLEMENTATION_ERROR = "No implementation for this type of operation.";
    private final BudgetService budgetService;
    private final TenderService tenderService;
    private final EnquiryService enquiryService;

    public MainServiceImpl(final BudgetService budgetService,
                           final TenderServiceImpl tenderService,
                           final EnquiryService enquiryService) {
        this.tenderService = tenderService;
        this.enquiryService = enquiryService;
        this.budgetService = budgetService;
    }

    @Override
    public ResponseDto createRelease(final String cpId,
                                     final String ocId,
                                     final String stage,
                                     final String operation,
                                     final String phase,
                                     final LocalDateTime releaseDate,
                                     final JsonNode data) {
        Operation operationType = Operation.fromValue(operation);
        switch (operationType) {
            case CREATE_EI:
                return budgetService.createEi(cpId, stage, data);
            case UPDATE_EI:
                return budgetService.updateEi(cpId, stage, data);
            case CREATE_FS:
                return budgetService.createFs(cpId, stage, data);
            case UPDATE_FS:
                Objects.requireNonNull(ocId, "ocId " + PARAM_ERROR);
                return budgetService.updateFs(cpId, ocId, stage, data);
            case CREATE_CN:
                Objects.requireNonNull(releaseDate, "releaseDate " + PARAM_ERROR);
                return tenderService.createCn(cpId, stage, releaseDate, data);
            case UPDATE_CN:
                throw new ErrorException(IMPLEMENTATION_ERROR);
            case CREATE_ENQUIRY:
                Objects.requireNonNull(ocId, "ocId " + PARAM_ERROR);
                return enquiryService.createEnquiry(cpId, ocId, stage, data);
            case UPDATE_ENQUIRY:
                Objects.requireNonNull(ocId, "ocId " + PARAM_ERROR);
                enquiryService.updateEnquiry(cpId, ocId, stage, data);
            default:
                throw new ErrorException(IMPLEMENTATION_ERROR);
        }
    }


    private enum Operation {

        CREATE_EI("createEI"),
        UPDATE_EI("updateEI"),
        CREATE_FS("createFS"),
        UPDATE_FS("updateFS"),
        CREATE_CN("createCN"),
        UPDATE_CN("updateCN"),
        CREATE_ENQUIRY("createEnquiry"),
        UPDATE_ENQUIRY("updateEnquiry");

        private static final Map<String, Operation> CONSTANTS = new HashMap<>();

        static {
            for (final Operation c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        Operation(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static Operation fromValue(final String value) {
            final Operation constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(
                        "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
            }
            return constant;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }
    }


}
