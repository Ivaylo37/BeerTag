package com.company.web.springdemo.models;

public class User {

    private int id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isAdmin;

    public User() {
    }

    public User(int id, String username, boolean isAdmin) {
        this.id = id;
        this.userName = username;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}