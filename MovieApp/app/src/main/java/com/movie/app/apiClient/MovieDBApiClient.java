/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.apiClient;

import com.movie.lib.MovieDBApi;
import com.movie.lib.MovieDBApiBuilder;
import com.movie.lib.models.Response;
import com.movie.lib.models.Review;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Observable;

public class MovieDBApiClient {

    private static volatile MovieDBApiClient movieDBApiClient;
    private final MovieDBApi movieDBApi;

    private MovieDBApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        movieDBApi = MovieDBApiBuilder.create(client);
    }

    public static MovieDBApiClient getInstance() {
        if (movieDBApiClient == null) {
            movieDBApiClient = new MovieDBApiClient();
        }
        return movieDBApiClient;
    }

    public Observable<Response> getMoviesListFromAPIPageWise(int pageNumber) {
        return movieDBApi.getLatestMovies(pageNumber);
    }

    public Observable<Review> getMovieReviewsFromAPI(String movieID) {
        return movieDBApi.getMovieReview(movieID);
    }
}
