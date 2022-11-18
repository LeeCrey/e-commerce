package org.ethio.gpro.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.responses.RegistrationResponse;


public class JsonHelper {
    public static RegistrationResponse parseAccountError(String message) throws JsonProcessingException {
        return new ObjectMapper().readerFor(RegistrationResponse.class).readValue(message);
    }

    public static Customer parseCustomer(String message) throws JsonProcessingException {
        return new ObjectMapper().readerFor(Customer.class).readValue(message);
    }

    public static RegistrationResponse parseSignUpError(String error) throws JsonProcessingException {
        return new ObjectMapper().readerFor(RegistrationResponse.class).readValue(error);
    }
}
