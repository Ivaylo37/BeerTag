package com.company.web.springdemo.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {
    @NotNull(message = "User name can't be empty")
    @Size(min = 2, max = 20, message = "Name should be between 2 and 20 symbols")
    private String userName;
    @NotNull(message = "Password can't be empty")
    @Size(min = 8, message = "Password should be at least 8 symbols")
    private String password;
    @NotNull(message = "First name can't be empty")
    private String firstName;
    @NotNull(message = "Last name can't be empty")
    private String lastName;
    @NotNull(message = "Email can't be empty")
    private String email;


    public UserDto() {
    }

    public UserDto(String userName, String password, String firstName, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
