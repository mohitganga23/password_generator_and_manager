package com.example.passwordgeneratormanager;

public class Password {
    int id;
    String title, password;

    public Password() {}

    public Password(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public Password(int id, String title, String password) {
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
