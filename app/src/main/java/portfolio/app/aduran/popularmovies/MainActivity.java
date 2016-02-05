package portfolio.app.aduran.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;
import portfolio.app.aduran.popularmovies.models.Movie;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MoviePosterFragment.newInstance(2))
                    .commit();
        }

    }


    @Override
    public void onListFragmentInteraction(Uri movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(MovieFragment.MOVIE_URI, movie);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Movie movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(MovieFragment.MOVIE, movie);
        startActivity(intent);
    }
}
