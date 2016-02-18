package portfolio.app.aduran.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import portfolio.app.aduran.popularmovies.ViewAdapters.MovieRecyclerViewAdapter;
import portfolio.app.aduran.popularmovies.data.MovieColumns;
import portfolio.app.aduran.popularmovies.data.MovieProvider;
import portfolio.app.aduran.popularmovies.interfaces.UpdateMovieDetailsListener;
import portfolio.app.aduran.popularmovies.models.Movie;
import portfolio.app.aduran.popularmovies.models.Review;
import portfolio.app.aduran.popularmovies.models.Trailer;


public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, UpdateMovieDetailsListener {
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;
    public static final String MOVIE_URI = "URI";
    public static final String MOVIE = "MOVIE";
    private static final int MOVIE_LOADER = 0;
    private Uri mUri;
    private Movie mMovie;
    private Trailer mTrailer;
    private RecyclerView list;
    private ArrayList<Object> mMovieInfo;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;

    public MovieFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();


        if (arguments != null) {
            if (arguments.containsKey(MovieFragment.MOVIE_URI))
                mUri = arguments.getParcelable(MovieFragment.MOVIE_URI);
            else
                mMovie = arguments.getParcelable(MovieFragment.MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        list = (RecyclerView) view.findViewById(R.id.list);

        if (null != mMovie) {
            showMovie();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            int movieId = data.getInt(data.getColumnIndex(MovieColumns.COLUMN_MOVIE_ID));
            String title = data.getString(data.getColumnIndex(MovieColumns.COLUMN_ORIGINAL_TITLE));
            String poster = data.getString(data.getColumnIndex(MovieColumns.COLUMN_POSTER_PATH));
            String date = data.getString(data.getColumnIndex(MovieColumns.COLUMN_RELEASE_DATE));
            double average = data.getDouble(data.getColumnIndex(MovieColumns.COLUMN_VOTE_AVERAGE));
            String overview = data.getString(data.getColumnIndex(MovieColumns.COLUMN_OVERVIEW));
            double popularity = data.getDouble(data.getColumnIndex(MovieColumns.COLUMN_POPULARITY));
            byte[] image = data.getBlob(data.getColumnIndex(MovieColumns.COLUMN_POSTER_IMAGE));


            mMovie = new Movie(movieId, title, poster, overview, average, date, popularity);
            mMovie.image = image;
            showMovie();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showMovie() {
        Cursor cursor = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI, null, MovieColumns.COLUMN_MOVIE_ID +"="+mMovie.getMovieId(), null,null);

        if(cursor != null)
            if(cursor.moveToFirst()) {
                mMovie.favoriteUri = (MovieProvider.Movies.withId(cursor.getLong(cursor.getColumnIndex(MovieColumns._ID))));
            }

        mMovieInfo = new ArrayList<>();
        mMovieInfo.add(mMovie);

        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(mMovieInfo, getActivity());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(movieRecyclerViewAdapter);

        new FetchMovieTrailerTask().execute(mMovie.getMovieId() + "", this);

    }


   @Override
    public void addTrailers(ArrayList<Trailer> trailers) {
       if(trailers != null) {
           int size = trailers.size();
           if (size > 1) {
               mMovieInfo.add("Trailers:");
           } else if (size == 1) {
               mMovieInfo.add("Trailer:");
           }
           for (int i = 0; i < size; i++) {
               if (i == 0) {
                   mTrailer = trailers.get(i);
               }
               mMovieInfo.add(trailers.get(i));
           }
           movieRecyclerViewAdapter.notifyDataSetChanged();
       }
       new FetchMovieReviewTask().execute(mMovie.getMovieId() + "", this);

    }

    @Override
    public void addReviews(ArrayList<Review> reviews) {

        if (reviews != null) {
            int size = reviews.size();
            if (size > 1) {
                mMovieInfo.add("Reviews:");
            } else if (size == 1) {
                mMovieInfo.add("Review:");
            }
            for (int i = 0; i < size; i++) {
                mMovieInfo.add(reviews.get(i));
            }
            movieRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mTrailer != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mTrailer.getYouTubeTrailer());

        return shareIntent;
    }
}
