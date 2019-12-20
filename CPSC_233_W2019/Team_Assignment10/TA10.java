import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code TA10} class provides a static recursive method that will remove all the vowels from a given string and
 * return what is left as a new string
 *
 * @author Group25
 * @date 2019-04-05
 */
public class TA10 {

    /**
     * Define a hash set constant which contains all uppercase vowels
     */
    private static final HashSet<Character> VOWELS_SET =
            Stream.of('A', 'E', 'I', 'O', 'U').collect(Collectors.toCollection(HashSet::new));

    /**
     * a function takes a string and return a string without containing any vowels
     *
     * @param s string to take
     * @return a string without any vowels
     */
    public static String removeVowels(String s) {
        String result = "";

        // base case
        if (s.isEmpty()) {
            return result;
        }

        char ch = s.charAt(0);
        if (!VOWELS_SET.contains(Character.toUpperCase(ch))) {
            result += ch;
        }
        return result + removeVowels(s.substring(1));
    }
}
