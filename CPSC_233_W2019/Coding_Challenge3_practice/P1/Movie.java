import java.util.Date;

public class Movie extends Theatre {

    private String director;
    private Date   releaseDate;

    public Movie(String aTitle, int aRating, String aDirector, Date aReleaseDate) {
        super(aTitle, aRating);
        setDirector(aDirector);
        setReleaseDate(aReleaseDate);
    }

    public Movie(Movie other) {
        this(other.getTitle(), other.getRating(), other.getDirector(), other.getReleaseDate());
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String aDirector) {
        director = aDirector;
    }

    public Date getReleaseDate() {
        return (Date) releaseDate.clone();
    }

    public void setReleaseDate(Date aReleaseDate) {
        if (releaseDate == null) {
            Date temp = new Date();
            if (aReleaseDate.after(temp)) {
                if (aReleaseDate.getTime() - temp.getTime() <= 31536000000L) {
                    releaseDate = (Date) aReleaseDate.clone();
                } else {
                    releaseDate = (Date) temp.clone();
                }
            } else {
                releaseDate = (Date) aReleaseDate.clone();
            }
        } else {
            if (aReleaseDate.after(releaseDate)) {
                if (aReleaseDate.getTime() - releaseDate.getTime() <= 31536000000L) {
                    releaseDate = (Date) aReleaseDate.clone();
                }
            } else {
                releaseDate = (Date) aReleaseDate.clone();
            }
        }
    }

    @Override
    public String getCategory() {
        return releaseDate + "-" + super.getCategory();
    }
}
