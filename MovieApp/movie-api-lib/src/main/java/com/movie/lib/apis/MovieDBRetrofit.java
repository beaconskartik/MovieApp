/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.lib.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movie.lib.MovieDBApi;
import com.movie.lib.models.Response;
import com.movie.lib.models.Review;
import com.movie.lib.utils.MovieRetrofitUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MovieDBRetrofit implements MovieDBApi {

    private final RetrofitApi retrofitAPI;

    public MovieDBRetrofit(OkHttpClient client) {
        retrofitAPI = getApiService(client);
    }

    @Override
    public Observable<Response> getLatestMovies(int page) {
        return retrofitAPI.getLatestMovies(page);
    }

    @Override
    public Observable<Review> getMovieReview(String movieId) {
        return retrofitAPI.getReviews(movieId);
    }

    private RetrofitApi getApiService(OkHttpClient client) {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieRetrofitUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .client(client)
                .build();
        return retrofit.create(RetrofitApi.class);
    }
}
