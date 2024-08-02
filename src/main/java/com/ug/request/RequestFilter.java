package com.ug.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RequestFilter {

    /**
     * Filter request data to identify missing/unauthorized fields.
     *
     * @param requestPayload The request payload.
     * @return A map containing unauthorized and missing fields.
     */
    protected Map<String, Object> requestPayloadFilter(Map<String, Object> requestPayload) {
        Map<String, Object> result = new HashMap<>();
        result.put("unauthorized_fields", findUnAuthorizedFields(requestPayload, this.getRequestPossibleFields(), ""));
        result.put("missing_fields", findMissingFields(this.getRequestPossibleFields(), requestPayload, ""));
        return result;
    }

    /**
     * Find unauthorized fields from request.
     *
     * @param requestPayload The request payload.
     * @param authorizedFields The authorized fields.
     * @param prefix The prefix for nested fields.
     * @return A list of unauthorized fields.
     */
    @SuppressWarnings("unchecked")
    protected static List<String> findUnAuthorizedFields(
        Map<String, Object> requestPayload,
        Map<String, Object> authorizedFields,
        String prefix
    ) {
        List<String> unAuthorizedFields = new ArrayList<>();
        for (Map.Entry<String, Object> entry : requestPayload.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            String fullKey = prefix + field;
            if (!authorizedFields.containsKey(field)) {
                unAuthorizedFields.add(fullKey);
            } else if (isMap(value) && isMap(authorizedFields.get(field))) {
                unAuthorizedFields.addAll(
                    findUnAuthorizedFields(
                        (Map<String, Object>) value,
                        (Map<String, Object>) authorizedFields.get(field),
                        fullKey + "."
                    )
                );
            }
        }
        return unAuthorizedFields;
    }

    /**
     * Find missing fields from request.
     *
     * @param authorizedFields The authorized fields.
     * @param requestPayload The request payload.
     * @param prefix The prefix for nested fields.
     * @return A map of missing fields with their error messages.
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, String> findMissingFields(Map<String, Object> authorizedFields, Map<String, Object> requestPayload, String prefix) {
        Map<String, String> missingFields = new HashMap<>();
        for (Map.Entry<String, Object> entry : authorizedFields.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            String fullKey = prefix + field;
            if (Boolean.TRUE.equals(value) && !requestPayload.containsKey(field)) {
                missingFields.put(fullKey, isMap(value) ? "required field type not matching object" : "required");
            } else if (requestPayload.containsKey(field)) {
                if (isMap(value) && !(requestPayload.get(field) instanceof Map)) {
                    missingFields.put(fullKey, "required field type not matching array");
                } else if (isMap(requestPayload.get(field)) && isMap(value)) {
                    missingFields.putAll(findMissingFields((Map<String, Object>) value, (Map<String, Object>) requestPayload.get(field), fullKey + "."));
                }
            }
        }
        return missingFields;
    }

    /**
     * Check if the value is an object.
     *
     * @param value The value to check.
     * @return True if the value is a map, false otherwise.
     */
    protected static boolean isMap(Object value) {
        return value instanceof Map;
    }

    /**
     * All possible request fields. Set field value to null by default.
     * Even if the field is not required, you have to register it into the fields list.
     * This will allow you to control all fields in the request and avoid having unexpected fields.
     * This method should be overridden by subclasses to provide the specific fields relevant to that request type.
     *
     * @return A map of possible fields with their default values.
     */
    protected Map<String, Object> getRequestPossibleFields() {
        return Map.of();
    }
}
