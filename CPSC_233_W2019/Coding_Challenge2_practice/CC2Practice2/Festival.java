import java.util.ArrayList;

public class Festival {

    private String           name;
    private ArrayList<Movie> movieList = new ArrayList<>();

    public Festival(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Movie> getMovieList() {
        ArrayList<Movie> result = new ArrayList<>();
        for (Movie i : movieList) {
            result.add(new Movie(i));
        }
        return result;
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = new ArrayList<>();
        for (Movie i : movieList) {
            this.movieList.add(new Movie(i));
        }
    }

    public void addMovie(Movie movie) {
        movieList.add(new Movie(movie));
    }

    public Movie getMovieWithLowestRating() {
        Movie result = null;
        if (movieList.isEmpty()) {
            return result;
        }
        int lowestRating = Movie.MAX_RATING + 5;
        for (Movie i : movieList) {
            if (i.getRating() < lowestRating) {
                lowestRating = i.getRating();
            }
        }
        for (Movie i : movieList) {
            if (i.getRating() == lowestRating) {
                return new Movie(i);
            }
        }
        return result;
    }
}
