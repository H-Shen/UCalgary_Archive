import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FestivalTest {

    @Test
    public void test_ConstructorAndGetter() {
        Festival c = new Festival("Test Constructor and Getter");
        assertEquals("Testing constructor and getter", "Test Constructor and Getter", c.getName());
    }

    @Test
    public void test_addMovie_addingOne() {
        Festival c = new Festival("Test");
        Movie    m = new Movie("TEST", 5);
        c.addMovie(m);
        ArrayList<Movie> list = c.getMovieList();
        Movie            m2   = null;

        if (list.size() > 0) {
            m2 = list.get(0);
        }


        assertEquals("Festival only has one movie ('Test',5) - testing size.", 1, list.size());
        assertEquals("Festival only has one movie ('Test',5) - testing title.", "TEST", m2.getTitle());
        assertEquals("Festival only has one movie ('Test',5)- testing rating.", 5, m2.getRating());
    }

    @Test
    public void test_addMovie_addingMany() {
        Festival c  = new Festival("Test");
        Movie    m1 = new Movie("MOVIE1", 7);
        Movie    m2 = new Movie("MOVIE2", 7);
        Movie    m3 = new Movie("MOVIE3", 5);
        Movie    m4 = new Movie("MOVIE4", 10);
        Movie    m5 = new Movie("MOVIE5", 9);
        Movie    m6 = new Movie("MOVIE6", 4);
        c.addMovie(m1);
        c.addMovie(m2);
        c.addMovie(m3);
        c.addMovie(m4);
        c.addMovie(m5);
        c.addMovie(m6);

        ArrayList<Movie> list = c.getMovieList();

        assertEquals("Expected list of size 6 after adding 6 movies", 6, list.size());
        assertEquals("Movie 1 test - testing title", "MOVIE1", list.get(0).getTitle());
        assertEquals("Movie 2 test - testing title", "MOVIE2", list.get(1).getTitle());
        assertEquals("Movie 3 test - testing title", "MOVIE3", list.get(2).getTitle());
        assertEquals("Movie 4 test - testing title", "MOVIE4", list.get(3).getTitle());
        assertEquals("Movie 5 test - testing title", "MOVIE5", list.get(4).getTitle());
        assertEquals("Movie 6 test - testing title", "MOVIE6", list.get(5).getTitle());
    }

    @Test
    public void test_addMovie_addingOne_EncapsulationTest() {
        Festival c = new Festival("Test");
        Movie    m = new Movie("TEST", 5);
        c.addMovie(m);
        m.setTitle("Changed Title");
        ArrayList<Movie> list = c.getMovieList();
        Movie            m2   = null;

        if (list.size() > 0) {
            m2 = list.get(0);
        }

        assertEquals("Festival only has one movie ('Test',5)- testing encapsulation (changed title of original).", "TEST", m2.getTitle());

    }

    @Test
    public void test_getMovieList_Encapsulation() {
        Festival c  = new Festival("Test");
        Movie    m1 = new Movie("MOVIE1", 7);
        Movie    m2 = new Movie("MOVIE2", 7);
        Movie    m3 = new Movie("MOVIE3", 5);
        Movie    m4 = new Movie("MOVIE4", 10);
        Movie    m5 = new Movie("MOVIE5", 9);
        Movie    m6 = new Movie("MOVIE6", 4);
        c.addMovie(m1);
        c.addMovie(m2);
        c.addMovie(m3);
        c.addMovie(m4);
        c.addMovie(m5);
        c.addMovie(m6);

        ArrayList<Movie> list = c.getMovieList();

        list.get(0).setTitle("Changed1");
        list.get(1).setTitle("Changed2");
        list.get(2).setTitle("Changed3");
        list.get(3).setTitle("Changed4");
        list.get(4).setTitle("Changed5");
        list.get(5).setTitle("Changed6");

        list = c.getMovieList();


        assertEquals("Movie 1 test - testing title", "MOVIE1", list.get(0).getTitle());
        assertEquals("Movie 2 test - testing title", "MOVIE2", list.get(1).getTitle());
        assertEquals("Movie 3 test - testing title", "MOVIE3", list.get(2).getTitle());
        assertEquals("Movie 4 test - testing title", "MOVIE4", list.get(3).getTitle());
        assertEquals("Movie 5 test - testing title", "MOVIE5", list.get(4).getTitle());
        assertEquals("Movie 6 test - testing title", "MOVIE6", list.get(5).getTitle());

    }

    @Test
    public void test_GetMovieWithLowestRating_emptyList() {
        Festival c = new Festival("test");
        assertEquals("No movies added to list.", null, c.getMovieWithLowestRating());
    }

    @Test
    public void test_GetMovieWithLowestRating_OneMovieInFestival() {
        Festival c = new Festival("test");
        Movie    m = new Movie("TEST1", 1);
        c.addMovie(m);
        Movie lowest = c.getMovieWithLowestRating();
        assertEquals("Festival only has one movie ('Test1',1) - testing title.", "TEST1", lowest.getTitle());
        assertEquals("Festival only has one movie ('Test1',1)- testing rating.", 1, lowest.getRating());
        //assertFalse("Festival only has one movie ('Test1',1)- testing encapsulation.", lowest == m);
    }

    @Test
    public void test_GetMovieWithLowestRating_ListHasTwoMoviesWithSameRating() {
        Festival c  = new Festival("test");
        Movie    m1 = new Movie("MOVIE1", 3);
        Movie    m2 = new Movie("Movie2", 3);
        c.addMovie(m1);
        c.addMovie(m2);

        Movie lowest = c.getMovieWithLowestRating();

        assertEquals("Festival only has two movies with same rating, expected to get first added ('MOVIE1') - testing title.", "MOVIE1", lowest.getTitle());
        assertEquals("Festival only has two movies with same rating, expected to get first added ('MOVIE1')- testing rating.", 3, lowest.getRating());
        //assertFalse("Festival only has two movies with same rating, expected to get first added ('MOVIE1')- testing encapsulation.", lowest == m1);
    }

    @Test
    public void test_GetMovieWithLowestRating_LowestInMiddle() {
        Festival c  = new Festival("test");
        Movie    m1 = new Movie("MOVIE1", 3);
        Movie    m2 = new Movie("MOVIE2", 1);
        Movie    m3 = new Movie("MOVIE3", 2);
        c.addMovie(m1);
        c.addMovie(m2);
        c.addMovie(m3);

        Movie lowest = c.getMovieWithLowestRating();

        assertEquals("Festival has three movies with lowest rating in middle ('MOVIE2',1) - testing title.", "MOVIE2", lowest.getTitle());
        assertEquals("Festival has three movies with lowest rating in middle ('MOVIE2',1)- testing rating.", 1, lowest.getRating());
        assertFalse("Festival has three movies with lowest rating in middle ('MOVIE2',1)- testing encapsulation.", lowest == m2);
    }

    @Test
    public void test_GetMovieWithLowestRating_LastIsLowest() {
        Festival c  = new Festival("test");
        Movie    m1 = new Movie("MOVIE1", 7);
        Movie    m2 = new Movie("MOVIE2", 7);
        Movie    m3 = new Movie("MOVIE3", 5);
        Movie    m4 = new Movie("MOVIE4", 10);
        Movie    m5 = new Movie("MOVIE5", 9);
        Movie    m6 = new Movie("MOVIE6", 4);
        c.addMovie(m1);
        c.addMovie(m2);
        c.addMovie(m3);
        c.addMovie(m4);
        c.addMovie(m5);
        c.addMovie(m6);

        Movie lowest = c.getMovieWithLowestRating();

        assertEquals("Festival has three movies with lowest rating at end ('MOVIE6',4) - testing title.", "MOVIE6", lowest.getTitle());
        assertEquals("Festival has three movies with lowest rating at end ('MOVIE6',4)- testing rating.", 4, lowest.getRating());

        lowest.setTitle("Changed");
        Movie b = c.getMovieList().get(5);

        assertEquals("Festival has three movies with lowest rating at end ('MOVIE6',4)- testing encapsulation.", "MOVIE6", b.getTitle());
    }

}
