package com.procurement.notice.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.exception.EnumException;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.exception.ErrorType;
import com.procurement.notice.model.bpe.ResponseDto;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    private final BudgetService budgetService;
    private final ReleaseService releaseService;
    private final EnquiryService enquiryService;

    public MainServiceImpl(final BudgetService budgetService,
                           final ReleaseServiceImpl tenderService,
                           final EnquiryService enquiryService) {
        this.releaseService = tenderService;
        this.enquiryService = enquiryService;
        this.budgetService = budgetService;
    }

    @Override
    public ResponseDto createRelease(final String cpId,
                                     final String ocId,
                                     final String stage,
                                     final String previousStage,
                                     final String operation,
                                     final String phase,
                                     final LocalDateTime releaseDate,
                                     final JsonNode data) {
        final Operation operationType = Operation.fromValue(operation);
        switch (operationType) {
            case CREATE_EI:
                return budgetService.createEi(cpId, stage, releaseDate, data);
            case UPDATE_EI:
                return budgetService.updateEi(cpId, stage, releaseDate, data);
            case CREATE_FS:
                return budgetService.createFs(cpId, stage, releaseDate, data);
            case UPDATE_FS:
                return budgetService.updateFs(cpId, ocId, stage, releaseDate, data);
            case CREATE_CN:
                return releaseService.createCn(cpId, stage, releaseDate, data);
            case CREATE_PN:
                return releaseService.createPn(cpId, stage, releaseDate, data);
            case CREATE_PIN:
                return releaseService.createPin(cpId, stage, releaseDate, data);
            case UPDATE_CN:
                throw new ErrorException(ErrorType.IMPLEMENTATION_ERROR);
            case CREATE_ENQUIRY:
                return enquiryService.createEnquiry(cpId, stage, releaseDate, data);
            case ADD_ANSWER:
                return enquiryService.addAnswer(cpId, stage, releaseDate, data);
            case UNSUSPEND_TENDER:
                return enquiryService.unsuspendTender(cpId, stage, releaseDate, data);
            case TENDER_PERIOD_END:
                return releaseService.tenderPeriodEnd(cpId, stage, releaseDate, data);
            case SUSPEND_TENDER:
                return releaseService.suspendTender(cpId, stage, releaseDate, data);
            case AWARD_BY_BID:
                return releaseService.awardByBid(cpId, stage, releaseDate, data);
            case AWARD_PERIOD_END:
                return releaseService.awardPeriodEnd(cpId, stage, releaseDate, data);
            case STANDSTILL_PERIOD_END:
                return releaseService.standstillPeriodEnd(cpId, stage, releaseDate, data);
            case START_NEW_STAGE:
                return releaseService.startNewStage(cpId, stage, previousStage, releaseDate, data);
            default:
                throw new ErrorException(ErrorType.IMPLEMENTATION_ERROR);
        }
    }

    private enum Operation {

        CREATE_EI("createEI"),
        UPDATE_EI("updateEI"),
        CREATE_FS("createFS"),
        UPDATE_FS("updateFS"),
        CREATE_CN("createCN"),
        CREATE_PN("createPN"),
        CREATE_PIN("createPIN"),
        UPDATE_CN("updateCN"),
        CREATE_ENQUIRY("createEnquiry"),
        ADD_ANSWER("addAnswer"),
        UNSUSPEND_TENDER("unsuspendTender"),
        TENDER_PERIOD_END("tenderPeriodEnd"),
        SUSPEND_TENDER("suspendTender"),
        AWARD_BY_BID("awardByBid"),
        AWARD_PERIOD_END("awardPeriodEnd"),
        STANDSTILL_PERIOD_END("standstillPeriodEnd"),
        START_NEW_STAGE("startNewStage");

        private static final Map<String, Operation> CONSTANTS = new HashMap<>();
        private final String value;

        static {
            for (final Operation c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }


        Operation(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static Operation fromValue(final String value) {
            final Operation constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new EnumException(Operation.class.getName(), value, Arrays.toString(values()));
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
