package portfolio.app.aduran.popularmovies.ViewHolders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import portfolio.app.aduran.popularmovies.R;
import portfolio.app.aduran.popularmovies.dummy.DummyContent;

public class MoviePosterViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;
    public DummyContent.DummyItem mItem;

    public MoviePosterViewHolder(View view) {
        super(view);
        mView = view;
        mIdView = (TextView) view.findViewById(R.id.id);
        mContentView = (TextView) view.findViewById(R.id.content);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }
}
