package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public String tweetId;
    public Entity entity;
    public boolean wasLiked;
    public boolean retweeted;
    public  Tweet(){}
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.tweetId = jsonObject.getString("id_str");
        //Log.i("tweet", String.valueOf(tweet.tweetId));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        try {
            tweet.entity = Entity.fromJson(jsonObject.getJSONObject("entities"));
        } catch (JSONException e) {
            tweet.entity = null;
        }
        tweet.wasLiked = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        return tweet;
    }
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
     }
}
