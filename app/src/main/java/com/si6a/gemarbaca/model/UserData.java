package com.si6a.gemarbaca.model;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("id") private String id;
    @SerializedName("username") private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
