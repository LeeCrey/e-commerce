package org.ethio.gpro.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

// for session and registration
@JsonRootName(value = "customer")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Customer {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("password_confirmation")
    private String passwordConfirmation;

    @JsonProperty("profile_image")
    private String profileImageUrl;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @JsonIgnore
    public Customer setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        return this;
    }

    @JsonIgnore
    public CharSequence getFullName() {
        return firstName.concat(" ").concat(lastName);
    }

    @JsonIgnore
    public Customer setFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        return this;
    }

    public Integer getId() {
        return id;
    }
}
