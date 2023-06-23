package com.si6a.gemarbaca.model;

import com.google.gson.annotations.SerializedName;

public class GemarBacaData {

    @SerializedName("_id") private String id;
    @SerializedName("title") private String title;
    @SerializedName("about") private String about;
    @SerializedName("description") private String description;
    @SerializedName("image") private String image;
    @SerializedName("userId") private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
