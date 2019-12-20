#include <cmath>
#include <ctime>
#include <vector>
#include <string>
#include <algorithm>
#include <unordered_map>

#include "gwindow.h"
#include "gcolor.h"
#include "gfont.h"

const static std::vector<std::string> TAFFY_NAME_LIST =
        {
                "Circle",
                "Triangle",
                "Square",
                "Pentagon",
                "Parallelogram",
                "Diamond"
        };
const static std::string ERROR_MESSAGE = "Invalid arguments, program terminated.";

static int score = 0;
static bool is_game_over = false;
static int moves = 30;
static GRect *red_frame = nullptr;

/* Define the base class of taffies. */
class Taffy {
private:
    int x;
    int y;
public:
    Taffy() {}

    virtual ~Taffy();

    virtual void draw(GWindow *window) = 0;

    virtual void remove(GWindow *window) = 0;

    virtual std::string toString() const {
        return "Taffy";
    }

    int get_x() const { return x; }

    int get_y() const { return y; }

    void set_x(int x) { this->x = x; }

    void set_y(int y) { this->y = y; }
};

Taffy::~Taffy() {}

class Circle : public Taffy {
private:
    GObject *first = nullptr;
    GObject *second = nullptr;
    int x;
    int y;
public:
    int get_x() const { return x; }

    int get_y() const { return y; }

    void set_x(int x) { this->x = x; }

    void set_y(int y) { this->y = y; }

    Circle(int x, int y) {
        this->x = x;
        this->y = y;
        first = new GOval(this->x, this->y, 80, 80);
        second = new GOval(this->x, this->y, 80, 80);
        first->setAntiAliasing(true);
        second->setAntiAliasing(true);
        second->setFillColor(234, 30, 44);
        second->setFilled(true);
    }

    virtual ~Circle();

    void draw(GWindow *window) {
        if (first && second) {
            window->add(first);
            window->add(second);
        }
    }

    void remove(GWindow *window) {
        if (first && second) {
            window->remove(first);
            window->remove(second);
        }
    }

    std::string toString() const {
        return "Circle";
    }
};

Circle::~Circle() {
    delete first;
    delete second;
    first = nullptr;
    second = nullptr;
}

class Square : public Taffy {
private:
    GObject *first = nullptr;
    GObject *second = nullptr;
    int x;
    int y;
public:
    int get_x() const { return x; }

    int get_y() const { return y; }

    void set_x(int x) { this->x = x; }

    void set_y(int y) { this->y = y; }

    Square(int x, int y) {
        this->x = x;
        this->y = y;
        first = new GRect(this->x + 15, this->y + 15, 70, 70);
        second = new GRect(this->x + 15, this->y + 15, 70, 70);
        first->setAntiAliasing(true);
        second->setAntiAliasing(true);
        second->setFillColor(255, 253, 84);
        second->setFilled(true);
    }

    virtual ~Square();

    void draw(GWindow *window) {
        if (first && second) {
            window->add(first);
            window->add(second);
        }
    }

    void remove(GWindow *window) {
        if (first && second) {
            window->remove(first);
            window->remove(second);
        }
    }

    std::string toString() const {
        return "Square";
    }
};

Square::~Square() {
    delete first;
    delete second;
    first = nullptr;
    second = nullptr;
}

class Diamond : public Taffy {
private:
    GObject *first = nullptr;
    GObject *second = nullptr;
    int x;
    int y;
public:
    int get_x() const { return x; }

    int get_y() const { return y; }

    void set_x(int x) { this->x = x; }

    void set_y(int y) { this->y = y; }

    Diamond(int x, int y) {
        this->x = x;
        this->y = y;
        std::initializer_list<GPoint> points =
                {
                        GPoint(this->x + 50, this->y + 10),
                        GPoint(this->x + 90, this->y + 50),
                        GPoint(this->x + 50, this->y + 90),
                        GPoint(this->x + 10, this->y + 50)
                };
        first = new GPolygon(points);
        second = new GPolygon(points);
        first->setAntiAliasing(true);
        second->setAntiAliasing(true);
        second->setFillColor(117, 250, 76);
        second->setFilled(true);
    }

    virtual ~Diamond();

    void draw(GWindow *window) {
        if (first && second) {
            window->add(first);
            window->add(second);
        }
    }

    void remove(GWindow *window) {
        if (first && second) {
            window->remove(first);
            window->remove(second);
        }
    }

    std::string toString() const {
        return "Diamond";
    }
};

Diamond::~Diamond() {
    delete first;
    delete second;
    first = nullptr;
    second = nullptr;
}

class Triangle : public Taffy {
private:
    GObject *first = nullptr;
    GObject *second = nullptr;
    int x;
    int y;
public:
    int get_x() const { return x; }

    int get_y() const { return y; }

    void set_x(int x) { this->x = x; }

    void set_y(int y) { this->y = y; }

    Triangle(int x, int y) {
        this->x = x;
        this->y = y;
        std::initializer_list<GPoint> points =
                {
                        GPoint(this->x + 50, this->y + 20),
                        GPoint(this->x + 85, this->y + 80),
                        GPoint(this->x + 15, this->y + 80)
                };
        first = new GPolygon(points);
        second = new GPolygon(points);
        first->setAntiAliasing(true);
        second->setAntiAliasing(true);
        second->setFillColor(247, 201, 69);
        second->setFilled(true);
    }

    virtual ~Triangle();

    void draw(GWindow *window) {
        if (first && second) {
            window->add(first);
            window->add(second);
        }
    }

    void remove(GWindow *window) {
        if (first && second) {
            window->remove(first);
            window->remove(second);
        }
    }

    std::string toString() const {
        return "Triangle";
    }
};

Triangle::~Triangle() {
    delete first;
    delete second;
    first = nullptr;
    second = nullptr;
}

class Parallelogram : public Taffy {
private:
    GObject *first = nullptr;
    GObject *second = nullptr;
    int x;
    int y;
public:
    int get_x() const { return x; }

    int get_y() const { return y; }

    void set_x(int x) { this->x = x; }

    void set_y(int y) { this->y = y; }

    Parallelogram(int x, int y) {
        this->x = x;
        this->y = y;
        std::initializer_list<GPoint> points =
                {
                        GPoint(this->x + 30, this->y + 20),
                        GPoint(this->x + 90, this->y + 20),
                        GPoint(this->x + 70, this->y + 80),
                        GPoint(this->x + 10, this->y + 80)
                };
        first = new GPolygon(points);
        second = new GPolygon(points);
        first->setAntiAliasing(true);
        second->setAntiAliasing(true);
        second->setFillColor(0, 32, 245);
        second->setFilled(true);
    }

    virtual ~Parallelogram();

    void draw(GWindow *window) {
        if (first && second) {
            window->add(first);
            window->add(second);
        }
    }

    void remove(GWindow *window) {
        if (first && second) {
            window->remove(first);
            window->remove(second);
        }
    }

    std::string toString() const {
        return "Parallelogram";
    }
};

Parallelogram::~Parallelogram() {
    delete first;
    delete second;
    first = nullptr;
    second = nullptr;
}

class Pentagon : public Taffy {
private:
    GObject *first = nullptr;
    GObject *second = nullptr;
    int x;
    int y;
public:
    int get_x() const { return x; }

    int get_y() const { return y; }

    void set_x(int x) { this->x = x; }

    void set_y(int y) { this->y = y; }

    Pentagon(int x, int y) {
        this->x = x;
        this->y = y;
        std::initializer_list<GPoint> points =
                {
                        GPoint(this->x + 50, this->y + 10),
                        GPoint(this->x + 90, this->y + 40),
                        GPoint(this->x + 75, this->y + 80),
                        GPoint(this->x + 25, this->y + 80),
                        GPoint(this->x + 10, this->y + 40)
                };
        first = new GPolygon(points);
        second = new GPolygon(points);
        first->setAntiAliasing(true);
        second->setAntiAliasing(true);
        second->setFillColor(224, 138, 232);
        second->setFilled(true);
    }

    virtual ~Pentagon();

    void draw(GWindow *window) {
        if (first && second) {
            window->add(first);
            window->add(second);
        }
    }

    void remove(GWindow *window) {
        if (first && second) {
            window->remove(first);
            window->remove(second);
        }
    }

    std::string toString() const {
        return "Pentagon";
    }
};

Pentagon::~Pentagon() {
    delete first;
    delete second;
    first = nullptr;
    second = nullptr;
}

class Tuple {
    // save the position of a taffy, as well as its distance to the lower taffies
private:
    int first = -1;
    int second = -1;
    int third = -1;
public:
    Tuple() : first(-1), second(-1), third(-1) {}

    int get_first() const {
        return first;
    }

    int get_second() const {
        return second;
    }

    int get_third() const {
        return third;
    }

    void set_first(int first) {
        this->first = first;
    }

    void set_second(int second) {
        this->second = second;
    }

    void set_third(int third) {
        this->third = third;
    }

    bool is_default() {
        return (first == -1 && second == -1 && third == -1);
    }
};

struct Pair {
    Taffy *taffy;
    int distance;

    Pair(Taffy *obj, int dist) : taffy(obj), distance(dist) {}
};

/* Judge if two taffies are the same type. */
inline
bool if_taffy_same_type(Taffy *taffy0, Taffy *taffy1) {
    if (taffy0 && taffy1) {
        if (taffy0->toString() == taffy1->toString()) {
            if (std::find(TAFFY_NAME_LIST.begin(), TAFFY_NAME_LIST.end(), taffy0->toString()) !=
                TAFFY_NAME_LIST.end()) {
                return true;
            }
        }
    }
    return false;
}

/* Return if there are at least three same type of taffies in the array. */
inline
bool at_least_three_continuous(std::vector<Taffy *> array) {
    for (const auto &element : array) {
        if (!element) {
            return false;
        }
    }
    int continuous_count = 0;
    Taffy *previous_element = nullptr;
    for (const auto &element : array) {
        if (!previous_element) {
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

/* Return if there are at least three same type of taffies on a match in the matrix. */
inline
bool at_least_three_continuous_in_matrix(std::vector<std::vector<Taffy *> > board) {
    if (board.empty() || board.at(0).empty()) {
        return false;
    }
    size_t rows = board.size();
    size_t cols = board.at(0).size();

    // check rows
    for (size_t i = 0; i < rows; ++i) {
        std::vector<Taffy *> temp;
        for (size_t j = 0; j < cols; ++j) {
            temp.push_back(board.at(i).at(j));
        }
        if (at_least_three_continuous(temp)) {
            return true;
        }
    }

    // check columns
    for (size_t i = 0; i < cols; ++i) {
        std::vector<Taffy *> temp;
        for (size_t j = 0; j < rows; ++j) {
            temp.push_back(board.at(j).at(i));
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
inline
std::vector<std::pair<size_t, size_t> > at_least_three_continuous_array(std::vector<Taffy *> a) {
    std::vector<std::pair<size_t, size_t> > result;
    for (const auto &element : a) {
        if (!element) {
            return result;
        }
    }

    int continuous_count = 0;
    Taffy *previous_element = nullptr;
    size_t left = 0;
    size_t right = 0;

    for (size_t i = 0; i < a.size(); ++i) {
        if (!previous_element) {
            previous_element = a.at(i);
            ++continuous_count;
        } else if (if_taffy_same_type(a.at(i), previous_element)) {
            ++continuous_count;
            ++right;
        } else {
            if (continuous_count >= 3) {
                result.push_back(std::make_pair(left, right));
            }
            left = i;
            right = i;
            previous_element = a.at(i);
            continuous_count = 1;
        }
    }
    if (continuous_count >= 3) {
        result.push_back(std::make_pair(left, right));
    }
    return result;
}

/* Convert the array[i][j] to the corresponding coordinates in the canvas. */
inline
std::pair<int, int> array_to_canvas(size_t i, size_t j, std::string taffy_name) {
    if (taffy_name == "Circle") {
        return std::make_pair(100 * j + 10, 100 * i + 10);
    }
    return std::make_pair(100 * j, 100 * i);
}

/* Draw the red frame selector if no other red frame selectors exist in the canvas. */
inline
void draw_red_frame(GWindow *window, const std::pair<int, int> &red_frame_position) {
    if (red_frame_position.first != -1 && red_frame_position.second != -1) {
        size_t first_val = static_cast<size_t>(red_frame_position.first);
        size_t second_val = static_cast<size_t>(red_frame_position.second);
        std::string previous_color = window->getColor();
        auto pos = array_to_canvas(first_val, second_val, "Square");
        red_frame = new GRect(pos.first + 5, pos.second + 5, 90, 90);
        red_frame->setColor(234, 30, 44);
        window->add(red_frame);
    }
}

/**
 * Eliminate all continuous taffies which have at least three of same type on
 * a match in the board and update the score.
 */
inline
void eliminate_red_frame(GWindow *window, std::pair<int, int> &red_frame_position) {
    if (red_frame_position.first != -1 && red_frame_position.second != -1) {
        red_frame_position.first = -1;
        red_frame_position.second = -1;
        window->remove(red_frame);
        delete red_frame;
        red_frame = nullptr;
    }
}

/**
 * Initialize a board with 9*7 taffies where there are no three on a match.
 */
inline
void board_initialization(std::vector<std::vector<Taffy *> > &board) {
    std::deque<std::string> row_subarray;
    std::deque<std::string> col_subarray;
    for (size_t i = 0; i < 9; ++i) {
        row_subarray.clear();
        for (size_t j = 0; j < 7; ++j) {
            if (i > 1) {
                col_subarray.clear();
                col_subarray.push_back(board.at(i - 2).at(j)->toString());
                col_subarray.push_back(board.at(i - 1).at(j)->toString());
            }
            while (true) {
                size_t select = rand() % 6;
                std::string obj_name = TAFFY_NAME_LIST.at(select);
                if (i <= 1) {
                    if (row_subarray.size() == 2) {
                        if (row_subarray.at(0) == obj_name) {
                            if (row_subarray.at(1) == obj_name) {
                                continue;
                            }
                        }
                        row_subarray.push_back(obj_name);
                        row_subarray.pop_front();
                    } else {
                        row_subarray.push_back(obj_name);
                    }
                } else {
                    if (col_subarray.at(0) == obj_name) {
                        if (col_subarray.at(1) == obj_name) {
                            continue;
                        }
                    }
                    if (row_subarray.size() == 2) {
                        if (row_subarray.at(0) == obj_name) {
                            if (row_subarray.at(1) == obj_name) {
                                continue;
                            }
                        }
                        row_subarray.push_back(obj_name);
                        row_subarray.pop_front();
                        col_subarray.push_back(obj_name);
                        col_subarray.pop_front();
                    } else {
                        row_subarray.push_back(obj_name);
                        col_subarray.push_back(obj_name);
                    }
                }
                auto position = array_to_canvas(i, j, TAFFY_NAME_LIST.at(select));
                switch (select) {
                    case 0:
                        board.at(i).at(j) = new Circle(position.first, position.second);
                        break;
                    case 1:
                        board.at(i).at(j) = new Triangle(position.first, position.second);
                        break;
                    case 2:
                        board.at(i).at(j) = new Square(position.first, position.second);
                        break;
                    case 3:
                        board.at(i).at(j) = new Pentagon(position.first, position.second);
                        break;
                    case 4:
                        board.at(i).at(j) = new Parallelogram(position.first, position.second);
                        break;
                    default:
                        board.at(i).at(j) = new Diamond(position.first, position.second);
                        break;
                }
                break;
            }
        }
    }
}

/**
 * Mark all continuous taffies which have at least three of same type on a match as 'X' in a
 * temporary board.
 */
inline
std::vector<std::vector<char> > mark_conditional_taffies(const std::vector<std::vector<Taffy *> > &board) {
    std::vector<std::vector<char> > temp_board(9);
    for (auto &&i : temp_board) {
        i.resize(7);
    }
    for (auto &&i : temp_board) {
        for (auto &&j : i) {
            j = '0';
        }
    }

    for (size_t i = 0; i < 9; ++i) {
        auto temp = at_least_three_continuous_array(board.at(i));
        if (!temp.empty()) {
            for (auto aTemp : temp) {
                for (size_t idx = aTemp.first; idx <= aTemp.second; ++idx) {
                    temp_board.at(i).at(idx) = 'X';
                }
            }
        }
    }
    for (size_t i = 0; i < 7; ++i) {
        std::vector<Taffy *> temp_list;
        for (size_t j = 0; j < 9; ++j) {
            temp_list.push_back(board.at(j).at(i));
        }
        auto temp = at_least_three_continuous_array(temp_list);
        if (!temp.empty()) {
            for (const auto &aTemp : temp) {
                for (size_t idx = aTemp.first; idx <= aTemp.second; ++idx) {
                    temp_board.at(idx).at(i) = 'X';
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
inline
bool eliminate_condition_taffies(std::vector<std::vector<Taffy *> > &board, GWindow *window) {
    auto temp_board = mark_conditional_taffies(board);
    auto is_eliminated = false;
    for (size_t i = 0; i < 9; ++i) {
        for (size_t j = 0; j < 7; ++j) {
            if (temp_board.at(i).at(j) == 'X') {
                board.at(i).at(j)->remove(window);
                delete board.at(i).at(j);
                board.at(i).at(j) = nullptr;
                is_eliminated = true;
                ++score;
            }
        }
    }
    return is_eliminated;
}

/**
 * Swap the location of two taffies in the board and return it,
 * an exception will be raised if any argument is invalid.
 */
inline void
swap_taffy(GWindow *window, size_t i0, size_t j0, size_t i1, size_t j1, std::vector<std::vector<Taffy *> > &board) {
    if (i0 > board.size() - 1 || i1 > board.size() - 1) {
        throw std::invalid_argument(ERROR_MESSAGE);
    }
    if (j0 > board.at(0).size() || j1 > board.at(0).size()) {
        throw std::invalid_argument(ERROR_MESSAGE);
    }
    if (!board.at(i0).at(j0) || !board.at(i1).at(j1)) {
        throw std::invalid_argument(ERROR_MESSAGE);
    }
    if (i0 == i1 && j0 == j1) {
        return;
    }
    auto obj0_type = board.at(i0).at(j0)->toString();
    auto obj1_type = board.at(i1).at(j1)->toString();

    board.at(i0).at(j0)->remove(window);
    board.at(i1).at(j1)->remove(window);
    delete board.at(i0).at(j0);
    delete board.at(i1).at(j1);
    board.at(i0).at(j0) = nullptr;
    board.at(i1).at(j1) = nullptr;

    std::swap(obj0_type, obj1_type);
    auto pos = array_to_canvas(i0, j0, obj0_type);
    if (obj0_type == "Circle") {
        board.at(i0).at(j0) = new Circle(pos.first, pos.second);
    } else if (obj0_type == "Triangle") {
        board.at(i0).at(j0) = new Triangle(pos.first, pos.second);
    } else if (obj0_type == "Square") {
        board.at(i0).at(j0) = new Square(pos.first, pos.second);
    } else if (obj0_type == "Pentagon") {
        board.at(i0).at(j0) = new Pentagon(pos.first, pos.second);
    } else if (obj0_type == "Parallelogram") {
        board.at(i0).at(j0) = new Parallelogram(pos.first, pos.second);
    } else if (obj0_type == "Diamond") {
        board.at(i0).at(j0) = new Diamond(pos.first, pos.second);
    }

    pos = array_to_canvas(i1, j1, obj1_type);
    if (obj1_type == "Circle") {
        board.at(i1).at(j1) = new Circle(pos.first, pos.second);
    } else if (obj1_type == "Triangle") {
        board.at(i1).at(j1) = new Triangle(pos.first, pos.second);
    } else if (obj1_type == "Square") {
        board.at(i1).at(j1) = new Square(pos.first, pos.second);
    } else if (obj1_type == "Pentagon") {
        board.at(i1).at(j1) = new Pentagon(pos.first, pos.second);
    } else if (obj1_type == "Parallelogram") {
        board.at(i1).at(j1) = new Parallelogram(pos.first, pos.second);
    } else if (obj1_type == "Diamond") {
        board.at(i1).at(j1) = new Diamond(pos.first, pos.second);
    }
}

inline
Taffy *deep_copy(Taffy *object) {
    auto obj_name = object->toString();
    if (obj_name == "Circle") {
        return new Circle(object->get_x(), object->get_y());
    } else if (obj_name == "Triangle") {
        return new Triangle(object->get_x(), object->get_y());
    } else if (obj_name == "Square") {
        return new Square(object->get_x(), object->get_y());
    } else if (obj_name == "Pentagon") {
        return new Pentagon(object->get_x(), object->get_y());
    } else if (obj_name == "Diamond") {
        return new Diamond(object->get_x(), object->get_y());
    }
    return new Parallelogram(object->get_x(), object->get_y());
}

/**
 * Detect if a valid move exists by Brute-Force Algorithm when the board is full of taffies and
 * no three on a match.
 */
inline
bool detect_if_a_valid_move_exists(GWindow *window, const std::vector<std::vector<Taffy *> > &board) {

    std::vector<std::vector<Taffy *> > board_copy(9);
    for (auto &&i : board_copy) {
        i.resize(7);
    }
    for (size_t i = 0; i < 9; ++i) {
        for (size_t j = 0; j < 7; ++j) {
            board_copy.at(i).at(j) = deep_copy(board.at(i).at(j));
        }
    }

    // a lambda function to free memory of deepcopy before returning the function value
    auto free_memory = [](std::vector<std::vector<Taffy *> > board_copy) -> void {
        for (auto &&i : board_copy) {
            for (auto &&j : i) {
                if (j) {
                    delete j;
                    j = nullptr;
                }
            }
        }
    };

    bool no_exception;
    for (size_t i = 0; i < 9; ++i) {
        for (size_t j = 0; j < 7; ++j) {
            // swap with the upper taffy
            no_exception = true;
            try {
                swap_taffy(window, i, j, i - 1, j, board_copy);
            } catch (...) {
                no_exception = false;
            }
            if (no_exception) {
                if (at_least_three_continuous_in_matrix(board_copy)) {
                    free_memory(board_copy);
                    return true;
                }
                swap_taffy(window, i - 1, j, i, j, board_copy);
            }
            // swap with the lower taffy
            no_exception = true;
            try {
                swap_taffy(window, i, j, i + 1, j, board_copy);
            } catch (...) {
                no_exception = false;
            }
            if (no_exception) {
                if (at_least_three_continuous_in_matrix(board_copy)) {
                    free_memory(board_copy);
                    return true;
                }
                swap_taffy(window, i + 1, j, i, j, board_copy);
            }
            // swap with the left taffy
            no_exception = true;
            try {
                swap_taffy(window, i, j, i, j - 1, board_copy);
            } catch (...) {
                no_exception = false;
            }
            if (no_exception) {
                if (at_least_three_continuous_in_matrix(board_copy)) {
                    free_memory(board_copy);
                    return true;
                }
                swap_taffy(window, i, j - 1, i, j, board_copy);
            }
            // swap with the right taffy
            no_exception = true;
            try {
                swap_taffy(window, i, j, i, j + 1, board_copy);
            } catch (...) {
                no_exception = false;
            }
            if (no_exception) {
                if (at_least_three_continuous_in_matrix(board_copy)) {
                    free_memory(board_copy);
                    return true;
                }
                swap_taffy(window, i, j + 1, i, j, board_copy);
            }
        }
    }
    free_memory(board_copy);
    return false;
}

/* Animate the falling of taffies after the execution of eliminate_condition_taffies(). */
inline
void gravity_falling(GWindow *window, std::vector<std::vector<Taffy *> > &board) {
    std::vector<Tuple> falling_taffy_list;
    for (size_t i = 0; i < 7; ++i) {
        std::vector<Tuple> a;
        std::vector<Tuple> a_bak;
        for (size_t j = 0; j < 9; ++j) {
            a.push_back(Tuple());
            a_bak.push_back(Tuple());
        }
        for (size_t j = 0; j < 9; ++j) {
            if (board.at(j).at(i)) {
                a.at(j).set_first(static_cast<int>(j));
                a.at(j).set_second(static_cast<int>(i));
                a.at(j).set_third(0);
                a_bak.at(j).set_first(static_cast<int>(j));
                a_bak.at(j).set_second(static_cast<int>(i));
                a_bak.at(j).set_third(0);
            }
        }
        for (size_t j = 8 + 1; j-- > 0;) {
            if (!a.at(j).is_default()) {
                size_t k = j;
                while (true) {
                    try {
                        if (!a.at(k + 1).is_default()) {
                            break;
                        }
                    } catch (...) {
                        break;
                    }
                    a.at(k).set_third(a.at(k).get_third() + 1);
                    int temp_first = a.at(k).get_first();
                    int temp_second = a.at(k).get_second();
                    int temp_third = a.at(k).get_third();
                    a.at(k).set_first(a.at(k + 1).get_first());
                    a.at(k).set_second(a.at(k + 1).get_second());
                    a.at(k).set_third(a.at(k + 1).get_third());
                    a.at(k + 1).set_first(temp_first);
                    a.at(k + 1).set_second(temp_second);
                    a.at(k + 1).set_third(temp_third);
                    ++k;
                }
            }
        }
        for (size_t j = 0; j < 9; ++j) {
            if (!a.at(j).is_default() && a.at(j).get_third() != 0) {
                falling_taffy_list.push_back(a.at(j));
            }
        }
    }

    // board update
    if (!falling_taffy_list.empty()) {

        // generate taffies in updated locations from bottom to up, from left to right
        std::sort(falling_taffy_list.begin(), falling_taffy_list.end(), [](const Tuple &a, const Tuple &b) -> bool {
            if (a.get_first() == b.get_first()) {
                return a.get_second() < b.get_second();
            }
            return a.get_first() > b.get_first();
        });

        std::vector<std::string> falling_taffy_type_list(falling_taffy_list.size());
        for (size_t i = 0; i < falling_taffy_list.size(); ++i) {
            size_t i0 = static_cast<size_t>(falling_taffy_list.at(i).get_first());
            size_t j0 = static_cast<size_t>(falling_taffy_list.at(i).get_second());
            falling_taffy_type_list.at(i) = board.at(i0).at(j0)->toString();
            board.at(i0).at(j0)->remove(window);
            delete board.at(i0).at(j0);
            board.at(i0).at(j0) = nullptr;
        }
        for (size_t i = 0; i < falling_taffy_list.size(); ++i) {
            size_t i0 = static_cast<size_t>(falling_taffy_list.at(i).get_first());
            size_t j0 = static_cast<size_t>(falling_taffy_list.at(i).get_second());
            size_t d = static_cast<size_t>(falling_taffy_list.at(i).get_third());
            auto obj_name = falling_taffy_type_list.at(i);
            auto pos = array_to_canvas(i0 + d, j0, obj_name);

            if (obj_name == "Circle") {
                board.at(i0 + static_cast<size_t>(d)).at(j0) = new Circle(pos.first, pos.second);
            } else if (obj_name == "Triangle") {
                board.at(i0 + static_cast<size_t>(d)).at(j0) = new Triangle(pos.first, pos.second);
            } else if (obj_name == "Square") {
                board.at(i0 + static_cast<size_t>(d)).at(j0) = new Square(pos.first, pos.second);
            } else if (obj_name == "Pentagon") {
                board.at(i0 + static_cast<size_t>(d)).at(j0) = new Pentagon(pos.first, pos.second);
            } else if (obj_name == "Diamond") {
                board.at(i0 + static_cast<size_t>(d)).at(j0) = new Diamond(pos.first, pos.second);
            } else {
                board.at(i0 + static_cast<size_t>(d)).at(j0) = new Parallelogram(pos.first, pos.second);
            }

            board.at(i0 + static_cast<size_t>(d)).at(j0)->draw(window);
        }
    }
}

/* Generate a taffy of random type at board.at(i).at(j) and return the instance. */
inline
void random_taffy_generation(std::vector<std::vector<Taffy *> > &board, size_t i, size_t j) {
    size_t select = rand() % 6;
    auto position = array_to_canvas(i, j, TAFFY_NAME_LIST.at(select));
    switch (select) {
        case 0:
            board.at(i).at(j) = new Circle(position.first, position.second);
            break;
        case 1:
            board.at(i).at(j) = new Triangle(position.first, position.second);
            break;
        case 2:
            board.at(i).at(j) = new Square(position.first, position.second);
            break;
        case 3:
            board.at(i).at(j) = new Pentagon(position.first, position.second);
            break;
        case 4:
            board.at(i).at(j) = new Parallelogram(position.first, position.second);
            break;
        default:
            board.at(i).at(j) = new Diamond(position.first, position.second);
            break;
    }
}

/**
 * Refill the board by using random_taffy_generation(i, j).
 * Return nothing if the board is already full.
 */
inline
void board_refill(GWindow *window, std::vector<std::vector<Taffy *> > &board) {
    for (size_t i = 0; i < board.size(); ++i) {
        for (size_t j = 0; j < board.at(i).size(); ++j) {
            if (!board.at(i).at(j)) {
                random_taffy_generation(board, i, j);
                board.at(i).at(j)->draw(window);
            }
        }
    }
}

/* Check if a point is in the taffy. */
inline
bool point_in_taffy(double taffy_x, double taffy_y, double x, double y) {
    if (x > taffy_x - 50 && x < taffy_x + 50) {
        return (y > taffy_y - 50 && y < taffy_y + 50);
    }
    return false;
}

/* Convert the board.at(i).at(j) to the corresponding coordinates in the canvas. */
inline
bool mouse_coordinate_capture(std::pair<int, int> &red_frame_position, std::vector<std::vector<Taffy *> > &board) {
    double mouse_x = 0.0;
    double mouse_y = 0.0;
    while (true) {
        auto e = waitForEvent();
        if (e.getEventType() == MOUSE_CLICKED) {
            mouse_x = e.getX();
            mouse_y = e.getY();
            break;
        }
    }
    for (size_t i = 0; i < 9; ++i) {
        for (size_t j = 0; j < 7; ++j) {
            if (board.at(i).at(j)) {
                auto pos = array_to_canvas(i, j, board.at(i).at(j)->toString());
                pos.first += 50;
                pos.second += 50;
                if (point_in_taffy(pos.first, pos.second, mouse_x, mouse_y)) {
                    red_frame_position.first = static_cast<int>(i);
                    red_frame_position.second = static_cast<int>(j);
                    return true;
                }
            }
        }
    }
    return false;
}

/* Draw the status of the game. */
inline
void draw_status(GWindow *window, GText *moves_remaining, GText *score_indicator) {
    score_indicator->setText("Score: " + std::to_string(score));
    if (is_game_over) {
        moves_remaining->setText("GAME OVER!");
    } else {
        moves_remaining->setText("Moves remaining: " + std::to_string(moves));
    }
    window->add(moves_remaining);
    window->add(score_indicator);
}

/**
 * Judge if the player wants to cancel a move or swap taffies.
 * If no three in a match after the swap, an undoing swap will automatically
 * happen.
 */
inline std::string
cancel_or_swap(GWindow *window, std::pair<int, int> &red_frame_position, std::vector<std::vector<Taffy *> > &board) {
    double mouse_x = 0.0;
    double mouse_y = 0.0;
    while (true) {
        auto e = waitForEvent();
        if (e.getEventType() == MOUSE_CLICKED) {
            mouse_x = e.getX();
            mouse_y = e.getY();
            break;
        }
    }
    if (red_frame_position.first != -1 && red_frame_position.second != -1) {
        size_t i0 = static_cast<size_t>(red_frame_position.first);
        size_t j0 = static_cast<size_t>(red_frame_position.second);
        for (size_t i = 0; i < 9; ++i) {
            for (size_t j = 0; j < 7; ++j) {
                if (board.at(i).at(j)) {
                    auto pos = array_to_canvas(i, j, board.at(i).at(j)->toString());
                    pos.first += 50;
                    pos.second += 50;
                    if (point_in_taffy(pos.first, pos.second, mouse_x, mouse_y)) {
                        if (!(i == i0 && j == j0)) {
                            if (i == i0 - 1 && j == j0) {
                                swap_taffy(window, i0, j0, i, j, board);
                                if (at_least_three_continuous_in_matrix(board)) {
                                    board.at(i0).at(j0)->draw(window);
                                    board.at(i).at(j)->draw(window);
                                    eliminate_red_frame(window, red_frame_position);
                                    red_frame_position.first = -1;
                                    red_frame_position.second = -1;
                                    return "swap";
                                }
                                window->pause(100);
                                swap_taffy(window, i, j, i0, j0, board);
                                board.at(i0).at(j0)->draw(window);
                                board.at(i).at(j)->draw(window);
                            } else if (i == i0 + 1 && j == j0) {
                                swap_taffy(window, i0, j0, i, j, board);
                                if (at_least_three_continuous_in_matrix(board)) {
                                    board.at(i0).at(j0)->draw(window);
                                    board.at(i).at(j)->draw(window);
                                    eliminate_red_frame(window, red_frame_position);
                                    red_frame_position.first = -1;
                                    red_frame_position.second = -1;
                                    return "swap";
                                }
                                window->pause(100);
                                swap_taffy(window, i, j, i0, j0, board);
                                board.at(i0).at(j0)->draw(window);
                                board.at(i).at(j)->draw(window);
                            } else if (i == i0 && j == j0 - 1) {
                                swap_taffy(window, i0, j0, i, j, board);
                                if (at_least_three_continuous_in_matrix(board)) {
                                    board.at(i0).at(j0)->draw(window);
                                    board.at(i).at(j)->draw(window);
                                    eliminate_red_frame(window, red_frame_position);
                                    red_frame_position.first = -1;
                                    red_frame_position.second = -1;
                                    return "swap";
                                }
                                window->pause(100);
                                swap_taffy(window, i, j, i0, j0, board);
                                board.at(i0).at(j0)->draw(window);
                                board.at(i).at(j)->draw(window);
                            } else if (i == i0 && j == j0 + 1) {
                                swap_taffy(window, i0, j0, i, j, board);
                                if (at_least_three_continuous_in_matrix(board)) {
                                    board.at(i0).at(j0)->draw(window);
                                    board.at(i).at(j)->draw(window);
                                    eliminate_red_frame(window, red_frame_position);
                                    red_frame_position.first = -1;
                                    red_frame_position.second = -1;
                                    return "swap";
                                }
                                window->pause(100);
                                swap_taffy(window, i, j, i0, j0, board);
                                board.at(i0).at(j0)->draw(window);
                                board.at(i).at(j)->draw(window);
                            }
                            eliminate_red_frame(window, red_frame_position);
                            red_frame_position.first = -1;
                            red_frame_position.second = -1;
                            return "cancel";
                        }
                    }
                }
            }
        }
    }
    return "None";
}

static std::vector<std::vector<Taffy *> > board;

int main() {

    /* initialize random seed */
    srand(static_cast<unsigned>(time(nullptr)));

    /* initialization */
    std::pair<int, int> red_frame_position = std::make_pair(-1, -1);
    GWindow *window = new GWindow(700, 1000);
    window->setVisible(false);
    window->setTitle("Taffy Tangle");
    window->setBackground("White");
    window->setExitOnClose(true);
    window->requestFocus();

    GLine *boundary_line = new GLine(GPoint(0, 900), GPoint(700, 900));
    boundary_line->setAntiAliasing(true);

    GText *score_indicator = new GText("Score: " + std::to_string(score), 75, 960);
    score_indicator->setAntiAliasing(true);
    score_indicator->setFont(GFont::toQFont("Helvetica-30-Plain"));

    GText *moves_remaining = new GText("Moves remaining: " + std::to_string(moves), 375, 960);
    moves_remaining->setAntiAliasing(true);
    moves_remaining->setFont(GFont::toQFont("Helvetica-30-Plain"));
    window->add(boundary_line);

    /* initialize board */
    board.resize(9);
    for (auto &&i : board) {
        i.resize(7);
    }
    board_initialization(board);

    /* draw the board */
    for (size_t i = 0; i < 9; ++i) {
        for (size_t j = 0; j < 7; ++j) {
            if (board.at(i).at(j)) {
                board.at(i).at(j)->draw(window);
            }
        }
    }
    draw_status(window, moves_remaining, score_indicator);
    window->setVisible(true);

    while (moves && detect_if_a_valid_move_exists(window, board)) {
        while (true) {
            if (mouse_coordinate_capture(red_frame_position, board)) {
                break;
            }
        }
        draw_red_frame(window, red_frame_position);
        while (true) {
            auto temp = cancel_or_swap(window, red_frame_position, board);
            if (temp == "swap") {
                window->pause(200);
                --moves;
                draw_status(window, moves_remaining, score_indicator);
                while (true) {
                    if (eliminate_condition_taffies(board, window)) {
                        draw_status(window, moves_remaining, score_indicator);
                        window->pause(700);
                        gravity_falling(window, board);
                        window->pause(500);
                        board_refill(window, board);
                        window->pause(700);
                    } else {
                        break;
                    }
                }
                break;
            } else if (temp == "cancel") {
                window->pause(100);
                break;
            }
        }
    }

    is_game_over = true;
    draw_status(window, moves_remaining, score_indicator);

    // window will close after 30sec or the player could close it manually.
    window->pause(30000);
    window->clear();
    window->close();

    // free memory
    window->setVisible(false);
    for (size_t i = 0; i < 9; ++i) {
        for (size_t j = 0; j < 7; ++j) {
            if (board.at(i).at(j)) {
                board.at(i).at(j)->remove(window);
                delete board.at(i).at(j);
                board.at(i).at(j) = nullptr;
            }
        }
    }
    if (red_frame) {
        delete red_frame;
        red_frame = nullptr;
    }
    delete boundary_line;
    boundary_line = nullptr;
    delete score_indicator;
    boundary_line = nullptr;
    score_indicator = nullptr;
    delete moves_remaining;
    moves_remaining = nullptr;
    delete window;
    window = nullptr;

    return 0;
}
