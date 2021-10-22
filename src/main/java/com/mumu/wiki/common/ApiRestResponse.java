package com.mumu.wiki.common;

public class ApiRestResponse<T> {
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = null;
    private Boolean success;
    private String message;
    private T content;

    public ApiRestResponse(Boolean success, String message, T content) {
        this.success = success;
        this.message = message;
        this.content = content;
    }

    public ApiRestResponse() {
        this(true, OK_MSG);
    }

    public ApiRestResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<>();
    }

    public static <T> ApiRestResponse<T> success(T result) {
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setContent(result);
        return response;
    }

//    public static <T> ApiRestResponse<T> error(String content, String message) {
//        return new ApiRestResponse<>(content, message);
//    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + success +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}