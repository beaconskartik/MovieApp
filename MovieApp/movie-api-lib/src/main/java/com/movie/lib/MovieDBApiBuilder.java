/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.lib;

import com.movie.lib.apis.MovieDBRetrofit;

import okhttp3.OkHttpClient;

public class MovieDBApiBuilder {

    public static MovieDBApi create(OkHttpClient client) {
        return new MovieDBRetrofit(client);
    }
}
