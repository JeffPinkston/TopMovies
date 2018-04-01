package com.example.android.topmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.topmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private String[] mMovieData;
    private Picasso mPicasso;

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(String selectedMovie);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.length;
    }

    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        mPicasso = Picasso.with(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        try {
            JSONObject movie = new JSONObject(mMovieData[position]);
            Uri imageUri = NetworkUtils.buildImageUri(movie.getString("poster_path"));
            mPicasso.load(imageUri).into(holder.mMovieImage);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImage;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMovieImage = view.findViewById(R.id.iv_movie);
            mMovieImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String selectedMovie = mMovieData[getAdapterPosition()];
            mClickHandler.onClick(selectedMovie);
        }
    }
}
