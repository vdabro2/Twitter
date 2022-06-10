package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class ReplyActivity extends AppCompatActivity {
    Button bReply;
    EditText etReplyContent;
    TwitterClient client;
    TextView tvReplyingto;
    TextView actualName;
    TextView atName;
    TextView body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        bReply = findViewById(R.id.button2);
        tvReplyingto = findViewById(R.id.tvReplyingto);
        etReplyContent = findViewById(R.id.etReplyContent);
        body = findViewById(R.id.body);
        actualName = findViewById(R.id.actualName);
        atName = findViewById(R.id.atName);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        client = TwitterApp.getRestClient(this);
        body.setText(tweet.body);
        actualName.setText(tweet.user.name);
        atName.setText("@"+tweet.user.screenName);
        tvReplyingto.setText("replying to @" + tweet.user.screenName);
        bReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.postReply(tweet.tweetId, "@"+tweet.user.screenName + " " + etReplyContent.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        //Log.e("POSTED", "REPLY");
                        Intent intent = new Intent(ReplyActivity.this, TimelineActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });
            }
        });

    }
}