package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Profile;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    EditText etCompose;
    public static final int max = 280;
    public static final String TAG = "ComposeActivity";
    Button btnTweet;
    ImageButton ibExit;
    TwitterClient twitterClient;
    ImageView ivProfileImage;
    TextView tvName;
    TextView tvScreenName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        twitterClient = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        ibExit = findViewById(R.id.ibExit);
        btnTweet = findViewById(R.id.btnTweet);
        ivProfileImage = findViewById(R.id.ivProfileImage2);
        tvScreenName = findViewById(R.id.tvScreenName2);
        tvName = findViewById(R.id.tvName2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                startActivity(intent);
            }
        });
        // show user info
        twitterClient.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                try {
                    // use json to create user and user id info
                    // display in tv and tv and profile pic
                    Profile profile = Profile.fromJson(json.jsonObject);
                    tvName.setText(profile.name);
                    tvScreenName.setText(profile.username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DEBUG", "Fetch timeline error: " + throwable.toString());
            }

        });
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTweet.setBackgroundColor(Color.BLACK);
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Your tweet cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                } else if (tweetContent.length() > max) {
                    Toast.makeText(ComposeActivity.this, "Your tweet is too long",Toast.LENGTH_SHORT).show();
                    return;
                }


                Toast.makeText(ComposeActivity.this, tweetContent,Toast.LENGTH_SHORT).show();
                twitterClient.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, tweet.body);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure failed to publish", throwable);
                    }
                });
            }
        });
    }
}