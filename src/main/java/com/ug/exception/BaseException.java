package com.ug.exception;

import com.ug.enums.Status;
import com.ug.response.StatusCode;

import java.util.Map;

public class BaseException extends Exception implements ExceptionInterface {
    /**
     * Exception status code.
     */
    protected int statusCode = StatusCode.BAD_REQUEST.getValue();

    /**
     * Custom data into the exception.
     */
    protected Map<String, Object> errors;

    public BaseException(Map<String, Object> errors) {
        super((String) errors.getOrDefault("message", ""));
        errors.remove("message");
        this.errors = errors;
    }

    /**
     * Format exception as array.
     *
     * @return A map representing the formatted exception.
     */
    @Override
    public Map<String, Object> format() {
        return Map.of(
            "status", Status.ERROR.getValue(),
            "error_code", this.statusCode,
            "message", this.getMessage(),
            "details", this.getDetails()
        );
    }

    /**
     * Get custom exception errors details.
     *
     * @return A map representing the custom exception errors details.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getDetails() {
        return (Map<String, Object>) this.errors.getOrDefault("details", Map.of());
    }

    /**
     * Get custom exception errors details message.
     *
     * @return A string representing the custom exception errors details message.
     */
    @Override
    public String getDetailsMessage() {
        return (String) this.getDetails().getOrDefault("error", "");
    }

    /**
     * Get custom exception errors.
     *
     * @return A map representing the custom exception errors.
     */
    @Override
    public Map<String, Object> getErrors() {
        return this.errors;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}