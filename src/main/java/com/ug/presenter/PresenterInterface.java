package com.ug.presenter;

import com.ug.response.ResponseInterface;

import java.util.Map;

public interface PresenterInterface {
    /**
     * Use by use case to send application response.
     *
     * @param response The response to be presented.
     */
    void present(ResponseInterface response);

    /**
     * Get the use case response that was sent.
     *
     * @return ResponseInterface The response that was sent, or null if no response was sent.
     */
    ResponseInterface getResponse();

    /**
     * Return the formatted use case response that was sent.
     *
     * @return Object The formatted response.
     */
    Map<String, Object> getFormattedResponse();
}
