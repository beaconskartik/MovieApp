/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.lib;

import com.movie.lib.models.Response;
import com.movie.lib.models.Review;

import rx.Observable;

public interface MovieDBApi {

    Observable<Response> getLatestMovies(int page);

    Observable<Review> getMovieReview(String movieId);
}
