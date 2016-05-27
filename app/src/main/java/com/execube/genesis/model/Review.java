package com.execube.genesis.model;

/**
 * Created by Prateek Phoenix on 5/22/2016.
 */
public class Review {


    private int mId;
    private String mAuthor;
    private String mContent;
    private int mTotalResults;


    public Review() {
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        mTotalResults = totalResults;
    }
}
