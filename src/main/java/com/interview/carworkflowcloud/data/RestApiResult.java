package com.interview.carworkflowcloud.data;

public enum RestApiResult {

    COMPLETED_OK("Ok"),
    COMPLETED_FAILED("Task not found"),
    INCOMPLETE("Fail");

    private final String message;

    RestApiResult(String message) {
        this.message = message;
    }


}
