package com.redmadintern.mikhalevich.security.model.server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("error_type")
    @Expose
    private String errorType;
    @Expose
    private Integer code;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;

    /**
     *
     * @return
     * The errorType
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     *
     * @return
     * The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @return
     * The errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}