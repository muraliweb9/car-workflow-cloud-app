package com.interview.carworkflowcloud.consts;

import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError;
import lombok.Getter;

@Getter
public enum ErrorCode {

    HANDOVER_FAILURE("HANDOVER_FAIL_1", "The car did not meet quality criteria"),
    FINALISE_FAILURE("FINALISE_FAIL_1", "The customer details incomplete");

    String errCode;
    String errMessage;

    ErrorCode(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public ZeebeBpmnError bpmnError() {
        return new ZeebeBpmnError(errCode, errMessage);
    }

}
