package portfolio.app.aduran.popularmovies;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import portfolio.app.aduran.popularmovies.data.generated.MovieDatabase;
import portfolio.app.aduran.popularmovies.models.Movie;


public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();
    public static final String MOVIE_URI = "URI";
    private static final int MOVIE_LOADER = 0;
    private Uri mUri;

    private ImageView mMoviePosterView;
    private TextView mMovieTitleView;
    private TextView mMovieSynopsis;
    private TextView mMovieDateView;
    private TextView mMovieRatingView;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();

        if(arguments != null)
            mUri = arguments.getParcelable(MovieFragment.MOVIE_URI);

        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        mMoviePosterView = (ImageView) view.findViewById(R.id.movie_poster);
        mMovieTitleView = ((TextView) view.findViewById(R.id.movie_title));
        mMovieSynopsis = ((TextView) view.findViewById(R.id.movie_synopsis));
        mMovieDateView = ((TextView) view.findViewById(R.id.movie_date));
        mMovieRatingView = ((TextView) view.findViewById(R.id.movie_rating));

        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //Picasso.with(getActivity()).load(movie.getPosterFullURL()).into((ImageView) view.findViewById(R.id.movie_poster));
        //((TextView) view.findViewById(R.id.movie_title)).setText(movie.getOriginalTitle());
        //((TextView) view.findViewById(R.id.movie_synopsis)).setText(movie.getPlotSynopsis());
        /*try {
            Date movieDate = simpleDateFormat.parse(movie.getReleaseDate());
            simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
            ((TextView) view.findViewById(R.id.movie_date)).setText(simpleDateFormat.format(movieDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        //((TextView) view.findViewById(R.id.movie_rating)).setText(Math.round(movie.getUserRating()) + "/10");


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
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

            int movieId = data.getInt(data.getColumnIndex("movie_id"));
            String title = data.getString(data.getColumnIndex("original_title"));
            String poster = data.getString(data.getColumnIndex("poster_path"));
            String date = data.getString(data.getColumnIndex("release_date"));
            double average = data.getDouble(data.getColumnIndex("vote_average"));
            String overview = data.getString(data.getColumnIndex("overview"));
            double popularity = data.getDouble(data.getColumnIndex("popularity"));

            Movie movie = new Movie(movieId, title, poster,overview, average, date, popularity);
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

            mMovieRatingView.setText(Math.round(movie.getUserRating()) + "/10");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
