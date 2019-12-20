public class Vowels {

    public static int numOfVowels(String s) {
        int count = 0;
        if (s.isEmpty()) {
            return count;
        }
        char ch = Character.toUpperCase(s.charAt(0));
        if (ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U') {
            ++count;
        }
        return (s.length() == 1) ? count : count + numOfVowels(s.substring(1));
    }
}
