/**
 * This program saves all movie items and its relative rating data into multiple
 * arrays, calculates the similarity of ratings of the user and other raters.
 * Output a list of personalized movie recommendations according to the order in
 * the original movie list.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 10/08/2018
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

class HaohuShen_p4 {

    private static final int[] RATING_TO_VALUE    = {0, -5, -3, 1, 3, 5};
    private static final int   MAX_RECOMMENDATION = 12;

    private static String file_movie_list     = "";
    private static String file_class_rating   = "";
    private static String file_my_rating_list = "";
    private static int    movie_count         = 0;

    /**
     * Validate the external input arguments, including the total number of
     * arguments as well as the existence and the reading permission of files.
     * Any invalid element will make the function return False, otherwise return
     * True.
     */
    private static boolean arguments_validation(String[] args) {
        if (args.length == 3) {
            file_movie_list = args[0];
            file_class_rating = args[1];
            file_my_rating_list = args[2];
            return true;
        }
        return false;
    }

    /**
     * Calculate the similarity by summing the inner products of corresponding
     * entries in two arrays.
     */
    private static int similarity(ArrayList<Integer> a, ArrayList<Integer> b) {
        int sum = 0;
        for (int i = 0; i < a.size(); ++i) {
            sum += a.get(i) * b.get(i);
        }
        return sum;
    }

    private static ArrayList<Integer> data_conversion(ArrayList<Integer> a) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer i : a) {
            result.add(RATING_TO_VALUE[i]);
        }
        return result;
    }

    private static ArrayList<Integer> parse_rating_list(String rating_list) {
        String[]           tmp    = rating_list.split(",");
        ArrayList<Integer> result = new ArrayList<>();
        try {
            if (tmp.length != movie_count) {
                throw new IllegalArgumentException();
            }
            for (String aTmp : tmp) {
                int tmp_int = Integer.parseInt(aTmp);
                if (tmp_int < 0 || tmp_int > 5) {
                    throw new IllegalArgumentException();
                }
                result.add(tmp_int);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    public static void main(String[] args) {

        if (!arguments_validation(args)) {
            System.out.println("Arguments invalid, program terminated.");
            System.exit(0);
        }

        // parse movie_list
        ArrayList<String> movie_list = new ArrayList<>();
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

        // parse my_rating_list
        ArrayList<Integer> my_rating_list = new ArrayList<>();
        String             my_name        = "";
        try {
            File            in = new File(file_my_rating_list);
            FileInputStream is = new FileInputStream(in);
            BufferedReader  br = new BufferedReader(new InputStreamReader(is));
            my_name = br.readLine();
            my_rating_list = parse_rating_list(br.readLine());
            my_rating_list = data_conversion(my_rating_list);

        } catch (Exception e) {
            System.out.println("Rating data invalid, program terminated.");
            System.exit(0);
        }

        // parse other people's rating list
        ArrayList<Person>  other_people = new ArrayList<>();
        ArrayList<Integer> other_rating_list;
        String             username;
        try {
            File            in = new File(file_class_rating);
            FileInputStream is = new FileInputStream(in);
            BufferedReader  br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                username = br.readLine();
                if (username == null) {
                    break;
                }
                if (username.equals(my_name)) {
                    br.readLine();
                    continue;
                }
                other_rating_list = parse_rating_list(br.readLine());
                other_rating_list = data_conversion(other_rating_list);
                Person temp_person = new HaohuShen_p4().new Person();
                temp_person.set_rating_list(other_rating_list);
                temp_person.set_similarity(similarity(other_rating_list, my_rating_list));
                other_people.add(temp_person);
            }
        } catch (Exception e) {
            System.out.println("Rating data invalid, program terminated.");
            System.exit(0);
        }

        // calculation of the maximal similarity
        int max_similarity = Collections.max(other_people).get_similarity();

        // obtain the recommended movies and
        // sort the recommended movie list according to the original index
        ArrayList<Map.Entry<String, Integer>> movie_list_with_index = new ArrayList<>();
        for (int i = 0; i < movie_list.size(); ++i) {
            movie_list_with_index.add(new AbstractMap.SimpleEntry<>(movie_list.get(i), i));
        }
        ArrayList<Map.Entry<String, Integer>> recommend = new ArrayList<>();
        for (Person i : other_people) {
            if (i.get_similarity() == max_similarity) {
                int max_rate = -1;
                for (int j = 0; j < movie_count; ++j) {
                    if (my_rating_list.get(j).equals(0) && !i.get_rating_list().get(j).equals(0)) {
                        max_rate = Math.max(max_rate, i.get_rating_list().get(j));
                    }
                }
                for (int j = 0; j < movie_count; ++j) {
                    if (my_rating_list.get(j).equals(0) && !i.get_rating_list().get(j).equals(0)) {
                        if (i.get_rating_list().get(j).equals(max_rate)) {
                            recommend.add(movie_list_with_index.get(j));
                        }
                    }
                }
            }
        }
        recommend.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        //output
        System.out.println("Hello " + my_name + "!");
        System.out.println();
        System.out.println("From your ratings of our " + movie_count + " movies, our CPSC " +
                "231 class believes that you might also like:");
        for (int i = 0; i < recommend.size(); ++i) {
            if (i == MAX_RECOMMENDATION) {
                break;
            }
            System.out.println("  " + recommend.get(i).getKey());
        }
    }

    /**
     * Define a class named Person to save all relative data of an individual.
     */
    private class Person implements Comparable<Person> {

        // field
        private ArrayList<Integer> rating_list;
        private int                similarity;

        // constructor
        Person() {
            rating_list = new ArrayList<>();
            similarity = 0;
        }

        // getter
        ArrayList<Integer> get_rating_list() {
            return new ArrayList<>(rating_list);
        }

        void set_similarity(int similarity) {
            this.similarity = similarity;
        }

        // setter
        void set_rating_list(ArrayList<Integer> rating_list) {
            this.rating_list = new ArrayList<>();
            this.rating_list.addAll(rating_list);
        }

        int get_similarity() {
            return similarity;
        }

        @Override
        // compareTo
        public int compareTo(Person other) {
            return Integer.compare(similarity, other.get_similarity());
        }
    }
}
