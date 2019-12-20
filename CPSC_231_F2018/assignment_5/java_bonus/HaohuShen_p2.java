/**
 * This program simulates a visual and interactive environment that allows a
 * human player to play a variant of the match-three game, Taffy Tangle.
 * <p>
 * Compilation:  javac -cp stdlib.jar: HaohuShen_p2.java (Linux / macOS)
 * Execution:    java  -cp stdlib.jar: HaohuShen_p2 (Linux / macOS)
 * <p>
 * Compilation and execution in Windows 10:
 * java -cp stdlib.jar HaohuShen_p2.java
 * <p>
 * Dependencies: stdlib.jar
 * <p>
 * stdlib.jar is downloaded from:
 * https://introcs.cs.princeton.edu/java/stdlib/stdlib.jar
 * And it is also uploaded as a part of assignment5.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 11/18/2018
 */

import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Define the base class of taffies.
 */
abstract class Taffy {

    private final Color color;
    private       int   x;
    private       int   y;

    Taffy(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int get_x() {
        return x;
    }

    public void set_x(int x) {
        this.x = x;
    }

    public int get_y() {
        return y;
    }

    public void set_y(int y) {
        this.y = y;
    }

    Color get_color() {
        return color;
    }

    public void draw() {
    }
}

class Circle extends Taffy {

    private final int r;

    public Circle(int x, int y) {
        super(x, y, new Color(234, 30, 44));
        r = 40;
    }

    public void draw() {
        StdDraw.setPenColor(super.get_color());
        StdDraw.filledCircle(super.get_x(), super.get_y(), r);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.circle(super.get_x(), super.get_y(), r);
    }
}

class Triangle extends Taffy {

    public Triangle(int x, int y) {
        super(x, y, new Color(247, 201, 69));
    }

    public void draw() {
        double[] x_set = {super.get_x() - 35, super.get_x() + 35, super.get_x()};
        double[] y_set = {super.get_y() - 10 - 35 / Math.pow(3, 0.5),
                super.get_y() - 10 - 35 / Math.pow(3, 0.5),
                super.get_y() - 10 + 70 / Math.pow(3, 0.5)};

        StdDraw.setPenColor(super.get_color());
        StdDraw.filledPolygon(x_set, y_set);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.polygon(x_set, y_set);
    }
}

class Square extends Taffy {

    private final int r;

    public Square(int x, int y) {
        super(x, y, new Color(255, 253, 84));
        r = 35;
    }

    public void draw() {
        double[] x_set = {super.get_x() - r, super.get_x() + r,
                super.get_x() + r, super.get_x() - r};
        double[] y_set = {super.get_y() - r, super.get_y() - r,
                super.get_y() + r, super.get_y() + r};

        StdDraw.setPenColor(super.get_color());
        StdDraw.filledPolygon(x_set, y_set);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.polygon(x_set, y_set);
    }
}

class Pentagon extends Taffy {

    private final int r;

    public Pentagon(int x, int y) {
        super(x, y, new Color(224, 138, 232));
        r = 40;
    }

    public void draw() {
        double[] x_set = {0, 0, 0, 0, 0};
        double[] y_set = {0, 0, 0, 0, 0};
        for (int i = 1; i <= 5; ++i) {
            x_set[i - 1] = super.get_x() + r * Math.sin(0.4 * (i - 1) * Math.PI);
            y_set[i - 1] = super.get_y() + r * Math.cos(0.4 * (i - 1) * Math.PI);
        }

        StdDraw.setPenColor(super.get_color());
        StdDraw.filledPolygon(x_set, y_set);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.polygon(x_set, y_set);
    }
}

class Diamond extends Taffy {

    private final int r;

    public Diamond(int x, int y) {
        super(x, y, new Color(117, 250, 76));
        r = 40;
    }

    public void draw() {
        double[] x_set = {super.get_x() - r, super.get_x(), super.get_x() + r, super.get_x()};
        double[] y_set = {super.get_y(), super.get_y() + r, super.get_y(), super.get_y() - r};

        StdDraw.setPenColor(super.get_color());
        StdDraw.filledPolygon(x_set, y_set);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.polygon(x_set, y_set);
    }
}

class Parallelogram extends Taffy {

    private final int h;

    public Parallelogram(int x, int y) {
        super(x, y, new Color(0, 32, 245));
        h = 60;
    }

    public void draw() {
        double[] x_set = {0, 0, 0, 0};
        double[] y_set = {0, 0, 0, 0};

        x_set[0] = super.get_x() - Math.pow(3, 0.5) * h / 2 + 10;
        x_set[1] = super.get_x() + (2.0 - Math.pow(3, 0.5)) * h / 2 + 10;
        x_set[2] = super.get_x() + Math.pow(3, 0.5) * h / 2 - 10;
        x_set[3] = super.get_x() - (2.0 - Math.pow(3, 0.5)) * h / 2 - 10;
        y_set[0] = super.get_y() - h / 2.0;
        y_set[1] = super.get_y() - h / 2.0;
        y_set[2] = super.get_y() + h / 2.0;
        y_set[3] = super.get_y() + h / 2.0;

        StdDraw.setPenColor(super.get_color());
        StdDraw.filledPolygon(x_set, y_set);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.polygon(x_set, y_set);
    }
}

/**
 * Define a temporary, 'empty' type of taffy.
 */
class Default extends Taffy {

    public Default() {
        super(0, 0, new Color(0, 0, 0));
    }
}

class Tuple {

    private int first;
    private int second;
    private int third;

    public Tuple() {
        first = -1;
        second = -1;
        third = -1;
    }

    public int get_first() {
        return first;
    }

    public void set_first(int first) {
        this.first = first;
    }

    public int get_second() {
        return second;
    }

    public void set_second(int second) {
        this.second = second;
    }

    public int get_third() {
        return third;
    }

    public void set_third(int third) {
        this.third = third;
    }

    public boolean is_default() {
        return (first == -1 && second == -1 && third == -1);
    }
}

class Pair {

    public final Taffy taffy;
    public       int   distance;

    public Pair(Taffy taffy, int distance) {
        this.taffy = taffy;
        this.distance = distance;
    }
}

class HaohuShen_p2 {

    private final static String[] TAFFY_NAME_LIST = {"Diamond",
            "Parallelogram", "Pentagon", "Square", "Triangle", "Circle"};

    private static final Taffy[][]                   board              = new Taffy[9][7];
    private static       Map.Entry<Integer, Integer> red_frame_position =
            new AbstractMap.SimpleEntry<>(-1, -1);

    private static int     score           = 0;
    private static boolean is_game_over    = false;
    private static int     moves_remaining = 30;

    /**
     * Check if a point is in the taffy.
     */
    private static boolean point_in_taffy(Taffy taffy) {
        double temp_x = StdDraw.mouseX();
        if (temp_x > taffy.get_x() - 50 && temp_x < taffy.get_x() + 50) {
            double temp_y = StdDraw.mouseY();
            return temp_y > taffy.get_y() - 50 && temp_y < taffy.get_y() + 50;
        }
        return false;
    }

    /**
     * Convert the array[i][j] to the corresponding coordinates in the canvas.
     */
    private static Map.Entry<Integer, Integer> array_to_canvas(int i, int j) {
        return new AbstractMap.SimpleEntry<>(100 * j, 100 * (8 - i));
    }

    /**
     * Draw the red frame selector if no other red frame selectors exist in the canvas.
     */
    private static void draw_red_frame() {
        if (red_frame_position.getKey() != -1 && red_frame_position.getValue() != -1) {
            Map.Entry<Integer, Integer> pos = array_to_canvas(red_frame_position.getKey(),
                    red_frame_position.getValue());

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.square(pos.getKey(), pos.getValue(), 45);
        }
    }

    /**
     * Capture the mouse-click position, if it is legal, then assign red_frame_position the center
     * of the grid which is clicked and return true, otherwise return false.
     */
    private static boolean mouse_coordinate_capture() {
        if (StdDraw.isMousePressed()) {
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 7; ++j) {
                    if (!board[i][j].getClass().getName().equals("Default")) {
                        if (point_in_taffy(board[i][j])) {
                            red_frame_position = new AbstractMap.SimpleEntry<>(i, j);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Swap the location of two taffies in the board and return it,
     * an exception will be raised if any argument is invalid.
     */
    private static void swap(int i0, int j0, int i1, int j1, Taffy[][] board) {
        if (i0 < 0 || i0 > board.length - 1) {
            throw new IllegalArgumentException();
        }
        if (i1 < 0 || i1 > board.length - 1) {
            throw new IllegalArgumentException();
        }
        if (j0 < 0 || j0 > board[0].length - 1) {
            throw new IllegalArgumentException();
        }
        if (j1 < 0 || j1 > board[0].length - 1) {
            throw new IllegalArgumentException();
        }

        int temp_x = board[i0][j0].get_x();
        int temp_y = board[i0][j0].get_y();

        board[i0][j0].set_x(board[i1][j1].get_x());
        board[i0][j0].set_y(board[i1][j1].get_y());

        board[i1][j1].set_x(temp_x);
        board[i1][j1].set_y(temp_y);

        Taffy temp_taffy = board[i0][j0];
        board[i0][j0] = board[i1][j1];
        board[i1][j1] = temp_taffy;
    }

    /**
     * Judge if two taffies are the same type.
     */
    private static boolean if_taffy_same_type(Taffy taffy0, Taffy taffy1) {
        if (taffy0.getClass().getName().equals(taffy1.getClass().getName())) {
            return Arrays.asList(TAFFY_NAME_LIST).contains(taffy0.getClass().getName());
        }
        return false;
    }

    /**
     * Return if there are at least three same type of taffies in the array.
     */
    private static boolean at_least_three_continuous(ArrayList<Taffy> array) {
        int   continuous_count = 0;
        Taffy previous_element = null;
        for (Taffy element : array) {
            if (null == previous_element) {
                previous_element = element;
                ++continuous_count;
            } else if (if_taffy_same_type(element, previous_element)) {
                ++continuous_count;
            } else {
                if (continuous_count >= 3) {
                    return true;
                }
                previous_element = element;
                continuous_count = 1;
            }
        }
        return (continuous_count >= 3);
    }

    /**
     * Return if there are at least three same type of taffies on a match in the matrix.
     */
    private static boolean at_least_three_continuous_in_matrix(Taffy[][] board) {
        int rows;
        int cols;
        try {
            rows = board.length;
            cols = board[0].length;
        } catch (Exception e) {
            return false;
        }

        // check rows
        for (int i = 0; i < rows; ++i) {
            if (at_least_three_continuous(new ArrayList<>(Arrays.asList(board[i])))) {
                return true;
            }
        }

        // check columns
        for (int i = 0; i < cols; ++i) {
            ArrayList<Taffy> temp = new ArrayList<>();
            for (int j = 0; j < rows; ++j) {
                temp.add(board[j][i]);
            }
            if (at_least_three_continuous(temp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Judge if there are at least three same type of taffies in the array
     * Save each satisfied continuous subarray by its head and tail indices as
     * a tuple to a list and return the list.
     */
    private static ArrayList<Map.Entry<Integer, Integer>>
    at_least_three_continuous_array(ArrayList<Taffy> a) {

        ArrayList<Map.Entry<Integer, Integer>> result           = new ArrayList<>();
        int                                    continuous_count = 0;
        Taffy                                  previous_element = null;
        int                                    left             = 0;
        int                                    right            = 0;

        for (int i = 0; i < a.size(); ++i) {
            if (null == previous_element) {
                previous_element = a.get(i);
                ++continuous_count;
            } else if (if_taffy_same_type(a.get(i), previous_element)) {
                ++continuous_count;
                ++right;
            } else {
                if (continuous_count >= 3) {
                    result.add(new AbstractMap.SimpleEntry<>(left, right));
                }
                left = i;
                right = i;
                previous_element = a.get(i);
                continuous_count = 1;
            }
        }
        if (continuous_count >= 3) {
            result.add(new AbstractMap.SimpleEntry<>(left, right));
        }
        return result;
    }

    /**
     * Judge if the player wants to cancel a move or swap taffies.
     * If no three in a match after the swap, an undoing swap will automatically
     * happen.
     */
    private static String cancel_or_swap() {
        if (StdDraw.isMousePressed()) {
            if (red_frame_position.getKey() != -1 && red_frame_position.getValue() != -1) {
                int i0 = red_frame_position.getKey();
                int j0 = red_frame_position.getValue();
                for (int i = 0; i < 9; ++i) {
                    for (int j = 0; j < 7; ++j) {
                        if (!board[i][j].getClass().getName().equals("Default")) {
                            if (point_in_taffy(board[i][j])) {
                                if (!(i == i0 && j == j0)) {
                                    red_frame_position = new AbstractMap.SimpleEntry<>(-1, -1);
                                    if (i == i0 - 1 && j == j0) {
                                        swap(i0, j0, i, j, board);
                                        if (at_least_three_continuous_in_matrix(board)) {
                                            return "swap";
                                        }
                                        StdDraw.clear();
                                        draw_state();
                                        StdDraw.show();
                                        StdDraw.pause(200);
                                        swap(i, j, i0, j0, board);
                                    } else if (i == i0 + 1 && j == j0) {
                                        swap(i0, j0, i, j, board);
                                        if (at_least_three_continuous_in_matrix(board)) {
                                            return "swap";
                                        }
                                        StdDraw.clear();
                                        draw_state();
                                        StdDraw.show();
                                        StdDraw.pause(200);
                                        swap(i, j, i0, j0, board);
                                    } else if (i == i0 && j == j0 - 1) {
                                        swap(i0, j0, i, j, board);
                                        if (at_least_three_continuous_in_matrix(board)) {
                                            return "swap";
                                        }
                                        StdDraw.clear();
                                        draw_state();
                                        StdDraw.show();
                                        StdDraw.pause(200);
                                        swap(i, j, i0, j0, board);
                                    } else if (i == i0 && j == j0 + 1) {
                                        swap(i0, j0, i, j, board);
                                        if (at_least_three_continuous_in_matrix(board)) {
                                            return "swap";
                                        }
                                        StdDraw.clear();
                                        draw_state();
                                        StdDraw.show();
                                        StdDraw.pause(200);
                                        swap(i, j, i0, j0, board);
                                    }
                                    return "cancel";
                                }
                            }
                        }
                    }
                }
            }
        }
        return "None";
    }

    /**
     * Initialize a board with 9*7 taffies where there are no three on a match.
     */
    private static void board_initialization() {
        LinkedList<String> row_subarray = new LinkedList<>();
        LinkedList<String> col_subarray = new LinkedList<>();
        for (int i = 0; i < 9; ++i) {
            row_subarray.clear();
            for (int j = 0; j < 7; ++j) {
                if (i > 1) {
                    col_subarray.clear();
                    col_subarray.add(board[i - 2][j].getClass().getName());
                    col_subarray.add(board[i - 1][j].getClass().getName());
                }
                while (true) {
                    Taffy  obj      = random_taffy_generation(i, j);
                    String obj_name = obj.getClass().getName();
                    if (i <= 1) {
                        if (row_subarray.size() == 2) {
                            if (row_subarray.get(0).equals(obj_name)) {
                                if (row_subarray.get(1).equals(obj_name)) {
                                    continue;
                                }
                            }
                            row_subarray.add(obj_name);
                            row_subarray.poll();
                        } else {
                            row_subarray.add(obj_name);
                        }
                    } else {
                        if (col_subarray.get(0).equals(obj_name)) {
                            if (col_subarray.get(1).equals(obj_name)) {
                                continue;
                            }
                        }
                        if (row_subarray.size() == 2) {
                            if (row_subarray.get(0).equals(obj_name)) {
                                if (row_subarray.get(1).equals(obj_name)) {
                                    continue;
                                }
                            }
                            row_subarray.add(obj_name);
                            row_subarray.poll();
                            col_subarray.add(obj_name);
                            col_subarray.poll();
                        } else {
                            row_subarray.add(obj_name);
                            col_subarray.add(obj_name);
                        }
                    }
                    board[i][j] = obj;
                    break;
                }
            }
        }
    }

    /**
     * Mark all continuous taffies which have at least three of same type on a match as 'X' in a
     * temporary board.
     */
    private static char[][] mark_conditional_taffies() {
        char[][] temp_board = new char[9][7];
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 7; ++j) {
                temp_board[i][j] = '0';
            }
        }
        for (int i = 0; i < 9; ++i) {
            ArrayList<Map.Entry<Integer, Integer>> temp = at_least_three_continuous_array(
                    new ArrayList<>(Arrays.asList(board[i]))
            );
            if (!temp.isEmpty()) {
                for (Map.Entry<Integer, Integer> aTemp : temp) {
                    for (int idx = aTemp.getKey(); idx <= aTemp.getValue(); ++idx) {
                        temp_board[i][idx] = 'X';
                    }
                }
            }
        }
        for (int i = 0; i < 7; ++i) {
            ArrayList<Taffy> temp_list = new ArrayList<>();
            for (int j = 0; j < 9; ++j) {
                temp_list.add(board[j][i]);
            }
            ArrayList<Map.Entry<Integer, Integer>> temp = at_least_three_continuous_array(
                    temp_list);
            if (!temp.isEmpty()) {
                for (Map.Entry<Integer, Integer> aTemp : temp) {
                    for (int idx = aTemp.getKey(); idx <= aTemp.getValue(); ++idx) {
                        temp_board[idx][i] = 'X';
                    }
                }
            }
        }
        return temp_board;
    }

    /**
     * Eliminate all continuous taffies which have at least three of same type on
     * a match in the board and update the score.
     */
    private static boolean eliminate_condition_taffies() {
        char[][] temp_board = mark_conditional_taffies();

        boolean is_eliminated = false;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 7; ++j) {
                if (temp_board[i][j] == 'X') {
                    board[i][j] = new Default();
                    is_eliminated = true;
                    ++score;
                }
            }
        }
        return is_eliminated;
    }

    /**
     * Generate a taffy of random type at board[i][j] and return the instance.
     */
    private static Taffy random_taffy_generation(int i, int j) {
        Map.Entry<Integer, Integer> pos = array_to_canvas(i, j);
        Taffy                       obj;
        String obj_name =
                TAFFY_NAME_LIST[ThreadLocalRandom.current().nextInt(0, TAFFY_NAME_LIST.length)];

        switch (obj_name) {
            case "Circle":
                obj = new Circle(pos.getKey(), pos.getValue());
                break;
            case "Triangle":
                obj = new Triangle(pos.getKey(), pos.getValue());
                break;
            case "Square":
                obj = new Square(pos.getKey(), pos.getValue());
                break;
            case "Pentagon":
                obj = new Pentagon(pos.getKey(), pos.getValue());
                break;
            case "Diamond":
                obj = new Diamond(pos.getKey(), pos.getValue());
                break;
            default:
                obj = new Parallelogram(pos.getKey(), pos.getValue());
        }
        return obj;
    }

    /**
     * Animate falling of a taffy from (x0, taffy.get_y()) to (x0, get_y() - 100).
     */
    private static ArrayList<Taffy> falling_in_a_unit(ArrayList<Taffy> taffy_list) {
        if (taffy_list.isEmpty()) {
            return new ArrayList<>();
        }
        for (int i = 0; i < 4; ++i) {
            StdDraw.clear();
            for (Taffy aTaffy_list : taffy_list) {
                aTaffy_list.set_y(aTaffy_list.get_y() - 25);
                aTaffy_list.draw();
            }
            draw_state();
            StdDraw.show();
            StdDraw.pause(20);
        }
        return taffy_list;
    }

    /**
     * Refill the board by using random_taffy_generation(i, j).
     * Return nothing if the board is already full.
     */
    private static void board_refill() {
        int depth = 0;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 7; ++j) {
                if (board[i][j].getClass().getName().equals("Default")) {
                    ++depth;
                    break;
                }
            }
        }
        if (depth == 0) {
            return;
        }

        // generate random taffies
        Taffy[][] upper_board = new Taffy[depth][7];
        for (int i = 0; i < depth; ++i) {
            for (int j = 0; j < 7; ++j) {
                upper_board[i][j] = new Default();
            }
        }
        ArrayList<Taffy> temp_taffy_save = new ArrayList<>();
        ArrayList<Taffy> taffy_list      = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (board[j][i].getClass().getName().equals("Default")) {
                    upper_board[j][i] = random_taffy_generation(j, i);
                    temp_taffy_save.add(deep_copy(upper_board[j][i]));
                    upper_board[j][i].set_y(upper_board[j][i].get_y() + 100 * depth);
                    taffy_list.add(upper_board[j][i]);
                }
            }
        }

        // simulate the gravity
        for (int i = 0; i < depth; ++i) {
            taffy_list = falling_in_a_unit(taffy_list);
        }

        // board update
        int pos = 0;
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (board[j][i].getClass().getName().equals("Default")) {
                    board[j][i] = temp_taffy_save.get(pos);
                    ++pos;
                }
            }
        }
    }

    /**
     * Animate the falling of taffies after the execution of eliminate_condition_taffies()
     */
    private static void gravity_animation() {
        ArrayList<Tuple> falling_taffy_list = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            ArrayList<Tuple> a     = new ArrayList<>();
            ArrayList<Tuple> a_bak = new ArrayList<>();
            for (int j = 0; j < 9; ++j) {
                a.add(new Tuple());
                a_bak.add(new Tuple());
            }
            for (int j = 0; j < 9; ++j) {
                if (!board[j][i].getClass().getName().equals("Default")) {
                    a.get(j).set_first(j);
                    a.get(j).set_second(i);
                    a.get(j).set_third(0);

                    a_bak.get(j).set_first(j);
                    a_bak.get(j).set_second(i);
                    a_bak.get(j).set_third(0);
                }
            }
            for (int j = 8; j >= 0; --j) {
                if (!a.get(j).is_default()) {
                    int k = j;
                    while (true) {

                        try {
                            if (!a.get(k + 1).is_default()) {
                                break;
                            }
                        } catch (Exception e) {
                            break;
                        }
                        a.get(k).set_third(a.get(k).get_third() + 1);

                        int temp_first  = a.get(k).get_first();
                        int temp_second = a.get(k).get_second();
                        int temp_third  = a.get(k).get_third();

                        a.get(k).set_first(a.get(k + 1).get_first());
                        a.get(k).set_second(a.get(k + 1).get_second());
                        a.get(k).set_third(a.get(k + 1).get_third());

                        a.get(k + 1).set_first(temp_first);
                        a.get(k + 1).set_second(temp_second);
                        a.get(k + 1).set_third(temp_third);
                        ++k;
                    }
                }
            }
            for (int j = 0; j < 9; ++j) {
                if (!a.get(j).is_default() && a.get(j).get_third() != 0) {
                    falling_taffy_list.add(a.get(j));
                }
            }
        }

        if (!falling_taffy_list.isEmpty()) {
            ArrayList<Pair> taffy_array = new ArrayList<>();
            for (Tuple aFalling_taffy_list : falling_taffy_list) {
                int i0 = aFalling_taffy_list.get_first();
                int j0 = aFalling_taffy_list.get_second();
                taffy_array.add(new Pair(board[i0][j0], aFalling_taffy_list.get_third()));
            }
            boolean need_to_check;
            while (true) {
                need_to_check = false;
                ArrayList<Taffy> temp_list = new ArrayList<>();
                for (Pair aTaffy_array : taffy_array) {
                    if (aTaffy_array.distance != 0) {
                        need_to_check = true;
                        temp_list.add(aTaffy_array.taffy);
                        --aTaffy_array.distance;
                    }
                }
                if (!need_to_check) {
                    break;
                }
                falling_in_a_unit(temp_list);
            }

            // board update
            for (int i = 0; i < 7; ++i) {
                ArrayList<Taffy> temp_list = new ArrayList<>();
                int              pos       = 0;
                for (int j = 8; j >= 0; --j) {
                    if (!board[j][i].getClass().getName().equals("Default")) {
                        temp_list.add(board[j][i]);
                    }
                }
                for (int j = 8; j >= 0; --j) {
                    try {
                        board[j][i] = temp_list.get(pos);
                        board[j][i].set_y(100 * (8 - j));
                        ++pos;
                    } catch (Exception e) {
                        board[j][i] = new Default();
                    }
                }
            }
        }
    }

    /**
     * Detect if a valid move exists by Brute-Force Algorithm when the board is full of taffies and
     * no three on a match.
     */
    private static boolean detect_if_a_valid_move_exists() {
        Taffy[][] board_copy = new Taffy[9][7];
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 7; ++j) {
                board_copy[i][j] = deep_copy(board[i][j]);
            }
        }
        boolean no_exception;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 7; ++j) {
                // swap with the upper taffy
                no_exception = true;
                try {
                    swap(i, j, i - 1, j, board_copy);
                } catch (Exception e) {
                    no_exception = false;
                }
                if (no_exception) {
                    if (at_least_three_continuous_in_matrix(board_copy)) {
                        return true;
                    }
                    swap(i - 1, j, i, j, board_copy);
                }
                // swap with the lower taffy
                no_exception = true;
                try {
                    swap(i, j, i + 1, j, board_copy);
                } catch (Exception e) {
                    no_exception = false;
                }
                if (no_exception) {
                    if (at_least_three_continuous_in_matrix(board_copy)) {
                        return true;
                    }
                    swap(i + 1, j, i, j, board_copy);
                }
                // swap with the left taffy
                no_exception = true;
                try {
                    swap(i, j, i, j - 1, board_copy);
                } catch (Exception e) {
                    no_exception = false;
                }
                if (no_exception) {
                    if (at_least_three_continuous_in_matrix(board_copy)) {
                        return true;
                    }
                    swap(i, j - 1, i, j, board_copy);
                }
                // swap with the right taffy
                no_exception = true;
                try {
                    swap(i, j, i, j + 1, board_copy);
                } catch (Exception e) {
                    no_exception = false;
                }
                if (no_exception) {
                    if (at_least_three_continuous_in_matrix(board_copy)) {
                        return true;
                    }
                    swap(i, j + 1, i, j, board_copy);
                }
            }
        }
        return false;
    }

    /**
     * Draw the board and the game status after its update.
     */
    private static void draw_state() {

        // draw the board
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 7; ++j) {
                if (!board[i][j].getClass().getName().equals("Default")) {
                    board[i][j].draw();
                }
            }
        }

        // draw the status
        StdDraw.line(-50, -50, 650, -50);
        StdDraw.setFont(new Font("Helvetica", Font.PLAIN, 30));
        StdDraw.text(75, -100, "Score: " + score);
        if (is_game_over) {
            StdDraw.text(450, -100, "GAME OVER!");
        } else {
            StdDraw.text(450, -100, "Moves remaining: " + moves_remaining);
        }
        if (red_frame_position.getKey() != -1 && red_frame_position.getValue() != -1) {
            draw_red_frame();
        }
    }

    private static Taffy deep_copy(Taffy object) {
        String obj_name = object.getClass().getName();
        Taffy  result;
        switch (obj_name) {
            case "Circle":
                result = new Circle(object.get_x(), object.get_y());
                break;
            case "Triangle":
                result = new Triangle(object.get_x(), object.get_y());
                break;
            case "Square":
                result = new Square(object.get_x(), object.get_y());
                break;
            case "Pentagon":
                result = new Pentagon(object.get_x(), object.get_y());
                break;
            case "Diamond":
                result = new Diamond(object.get_x(), object.get_y());
                break;
            default:
                result = new Parallelogram(object.get_x(), object.get_y());
        }
        return result;
    }

    public static void main(String[] args) {

        // initialization of the canvas
        StdDraw.setCanvasSize(700, 1000);
        StdDraw.setXscale(-50, 650);
        StdDraw.setYscale(-150, 850);
        StdDraw.setPenRadius(0.002);
        StdDraw.enableDoubleBuffering();

        // initialization of the board
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 7; ++j) {
                board[i][j] = random_taffy_generation(i, j);
            }
        }
        board_initialization();

        while (moves_remaining != 0 && detect_if_a_valid_move_exists()) {
            while (true) {
                StdDraw.clear();
                draw_state();
                if (mouse_coordinate_capture()) {
                    break;
                }
                StdDraw.show();
                StdDraw.pause(10);
            }
            while (true) {
                StdDraw.clear();
                draw_state();
                String temp = cancel_or_swap();
                if (temp.equals("swap")) {
                    StdDraw.clear();
                    draw_state();
                    StdDraw.show();
                    StdDraw.pause(200);
                    --moves_remaining;
                    while (true) {
                        if (eliminate_condition_taffies()) {
                            StdDraw.show();
                            StdDraw.pause(100);
                            StdDraw.clear();
                            draw_state();
                            StdDraw.show();
                            StdDraw.pause(500);
                            gravity_animation();
                            StdDraw.clear();
                            draw_state();
                            StdDraw.show();
                            StdDraw.pause(500);
                            StdDraw.clear();
                            board_refill();
                            StdDraw.pause(500);
                        } else {
                            break;
                        }
                    }
                    break;
                } else if (temp.equals("cancel")) {
                    StdDraw.clear();
                    draw_state();
                    StdDraw.show();
                    StdDraw.pause(100);
                    break;
                }
                StdDraw.show();
                StdDraw.pause(10);
            }
        }
        StdDraw.clear();
        is_game_over = true;
        draw_state();

        // wait for the player to close the window manually.
        StdDraw.show();
    }
}
