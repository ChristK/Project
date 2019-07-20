package com.example.project.Bean;

public class User {
    private int id;
    private String Username;
    private String Password;
    private String Email;

    public User() {
    }

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.Username = username;
        this.Password = password;
        this.Email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }



}
