package portfolio.app.aduran.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import portfolio.app.aduran.popularmovies.R;
import portfolio.app.aduran.popularmovies.models.Trailer;

/**
 * Created by aduran on 2/17/16.
 */
public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {
    public TextView mTrailerNameView;
    public Trailer mItem;
    public View mView;


    public MovieTrailerViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mTrailerNameView = (TextView) itemView.findViewById(R.id.trailer_name);
    }
}
