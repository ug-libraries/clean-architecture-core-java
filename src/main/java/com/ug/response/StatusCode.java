package com.ug.response;

public enum StatusCode {
    OK(200),
    NO_CONTENT(204),
    BAD_REQUEST(400);

    private final int value;

    StatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
