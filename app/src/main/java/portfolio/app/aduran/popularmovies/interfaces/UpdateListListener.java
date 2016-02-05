package portfolio.app.aduran.popularmovies.interfaces;


import java.util.ArrayList;

import portfolio.app.aduran.popularmovies.models.Movie;

/**
 * Created by aduran on 2/5/16.
 */
public interface UpdateListListener {
    void updateList(ArrayList<Movie> movies);
}
