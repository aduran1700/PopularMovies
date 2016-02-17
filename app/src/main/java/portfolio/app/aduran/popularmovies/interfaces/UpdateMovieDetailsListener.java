package portfolio.app.aduran.popularmovies.interfaces;


import java.util.ArrayList;

import portfolio.app.aduran.popularmovies.models.Movie;
import portfolio.app.aduran.popularmovies.models.Review;
import portfolio.app.aduran.popularmovies.models.Trailer;

public interface UpdateMovieDetailsListener {
    void addTrailers(ArrayList<Trailer> trailers);
    void addReviews(ArrayList<Review> reviews);

}
