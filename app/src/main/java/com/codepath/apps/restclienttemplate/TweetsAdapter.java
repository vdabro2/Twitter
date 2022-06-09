package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.opengles.GL;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    List<Tweet> tweets;
    Context context;

    public TweetsAdapter(Context context,List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tweets.get(position));
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        ImageView embedded;
        TextView tvBody;
        ImageButton ibHeart;
        TwitterClient client;
        TextView tvName;
        TextView tvScreenName;
        TextView tvRelativeTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client = TwitterApp.getRestClient(context); //??????
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            tvName = itemView.findViewById(R.id.tvName);
            ibHeart = itemView.findViewById(R.id.ibHeart);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvName.setText(tweet.user.name);

            if (tweet.user.profileImageUrl != null && tweet.user.profileImageUrl != "" && tweet.user.profileImageUrl != "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png")
                Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(80)).into(ivProfileImage);
            if (tweet.entity != null && tweet.entity.imageURL != "") {
                embedded = itemView.findViewById(R.id.ivEmb);
                embedded.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.entity.imageURL).transform(new RoundedCorners(30)).into(embedded);
            }
            String relative_time = getRelativeTimeAgo(tweet.createdAt);
            tvRelativeTime.setText(relative_time);
            if (tweet.wasLiked == true) {
                ibHeart.setBackgroundResource(R.drawable.ic_vector_heart);
            } else {
                ibHeart.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
            }
            ibHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tweet.wasLiked == false ) {
                        client.publishFavorite(tweet.tweetId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {

                                ibHeart.setBackgroundResource(R.drawable.ic_vector_heart);
                                tweet.wasLiked = true;

                                // change button color
                                Log.i("onCLick", "fav published");

                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("onCLick", "fav NOT published");
                            }
                        });
                    } else {
                        client.publishUnFavorite(tweet.tweetId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                ibHeart.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                                tweet.wasLiked = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });
                    }

                }
            });

        }


        // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return relativeDate;
        }
    }
}
