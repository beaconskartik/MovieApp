/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.movedetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.movie.app.R;
import com.movie.app.model.MovieDateReviewEntry;
import com.movie.app.utils.MovieDateUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewListAdapter extends RecyclerView.Adapter<MovieReviewListAdapter.ReviewViewHolder> {
    public MovieReviewListAdapter(MovieReviewReadMoreButtonHanlder hanlder) {
        mHandler = hanlder;
        mReviewList = new ArrayList<>();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        ((ReviewViewHolder) holder).bindData(mReviewList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public void addAllItems(List<MovieDateReviewEntry> list) {
        mReviewList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearAdapterWithoutNotifyDataSetChanged() {
        mReviewList.clear();
    }

    public void clearAdapter() {
        mReviewList.clear();
        notifyDataSetChanged();
    }

    public void addItemWithNotifyRangeRefresh(ArrayList<MovieDateReviewEntry> list) {
        int size = mReviewList.size();
        mReviewList.addAll(list);
        notifyItemRangeChanged(size, list.size());
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mAuthor;
        final TextView mReview;
        final Button mReadMoreButton;

        private ReviewViewHolder(View view) {
            super(view);
            mView = view;
            mAuthor = (TextView) view.findViewById(R.id.review_item_author);
            mReview = (TextView) view.findViewById(R.id.review_item_review);
            mReadMoreButton = (Button) view.findViewById(R.id.review_item_readmore);
            mReadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.onButtonClick(v);
                }
            });
        }

        private void bindData(MovieDateReviewEntry entry, int position) {
            final String author = entry.getReviewAuthor();
            final String content = entry.getReviewContent();
            final String contentShort = MovieDateUtils.truncateString(content, 300);
            mAuthor.setText(author);
            mReview.setText(contentShort);
        }
    }

    public interface MovieReviewReadMoreButtonHanlder {
        public void onButtonClick(View view);
    }

    private ArrayList<MovieDateReviewEntry> mReviewList;
    private MovieReviewReadMoreButtonHanlder mHandler;
}
