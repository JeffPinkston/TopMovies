package com.example.android.topmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String TOP_RATED_BASE_URL = BASE_URL + "top_rated";
    public static final String POPULAR_BASE_URL = BASE_URL + "popular";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String API_KEY = "";
    private static final String API_KEY_PARAM = "api_key";
    private static final String LANG_PARAM = "language";
    private static final String EN_US = "en-US";


    public static URL buildURL(String urlString) {
        Uri builtUri = Uri.parse(urlString).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANG_PARAM, EN_US)
                .appendQueryParameter("page", "1")
                .build();
    
        URL url = buildURL(builtUri);

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static Uri buildImageUri(String urlString) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL + urlString).buildUpon().build();
        return builtUri;
    }

    public static URL buildURL(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static boolean isOnline(ConnectivityManager cm) {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getResponseFromOkHttp(String url) throws IOException {
        OkHttpClient client =  new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
