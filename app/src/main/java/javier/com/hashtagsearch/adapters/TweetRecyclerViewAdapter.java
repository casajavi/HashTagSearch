package javier.com.hashtagsearch.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.percent.PercentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import javier.com.hashtagsearch.R;
import javier.com.hashtagsearch.models.SearchTweet;
import javier.com.hashtagsearch.utils.CropCircleTransformation;

public class TweetRecyclerViewAdapter extends RecyclerView.Adapter<TweetRecyclerViewAdapter.ViewHolder> {

    private List<SearchTweet> mValues;
    private final OnItemListener mListener;
    private Context context;

    public TweetRecyclerViewAdapter(List<SearchTweet> items, OnItemListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    public void updateItems(List<SearchTweet> mValues) {
        this.mValues = mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        SearchTweet currentTweet = mValues.get(position);

        holder.searchTweet = currentTweet;

        Glide.with(context.getApplicationContext()).load(currentTweet.getUserImageUrl())
                .bitmapTransform(new CropCircleTransformation(context)).into(holder.imageViewUser);

        holder.textViewProfileName.setText(currentTweet.getProfileName());
        holder.textViewUsername.setText(currentTweet.getUsername());
        holder.textViewContent.setText(currentTweet.getTweetContent());

        if (currentTweet.getTweetImage() != null) {
            holder.layoutTweetImage.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext()).load(currentTweet.getTweetImage())
                    .into(holder.imageViewTweet);
        } else {
            holder.layoutTweetImage.setVisibility(View.GONE);
            Glide.clear(holder.imageViewTweet);
            holder.imageViewTweet.setImageDrawable(null);
        }

        holder.imageViewTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemImageInteraction(holder.imageViewTweet.getDrawable());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_content)
        FrameLayout contentView;
        @BindView(R.id.imageView_user)
        ImageView imageViewUser;
        @BindView(R.id.text_profile_name)
        TextView textViewProfileName;
        @BindView(R.id.text_username)
        TextView textViewUsername;
        @BindView(R.id.text_tweet_content)
        TextView textViewContent;
        @BindView(R.id.layout_tweet_image)
        PercentFrameLayout layoutTweetImage;
        @BindView(R.id.imageView_tweet)
        ImageView imageViewTweet;

        SearchTweet searchTweet;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnItemListener {
        // TODO: Update argument type and name
        void onItemImageInteraction(Drawable drawable);
    }
}
