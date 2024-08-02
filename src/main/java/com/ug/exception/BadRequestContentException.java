package com.ug.exception;

import java.util.Map;

public class BadRequestContentException extends BaseException {
    public BadRequestContentException(Map<String, Object> errors) {
        super(errors);
    }
}
