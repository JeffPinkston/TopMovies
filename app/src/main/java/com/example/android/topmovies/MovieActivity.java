package com.example.android.topmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.topmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private ImageView mMovieImageDetail;
    private TextView mMovieTitle;
    private TextView mMovieYear;
    private TextView mMovieScore;
    private TextView mMovieSummary;
    private JSONObject mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent initialIntent = getIntent();
        try {
            mMovieData = new JSONObject(initialIntent.getStringExtra(Intent.EXTRA_TEXT));
            mMovieImageDetail = findViewById(R.id.iv_movie_detail);
            mMovieTitle = findViewById(R.id.tv_title);
            mMovieYear = findViewById(R.id.tv_year);
            mMovieScore = findViewById(R.id.tv_score);
            mMovieSummary = findViewById(R.id.tv_summary);
            mMovieTitle.setText(mMovieData.getString("title"));
            mMovieYear.setText(mMovieData.getString("release_date"));

            mMovieScore.setText(String.valueOf(mMovieData.getString("vote_average")) + " / 10");
            mMovieSummary.setText(mMovieData.getString("overview"));
            Uri movieImageUri = NetworkUtils.buildImageUri(mMovieData.getString("poster_path"));
            Picasso.with(this).load(movieImageUri).into(mMovieImageDetail);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

    }
}
