public class BasicJavaP1 {

    public static long floor(double num) {
        return (long) num;
    }

    public static double conversion(double fahr) {
        return (fahr - 32.0) / 1.8;
    }

    public static boolean willRoundUp(double num) {
        return (int) (num * 10) % 10 >= 5;
    }

    public static int sumRange(int start, int end) {
        long endLong = end - 1;
        long gap     = 1;
        if (start > end) {
            endLong = end + 1;
            gap = -1;
        }
        long n = (endLong - (long) start) / gap + 1;
        return (int) (((long) start + endLong) * n / 2);
    }

    public static int countChar(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (c == str.charAt(i)) {
                ++count;
            }
        }
        return count;
    }

    public static int addDigits(int num) {
        int sum = 0;
        while (num > 0) {
            sum = sum + num % 10;
            num /= 10;
        }
        return sum;
    }
}
