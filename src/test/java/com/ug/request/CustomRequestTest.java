package com.ug.request;

import com.ug.enums.Status;
import com.ug.exception.BadRequestContentException;
import com.ug.response.StatusCode;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CustomRequestTest {
    @Test
    public void shouldBeAbleToBuildCustomNewRequest() throws BadRequestContentException {
        class CustomRequest extends Request {}
        RequestInterface customRequest = new CustomRequest();
        RequestInterface instanceRequest = customRequest.createFromPayload(Map.of());
        assertTrue(instanceRequest instanceof CustomRequest);
    }

    @Test
    public void shouldBeAbleToBuildCustomNewRequestAndGetRequestId() throws BadRequestContentException {
        class CustomRequest extends Request {}
        RequestInterface customRequest = new CustomRequest();
        RequestInterface instanceRequest = customRequest.createFromPayload(Map.of());
        assertNotNull(instanceRequest.getRequestId());
    }

    @Test
    public void shouldBeAbleToBuildCustomNewRequestWithParameters() throws BadRequestContentException {
        class CustomRequest extends Request {
            @Override
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", true
                );
            }
        }

        RequestInterface customRequest = new CustomRequest();
        RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
            "field_1", true,
            "field_2", true
        ));
        assertTrue(instanceRequest instanceof CustomRequest);
    }

    @Test
    public void shouldBeAbleToBuildCustomNewRequestWithOptionalParameters() throws BadRequestContentException {
        class CustomRequest extends Request {
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", false
                );
            }
        }
        RequestInterface customRequest = new CustomRequest();
        RequestInterface instanceRequest = customRequest.createFromPayload(Map.of("field_1", true));
        assertTrue(instanceRequest instanceof CustomRequest);
    }

    @Test
    public void shouldBeAbleToBuildCustomNewRequestAndGetAsObject() throws BadRequestContentException {
        class CustomRequest extends Request {
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", true
                );
            }
        }

        Map<String, Object> payload = Map.of(
            "field_1", new String[]{"yes", "no"},
            "field_2", 3
        );

        RequestInterface customRequest = new CustomRequest();
        RequestInterface instanceRequest = customRequest.createFromPayload(payload);

        assertEquals(payload.get("field_1"), instanceRequest.toArray().get("field_1"));
        assertEquals(payload.get("field_2"), instanceRequest.toArray().get("field_2"));
    }

    @Test
    public void shouldNotBeAbleToBuildCustomNewRequestWithMissingParameters() {
        class CustomRequest extends Request {
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", true
                );
            }
        }

        RequestInterface customRequest = new CustomRequest();
        try {
            RequestInterface instanceRequest = customRequest.createFromPayload(Map.of("field_1", true));
        } catch (BadRequestContentException error) {
            Map<String, Object> errorDetails = error.format();
            assertEquals(Status.ERROR.getValue(), errorDetails.get("status"));
            assertEquals(StatusCode.BAD_REQUEST.getValue(), errorDetails.get("error_code"));
            assertEquals("missing.required.fields", errorDetails.get("message"));
            assertEquals(Map.of("missing_fields", Map.of("field_2", "required")), errorDetails.get("details"));
        }
    }

    @Test
    public void shouldNotBeAbleToBuildCustomNewRequestWithMissingNestedParameters() {
        class CustomRequest extends Request {
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", Map.of("field_3", true)
                );
            }
        }

        RequestInterface customRequest = new CustomRequest();
        try {
            RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
                "field_1", true,
                "field_2", Map.of()
            ));
        } catch (BadRequestContentException error) {
            Map<String, Object> errorDetails = error.format();
            assertEquals(Status.ERROR.getValue(), errorDetails.get("status"));
            assertEquals(StatusCode.BAD_REQUEST.getValue(), errorDetails.get("error_code"));
            assertEquals("missing.required.fields", errorDetails.get("message"));
            assertEquals(Map.of("missing_fields", Map.of("field_2.field_3", "required")), errorDetails.get("details"));
        }
    }

    @Test
    public void shouldNotBeAbleToBuildCustomNewRequestWithMissingArrayParameters() {
        class CustomRequest extends Request {
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", Map.of("field_3", true)
                );
            }
        }

        RequestInterface customRequest = new CustomRequest();
        try {
            RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
                "field_1", true,
                "field_2", 1
            ));
        } catch (BadRequestContentException error) {
            Map<String, Object> errorDetails = error.format();
            assertEquals(Status.ERROR.getValue(), errorDetails.get("status"));
            assertEquals(StatusCode.BAD_REQUEST.getValue(), errorDetails.get("error_code"));
            assertEquals("missing.required.fields", errorDetails.get("message"));
            assertEquals(Map.of("missing_fields", Map.of("field_2", "required field type not matching array")), errorDetails.get("details"));
        }
    }

    @Test
    public void shouldNotBeAbleToBuildCustomNewRequestWithUnrequiredParameters() {
        class CustomRequest extends Request {
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", true
                );
            }
        }

        RequestInterface customRequest = new CustomRequest();
        try {
            RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
                "field_1", true,
                "field_2", true,
                "field_3", 1
            ));
        } catch (BadRequestContentException error) {
            Map<String, Object> errorDetails = error.format();
            assertEquals(Status.ERROR.getValue(), errorDetails.get("status"));
            assertEquals(StatusCode.BAD_REQUEST.getValue(), errorDetails.get("error_code"));
            assertEquals("illegal.fields", errorDetails.get("message"));
            assertEquals(Map.of("unrequired_fields", List.of("field_3")), errorDetails.get("details"));
        }
    }

    @Test
    public void shouldNotBeAbleToBuildCustomNewRequestWithUnrequiredNestedParameters() {
        class CustomRequest extends Request {
            public Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", Map.of("field_3", true)
                );
            }
        }

        RequestInterface customRequest = new CustomRequest();
        try {
            RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
                "field_1", true,
                "field_2", Map.of(
                    "field_3", 2,
                    "field_4", 2
                )
            ));
        } catch (BadRequestContentException error) {
            Map<String, Object> errorDetails = error.format();
            assertEquals(Status.ERROR.getValue(), errorDetails.get("status"));
            assertEquals(StatusCode.BAD_REQUEST.getValue(), errorDetails.get("error_code"));
            assertEquals("illegal.fields", errorDetails.get("message"));
            assertEquals(Map.of("unrequired_fields", List.of("field_2.field_4")), errorDetails.get("details"));
        }
    }
}
