package com.ug.response;

import com.ug.enums.Status;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class CustomResponseTest {
    @Test
    public void testGetCustomResponseWithoutContent() {
        Response instanceResponse = Response.create(
                false,
                StatusCode.NO_CONTENT.getValue(),
                "success.message",
                Map.of()
        );
        assertFalse(instanceResponse.isSuccess());
        assertEquals(StatusCode.NO_CONTENT.getValue(), instanceResponse.getStatusCode());
        assertEquals("success.message", instanceResponse.getMessage());
        assertEquals(Map.of(), instanceResponse.getData());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetCustomResponseWithContent() {
        Response instanceResponse = getInstanceResponse();
        assertTrue(instanceResponse.isSuccess());
        assertEquals(StatusCode.OK.getValue(), instanceResponse.getStatusCode());
        assertEquals("success.response", instanceResponse.getMessage());
        assertEquals("yes", instanceResponse.get("field_1"));
        assertNull(instanceResponse.get("field_6"));
        assertEquals(3, instanceResponse.get("field_2.field_3"));
        assertArrayEquals(new String[]{"nice"}, (String[]) instanceResponse.get("field_2.field_4.field_5"));

        Map<String, Object> expectedOutput = instanceResponse.output();
        Map<String, Object> expectedData = (Map<String, Object>) expectedOutput.get("data");

        assertEquals(Status.SUCCESS.getValue(), expectedOutput.get("status"));
        assertEquals(StatusCode.OK.getValue(), expectedOutput.get("code"));
        assertEquals("success.response", expectedOutput.get("message"));
        assertEquals("yes", expectedData.get("field_1"));

        Map<String, Object> retrievedField2 = (Map<String, Object>) expectedData.get("field_2");
        assertEquals(3, retrievedField2.get("field_3"));
    }

    private static Response getInstanceResponse() {
        Map<String, Object> data = Map.of(
            "field_1", "yes",
            "field_2", Map.of(
                "field_3", 3,
                "field_4", Map.of(
                    "field_5", new String[]{"nice"},
                    "field_2", true
                )
            )
        );

        return Response.create(
            true,
            StatusCode.OK.getValue(),
            "success.response",
            data
        );
    }
}
