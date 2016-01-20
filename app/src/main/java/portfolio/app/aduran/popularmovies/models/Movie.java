package portfolio.app.aduran.popularmovies.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    Movie(Parcel in) {
        this.originalTitle = in.readString();
        this.poster = in.readString();
        this.plotSynopsis = in.readString();
        this.userRating = in.readDouble();
        this.releaseDate = in.readString();
        this.popularity = in.readString();
    }

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
    private String popularity;

    public String getPosterFullURL() {
        return BASE_POSTER_URL + poster;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "originalTitle='" + originalTitle + '\'' +
                ", poster='" + poster + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                ", popularity='" + popularity + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(poster);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);
        dest.writeString(popularity);
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

    public String getPopularity() {
        return popularity;
    }
}
