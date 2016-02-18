package portfolio.app.aduran.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import portfolio.app.aduran.popularmovies.models.Movie;



public class MovieActivity extends AppCompatActivity {

    private static final String MOVIE_EXTRA = "movie";

    public MovieActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();

            Uri uri = getIntent().getExtras().getParcelable(MovieFragment.MOVIE_URI);
            Movie movie = getIntent().getExtras().getParcelable(MovieFragment.MOVIE);

            if(uri != null)
                arguments.putParcelable(MovieFragment.MOVIE_URI, uri);
            else if(movie != null)
                arguments.putParcelable(MovieFragment.MOVIE, movie);

            MovieFragment fragment = new MovieFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

    }




}
