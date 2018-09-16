/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.movedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.movie.app.MovieConstants;
import com.movie.app.MovieFragment;
import com.movie.app.R;
import com.movie.app.model.MovieDateReviewEntry;
import com.movie.app.apiClient.MovieReviewFetchListTask;

import java.util.List;

public class MovieDetailFragment extends MovieFragment {
    // Member variable
    private String mMovieId;

    private MovieReviewFetchListTask mMovieReviewFetchListAsyncTask;
    private RecyclerView mReviewRecyclerView;
    private MovieReviewListAdapter mReviewMovieAdapter;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        mMovieId = Integer.toString(arguments.getInt(MovieConstants.MOVIE_ID_TAG));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        String title = arguments.getString(MovieConstants.MOVIE_TITLE_TAG);
        String releaseYear = arguments.getString(MovieConstants.MOVIE_RELEASE_YEAR_TAG);
        String overview = arguments.getString(MovieConstants.MOVIE_OVERVIEW_TAG);
        String voteCount = arguments.getString(MovieConstants.MOVIE_VOTE_COUNT_TAG);

        ((TextView) view.findViewById(R.id.detail_title)).setText(title);
        ((TextView) view.findViewById(R.id.detail_release_year)).setText(releaseYear);
        ((TextView) view.findViewById(R.id.detail_overview)).setText(overview);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.detail_rating_bar);
        ratingBar.setRating(Float.parseFloat(voteCount));

        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.detail_reviews_list);
        mReviewMovieAdapter = new MovieReviewListAdapter(new MovieReviewListAdapter
                .MovieReviewReadMoreButtonHanlder() {
            @Override
            public void onButtonClick(View v) {
                // TODO <kartik> need to create seperate layout.
            }
        });

        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mReviewRecyclerView.setAdapter(mReviewMovieAdapter);

        // Calling Async Task to fetch reviews
        mMovieReviewFetchListAsyncTask = new MovieReviewFetchListTask(mMovieId,
                new MovieReviewFetchListTask.MovieReviewFetchListCallback() {
                    @Override
                    public void onStart() {
                        // TODO <kartik> implement progress thing
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onCompletion(List<MovieDateReviewEntry> movieDateReviewEntryArrayList) {

                        if (movieDateReviewEntryArrayList != null && !movieDateReviewEntryArrayList.isEmpty()) {
                            mReviewMovieAdapter.addAllItems(movieDateReviewEntryArrayList);
                        } else {
                            View view = getView();
                            if (view != null) {
                                Snackbar.make(getView(), "Error in fetching movie detail",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.movie_filter_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.movie_filter_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean handleBackButton() {
        mMovieReviewFetchListAsyncTask.onCancelled();
        return super.handleBackButton();
    }
}
