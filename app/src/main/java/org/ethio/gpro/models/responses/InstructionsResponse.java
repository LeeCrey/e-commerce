package org.ethio.gpro.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstructionsResponse {
    @JsonProperty("okay")
    private Boolean okay;
    @JsonProperty("message")
    private String message;

    public Boolean getOkay() {
        return okay;
    }

    public String getMessage() {
        return message;
    }

    public void setOkay(Boolean okay) {
        this.okay = okay;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
