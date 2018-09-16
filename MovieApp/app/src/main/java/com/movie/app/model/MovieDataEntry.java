/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.model;

public class MovieDataEntry {
    public MovieDataEntry(int movieID, String movieOriginalLanguage,
                          String movieOriginalTitle, String movieOverview, String movieReleaseDate, String moviePosterPath,
                          Double moviePopularity, String movieTitle, boolean movieVideo, Double movieVoteAverage,
                          String movieVoteCount) {
        mMovieID = movieID;
        mMovieOriginalLanguage = movieOriginalLanguage;
        mMovieOriginalTitle = movieOriginalTitle;
        mMovieOverview = movieOverview;
        mMovieReleaseDate = movieReleaseDate;
        mMoviePosterPath = moviePosterPath;
        mMoviePopularity = moviePopularity;
        mMovieTitle = movieTitle;
        mMovieVideo = movieVideo;
        mMovieVoteAverage = movieVoteAverage;
        mMovieVoteCount = movieVoteCount;
    }

    public int getMovieID() {
        return mMovieID;
    }

    public String getMovieOriginalLanguage() {
        return mMovieOriginalLanguage;
    }

    public String getMovieOriginalTitle() {
        return mMovieOriginalTitle;
    }

    public String getMovieOverview() {
        return mMovieOverview;
    }

    public String getMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public String getMoviePosterPath() {
        return mMoviePosterPath;
    }

    public Double getMoviePopularity() {
        return mMoviePopularity;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public boolean getMovieVideo() {
        return mMovieVideo;
    }

    public Double getMovieVoteAverage() {
        return mMovieVoteAverage;
    }

    public String getMovieVoteCount() {
        return mMovieVoteCount;
    }

    private int mMovieID;
    private String mMovieOriginalLanguage;
    private String mMovieOriginalTitle;
    private String mMovieOverview;
    private String mMovieReleaseDate;
    private String mMoviePosterPath;
    private Double mMoviePopularity;
    private String mMovieTitle;
    private boolean mMovieVideo;
    private Double mMovieVoteAverage;
    private String mMovieVoteCount;
}
