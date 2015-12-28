package portfolio.app.aduran.popularmovies;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Movie movie = getIntent().getParcelableExtra(MOVIE_EXTRA);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Picasso.with(this).load(movie.getPosterFullURL()).into((ImageView) findViewById(R.id.movie_poster));
        ((TextView) findViewById(R.id.movie_title)).setText(movie.getOriginalTitle());
        ((TextView) findViewById(R.id.movie_synopsis)).setText(movie.getPlotSynopsis());
        try {
            Date movieDate = simpleDateFormat.parse(movie.getReleaseDate());
            simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
            ((TextView) findViewById(R.id.movie_date)).setText(simpleDateFormat.format(movieDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.movie_rating)).setText(Math.round(movie.getUserRating()) + "/10");


    }


}
