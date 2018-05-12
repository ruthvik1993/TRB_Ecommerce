package com.thericebag.application.application.Services;

import java.util.Map;

/**
 * Created by abhilash on 01-09-2016.
 */

public class TheRiceBagResponse {

    private boolean mIsSuccess;

    private Map<String, String> mHeaders;

    private String mResponseBody;

    private String mErrorMessage;

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        mIsSuccess = isSuccess;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public String getResponseBody() {
        return mResponseBody;
    }

    public void setResponseBody(String responseBody) {
        mResponseBody = responseBody;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }
}
