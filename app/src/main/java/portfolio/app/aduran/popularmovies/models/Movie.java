package portfolio.app.aduran.popularmovies.models;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    Movie(Parcel in) {
        this.movieId = in.readInt();
        this.originalTitle = in.readString();
        this.poster = in.readString();
        this.plotSynopsis = in.readString();
        this.userRating = in.readDouble();
        this.releaseDate = in.readString();
        this.popularity = in.readDouble();
    }

    public Movie(int movieId, String originalTitle, String poster, String plotSynopsis, double userRating, String releaseDate, double popularity) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.poster = poster;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }

    @SerializedName("id")
    private int movieId;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("poster_path")
    private String poster;

    @SerializedName("overview")
    private String plotSynopsis;

    @SerializedName("vote_average")
    private double userRating;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("popularity")
    private double popularity;

    public Uri favoriteUri;

    public String getPosterFullURL() {
        return BASE_POSTER_URL + poster;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(originalTitle);
        dest.writeString(poster);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);
        dest.writeDouble(popularity);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getMovieId() {
        return movieId;
    }
}
