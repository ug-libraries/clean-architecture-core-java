package com.ug.response;

import com.ug.enums.Status;

import java.util.HashMap;
import java.util.Map;

public class Response implements ResponseInterface {
    private final boolean success;
    private final int statusCode;
    private final String message;
    private final Map<String, Object> data;

    public Response(boolean success, int statusCode, String message, Map<String, Object> data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static Response create(boolean success, int statusCode, String message, Map<String, Object> data) {
        return new Response(success, statusCode, message, data);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object get(String fieldName) {
        Object value = data;
        for (String key : fieldName.split("\\.")) {
            if (!(value instanceof Map)) {
                return null;
            }
            value = ((Map<String, Object>) value).get(key);
            if (value == null) {
                return null;
            }
        }
        return value;
    }

    @Override
    public Map<String, Object> output() {
        Map<String, Object> output = new HashMap<>();
        output.put("status", status());
        output.put("code", statusCode);
        output.put("message", message);
        output.putAll(mapDataKeyAccordingToResponseStatus());
        return output;
    }

    private String status() {
        return isSuccess() ? Status.SUCCESS.getValue() : Status.ERROR.getValue();
    }

    private Map<String, Object> mapDataKeyAccordingToResponseStatus() {
        Map<String, Object> map = new HashMap<>();
        if (isSuccess()) {
            map.put("data", this.getData());
        } else {
            map.put("details", this.getData());
        }
        return map;
    }
}