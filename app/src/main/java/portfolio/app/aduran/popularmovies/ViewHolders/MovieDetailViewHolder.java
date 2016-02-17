package portfolio.app.aduran.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import portfolio.app.aduran.popularmovies.R;
import portfolio.app.aduran.popularmovies.models.Movie;

public class MovieDetailViewHolder extends RecyclerView.ViewHolder {
    public ImageView mMoviePosterView;
    public TextView mMovieTitleView;
    public TextView mMovieSynopsis;
    public TextView mMovieDateView;
    public TextView mMovieRatingView;
    public Button mFavButtonView;
    public Movie mItem;


    public MovieDetailViewHolder(View view) {
        super(view);

        mMoviePosterView = (ImageView) view.findViewById(R.id.movie_poster);
        mMovieTitleView = (TextView) view.findViewById(R.id.movie_title);
        mMovieSynopsis = (TextView) view.findViewById(R.id.movie_synopsis);
        mMovieDateView = (TextView) view.findViewById(R.id.movie_date);
        mMovieRatingView = (TextView) view.findViewById(R.id.movie_rating);
        mFavButtonView = (Button) view.findViewById(R.id.fav_button);
    }
}
