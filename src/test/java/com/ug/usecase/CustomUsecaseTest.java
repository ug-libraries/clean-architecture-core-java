package com.ug.usecase;

import com.ug.enums.Status;
import com.ug.exception.BadRequestContentException;
import com.ug.exception.BaseException;
import com.ug.presenter.Presenter;
import com.ug.presenter.PresenterInterface;
import com.ug.request.Request;
import com.ug.response.Response;
import com.ug.response.ResponseInterface;
import com.ug.response.StatusCode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CustomUsecaseTest {
    private PresenterInterface instancePresenter;

    @Before
    public void setUp() {
        class CustomPresenter extends Presenter {}
        instancePresenter = new CustomPresenter();
    }

    @Test
    public void testExecuteCustomUsecaseWithPresenterWithoutRequest() throws Exception {
        class CustomUsecase extends Usecase {
            @Override
            public void execute() {
                this.presenter.present(
                    Response.create(
                        true,
                        StatusCode.NO_CONTENT.getValue(),
                        "success.response", Map.of(
                            "field_1", "yes",
                            "field_2", new HashMap<>()
                        )
                    )
                );
            }
        }

        CustomUsecase instanceUsecase = new CustomUsecase();
        instanceUsecase
            .withPresenter(instancePresenter)
            .execute();

        ResponseInterface response = instancePresenter.getResponse();
        assertNotNull(response);
        assertTrue(response instanceof Response);
        assertTrue(response.isSuccess());
        assertEquals("success.response", response.getMessage());
        assertEquals(StatusCode.NO_CONTENT.getValue(), response.getStatusCode());
        assertEquals(Map.of("field_1", "yes", "field_2", new HashMap<>()), response.getData());
        assertEquals("yes", response.get("field_1"));
        assertNull(response.get("field_3"));
        assertEquals(Map.of(
            "status", Status.SUCCESS.getValue(),
            "code", StatusCode.NO_CONTENT.getValue(),
            "message", "success.response",
            "data", Map.of("field_1", "yes", "field_2", new HashMap<>())
        ), instancePresenter.getFormattedResponse());
    }

    @Test
    public void testExecuteCustomUsecaseWithRequestAndPresenter() throws Exception {
        class CustomUsecase extends Usecase {
            @Override
            public void execute() {
                this.presenter.present(
                    Response.create(true, StatusCode.OK.getValue(), "success.response", this.getRequestData())
                );
            }
        }

        class CustomRequest extends Request {
            @Override
            protected Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_2", true
                );
            }
        }

        Map<String, Object> payload = Map.of(
            "field_1", true,
            "field_2", 3
        );

        CustomUsecase instanceUsecase = new CustomUsecase();
        instanceUsecase.withRequest((new CustomRequest()).createFromPayload(payload))
                .withPresenter(instancePresenter)
                .execute();

        ResponseInterface response = instancePresenter.getResponse();
        assertNotNull(response);
        assertTrue(response instanceof Response);
        assertTrue(response.isSuccess());
        assertEquals("success.response", response.getMessage());
        assertEquals(StatusCode.OK.getValue(), response.getStatusCode());
        assertEquals(payload, response.getData());
        assertEquals(true, response.get("field_1"));
        assertEquals(3, response.get("field_2"));
        assertEquals(Map.of(
            "status", Status.SUCCESS.getValue(),
            "code", StatusCode.OK.getValue(),
            "message", "success.response",
            "data", payload
        ), instancePresenter.getFormattedResponse());
    }

    @Test
    public void testExecuteCustomUsecaseWithoutRequestAndPresenter() throws BadRequestContentException {
        class CustomUsecase extends Usecase {
            @Override
            public void execute() throws BadRequestContentException {
                throw new BadRequestContentException(
                    new HashMap<String, Object>() {{
                        put("message", "BadRequestContentError");
                        put("details", Map.of("field_1", "yes"));
                    }}
                );
            }
        }

        CustomUsecase instanceUsecase = new CustomUsecase();
        try {
            instanceUsecase.execute();
        } catch (BaseException error) {
            Map<String, Object> errorDetails = error.format();
            assertEquals(Status.ERROR.getValue(), errorDetails.get("status"));
            assertEquals(StatusCode.BAD_REQUEST.getValue(), errorDetails.get("error_code"));
            assertEquals("BadRequestContentError", errorDetails.get("message"));
            assertEquals(Map.of("field_1", "yes"), errorDetails.get("details"));
        }
    }

    @Test
    public void testExecuteCustomUsecaseWithRequestWithoutPresenter() {
        class CustomUsecase extends Usecase {
            @Override
            public void execute() throws BadRequestContentException {
                Map<String, Object> requestData = new HashMap<>(this.getRequestData());
                throw new BadRequestContentException(
                    new HashMap<String, Object>() {{
                        put("message", "BadRequestContentError");
                        put("details", requestData);
                    }}
                );
            }
        }

        class CustomRequest extends Request {
            protected Map<String, Object> getRequestPossibleFields() {
                return Map.of(
                    "field_1", true,
                    "field_4", true
                );
            }
        }

        Map<String, Object> payload = Map.of(
            "field_1", true,
            "field_4", new String[]{"nice", "good"}
        );

        try {
            CustomUsecase instanceUsecase = new CustomUsecase();
            instanceUsecase
                .withRequest((new CustomRequest()).createFromPayload(payload))
                .execute();
        } catch (BaseException error) {
            Map<String, Object> errorDetails = error.format();
            assertEquals(Status.ERROR.getValue(), errorDetails.get("status"));
            assertEquals(StatusCode.BAD_REQUEST.getValue(), errorDetails.get("error_code"));
            assertEquals("BadRequestContentError", errorDetails.get("message"));
            assertEquals(payload, errorDetails.get("details"));
        }
    }
}
