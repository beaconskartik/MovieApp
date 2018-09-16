/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.apiClient;

import com.movie.app.model.MovieDateReviewEntry;
import com.movie.app.utils.SubscriberImpl;
import com.movie.lib.models.Review;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

// TODO <kartik> Can consider merging all these async task since they share common thing
public class MovieReviewFetchListTask {

    // Member variable
    private String mMovieID;
    private MovieReviewFetchListCallback mReviewFetchListCallback;
    private List<MovieDateReviewEntry> movieDateReviewEntries;
    private CompositeSubscription compositeSubscription;

    public MovieReviewFetchListTask(String movieID, MovieReviewFetchListCallback callback) {
        mMovieID = movieID;
        mReviewFetchListCallback = callback;
        movieDateReviewEntries = new ArrayList<>();
        compositeSubscription = new CompositeSubscription();
        onPreExecute();
        fetchReviews();
    }

    private void onPreExecute() {
        if (mReviewFetchListCallback != null) {
            mReviewFetchListCallback.onStart();
        }
    }

    private void fetchReviews() {
        compositeSubscription.add(MovieDBApiClient.getInstance().getMovieReviewsFromAPI(mMovieID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Review>() {
                    @Override
                    public void call(Review review) {
                        movieDateReviewEntries = convertMovieReview(review);
                        onPostExecute(movieDateReviewEntries);
                    }
                }).doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        movieDateReviewEntries = null;
                        onPostExecute(movieDateReviewEntries);
                    }
                }).subscribe(new SubscriberImpl<>("Movie_debug", "fetchReviews")));
    }

    private void onPostExecute(List<MovieDateReviewEntry> reviewEntryList) {
        compositeSubscription.clear();
        if (mReviewFetchListCallback != null) {
            mReviewFetchListCallback.onCompletion(reviewEntryList);
        }
    }

    public void onCancelled() {
        compositeSubscription.clear();
        if (mReviewFetchListCallback != null) {
            mReviewFetchListCallback.onCancel();
        }
    }

    public interface MovieReviewFetchListCallback {
        public void onStart();

        public void onCancel();

        public void onCompletion(List<MovieDateReviewEntry> movieDateReviewEntryArrayList);
    }

    private List<MovieDateReviewEntry> convertMovieReview(Review review) {
        List<MovieDateReviewEntry> reviewEntryList = new ArrayList<>();
        List<Review.Result> reviewList = review.getResults();

        for (Review.Result reviewResult : reviewList) {
            reviewEntryList.add(new MovieDateReviewEntry(reviewResult.getId(), reviewResult.getAuthor(),
                    reviewResult.getContent(), reviewResult.getUrl()));
        }
        return reviewEntryList;
    }
}
