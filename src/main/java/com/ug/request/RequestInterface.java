package com.ug.request;

import com.ug.exception.BadRequestContentException;

import java.util.Map;

public interface RequestInterface {
    /**
     * Create a request from the given payload.
     *
     * @param payload The payload to create the request from.
     * @return An instance of RequestInterface.
     */
    RequestInterface createFromPayload(Map<String, Object> payload) throws BadRequestContentException;

    /**
     * Get application request unique id.
     */
    String getRequestId();

    /**
     * Get application request data as object.
     */
    Map<String, Object> toArray();

    /**
     * Get a specific field value from the request data.
     *
     * @param fieldName The field name.
     * @param defaultValue The default value to return if the field is missing.
     * @return The field value or the default value.
     */
    Object get(String fieldName, Object defaultValue);

    /**
     * Get a specific field value from the request data.
     *
     * @param fieldName The field name.
     * @return The field value.
     */
    Object get(String fieldName);
}
