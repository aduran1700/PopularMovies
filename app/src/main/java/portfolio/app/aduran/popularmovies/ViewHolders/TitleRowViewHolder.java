package portfolio.app.aduran.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import portfolio.app.aduran.popularmovies.R;

/**
 * Created by aduran on 2/17/16.
 */
public class TitleRowViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitleView;

    public TitleRowViewHolder(View itemView) {
        super(itemView);


        mTitleView = (TextView) itemView.findViewById(R.id.row_title);

    }
}
