/**
 * This program saves all movie items and its relative rating data into multiple
 * arrays, calculates every movie's average rating and variance, as well as
 * sorts the arrays and print out contents according to the requirements.
 * Fractions would be used to express average ratings and variances in order to
 * avoid absolute errors while being sorted.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 10/14/2018
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Comparator;

class HaohuShen_p3 {

    private static String            file_movie_list       = "";
    private static String            file_class_rating     = "";
    private static int               movie_count           = 0;
    private static int               student_count         = 0;
    private static ArrayList<String> movie_list            = new ArrayList<>();
    private static int[][]           stats                 = null;
    private static int[]             rating_list           = null;
    private static Fraction[]        average_rating_list   = null;
    private static Fraction[]        variance_list         = null;
    private static String[]          most_popular_list     = null;
    private static String[]          least_popular_list    = null;
    private static String[]          highest_rated_list    = null;
    private static String[]          lowest_rated_list     = null;
    private static String[]          most_contentious_list = null;

    /**
     * Validate the external input arguments, including the total number of
     * arguments as well as the existence and the reading permission of files.
     * Any invalid element will make the function return False, otherwise return
     * True.
     */
    private static boolean arguments_validation(String[] args) {
        if (args.length == 2) {
            file_movie_list = args[0];
            file_class_rating = args[1];
            return true;
        }
        return false;
    }

    /**
     * Validate every element in the rating list, including the length of list
     * and whether every element should be an integer between 0 and 5. Any
     * invalid element will make the function return False, otherwise return
     * True. The global variable rating_list will be changed.
     */
    private static boolean is_valid(String rating_string) {
        String[] rating_list_str = rating_string.split(",");
        if (rating_list_str.length != movie_count) {
            return false;
        }
        rating_list = new int[rating_list_str.length];
        for (int i = 0; i < rating_list.length; ++i) {
            rating_list[i] = Integer.parseInt(rating_list_str[i]);
        }
        for (int i : rating_list) {
            if (i < 0 || i > 5) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the average rating of each movie in movie_list and assign the
     * result to the corresponding index in average_point_cnt. The average rating
     * of the movies had been never seen would be ignored.
     */
    private static void calculate_average_rating() {
        for (int i = 0; i < movie_count; ++i) {
            if (stats[i][6] == 0) {
                continue;
            }
            int total = stats[i][1] + stats[i][2] * 2 + stats[i][3] * 3 + stats[i][4] * 4 +
                    stats[i][5] * 5;
            BigInteger n = new BigInteger(String.valueOf(total));
            BigInteger d = new BigInteger(String.valueOf(stats[i][6]));
            average_rating_list[i].set_numerator(n);
            average_rating_list[i].set_denominator(d);
            average_rating_list[i].reduce();
        }
    }

    /**
     * Calculate the variance of each movie in movie_list using the formula in
     * the description and assign the result to the corresponding index in
     * variance_cnt. When only 1 person rated the movie (N = 1) would cause
     * the program terminated immediately.
     */
    private static void calculate_variance() {
        for (int i = 0; i < movie_count; ++i) {
            if (stats[i][6] == 1) {
                System.out.println("Rating data invalid, program terminated.");
                System.exit(0);
            }

            int a = stats[i][1];
            int b = stats[i][2];
            int c = stats[i][3];
            int d = stats[i][4];
            int e = stats[i][5];
            int N = c * d + 4 * c * e + d * e + b * (c + 4 * d + 9 * e) + a * (b + 4 * c +
                    9 * d + 16 * e);
            int D = (a + b + c + d + e) * (a + b + c + d + e - 1);

            variance_list[i].set_numerator(new BigInteger(String.valueOf(N)));
            variance_list[i].set_denominator(new BigInteger(String.valueOf(D)));
        }
    }

    /**
     * Sort the movies by the most number of nonzero ratings, store and
     * return the result as a list including no more than five entries.
     */
    private static void most_popular_movies() {
        if (movie_count == 0) {
            return;
        }
        ArrayList<Map.Entry<String, Integer>> result = new ArrayList<>();
        for (int i = 0; i < movie_count; ++i) {
            result.add(new AbstractMap.SimpleEntry<>(movie_list.get(i), stats[i][6]));
        }
        result.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (int i = 0; i < movie_count; ++i) {
            most_popular_list[i] = result.get(i).getKey();
        }
    }

    /**
     * Sort the movies by the most number of zero ratings, store and
     * return the result as a list including no more than five entries.
     */
    private static void least_popular_movies() {
        if (movie_count == 0) {
            return;
        }
        ArrayList<Map.Entry<String, Integer>> result = new ArrayList<>();
        for (int i = 0; i < movie_count; ++i) {
            result.add(new AbstractMap.SimpleEntry<>(movie_list.get(i), stats[i][0]));
        }
        result.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (int i = 0; i < movie_count; ++i) {
            least_popular_list[i] = result.get(i).getKey();
        }
    }

    /**
     * Sort the movies by the highest average ratings with at least 10 nonzero
     * ratings, store and return the result as a list including no more than
     * five entries.
     */
    private static void highest_rated_movies() {
        if (movie_count == 0) {
            return;
        }
        ArrayList<Map.Entry<String, Fraction>> result = new ArrayList<>();
        for (int i = 0; i < movie_count; ++i) {
            if (stats[i][6] >= 10) {
                result.add(new AbstractMap.SimpleEntry<>(movie_list.get(i), average_rating_list[i]));
            }
        }
        result.sort(new Comparator<Map.Entry<String, Fraction>>() {
            @Override
            public int compare(Map.Entry<String, Fraction> o1, Map.Entry<String, Fraction> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        highest_rated_list = new String[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            highest_rated_list[i] = result.get(i).getKey();
        }
    }

    /**
     * Sort the movies by the lowest average ratings with at least 10 nonzero
     * ratings, store and return the result as a list including no more than
     * five entries.
     */
    private static void lowest_rated_movies() {
        if (movie_count == 0) {
            return;
        }
        ArrayList<Map.Entry<String, Fraction>> result = new ArrayList<>();
        for (int i = 0; i < movie_count; ++i) {
            if (stats[i][6] >= 10) {
                result.add(new AbstractMap.SimpleEntry<>(movie_list.get(i), average_rating_list[i]));
            }
        }
        result.sort(new Comparator<Map.Entry<String, Fraction>>() {
            @Override
            public int compare(Map.Entry<String, Fraction> o1, Map.Entry<String, Fraction> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        lowest_rated_list = new String[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            lowest_rated_list[i] = result.get(i).getKey();
        }
    }

    /**
     * Sort the movies by the highest variance ratings with at least 10 nonzero
     * ratings, store and return the result as a list including no more than
     * five entries.
     */
    private static void most_contentious_movies() {
        if (movie_count == 0) {
            return;
        }
        ArrayList<Map.Entry<String, Fraction>> result = new ArrayList<>();
        for (int i = 0; i < movie_count; ++i) {
            if (stats[i][6] >= 10) {
                result.add(new AbstractMap.SimpleEntry<>(movie_list.get(i), variance_list[i]));
            }
        }
        result.sort(new Comparator<Map.Entry<String, Fraction>>() {
            @Override
            public int compare(Map.Entry<String, Fraction> o1, Map.Entry<String, Fraction> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        most_contentious_list = new String[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            most_contentious_list[i] = result.get(i).getKey();
        }
    }

    public static void main(String[] args) {

        if (!arguments_validation(args)) {
            System.out.println("Arguments invalid, program terminated.");
            System.exit(0);
        }

        // initialization
        try {
            File            in = new File(file_movie_list);
            FileInputStream is = new FileInputStream(in);
            BufferedReader  br = new BufferedReader(new InputStreamReader(is));
            String          movie_name;
            while (true) {
                movie_name = br.readLine();
                if (movie_name == null) {
                    break;
                }
                movie_list.add(movie_name);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Movie data invalid, program terminated.");
            System.exit(0);
        }
        movie_count = movie_list.size();

        int item_count = 7;
        stats = new int[movie_count][item_count];
        for (int i = 0; i < movie_count; ++i) {
            for (int j = 0; j < item_count; ++j) {
                stats[i][j] = 0;
            }
        }

        average_rating_list = new Fraction[movie_count];
        for (int i = 0; i < movie_count; ++i) {
            average_rating_list[i] = new HaohuShen_p3().new Fraction();
        }

        variance_list = new Fraction[movie_count];
        for (int i = 0; i < movie_count; ++i) {
            variance_list[i] = new HaohuShen_p3().new Fraction();
        }

        most_popular_list = new String[movie_count];
        for (int i = 0; i < movie_count; ++i) {
            most_popular_list[i] = movie_list.get(i);
        }

        least_popular_list = new String[movie_count];
        for (int i = 0; i < movie_count; ++i) {
            least_popular_list[i] = movie_list.get(i);
        }

        // statistics
        try {
            File            in  = new File(file_class_rating);
            FileInputStream is  = new FileInputStream(in);
            BufferedReader  br  = new BufferedReader(new InputStreamReader(is));
            String          tmp = null;
            while (true) {
                if (br.readLine() == null) {
                    break;
                } else {
                    ++student_count;
                }
                if (!is_valid(br.readLine())) {
                    System.out.println("Rating data invalid, program terminated.");
                    System.exit(0);
                }
                for (int i = 0; i < movie_count; ++i) {
                    if (rating_list[i] == 0) {
                        ++stats[i][0];
                        continue;
                    }
                    ++stats[i][rating_list[i]];
                    ++stats[i][6];
                }
            }
        } catch (Exception e) {
            System.out.println("Rating data invalid, program terminated.");
            System.exit(0);
        }

        calculate_average_rating();
        calculate_variance();

        // output

        /***
         * Calculate the average number of movies the class had seen.
         * If no students see movies or no movies in the movie list, a zero
         * will be returned. The return values is a string.
         */
        String avg;
        if (student_count == 0 || movie_count == 0) {
            avg = "0";
        } else {
            int total_movies_seen = 0;
            for (int i = 0; i < movie_count; ++i) {
                total_movies_seen += stats[i][6];
            }
            if (total_movies_seen % student_count == 0) {
                avg = String.valueOf(total_movies_seen / student_count);
            } else {
                avg = String.format("%.3f", total_movies_seen * 1.0 / student_count);
            }
        }
        System.out.println("The average student in CPSC 231 has seen " + avg + " of the "
                + movie_count + " movies.");
        System.out.println();

        most_popular_movies();
        if (most_popular_list.length == 0) {
            System.out.println("No most popular movies available.");
        } else if (most_popular_list.length == 1) {
            System.out.println("The most popular movie was:");
            System.out.println("  " + most_popular_list[0]);
        } else {
            System.out.println("The most popular movies were:");
            int counter = 0;
            for (String i : most_popular_list) {
                ++counter;
                System.out.println("  " + i);
                if (counter == 5) {
                    break;
                }
            }
        }
        System.out.println();

        least_popular_movies();
        if (least_popular_list.length == 0) {
            System.out.println("No least popular movies available.");
        } else if (least_popular_list.length == 1) {
            System.out.println("The least popular movie was:");
            System.out.println("  " + least_popular_list[0]);
        } else {
            System.out.println("The least popular movies were:");
            int counter = 0;
            for (String i : least_popular_list) {
                ++counter;
                System.out.println("  " + i);
                if (counter == 5) {
                    break;
                }
            }
        }
        System.out.println();

        highest_rated_movies();
        if (highest_rated_list.length == 0) {
            System.out.println("No highest rated movies available.");
        } else if (highest_rated_list.length == 1) {
            System.out.println("The highest rated movie was:");
            System.out.println("  " + highest_rated_list[0]);
        } else {
            System.out.println("The highest rated movies were:");
            int counter = 0;
            for (String i : highest_rated_list) {
                ++counter;
                System.out.println("  " + i);
                if (counter == 5) {
                    break;
                }
            }
        }
        System.out.println();

        lowest_rated_movies();
        if (lowest_rated_list.length == 0) {
            System.out.println("No lowest rated movies available.");
        } else if (highest_rated_list.length == 1) {
            System.out.println("The lowest rated movie was:");
            System.out.println("  " + lowest_rated_list[0]);
        } else {
            System.out.println("The lowest rated movies were:");
            int counter = 0;
            for (String i : lowest_rated_list) {
                ++counter;
                System.out.println("  " + i);
                if (counter == 5) {
                    break;
                }
            }
        }
        System.out.println();

        most_contentious_movies();
        if (most_contentious_list.length == 0) {
            System.out.println("No most contentious movies available.");
        } else if (most_contentious_list.length == 1) {
            System.out.println("The most contentious movie was:");
            System.out.println("  " + most_contentious_list[0]);
        } else {
            System.out.println("The most contentious movies were:");
            int counter = 0;
            for (String i : most_contentious_list) {
                ++counter;
                System.out.println("  " + i);
                if (counter == 5) {
                    break;
                }
            }
        }
    }

    public class Fraction implements Comparable<Fraction> {

        // field
        private BigInteger numerator;
        private BigInteger denominator;

        // constructor without arguments
        Fraction() {
            numerator = new BigInteger("0");
            denominator = new BigInteger("1");
        }

        // constructor with arguments
        Fraction(BigInteger numerator, BigInteger denominator) {
            if (denominator.compareTo(new BigInteger("0")) == 0) {
                throw new IllegalArgumentException();
            }

            this.numerator = numerator;
            this.denominator = denominator;

            // the denominator would always be positive
            if (this.denominator.compareTo(new BigInteger("0")) < 0) {
                this.denominator = this.denominator.multiply(new BigInteger("-1"));
                this.numerator = this.numerator.multiply(new BigInteger("-1"));
            }
            reduce();
        }

        // copy constructor
        Fraction(Fraction obj) {
            numerator = obj.get_numerator();
            denominator = obj.get_denominator();
            reduce();
        }

        // subtract
        Fraction subtract(Fraction a) {
            BigInteger n = numerator.multiply(a.get_denominator()).subtract(denominator.multiply(
                    a.get_numerator()));
            BigInteger m = denominator.multiply(a.get_denominator());
            return new Fraction(n, m);
        }

        // getter
        BigInteger get_numerator() {
            return numerator;
        }

        BigInteger get_denominator() {
            return denominator;
        }

        // setter
        void set_numerator(BigInteger numerator) {
            this.numerator = numerator;
        }

        void set_denominator(BigInteger denominator) {
            this.denominator = denominator;
        }

        // reducer
        void reduce() {
            BigInteger gcd = numerator.gcd(denominator);
            numerator = numerator.divide(gcd);
            denominator = denominator.divide(gcd);
        }

        // to string
        @Override
        public String toString() {
            return numerator.toString() + "/" + denominator.toString();
        }

        // compare to
        @Override
        public int compareTo(Fraction other) {
            Fraction a = new Fraction(this.subtract(other));
            if (a.get_numerator().compareTo(new BigInteger("0")) > 0) {
                return 1;
            }
            if (a.get_numerator().compareTo(new BigInteger("0")) < 0) {
                return -1;
            }
            return 0;
        }
    }
}
