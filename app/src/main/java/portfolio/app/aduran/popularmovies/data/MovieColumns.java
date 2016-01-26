package portfolio.app.aduran.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;


public interface MovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(DataType.Type.INTEGER) String COLUMN_MOVIE_ID = "movie_id";
    @DataType(DataType.Type.TEXT) String COLUMN_ORIGINAL_TITLE = "original_title";
    @DataType(DataType.Type.TEXT)  String COLUMN_POSTER_PATH = "poster_path";
    @DataType(DataType.Type.TEXT) String COLUMN_RELEASE_DATE = "release_date";
    @DataType(DataType.Type.INTEGER)  String COLUMN_VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.TEXT) String COLUMN_OVERVIEW = "overview";
    @DataType(DataType.Type.INTEGER) String COLUMN_POPULARITY = "popularity";

}
