package portfolio.app.aduran.popularmovies.models;


import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("original_title")
    public String originalTitle;

    @SerializedName("poster_path")
    public String poster;

    @SerializedName("overview")
    public String plotSynopsis;

    @SerializedName("vote_average")
    public double userRating;

    @SerializedName("release_date")
    public String releaseDate;

    @Override
    public String toString() {
        return "Movie{" +
                "originalTitle='" + originalTitle + '\'' +
                ", poster='" + poster + '\'' +
                ", plotSynpsis='" + plotSynopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
