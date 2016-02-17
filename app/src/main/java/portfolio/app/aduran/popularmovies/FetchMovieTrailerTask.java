package portfolio.app.aduran.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

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

import portfolio.app.aduran.popularmovies.interfaces.UpdateMovieDetailsListener;
import portfolio.app.aduran.popularmovies.models.Trailer;


public class FetchMovieTrailerTask extends AsyncTask<Object, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = FetchMovieTrailerTask.class.getSimpleName();
    private final String API_KEY = "abc9deb8e6d7494797aad038604f7aeb";
    private UpdateMovieDetailsListener updateMovieDetailsListener;

    @Override
    protected ArrayList<Trailer> doInBackground(Object... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieId = (String) params[0];
        updateMovieDetailsListener = (UpdateMovieDetailsListener) params[1];
        String movieTrailerJsonStr = null;

        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String API_KEY_PARAM = "api_key";

            Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath("videos")
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

            movieTrailerJsonStr = buffer.toString();
            Log.v(LOG_TAG, movieTrailerJsonStr);
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


        ArrayList<Trailer> trailerList = new ArrayList<>();
        try {
            JSONObject trailersJson = new JSONObject(movieTrailerJsonStr);
            JSONArray trailersArray = trailersJson.getJSONArray("results");

            if(trailersArray.length() > 0) {


                for (int i = 0; i < trailersArray.length(); i++) {
                    trailerList.add(new Gson().fromJson(trailersArray.get(i).toString(), Trailer.class));
                }
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        }


        return trailerList;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        updateMovieDetailsListener.addTrailers(trailers);
        super.onPostExecute(trailers);
    }
}
