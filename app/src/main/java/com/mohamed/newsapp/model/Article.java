package com.mohamed.newsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 09/03/18.
 */

public class Article implements Parcelable {

    @SerializedName("source")
    private Source mSource;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("urlToImage")
    private String mUrlToImage;
    @SerializedName("publishedAt")
    private String mPublishedAt;


    public Article(Source mSource, String mAuthor, String mTitle, String mDescription, String mUrl, String mUrlToImage, String mPublishedAt) {
        this.mSource = mSource;
        this.mAuthor = mAuthor;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mUrl = mUrl;
        this.mUrlToImage = mUrlToImage;
        this.mPublishedAt = mPublishedAt;
    }

    private Article(Parcel in) {
        mAuthor = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mUrl = in.readString();
        mUrlToImage = in.readString();
        mPublishedAt = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public Source getmSource() {
        return mSource;
    }

    public void setmSource(Source mSource) {
        this.mSource = mSource;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmUrlToImage() {
        return mUrlToImage;
    }

    public void setmUrlToImage(String mUrlToImage) {
        this.mUrlToImage = mUrlToImage;
    }

    public String getmPublishedAt() {
        return mPublishedAt;
    }

    public void setmPublishedAt(String mPublishedAt) {
        this.mPublishedAt = mPublishedAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mUrl);
        dest.writeString(mUrlToImage);
        dest.writeString(mPublishedAt);
    }
}
