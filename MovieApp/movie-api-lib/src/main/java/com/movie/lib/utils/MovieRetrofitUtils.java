/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.lib.utils;

import com.movie.lib.BuildConfig;

public final class MovieRetrofitUtils {


    private static final String API_KEY = BuildConfig.API_KEY;

    public static final String BASE_URL = "https://api.themoviedb.org";
    public static final String LATEST_MOVIES_URL = "/3/movie/now_playing?api_key=" + API_KEY;
    public static final String REVIEW_URL = "/3/movie/{movie_id}/reviews?api_key=" + API_KEY;
    public static final String PAGE = "page";
    public static final String MOVIE_ID = "movie_id";
}
