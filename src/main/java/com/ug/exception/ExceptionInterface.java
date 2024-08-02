package com.ug.exception;

import java.util.Map;

public interface ExceptionInterface {
    /**
     * Format exception as array.
     *
     * @return A map representing the formatted exception.
     */
    Map<String, Object> format();

    /**
     * Get custom exception errors.
     *
     * @return A map representing the custom exception errors.
     */
    Map<String, Object> getErrors();

    /**
     * Get custom exception errors details.
     *
     * @return A map representing the custom exception errors details.
     */
    Map<String, Object> getDetails();

    /**
     * Get custom exception errors details message.
     *
     * @return A string representing the custom exception errors details message.
     */
    String getDetailsMessage();

    /**
     * Get error message.
     *
     * @return A string representing the error message.
     */
    String getMessage();
}
