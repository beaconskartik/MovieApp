/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.apiClient;

import com.movie.app.model.MovieDataEntry;
import com.movie.app.utils.SubscriberImpl;
import com.movie.lib.models.Movie;
import com.movie.lib.models.Response;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

// TODO <kartik> Can consider merging all these async task since they share common thing
public class MovieFetchListTask {

    // Member variable
    private int mPageNumber;
    private MovieFetchListCallback mFetchListCallback;
    private CompositeSubscription compositeSubscription;
    private List<MovieDataEntry> movieArrayList = new ArrayList<>();


    public MovieFetchListTask(int pageNum, MovieFetchListCallback callback) {
        mPageNumber = pageNum;
        mFetchListCallback = callback;
        compositeSubscription = new CompositeSubscription();
        onPreExecute();
        fetchList();
    }

    private void onPreExecute() {
        if (mFetchListCallback != null) {
            mFetchListCallback.onStart();
        }
    }

    private void fetchList() {
        compositeSubscription.add(MovieDBApiClient.getInstance().getMoviesListFromAPIPageWise(mPageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        movieArrayList = convertMovieIntoMovieDataEntry(response);
                        onPostExecute(movieArrayList);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        movieArrayList = null;
                        onPostExecute(movieArrayList);
                    }
                })
                .subscribe(new SubscriberImpl<>("Movie_debug", "fetchList")));
    }

    private void onPostExecute(List<MovieDataEntry> movieDataEntries) {
        if (mFetchListCallback != null) {
            compositeSubscription.clear();
            mFetchListCallback.onCompletion(movieDataEntries);
        }
    }

    public void onCancelled() {
        if (mFetchListCallback != null) {
            compositeSubscription.clear();
            mFetchListCallback.onCancel();
        }
    }

    public interface MovieFetchListCallback {
        void onStart();
        void onCancel();
        void onCompletion(List<MovieDataEntry> movieArrayList);
    }

    private List<MovieDataEntry> convertMovieIntoMovieDataEntry(Response response) {
        List<MovieDataEntry> movieDataEntryList = new ArrayList<>();
        List<Movie> movieList = response.getResults();

        for (Movie movie : movieList) {
            movieDataEntryList.add(new MovieDataEntry(movie.getId(), movie.getOriginalLanguage(),
                    movie.getOriginalTitle(), movie.getOverview(), movie.getReleaseDate(), movie.getPosterPath(),
                    movie.getPopularity(), movie.getTitle(), movie.getVideo(), movie.getVoteAverage(), movie.getVoteCount().toString()));
        }
        return movieDataEntryList;
    }
}
