package portfolio.app.aduran.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;
import portfolio.app.aduran.popularmovies.models.Movie;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener{
    private boolean mTwoPane;
    private static final String MOVIE_FRAGMENT_TAG = "MFTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int columns = 2;

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            columns = 1;

            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieFragment(), MOVIE_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movie_poster, MoviePosterFragment.newInstance(columns))
                    .commit();
        }
    }


    @Override
    public void onListFragmentInteraction(Uri movie) {

        if(mTwoPane) {
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

        if(mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(MovieFragment.MOVIE_URI, movie);

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
