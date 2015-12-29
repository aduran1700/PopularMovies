package portfolio.app.aduran.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import java.util.List;

import portfolio.app.aduran.popularmovies.ViewAdapters.MoviePosterRecyclerViewAdapter;
import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;
import portfolio.app.aduran.popularmovies.models.Movie;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MoviePosterFragment extends Fragment {

    private final String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private final String API_KEY = "abc9deb8e6d7494797aad038604f7aeb";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private MoviePosterRecyclerViewAdapter moviePosterRecyclerViewAdapter;
    private OnListFragmentInteractionListener mListener;
    private List<Movie> movieList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoviePosterFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();

    }

    @SuppressWarnings("unused")
    public static MoviePosterFragment newInstance(int columnCount) {
        MoviePosterFragment fragment = new MoviePosterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movieposter_list, container, false);
        movieList = new ArrayList<>();


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            moviePosterRecyclerViewAdapter = new MoviePosterRecyclerViewAdapter(movieList, mListener, getActivity());
            recyclerView.setAdapter(moviePosterRecyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateMovies() {
        new MovieAsyncTask().execute("popularity.desc");
    }

    public class MovieAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params.length == 0)
                return null;

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


            return getMovieList(movieJsonStr);
        }

        private ArrayList<Movie> getMovieList(String movieJsonStr) {


            ArrayList<Movie> moviesList = new ArrayList<>();
            try {
                JSONObject moviesJson = new JSONObject(movieJsonStr);
                JSONArray moviesArray = moviesJson.getJSONArray("results");


                for (int i = 0; i < moviesArray.length(); i++)
                    moviesList.add(new Gson().fromJson(moviesArray.get(i).toString(), Movie.class));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
                return new ArrayList<>();
            }

            return moviesList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if(movies != null) {
                movieList.clear();
                movieList.addAll(movies);
                moviePosterRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }


}
