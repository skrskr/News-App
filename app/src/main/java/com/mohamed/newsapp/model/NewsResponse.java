package com.mohamed.newsapp.model;

import com.google.gson.annotations.SerializedName;
import com.mohamed.newsapp.model.Article;

import java.util.List;

/**
 * Created by mohamed on 09/03/18.
 */

public class NewsResponse {
    @SerializedName("status")
    private String mStatus;
    @SerializedName("totalResults")
    private int mTotalResults;
    @SerializedName("articles")
    private List<Article> mArticles;

    public NewsResponse(String mStatus, int mTotalResults, List<Article> mArticles) {
        this.mStatus = mStatus;
        this.mTotalResults = mTotalResults;
        this.mArticles = mArticles;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public int getmTotalResults() {
        return mTotalResults;
    }

    public void setmTotalResults(int mTotalResults) {
        this.mTotalResults = mTotalResults;
    }

    public List<Article> getmArticles() {
        return mArticles;
    }

    public void setmArticles(List<Article> mArticles) {
        this.mArticles = mArticles;
    }
}
