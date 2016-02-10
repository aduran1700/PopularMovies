package portfolio.app.aduran.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import portfolio.app.aduran.popularmovies.data.MovieColumns;
import portfolio.app.aduran.popularmovies.data.MovieProvider;
import portfolio.app.aduran.popularmovies.interfaces.UpdateMovieDetailsListener;
import portfolio.app.aduran.popularmovies.models.Movie;
import portfolio.app.aduran.popularmovies.models.Trailer;


public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, UpdateMovieDetailsListener {
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();
    public static final String MOVIE_URI = "URI";
    public static final String MOVIE = "MOVIE";
    private static final int MOVIE_LOADER = 0;
    private Uri mUri;
    private Movie movie;

    private ImageView mMoviePosterView;
    private TextView mMovieTitleView;
    private TextView mMovieSynopsis;
    private TextView mMovieDateView;
    private TextView mMovieRatingView;
    private Button mFavButtonView;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();


        if (arguments != null) {
            if (arguments.containsKey(MovieFragment.MOVIE_URI))
                mUri = arguments.getParcelable(MovieFragment.MOVIE_URI);
            else
                movie = arguments.getParcelable(MovieFragment.MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        mMoviePosterView = (ImageView) view.findViewById(R.id.movie_poster);
        mMovieTitleView = (TextView) view.findViewById(R.id.movie_title);
        mMovieSynopsis = (TextView) view.findViewById(R.id.movie_synopsis);
        mMovieDateView = (TextView) view.findViewById(R.id.movie_date);
        mMovieRatingView = (TextView) view.findViewById(R.id.movie_rating);
        mFavButtonView = (Button) view.findViewById(R.id.fav_button);

        if (null != movie) {
            showMovie();
        }




        mFavButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri == null)
                    addToFavorites();
                else
                    removeFromFavorites();
            }
        });

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

            movie = new Movie(movieId, title, poster, overview, average, date, popularity);
            showMovie();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showMovie() {
        new FetchMovieTrailerTask().execute(movie.getMovieId() + "");

        Cursor cursor = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI, null, MovieColumns.COLUMN_MOVIE_ID +"="+movie.getMovieId(), null,null);

        if(cursor != null)
            if(cursor.moveToFirst()) {
                mFavButtonView.setText(getString(R.string.remove_favorite));
                mUri = MovieProvider.Movies.withId(cursor.getLong(cursor.getColumnIndex(MovieColumns._ID)));
            }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Picasso.with(getActivity()).load(movie.getPosterFullURL()).into(mMoviePosterView);
        mMovieTitleView.setText(movie.getOriginalTitle());
        mMovieSynopsis.setText(movie.getPlotSynopsis());
        try {
            Date movieDate = simpleDateFormat.parse(movie.getReleaseDate());
            simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
            mMovieDateView.setText(simpleDateFormat.format(movieDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String ratingText = movie.getUserRating() + "/10";

        mMovieRatingView.setText(ratingText);
    }

    private void addToFavorites() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieColumns.COLUMN_MOVIE_ID, movie.getMovieId());
        contentValues.put(MovieColumns.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieColumns.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(MovieColumns.COLUMN_VOTE_AVERAGE, movie.getUserRating());
        contentValues.put(MovieColumns.COLUMN_POSTER_PATH, movie.getPosterFullURL());
        contentValues.put(MovieColumns.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieColumns.COLUMN_OVERVIEW, movie.getPlotSynopsis());

        mUri = getActivity().getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, contentValues);

        mFavButtonView.setText(getString(R.string.remove_favorite));
    }

    private void removeFromFavorites() {
        getActivity().getContentResolver().delete(mUri, null, null);
        mUri = null;
        mFavButtonView.setText(getString(R.string.mark_favorite));
    }

    @Override
    public void addTrailers(ArrayList<Trailer> trailers) {

    }

    @Override
    public void addReviews() {

    }
}
