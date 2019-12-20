public class Movie {

    public static final int    MIN_RATING = 0;
    public static final int    MAX_RATING = 10;
    private             String title;
    private             int    rating;

    public Movie(String title, int rating) {
        this.title = title.toUpperCase();
        if (rating >= MIN_RATING && rating <= MAX_RATING) {
            this.rating = rating;
        }
    }

    public Movie(Movie other) {
        this.title = other.getTitle();
        this.rating = other.getRating();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.toUpperCase();
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= MIN_RATING && rating <= MAX_RATING) {
            this.rating = rating;
        }
    }

    public char getCategory() {
        switch (rating) {
            case 10:
            case 9:
                return 'A';
            case 8:
            case 7:
                return 'B';
            case 6:
            case 5:
                return 'C';
            case 4:
            case 3:
                return 'D';
            default:
                return 'F';
        }
    }
}
