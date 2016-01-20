package portfolio.app.aduran.popularmovies.ViewAdapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import portfolio.app.aduran.popularmovies.R;
import portfolio.app.aduran.popularmovies.ViewHolders.MoviePosterViewHolder;
import portfolio.app.aduran.popularmovies.data.MovieColumns;
import portfolio.app.aduran.popularmovies.data.MovieProvider;
import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;
import portfolio.app.aduran.popularmovies.models.Movie;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Movie} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MoviePosterRecyclerViewAdapter extends CursorRecyclerViewAdapter<MoviePosterViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;

    public MoviePosterRecyclerViewAdapter(OnListFragmentInteractionListener listener, Context context, Cursor cursor) {
        super(context, cursor);
        mListener = listener;
        this.mContext = context;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movieposter, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder viewHolder, final Cursor cursor) {
        DatabaseUtils.dumpCursor(cursor);

        Picasso.with(mContext).load(cursor.getString(cursor.getColumnIndex(MovieColumns.COLUMN_POSTER_PATH))).into(viewHolder.moviePoster);


        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(MovieProvider.Movies.withId(cursor.getColumnIndex(MovieColumns.COLUMN_MOVIE_ID)));
                }
            }
        });


    }

}
