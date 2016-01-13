package portfolio.app.aduran.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

import portfolio.app.aduran.popularmovies.models.Movie;

/**
 * Created by aduran on 1/13/16.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    private MovieDatabase() {}

    public static final int VERSION = 2;

    @Table(MovieColumns.class)
    public static final String MOVIES = "movies";

    @Table(FavoriteMovieColumns.class)
    public static final String FAVORITE_MOVIES = "favorite_movie";
}
