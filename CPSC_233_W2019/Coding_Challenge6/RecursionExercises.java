import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The class used in CC6
 *
 * @author Haohu Shen
 * @date 2018-04-13
 */
public class RecursionExercises {

    private static final HashSet<Character> VOWELS_SET = new HashSet<>(Arrays.asList('A', 'E', 'I', 'O', 'U', 'Y'));

    public int factorial(int n) {
        if (n < 0) {
            return 0;
        }
        return (n == 0) ? 1 : n * factorial(n - 1);
    }

    public int sum1(int n) {
        if (n < 0) {
            return 0;
        }
        return (n % 3 != 0) ? (n + sum1(n - 1)) : sum1(n - 1);
    }

    public int sum2(ArrayList<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        if (list.get(0) % 3 != 0) {
            return list.get(0) + sum2(new ArrayList<>(list.subList(1, list.size())));
        }
        return sum2(new ArrayList<>(list.subList(1, list.size())));
    }

    public String duplicateVowels(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        Character ch = Character.toUpperCase(str.charAt(0));
        if (VOWELS_SET.contains(ch)) {
            String sb = String.valueOf(str.charAt(0)) +
                    str.charAt(0);
            return sb + duplicateVowels(str.substring(1));
        }
        return str.charAt(0) + duplicateVowels(str.substring(1));
    }
}
