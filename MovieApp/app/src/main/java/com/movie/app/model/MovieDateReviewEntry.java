/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.model;

public class MovieDateReviewEntry {
    public MovieDateReviewEntry(String reviewID, String reviewAuthor,
                                String reviewContent, String reviewURL) {
        mReviewID = reviewID;
        mReviewAuthor = reviewAuthor;
        mReviewContent = reviewContent;
        mReviewURL = reviewURL;
    }

    public MovieDateReviewEntry(String errorString) {
        mError = errorString;
    }

    public String getReviewID() {
        return mReviewID;
    }

    public String getReviewAuthor() {
        return mReviewAuthor;
    }

    public String getReviewContent() {
        return mReviewContent;
    }

    public String getReviewURL() {
        return mReviewURL;
    }

    private String mReviewID;
    private String mReviewAuthor;
    private String mReviewContent;
    private String mReviewURL;
    private String mError;
}
