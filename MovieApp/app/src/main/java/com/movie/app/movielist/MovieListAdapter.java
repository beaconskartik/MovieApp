/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.movielist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movie.app.R;
import com.movie.app.model.MovieDataEntry;
import com.movie.app.utils.MovieDateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO <Kartik> Can we write more generic adapter?
public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean mShowProgressBar = false;
    private static final String DATE_FORMAT_API = "yyyy-MM-dd";
    private static final String DATE_FORMAT_DISPLAY = "yyyy";
    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w500";
    private final Context mContext;
    private ArrayList<MovieDataEntry> mMovieListArray;
    private ArrayList<MovieDataEntry> mMasterListArray;
    private String mSearchString = "";
    private MovieListItemOnClickListener mOnItemClickListener;

    // Constants
    public static final int ITEM_VIEW = 0;
    public static final int ITEM_PROGRESS = 1;


    public MovieListAdapter(Context context, MovieListItemOnClickListener listener) {
        mContext = context;
        mMovieListArray = new ArrayList<>();
        mMasterListArray = new ArrayList<>();
        mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == ITEM_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            holder = new MovieListViewHolder(view);
        } else if (viewType == ITEM_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            holder = new MovieProgressHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_VIEW) {
            ((MovieListViewHolder) holder).bindData(getItemAtPosition(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieListArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mShowProgressBar) {
            return ITEM_PROGRESS;
        }
        return super.getItemViewType(position);
    }

    public MovieDataEntry getItemAtPosition(int position) {
        return mMovieListArray.get(position);
    }

    public void addAllMovies(List<MovieDataEntry> moviesList) {

        if (mSearchString.isEmpty() || mSearchString == "") {
            mMasterListArray.addAll(moviesList);
        }
        mMovieListArray.addAll(moviesList);
        notifyDataSetChanged();
    }

    // TODO <kartik> improve this logic
    // These methods will be used by endless scrolling
    public void addItem(MovieDataEntry item) {
        mMasterListArray.add(item);
        mMovieListArray.add(item);
    }

    public void addAll(List<MovieDataEntry> moviesList) {
        mMasterListArray.addAll(moviesList);
        mMovieListArray.addAll(moviesList);
        notifyItemRangeChanged(mMovieListArray.size() - 1, moviesList.size());
    }

    public void removeItemAtPosition(int position) {
        mMovieListArray.remove(position);
        mMasterListArray.remove(position);
    }

    public void clearAdapter() {
        mMasterListArray.clear();
        mMovieListArray.clear();
        notifyDataSetChanged();
    }

    public void clearAdapterWithoutNotifyDataSet() {
        mMasterListArray.clear();
        mMovieListArray.clear();
    }

    class MovieProgressHolder extends RecyclerView.ViewHolder {
        private MovieProgressHolder(View view) {
            super(view);
            mView = view;
        }

        private View mView;
        private ProgressBar mProgressBar;
        private TextView mTextView;
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {
        private MovieListViewHolder(View view) {
            super(view);
            mView = view;
            mPoster = (ImageView) view.findViewById(R.id.movie_poster);
            mTitle = (TextView) view.findViewById(R.id.movie_title);
            mYear = (TextView) view.findViewById(R.id.movie_year);
        }

        private void bindData(final MovieDataEntry dataEntry, int position) {
            final int id = dataEntry.getMovieID();
            final String posterPath = POSTER_URL + dataEntry.getMoviePosterPath();
            final String title = dataEntry.getMovieTitle();
            String releaseDateString = dataEntry.getMovieReleaseDate();
            Date releaseDate = MovieDateUtils.getStringAsDate(releaseDateString, DATE_FORMAT_API, null);
            final String releaseYear = MovieDateUtils.getDateAsString(releaseDate, DATE_FORMAT_DISPLAY, null);

            Picasso.with(mContext).load(posterPath).into(mPoster);
            mTitle.setText(title);
            mYear.setText(releaseYear);

            if (mOnItemClickListener != null) {
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, dataEntry);
                    }
                });
            }
        }

        final View mView;
        final ImageView mPoster;
        final TextView mTitle;
        final TextView mYear;
    }

    public List<MovieDataEntry> filter(final String searchString) {
        List<MovieDataEntry> filteredModelList = null;
        if (searchString != null) {
            String trimSearchString = searchString.toLowerCase().trim();

            if (!TextUtils.equals(trimSearchString, mSearchString)) {
                mSearchString = trimSearchString;

                if (searchString.length() == 4 || TextUtils.equals(searchString, "")) {
                    filteredModelList = searchedFilterList(mMasterListArray, mSearchString);
                }
            }
        }
        return filteredModelList;
    }

    private List<MovieDataEntry> searchedFilterList(List<MovieDataEntry> movieList, String filterString) {
        List<MovieDataEntry> filteredList = new ArrayList<>();
        if (filterString != null && !TextUtils.equals(filterString, "")) {
            for (MovieDataEntry movieEntry : movieList) {
                String releaseDateString = movieEntry.getMovieReleaseDate();
                Date releaseDate = MovieDateUtils.getStringAsDate(releaseDateString, DATE_FORMAT_API, null);
                String releaseYear = MovieDateUtils.getDateAsString(releaseDate, DATE_FORMAT_DISPLAY, null);

                if (releaseYear != null) {
                    if (releaseYear.contains(filterString)) {
                        filteredList.add(movieEntry);
                    }
                }
            }
        } else {
            return movieList;
        }
        return filteredList;
    }

    public interface MovieListItemOnClickListener {
        void onItemClick(View v, MovieDataEntry entry);
    }

    public void showProgressBar(boolean showProgressBar) {
        mShowProgressBar = showProgressBar;
    }
}
