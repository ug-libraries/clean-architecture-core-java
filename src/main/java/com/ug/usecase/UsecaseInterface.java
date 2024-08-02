package com.ug.usecase;

import com.ug.exception.BaseException;
import com.ug.presenter.PresenterInterface;
import com.ug.request.RequestInterface;

public interface UsecaseInterface {
    /**
     * Execute the application request.
     */
    void execute() throws BaseException;

    /**
     * Set applicative request to be processed by usecase.
     *
     * @param request The request to be processed.
     * @return The current instance of the use case.
     */
    UsecaseInterface withRequest(RequestInterface request);

    /**
     * Set presenter to get usecase response.
     *
     * @param presenter The presenter to get the response.
     * @return The current instance of the use case.
     */
    UsecaseInterface withPresenter(PresenterInterface presenter);
}
