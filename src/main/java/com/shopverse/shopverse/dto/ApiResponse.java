package com.shopverse.shopverse.dto;

public class ApiResponse<T> {

    private String status;
    private String errorMessage;
    private T data;

    public ApiResponse() {

    }

    public ApiResponse(String status, String errorMessage, T data) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
