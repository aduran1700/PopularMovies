package portfolio.app.aduran.popularmovies.ViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import portfolio.app.aduran.popularmovies.R;
import portfolio.app.aduran.popularmovies.ViewHolders.MoviePosterViewHolder;
import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;
import portfolio.app.aduran.popularmovies.models.Movie;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Movie} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MoviePosterRecyclerViewAdapter extends RecyclerView.Adapter<MoviePosterViewHolder> {

    private final List<Movie> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;

    public MoviePosterRecyclerViewAdapter(List<Movie> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movieposter, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviePosterViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(context).load(holder.mItem.getPosterFullURL()).into(holder.moviePoster);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
