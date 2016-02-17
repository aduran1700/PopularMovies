package portfolio.app.aduran.popularmovies.ViewAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import portfolio.app.aduran.popularmovies.R;
import portfolio.app.aduran.popularmovies.ViewHolders.MovieDetailViewHolder;
import portfolio.app.aduran.popularmovies.ViewHolders.MovieReviewViewHolder;
import portfolio.app.aduran.popularmovies.ViewHolders.MovieTrailerViewHolder;
import portfolio.app.aduran.popularmovies.ViewHolders.TitleRowViewHolder;
import portfolio.app.aduran.popularmovies.data.MovieColumns;
import portfolio.app.aduran.popularmovies.data.MovieProvider;
import portfolio.app.aduran.popularmovies.models.Movie;
import portfolio.app.aduran.popularmovies.models.Review;
import portfolio.app.aduran.popularmovies.models.Trailer;


public class MovieRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int MOVIE_DETAIL_ROW = 1;
    private static final int MOVIE_TRAILER_ROW = 2;
    private static final int TITLE_ROW = 3;
    private static final int MOVIE_REVIEW_ROW = 4;

    private ArrayList<Object> movieDetailList;
    private Context context;

    public MovieRecyclerViewAdapter(ArrayList<Object> movieDetailList, Context context) {
        this.movieDetailList = movieDetailList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case MOVIE_DETAIL_ROW:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_detail_row, parent, false);
                return new MovieDetailViewHolder(view);

            case MOVIE_TRAILER_ROW:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trailer_row, parent, false);
                return new MovieTrailerViewHolder(view);

            case MOVIE_REVIEW_ROW:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.review_row, parent, false);
                return new MovieReviewViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.title_row, parent, false);
                return new TitleRowViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MovieDetailViewHolder) {
            final MovieDetailViewHolder movieDetailViewHolder = (MovieDetailViewHolder) holder;
            Movie movie = (Movie) movieDetailList.get(position);


            if(movie.favoriteUri != null)
                    movieDetailViewHolder.mFavButtonView.setText(context.getString(R.string.remove_favorite));
            else
                movieDetailViewHolder.mFavButtonView.setText(context.getString(R.string.mark_favorite));



            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Picasso.with(context).load(movie.getPosterFullURL()).into(movieDetailViewHolder.mMoviePosterView);
            movieDetailViewHolder.mMovieTitleView.setText(movie.getOriginalTitle());
            movieDetailViewHolder.mMovieSynopsis.setText(movie.getPlotSynopsis());
            try {
                Date movieDate = simpleDateFormat.parse(movie.getReleaseDate());
                simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
                movieDetailViewHolder.mMovieDateView.setText(simpleDateFormat.format(movieDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String ratingText = movie.getUserRating() + "/10";

            movieDetailViewHolder.mMovieRatingView.setText(ratingText);

            movieDetailViewHolder.mItem = movie;
            movieDetailViewHolder.mFavButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movieDetailViewHolder.mItem.favoriteUri == null)
                        addToFavorites(movieDetailViewHolder.mItem);
                    else
                        removeFromFavorites(movieDetailViewHolder.mItem);
                }
            });
        } else if(holder instanceof MovieTrailerViewHolder)  {
            final MovieTrailerViewHolder movieTrailerViewHolder = (MovieTrailerViewHolder) holder;
            Trailer trailer = (Trailer) movieDetailList.get(position);
            movieTrailerViewHolder.mItem = trailer;

            movieTrailerViewHolder.mTrailerNameView.setText(trailer.getName());
            movieTrailerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTrailer(movieTrailerViewHolder.mItem);
                }
            });


        } else if (holder instanceof TitleRowViewHolder) {
            TitleRowViewHolder titleRowViewHolder = (TitleRowViewHolder) holder;
            String title = (String) movieDetailList.get(position);

            titleRowViewHolder.mTitleView.setText(title);
        } else if(holder instanceof MovieReviewViewHolder) {
            MovieReviewViewHolder movieReviewViewHolder = (MovieReviewViewHolder) holder;
            Review review = (Review) movieDetailList.get(position);

            movieReviewViewHolder.mReviewContentView.setText(review.getContent());
            movieReviewViewHolder.mReviewAuthorView.setText(review.getAuthor());

        }

    }

    @Override
    public int getItemCount() {
        return movieDetailList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (movieDetailList.get(position) instanceof Movie) {
            return MOVIE_DETAIL_ROW;
        } else if(movieDetailList.get(position) instanceof Trailer) {
            return MOVIE_TRAILER_ROW;
        } else if(movieDetailList.get(position) instanceof Review){
            return MOVIE_REVIEW_ROW;
        } else
            return TITLE_ROW;
    }

    private void addToFavorites(Movie movie) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieColumns.COLUMN_MOVIE_ID, movie.getMovieId());
        contentValues.put(MovieColumns.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieColumns.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(MovieColumns.COLUMN_VOTE_AVERAGE, movie.getUserRating());
        contentValues.put(MovieColumns.COLUMN_POSTER_PATH, movie.getPosterFullURL());
        contentValues.put(MovieColumns.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieColumns.COLUMN_OVERVIEW, movie.getPlotSynopsis());

        movie.favoriteUri = context.getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, contentValues);

        this.notifyDataSetChanged();
    }

    private void removeFromFavorites(Movie movie) {
        context.getContentResolver().delete(movie.favoriteUri, null, null);
        movie.favoriteUri = null;
        this.notifyDataSetChanged();
    }

    private void showTrailer(Trailer trailer) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
    }
}
