package com.mohamed.newsapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 09/03/18.
 */

public class Source {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;

    public Source(String mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
