package com.ug.usecase;

import com.ug.presenter.PresenterInterface;
import com.ug.request.RequestInterface;
import com.ug.response.ResponseInterface;

import java.util.Map;

public abstract class Usecase implements UsecaseInterface {
    protected RequestInterface request;
    protected PresenterInterface presenter;

    /**
     * Set presenter to get usecase response.
     *
     * @param presenter The presenter to get usecase response
     * @return this
     */
    @Override
    public UsecaseInterface withPresenter(PresenterInterface presenter) {
        this.presenter = presenter;
        return this;
    }

    /**
     * Set applicative request to be processed by usecase.
     *
     * @param request The applicative request
     * @return this
     */
    @Override
    public UsecaseInterface withRequest(RequestInterface request) {
        this.request = request;
        return this;
    }

    /**
     * Transport given response to infrastructure layer.
     *
     * @param response The response to transport
     */
    protected void presentResponse(ResponseInterface response) {
        this.presenter.present(response);
    }

    /**
     * Get request data.
     *
     * @return Map<String, Object>
     */
    protected Map<String, Object> getRequestData() {
        return this.request.toArray();
    }

    /**
     * Get application request unique id.
     */
    protected String getRequestId() {
        return this.request.getRequestId();
    }

    protected Object getField(String fieldName) {
        return this.request.get(fieldName);
    }

    protected Object getField(String fieldName, Object defaultValue) {
        return this.request.get(fieldName, defaultValue);
    }
}
