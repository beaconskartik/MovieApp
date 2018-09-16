/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.movielist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.movie.app.MovieConstants;
import com.movie.app.MovieFragment;
import com.movie.app.R;
import com.movie.app.model.MovieDataEntry;
import com.movie.app.apiClient.MovieFetchListTask;
import com.movie.app.movedetail.MovieDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends MovieFragment {

    // Member variable
    private MovieFetchListTask mMovieFetchListAsyncTask;
    private RecyclerView mMovieListRecyclerView;
    private MovieListAdapter mMovieRecyclerViewListAdapter;
    private LinearLayout mProgressLayout;
    private FloatingActionButton mMovieFilterFAB;

    private MenuItem mSearchMenuItem;
    private MovieListSearchView mSearchView;
    private int mPageNum = 1;

    // Endless Scrolling Variable
    private boolean mLoading = true;
    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;


    public MovieListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list);
        mMovieRecyclerViewListAdapter = new MovieListAdapter(getActivity(),
                new MovieListAdapter.MovieListItemOnClickListener() {
                    @Override
                    public void onItemClick(View v, MovieDataEntry entry) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(MovieConstants.MOVIE_ID_TAG, entry.getMovieID());
                        arguments.putString(MovieConstants.MOVIE_TITLE_TAG, entry.getMovieTitle());
                        arguments.putString(MovieConstants.MOVIE_RELEASE_YEAR_TAG, entry.getMovieReleaseDate());
                        arguments.putString(MovieConstants.MOVIE_OVERVIEW_TAG, entry.getMovieOverview());
                        arguments.putString(MovieConstants.MOVIE_VOTE_COUNT_TAG, entry.getMovieVoteCount());
                        arguments.putString(MovieConstants.MOVIE_POSTER_PATH_TAG, entry.getMoviePosterPath());

                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, fragment).addToBackStack("MovieDetailFragment")
                                .commit();
                    }
                });

        // setting up layout manager + adapter
        mMovieListRecyclerView.setHasFixedSize(true);
        mMovieListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mMovieListRecyclerView.setAdapter(mMovieRecyclerViewListAdapter);

        // Fetching movies list
        fetchMovieList(mPageNum, true);

        // Endless scrolling
        mMovieListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mMovieListRecyclerView
                            .getLayoutManager();

                    mVisibleItemCount = mMovieListRecyclerView.getLayoutManager().getChildCount();
                    mTotalItemCount = layoutManager.getItemCount();
                    mPastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (mLoading) {
                        if ((mVisibleItemCount + mPastVisiblesItems) >= mTotalItemCount) {
                            mLoading = false;
                            fetchMovieList(mPageNum, false);
                        }
                    }
                }
            }
        });

        mMovieFilterFAB = (FloatingActionButton) view.findViewById(R.id.filter_by_date);
        mMovieFilterFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchView != null) {
                    if (mSearchMenuItem.isVisible()) {
                        mSearchMenuItem.expandActionView();
                    }
                }
            }
        });
    }

    @Override
    public boolean handleBackButton() {
        mMovieFetchListAsyncTask.onCancelled();
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mSearchView = new MovieListSearchView(getContext());
        mSearchView.setQueryHint("Please enter the year to filter");
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String arg0) {
                return false;
            }

            public boolean onQueryTextChange(String searchString) {
                if (mMovieRecyclerViewListAdapter != null) {
                    ArrayList<MovieDataEntry> list = (ArrayList<MovieDataEntry>) mMovieRecyclerViewListAdapter
                            .filter(searchString);
                    if (list != null && !list.isEmpty()) {
                        mMovieRecyclerViewListAdapter.addAllMovies(list);
                    } else {
                        Toast.makeText(getActivity(), "No movie in this year !!", Toast.LENGTH_LONG);
                    }
                }
                return false;
            }
        });

        mSearchMenuItem = menu.findItem(R.id.movie_filter_search);
        mSearchMenuItem.setActionView(mSearchView);
        mSearchMenuItem.setVisible(mMovieRecyclerViewListAdapter.getItemCount() != 0);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mSearchMenuItem.setVisible(mMovieRecyclerViewListAdapter.getItemCount() != 0);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            fetchMovieList(mPageNum, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MovieListSearchView
            extends SearchView {
        public MovieListSearchView(Context context) {
            super(context);
        }

        @Override
        public void onActionViewCollapsed() {
            super.onActionViewCollapsed();
            mMovieFilterFAB.hide();
        }

        @Override
        public void onActionViewExpanded() {
            super.onActionViewExpanded();
            mMovieFilterFAB.show();
        }
    }

    private void fetchMovieList(final int pageNumber, final boolean showFullProgressBar) {
        // Calling AsyncTask to fetch movies
        // TODO Kartik : fix this
        mMovieFetchListAsyncTask = new MovieFetchListTask(pageNumber, new MovieFetchListTask
                .MovieFetchListCallback() {
            @Override
            public void onStart() {
                mProgressLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                if (showFullProgressBar) {
                    mProgressLayout.setVisibility(View.VISIBLE);
                } else {
                    mMovieRecyclerViewListAdapter.showProgressBar(true);
                    mMovieRecyclerViewListAdapter.addItem(null);
                    mMovieRecyclerViewListAdapter
                            .notifyItemInserted(mMovieRecyclerViewListAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onCancel() {
                if (showFullProgressBar) {
                    mProgressLayout.setVisibility(View.GONE);
                } else {
                    mMovieRecyclerViewListAdapter.showProgressBar(false);
                    mMovieRecyclerViewListAdapter
                            .removeItemAtPosition(mMovieRecyclerViewListAdapter.getItemCount() - 1);
                    mMovieRecyclerViewListAdapter
                            .notifyItemRemoved(mMovieRecyclerViewListAdapter.getItemCount());
                }
            }

            @Override
            public void onCompletion(List<MovieDataEntry> movieArrayList) {
                if (showFullProgressBar) {
                    mProgressLayout.setVisibility(View.GONE);
                } else {
                    mLoading = true;
                    mMovieRecyclerViewListAdapter.showProgressBar(false);
                    mMovieRecyclerViewListAdapter
                            .removeItemAtPosition(mMovieRecyclerViewListAdapter.getItemCount() - 1);
                    mMovieRecyclerViewListAdapter
                            .notifyItemRemoved(mMovieRecyclerViewListAdapter.getItemCount());
                }
                if (movieArrayList != null) {
                    if (!movieArrayList.isEmpty()) {
                        mPageNum++;
                        if (mSearchMenuItem != null) {
                            mSearchMenuItem.setVisible(true);
                        }
                    }
                    getActivity().invalidateOptionsMenu();

                    if (showFullProgressBar) {
                        mMovieRecyclerViewListAdapter.clearAdapter();
                        mMovieRecyclerViewListAdapter.addAllMovies(movieArrayList);
                    } else {
                        mMovieRecyclerViewListAdapter.addAll(movieArrayList);
                    }

                } else {
                    View view = getView();
                    if (view != null) {
                        Snackbar.make(getView(), "Error in fetching data",
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
