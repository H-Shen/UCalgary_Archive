public class Movie extends StreamingMedia {

    private int rating;

    public Movie(String aTitle, int aLength, int aRating) {
        super(aTitle, aLength);
        setRating(aRating);
    }

    public Movie(Movie toCopy) {
        super(toCopy);
        setRating(toCopy.getRating());
    }

    @Override
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 0 && rating <= 10) {
            this.rating = rating;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " Rating: " + getRating();
    }
}
