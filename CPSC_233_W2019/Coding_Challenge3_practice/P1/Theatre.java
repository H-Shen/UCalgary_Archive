public class Theatre {

    private String title;
    private int    rating;

    public Theatre(String title, int rating) {
        this.title = title.toUpperCase();
        if (rating >= 0 && rating <= 10) {
            this.rating = rating;
        }
    }

    public Theatre(Theatre other) {
        this(other.getTitle(), other.getRating());
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

    public void setRating(int aRating) {
        if (aRating >= 0 && aRating <= 10) {
            rating = aRating;
        }
    }

    public String getCategory() {
        switch (rating) {
            case 10:
            case 9:
                return "A";
            case 8:
            case 7:
                return "B";
            case 6:
            case 5:
                return "C";
            case 4:
            case 3:
                return "D";
            default:
                return "F";
        }
    }
}
