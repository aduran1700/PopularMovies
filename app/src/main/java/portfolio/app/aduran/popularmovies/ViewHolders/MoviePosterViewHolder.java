package portfolio.app.aduran.popularmovies.ViewHolders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import portfolio.app.aduran.popularmovies.R;

public class MoviePosterViewHolder extends RecyclerView.ViewHolder {
    public final ImageView moviePoster;
    public int id;

    public MoviePosterViewHolder(View view) {
        super(view);
        moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
    }
}
