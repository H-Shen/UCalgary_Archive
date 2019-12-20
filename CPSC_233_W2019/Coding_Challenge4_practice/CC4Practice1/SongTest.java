import org.junit.Test;

import static org.junit.Assert.*;

public class SongTest {

    public static final String CLASSNAME = "Song";
    public static final String FILENAME  = CLASSNAME + ".java";


    // checks if numOfLikes is the only private thing in the class.
    private boolean instanceVariablesArePrivate() {
        boolean numOfLikesIsOnlyPrivate = true;
        boolean numOfLikesIsPrivate     = false;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("private")) {
                    line = line.trim();
                    if (line.length() >= 22) {
                        line = line.substring(0, 22);
                        if (line.equals("private int numOfLikes")) {
                            numOfLikesIsPrivate = true;
                        } else {
                            numOfLikesIsOnlyPrivate = false;
                        }
                    } else {
                        numOfLikesIsOnlyPrivate = false;
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            numOfLikesIsPrivate = false;
        } catch (IOException e) {
            numOfLikesIsPrivate = false;
        }
        return numOfLikesIsPrivate && numOfLikesIsOnlyPrivate;
    }

    private boolean hasMethod(String signature) {
        boolean contains = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains(signature)) {
                    contains = true;
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            contains = false;
        } catch (IOException e) {
            contains = false;
        }
        return contains;

    }

    private boolean toStrInvokesParentToStr() {
        boolean callsGetter = false;
        boolean callsParent = false;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("toString")) {
                    // This should be start of toString method
                    while (!line.contains("}")) {
                        line = in.readLine();
                        if (line.contains("getTitle") || line.contains("getLength")) {
                            callsGetter = true;
                        }
                        if (line.contains("super.toString()")) {
                            callsParent = true;
                        }
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            callsParent = false;
        } catch (IOException e) {
            callsParent = false;
        }
        return callsParent && !callsGetter;
    }

    private void testInterface() {
        assertTrue("Should only have one instance variable (numOfLikes).", instanceVariablesArePrivate());
        assertTrue("Should override toString and it should invoke parent toString (not getter methods in parent).", toStrInvokesParentToStr());
        assertFalse("Should not override getCategory.", hasMethod("char getCategory"));
        assertFalse("Should not override (or call) getTitle.", hasMethod("getTitle"));
        assertFalse("Should not override (or call) getLength.", hasMethod("getLength"));
        assertFalse("Should not override (or call) setTitle.", hasMethod("setTitle"));
        assertFalse("Should not override (or call) setLength.", hasMethod("setLength"));

    }


    // Testing constructors
    @Test
    public void test_Constructor() {
        testInterface();
        Song c = new Song("despicable me 3", 200);
        assertEquals("Created song with 'despicable me 3' title, length 200 - testing title", "DESPICABLE ME 3", c.getTitle());
        assertEquals("Created song with 'despicable me 3' title, length 200 - testing numOfLikes", 0, c.getNumOfLikes());
    }


    @Test
    public void test_CopyConstructor() {
        testInterface();
        Song c = new Song("TEST1", 145);
        c.addLikes(200);
        Song c2 = new Song(c);
        assertEquals("Testing Copy Constructor, copying 'TEST1' title, length 145, added 200 Likes - testing title", "TEST1", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'TEST1' title, length 145, added 200 Likes - testing length", 145, c2.getLength());
        assertEquals("Testing Copy Constructor, copying 'TEST1' title, length 145, added 200 Likes - testing numOfLikes", 200, c2.getNumOfLikes());
    }

    // Testing setter and getters


    @Test
    public void test_setter_and_getter_numOfLikes_addZero() {
        testInterface();
        Song c = new Song("TestAddZeroLikes", 11);
        c.addLikes(0);
        assertEquals("Set numOfLikes to zero.", 0, c.getNumOfLikes());
    }

    @Test
    public void test_setter_and_getter_numOfLikes_AddOne() {
        testInterface();
        Song c = new Song("TestAddOneLike", 11);
        c.addLikes(1);
        assertEquals("Added 1 like.", 1, c.getNumOfLikes());
    }

    @Test
    public void test_setter_and_getter_numOfLikes_negativeAdd() {
        testInterface();
        Song c = new Song("TestNegativeNumOfLikes", 11);
        c.addLikes(-1);
        assertEquals("Add negative number, should remain 0", 0, c.getNumOfLikes());
    }

    @Test
    public void test_setter_and_getter_numOfLikes_manyAdds() {
        testInterface();
        Song c = new Song("TestManyAdds", 11);
        c.addLikes(11);
        c.addLikes(11);
        c.addLikes(-5);
        c.addLikes(100);
        assertEquals("Many addLikes: 11, 11, -5, 100.", 122, c.getNumOfLikes());
    }

    @Test
    public void test_toString() {
        testInterface();
        Song c = new Song("TESTING_TO_STRING", 20);
        c.addLikes(123);
        assertEquals("title: TESTING_TO_STRING, numOfLikes 123 and length 20", "Title: TESTING_TO_STRING Length: 20 Likes: 123", c.toString());
    }

    @Test
    public void test_getCategory_F() {
        testInterface();
        Song c = new Song("TestingCategoryF", 0);
        assertEquals("No likes", 'F', c.getCategory());
        c.addLikes(5);
        assertEquals("Added 5 likes", 'F', c.getCategory());
        c.addLikes(4);
        assertEquals("Added 5 then 4 likes", 'F', c.getCategory());
    }

    @Test
    public void test_getCategory_D() {
        testInterface();
        Song c = new Song("TestingCategoryD", 0);
        c.addLikes(10);
        assertEquals("Added 10 likes", 'D', c.getCategory());
        c.addLikes(20);
        assertEquals("Added 10 then 20 likes", 'D', c.getCategory());
        c.addLikes(19);
        assertEquals("Added 10 then 20 then 19 likes", 'D', c.getCategory());
    }

    @Test
    public void test_getCategory_C() {
        testInterface();
        Song c = new Song("TestingCategoryC", 0);
        c.addLikes(50);
        assertEquals("Added 50 likes", 'C', c.getCategory());
        c.addLikes(200);
        assertEquals("Added 50 then 200 likes", 'C', c.getCategory());
        c.addLikes(249);
        assertEquals("Added 50 then 200 then 249 likes", 'C', c.getCategory());
    }

    @Test
    public void test_getCategory_B() {
        testInterface();
        Song c = new Song("TestingCategoryB", 0);
        c.addLikes(500);
        assertEquals("Added 500 likes", 'B', c.getCategory());
        c.addLikes(2000);
        assertEquals("Added 500 then 2000 likes", 'B', c.getCategory());
        c.addLikes(2499);
        assertEquals("Added 500 then 2000 then 2499 likes", 'B', c.getCategory());
    }

    @Test
    public void test_getCategory_A() {
        testInterface();
        Song c = new Song("TestingCategoryA", 0);
        c.addLikes(5000);
        assertEquals("Added 5000 likes", 'A', c.getCategory());
        c.addLikes(45678);
        assertEquals("Added 5000 then 45678 likes", 'A', c.getCategory());
    }
}
