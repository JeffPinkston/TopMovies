package com.example.android.topmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.topmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final String TOP_MOVIES = "top_movies";
    private final String POPULAR_MOVIES = "popular_movies";

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private String mCurrentState = TOP_MOVIES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mProgressBar = findViewById(R.id.pb_progress);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        loadMovieData(NetworkUtils.TOP_RATED_BASE_URL);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void loadMovieData(String url) {
        if (isOnline()) {
            new FetchMoviesTask().execute(url);
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(String selectedMovie) {
        Context context = this;
        Intent startMovieActivity = new Intent(this, MovieActivity.class);
        startMovieActivity.putExtra(Intent.EXTRA_TEXT, selectedMovie);
        startActivity(startMovieActivity);
    }

    public void showProgressBar() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showMovieResults() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String searchType = params[0];
            URL moviesRequestUrl = NetworkUtils.buildURL(searchType);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                JSONObject mainObject = new JSONObject(jsonMovieResponse);
                JSONArray resultsJSON = mainObject.getJSONArray("results");
                String[] simpleMovieData = new String[resultsJSON.length()];
                if (resultsJSON != null) {
                    for (int i = 0; i < resultsJSON.length(); i++) {
                        simpleMovieData[i] = resultsJSON.getJSONObject(i).toString();
                    }
                }

                return simpleMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            if (movieData != null) {
                mMoviesAdapter.setMovieData(movieData);
                showMovieResults();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            switch (mCurrentState) {
                case POPULAR_MOVIES:
                    loadMovieData(NetworkUtils.TOP_RATED_BASE_URL);
                    mCurrentState = TOP_MOVIES;
                    item.setTitle(R.string.view_top_movies);
                    return true;
                case TOP_MOVIES:
                    loadMovieData(NetworkUtils.POPULAR_BASE_URL);
                    mCurrentState = POPULAR_MOVIES;
                    item.setTitle(R.string.view_popular_movies);
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
