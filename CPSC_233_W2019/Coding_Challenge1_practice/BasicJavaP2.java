public class BasicJavaP2 {
    public static boolean isUpper(char aChar) {
        return (aChar >= 'A' && aChar <= 'Z');
    }

    public static double computePolynomial(double x) {
        return (3.0 - x) * (3.0 - x) + 4 * (7.0 + x) - 9.0;
    }

    public static long floorAfterMult(int num1, double num2) {
        return (long) (num1 * num2);
    }

    public static boolean containsAllChars(String str, String chars) {
        boolean hasAns;
        for (int i = 0; i < chars.length(); ++i) {
            hasAns = false;
            for (int j = 0; j < str.length(); ++j) {
                if (str.charAt(j) == chars.charAt(i)) {
                    hasAns = true;
                    break;
                }
            }
            if (!hasAns) {
                return false;
            }
        }
        return true;
    }
}
