package portfolio.app.aduran.popularmovies;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;

import net.simonvt.schematic.annotation.ContentProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import portfolio.app.aduran.popularmovies.data.MovieColumns;
import portfolio.app.aduran.popularmovies.data.MovieProvider;
import portfolio.app.aduran.popularmovies.models.Movie;

public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final String API_KEY = "abc9deb8e6d7494797aad038604f7aeb";
    private Context mContext;

    public FetchMoviesTask(Context context) {
        mContext = context;
    }


    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String sortBy = params[0];
        String movieJsonStr = null;

        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortBy)
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();


            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            movieJsonStr = buffer.toString();
            Log.v(LOG_TAG, movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }


        ArrayList<Movie> moviesList = new ArrayList<>();
        try {
            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(moviesArray.length());


            for (int i = 0; i < moviesArray.length(); i++) {
                moviesList.add(new Gson().fromJson(moviesArray.get(i).toString(), Movie.class));
                Movie movie = moviesList.get(i);

                ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(MovieProvider.Movies.CONTENT_URI);

                builder.withValue(MovieColumns.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                builder.withValue(MovieColumns.COLUMN_OVERVIEW, movie.getPlotSynopsis());
                builder.withValue(MovieColumns.COLUMN_POSTER_PATH, movie.getPosterFullURL());
                builder.withValue(MovieColumns.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                builder.withValue(MovieColumns.COLUMN_VOTE_AVERAGE, movie.getUserRating());
                builder.withValue(MovieColumns.COLUMN_POPULARITY, movie.getPopularity());
                batchOperations.add(builder.build());

            }


            mContext.getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }


        return null;
    }
}