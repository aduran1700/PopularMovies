package portfolio.app.aduran.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import portfolio.app.aduran.popularmovies.ViewAdapters.FavoriteMovieCursorRecyclerViewAdapter;
import portfolio.app.aduran.popularmovies.ViewAdapters.MoviePosterRecyclerViewAdapter;
import portfolio.app.aduran.popularmovies.data.MovieProvider;
import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;
import portfolio.app.aduran.popularmovies.interfaces.UpdateListListener;
import portfolio.app.aduran.popularmovies.models.Movie;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MoviePosterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, UpdateListListener {
    private final String LOG_TAG = MoviePosterFragment.class.getSimpleName();

    public static final String MOVIE_PREFERENCES = "Movie_Prefs" ;
    public static final String ARG_COLUMN_COUNT = "column-count";

    private static final int CURSOR_LOADER_ID = 0;

    private int mColumnCount = 2;
    private MoviePosterRecyclerViewAdapter moviePosterRecyclerViewAdapter;
    private FavoriteMovieCursorRecyclerViewAdapter favoriteMovieCursorRecyclerViewAdapter;
    private OnListFragmentInteractionListener mListener;
    private SharedPreferences sharedpreferences;
    private String sortBy;
    private ArrayList<Movie> movieList;
    private RecyclerView recyclerView;


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

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MOVIE_PREFERENCES, Context.MODE_PRIVATE);
        sortBy = sharedpreferences.getString("sort_order", "popularity.desc");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        View view = inflater.inflate(R.layout.fragment_movieposter_list, container, false);


        final Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        movieList = new ArrayList<>();
        moviePosterRecyclerViewAdapter = new MoviePosterRecyclerViewAdapter(movieList, mListener, getActivity());
        favoriteMovieCursorRecyclerViewAdapter = new FavoriteMovieCursorRecyclerViewAdapter(mListener, getActivity(), null);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Sort By").setItems(R.array.sort_types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] arrays = getResources().getStringArray(R.array.sort_types_values);
                        sortBy = arrays[which];
                        sharedpreferences.edit().putString("sort_order", sortBy).apply();

                        updateMovies();
                    }
                }).show();
            }
        });

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
        if(sortBy.contains("favorite")) {
            recyclerView.setAdapter(favoriteMovieCursorRecyclerViewAdapter);
        } else {
            recyclerView.setAdapter(moviePosterRecyclerViewAdapter);
            new FetchMoviesTask().execute(sortBy, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieProvider.Movies.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoriteMovieCursorRecyclerViewAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoriteMovieCursorRecyclerViewAdapter.swapCursor(null);

    }

    @Override
    public void updateList(ArrayList<Movie> movies) {
        if(movies != null) {
            movieList.clear();
            movieList.addAll(movies);
            moviePosterRecyclerViewAdapter.notifyDataSetChanged();
        }

    }
}
