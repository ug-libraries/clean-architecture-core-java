package com.ug.presenter;

import com.ug.response.ResponseInterface;

import java.util.Map;

public class Presenter implements PresenterInterface {
    private ResponseInterface response;

    @Override
    public void present(ResponseInterface response) {
        this.response = response;
    }

    @Override
    public ResponseInterface getResponse() {
        return this.response;
    }

    @Override
    public Map<String, Object> getFormattedResponse() {
        return this.response.output();
    }
}
