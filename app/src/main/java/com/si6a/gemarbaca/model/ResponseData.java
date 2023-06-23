package com.si6a.gemarbaca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {
    @SerializedName("message") private String message;
    @SerializedName("user") private UserData userData;
    @SerializedName("gemarBacas") private List<GemarBacaData> gemarBacaDataList;

    public String getMessage() {
        return message;
    }

    public UserData getUserData() {
        return userData;
    }

    public List<GemarBacaData> getGemarBacaDataList() {
        return gemarBacaDataList;
    }

}
