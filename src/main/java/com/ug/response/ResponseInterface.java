package com.ug.response;

import java.util.Map;

public interface ResponseInterface {
    /**
     * Check if response is success.
     *
     * @return true if the response is success, false otherwise.
     */
    boolean isSuccess();

    /**
     * Get response status code.
     *
     * @return The status code of the response.
     */
    int getStatusCode();

    /**
     * Get custom response message.
     *
     * @return The message of the response, or null if there is no message.
     */
    String getMessage();

    /**
     * Get the response data.
     *
     * @return The data of the response.
     */
    Map<String, Object> getData();

    /**
     * Get specific field from response.
     *
     * @param fieldName The name of the field to get.
     * @return The value of the specified field.
     */
    Object get(String fieldName);

    /**
     * Return response as array with more context.
     *
     * @return A map representing the response with more context.
     */
    Map<String, Object> output();
}