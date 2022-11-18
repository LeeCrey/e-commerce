package org.ethio.gpro.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.ethio.gpro.models.FormErrors;

public class RegistrationResponse {
    @JsonProperty("okay")
    private Boolean okay;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private FormErrors errors;

    public Boolean getOkay() {
        return okay;
    }

    public void setOkay(Boolean okay) {
        this.okay = okay;
    }

    public String getMessage() {
        return message;
    }

    public void setError(String message) {
        this.message = message;
    }

    public FormErrors getErrors() {
        return errors;
    }

    public void setErrors(FormErrors errors) {
        this.errors = errors;
    }
}
