package org.ethio.gpro.models.responses;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.ethio.gpro.models.FormErrors;

public class RegistrationResponse {
    @JsonProperty("okay")
    private Boolean okay;

    @JsonProperty("message")
    private String msg;

    @JsonProperty("errors")
    private FormErrors errors;

    public Boolean getOkay() {
        return okay;
    }

    public void setOkay(Boolean okay) {
        this.okay = okay;
    }

    public FormErrors getErrors() {
        return errors;
    }

    public void setErrors(FormErrors errors) {
        this.errors = errors;
    }

    @NonNull
    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "okay=" + okay +
                ", message='" + msg + '\'' +
                ", errors=" + errors +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
