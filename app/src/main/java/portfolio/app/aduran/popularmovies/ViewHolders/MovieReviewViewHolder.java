package portfolio.app.aduran.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import portfolio.app.aduran.popularmovies.R;
import portfolio.app.aduran.popularmovies.models.Review;
import portfolio.app.aduran.popularmovies.models.Trailer;

/**
 * Created by aduran on 2/17/16.
 */
public class MovieReviewViewHolder extends RecyclerView.ViewHolder {
    public TextView mReviewAuthorView;
    public TextView mReviewContentView;


    public MovieReviewViewHolder(View itemView) {
        super(itemView);

        mReviewAuthorView = (TextView) itemView.findViewById(R.id.review_author);
        mReviewContentView = (TextView) itemView.findViewById(R.id.review_content);
    }
}
