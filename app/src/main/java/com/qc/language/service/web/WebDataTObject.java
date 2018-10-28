package com.qc.language.service.web;

public class WebDataTObject {

    private boolean success;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return message;
    }

    public void setErrorMsg(String errorMsg) {
        this.message = errorMsg;
    }

}

