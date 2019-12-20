import java.math.BigDecimal;

public class BasicJava5 {

    public static boolean isDigit(char aChar) {
        return (aChar >= '0' && aChar <= '9');
    }

    public static int computePolynomial(double x) {
        double     a    = 0.5 * (x - 1.0) * (x - 1.0) - 4.0 * (11.0 - x) + 10.0;
        double     k    = a - (int) a;
        BigDecimal test = new BigDecimal(k);

        if (test.compareTo(new BigDecimal("0.5")) >= 0) {
            return (int) a + 1;
        }
        if (test.compareTo(new BigDecimal("-0.5")) <= 0) {
            return (int) a - 1;
        }
        return (int) a;
    }

    public static boolean charsUnusedInString(String str, String chars) {
        for (int i = 0; i < chars.length(); ++i) {
            for (int j = 0; j < str.length(); ++j) {
                if (str.charAt(j) == chars.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int countOnes(int nums) {
        if (nums < 0) {
            return countOnes(-nums);
        }
        int count = 0;
        while (nums > 0) {
            if (nums % 2 == 1) {
                ++count;
            }
            nums /= 2;
        }
        return count;
    }
}
