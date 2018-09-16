/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.lib.apis;

import com.movie.lib.models.Response;
import com.movie.lib.models.Review;
import com.movie.lib.utils.MovieRetrofitUtils;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitApi {

    @GET(MovieRetrofitUtils.LATEST_MOVIES_URL)
    Observable<Response> getLatestMovies(@Query(MovieRetrofitUtils.PAGE) int page);

    @GET(MovieRetrofitUtils.REVIEW_URL)
    Observable<Review> getReviews(@Path(MovieRetrofitUtils.MOVIE_ID) String movieId);

}
