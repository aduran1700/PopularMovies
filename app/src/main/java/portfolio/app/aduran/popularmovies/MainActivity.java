package portfolio.app.aduran.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;
import portfolio.app.aduran.popularmovies.models.Movie;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {
    private boolean mTwoPane;
    private static final String MOVIE_FRAGMENT_TAG = "MFTAG";
    private static final String MOVIE_POSTER_FRAGMENT_TAG = "MPFTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int column;

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            column = 1;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieFragment(), MOVIE_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            column = 2;

        }

        Bundle args = new Bundle();
        args.putInt(MoviePosterFragment.ARG_COLUMN_COUNT, column);

        MoviePosterFragment moviePosterFragment = new MoviePosterFragment();
        moviePosterFragment.setArguments(args);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movie_poster, moviePosterFragment, MOVIE_POSTER_FRAGMENT_TAG)
                    .commit();
        }
    }


    @Override
    public void onListFragmentInteraction(Uri movie) {

        if (movie != null)
            if (mTwoPane) {
                Bundle args = new Bundle();
                args.putParcelable(MovieFragment.MOVIE_URI, movie);

                MovieFragment movieFragment = new MovieFragment();
                movieFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, movieFragment, MOVIE_FRAGMENT_TAG)
                        .commit();

            } else {
                Intent intent = new Intent(this, MovieActivity.class);
                intent.putExtra(MovieFragment.MOVIE_URI, movie);
                startActivity(intent);
            }
    }

    @Override
    public void onListFragmentInteraction(Movie movie) {

        if (movie != null)
            if (mTwoPane) {
                Bundle args = new Bundle();
                args.putParcelable(MovieFragment.MOVIE, movie);

                MovieFragment movieFragment = new MovieFragment();
                movieFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, movieFragment, MOVIE_FRAGMENT_TAG)
                        .commit();

            } else {
                Intent intent = new Intent(this, MovieActivity.class);
                intent.putExtra(MovieFragment.MOVIE, movie);
                startActivity(intent);
            }
    }

}
