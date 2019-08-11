package com.example.project.Bean;

public class Type {
    private int id;
    private String username;
    private String type;

    public Type(){

    }
    public Type(String username,String type){
        super();
        this.username=username;
        this.type=type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
