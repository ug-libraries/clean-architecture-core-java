package com.ug.request;

import com.ug.exception.BadRequestContentException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class Request extends RequestFilter implements RequestInterface {
    protected String requestId;
    protected Map<String, Object> requestParams = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public RequestInterface createFromPayload(Map<String, Object> payload) throws BadRequestContentException {
        Map<String, Object> requestValidationResult = this.requestPayloadFilter(payload);
        this.throwMissingFieldsExceptionIfNeeded((Map<String, String>) requestValidationResult.get("missing_fields"));
        this.throwUnRequiredFieldsExceptionIfNeeded((List<String>) requestValidationResult.get("unauthorized_fields"));

        try {
            this.applyConstraintsOnRequestFields(payload);
        } catch (Exception error) {
            throw new BadRequestContentException(new HashMap<>() {{
                put("message", "invalid.request.fields");
                put("details", new HashMap<String, Object>() {{
                    put("error", error);
                }});
            }});
        }

        this.requestId = UUID.randomUUID().toString();
        this.requestParams = payload;
        
        return this;
    }

    /**
     * Throws an error if the request has missing fields.
     */
    protected void throwMissingFieldsExceptionIfNeeded(Map<String, String> missingFields) throws BadRequestContentException {
        if (!missingFields.isEmpty()) {
            throw new BadRequestContentException(new HashMap<>() {{
                put("message", "missing.required.fields");
                put("details", new HashMap<String, Map<String, String>>() {{
                    put("missing_fields", missingFields);
                }});
            }});
        }
    }

    /**
     * Throws an error if the request has unauthorized fields.
     */
    protected void throwUnRequiredFieldsExceptionIfNeeded(List<String> unauthorizedFields) throws BadRequestContentException {
        if (!unauthorizedFields.isEmpty()) {
            throw new BadRequestContentException(new HashMap<>() {{
                put("message", "illegal.fields");
                put("details", new HashMap<String, List<String>>() {{
                    put("unrequired_fields", unauthorizedFields);
                }});
            }});
        }
    }

    /**
     * Apply constraints on request fields if needed.
     */
    protected void applyConstraintsOnRequestFields(Map<String, Object> requestData) throws Exception {}


    /**
     * Get application request unique id.
     */
    @Override
    public String getRequestId() {
        return this.requestId;
    }

    /**
     * Get application request data as object.
     */
    @Override
    public Map<String, Object> toArray() {
        return this.requestParams;
    }

    /**
     * Get a specific field value from the request data.
     *
     * @param fieldName The field name.
     * @return The field value.
     */
    public Object get(String fieldName) {
        return get(fieldName, null);
    }

    /**
     * Get a specific field value from the request data.
     *
     * @param fieldName The field name.
     * @param defaultValue The default value to return if the field is missing.
     * @return The field value or the default value.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object get(String fieldName, Object defaultValue) {
        Object data = this.requestParams;
        for (String key : fieldName.split("\\.")) {
            if (!(data instanceof Map)) {
                return defaultValue;
            }
            data = ((Map<String, Object>) data).get(key);
            if (data == null) {
                return defaultValue;
            }
        }
        return data;
    }
}