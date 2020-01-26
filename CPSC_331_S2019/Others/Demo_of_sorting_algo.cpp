/**
 * A demo of implementations numerous common sorting algorithm.
 * To compile in g++ -std=c++17
 */
#include <vector>
#include <iostream>
#include <random>
#include <algorithm>

namespace InsertionSort {
    /**
     * The "Sorting Larger Subarray" Problem
     *
     * Precondition:
     * a) An array A with length n >= 1, storing elements from some ordered type T, is given as input.
     * b) An integer i such that 1 <= i <= n - 1 is given as input.
     * c) A[h] <= A[h + 1] for every integer h such that 0 <= h <= i - 2
     *
     * Postcondition:
     * a) The entries in A have been reordered or unchanged.
     * b) i has not been changed.
     * c) A[h] <= A[h + 1] for every integer h such that 0 <= h <= i - 1
     *
     *
     * @tparam T some ordered type
     * @param A an array whose base type is T
     * @param i an integer represents an index of an element in A
     */
    template<typename T>
    inline static
    void sortSubarray(std::vector<T> &A, const size_t &i) {
        size_t j = i;
        while ((j > 0) && (A.at(j - 1) > A.at(j))) {
            std::swap(A.at(j), A.at(j - 1));
            --j;
        }
    }

    template<typename T, typename ThreeWayComparator>
    inline static
    void sortSubarray(std::vector<T> &A, const size_t &i,
                      const ThreeWayComparator &comp) {
        size_t j = i;
        while ((j > 0) && (comp(A.at(j - 1), A.at(j)) > 0)) {
            std::swap(A.at(j), A.at(j - 1));
            --j;
        }
    }

    template<typename T>
    inline static
    void sort(std::vector<T> &A) {
        size_t i = 1;
        while (i < A.size()) {
            sortSubarray(A, i);
            ++i;
        }
    }

    template<typename T, typename ThreeWayComparator>
    inline static
    void sort(std::vector<T> &A, const ThreeWayComparator &comp) {
        size_t i = 1;
        while (i < A.size()) {
            sortSubarray(A, i, comp);
            ++i;
        }
    }
}

namespace SelectionSort {
    /**
     * Precondition:
     * a) An array A with length n >= 2, storing elements from some ordered type T, is given as input.
     * b) An integer i such that 0 <= i <= n - 2 is given as input.
     *
     * Postcondition:
     * a) The array A and the integer i are not changed.
     * b) An integer "smallest" that satisfies the following properties is returned as output.
     *    1) i <= smallest <= n - 1
     *    2) A[smallest] <= A[h] for every integer h such that i <= h <= n - 1.
     *
     * @tparam T some ordered type
     * @param A an array whose base type is T
     * @param i an integer represents an index of an element in A
     * @return smallest an integer represents an index of an element in A
     */
    template<typename T>
    inline static
    size_t minIndex(const std::vector<T> &A, const size_t &i) {
        size_t j = i + 1;
        size_t smallest = i;
        while (j < A.size()) {
            if (A.at(j) < A.at(smallest)) {
                smallest = j;
            }
            ++j;
        }
        return smallest;
    }

    template<typename T, typename ThreeWayComparator>
    inline static
    size_t minIndex(const std::vector<T> &A, const size_t &i,
                    const ThreeWayComparator &comp) {
        size_t j = i + 1;
        size_t smallest = i;
        while (j < A.size()) {
            if (comp(A.at(j), A.at(smallest)) < 0) {
                smallest = j;
            }
            ++j;
        }
        return smallest;
    }

    template<typename T>
    inline static
    void sort(std::vector<T> &A) {
        // Case 1: A.size() <= 1
        if (A.size() <= 1) {
            return;
        }
        // Case 2: A.size() >= 2
        size_t i = 0;
        size_t smallest;
        while (i <= A.size() - 2) {
            smallest = minIndex(A, i);
            std::swap(A.at(smallest), A.at(i));
            ++i;
        }
    }

    template<typename T, typename ThreeWayComparator>
    inline static
    void sort(std::vector<T> &A, const ThreeWayComparator &comp) {
        // Case 1: A.size() <= 1
        if (A.size() <= 1) {
            return;
        }
        // Case 2: A.size() >= 2
        size_t i = 0;
        size_t smallest;
        while (i <= A.size() - 2) {
            smallest = minIndex(A, i, comp);
            std::swap(A.at(smallest), A.at(i));
            ++i;
        }
    }
}

namespace HeapSort {
    /**
     * The heapSort using minHeap for solving the 'Sorting in Place' problem. The three-way comparator is
     * supported.
     * Sorting an array in non-decreasing order when array A with length >= 1,
     * storing elements from some ordered type T using heapsort.
     */
    auto isRoot = [](const int &x) { return (x == 0); };
    auto parent = [](const int &x) {
        if (x == 0) {
            throw std::out_of_range("");
        }
        return (x - 1) / 2;
    };
    auto hasLeft = [](const int &x, const int &heapSize) {
        return (2 * x + 1 < heapSize);
    };
    auto left = [](const int &x, const int &heapSize) {
        if (hasLeft(x, heapSize)) {
            return 2 * x + 1;
        }
        throw std::out_of_range("");
    };
    auto hasRight = [](const int &x, const int &heapSize) {
        return (2 * x + 2 < heapSize);
    };
    auto right = [](const int &x, const int &heapSize) {
        if (hasRight(x, heapSize)) {
            return 2 * x + 2;
        }
        throw std::out_of_range("");
    };

    template<typename T, typename ThreeWayComparator>
    inline static
    void bubbleUp(int x, std::vector<T> &A, const ThreeWayComparator &comp) {
        while (!isRoot(x) && comp(A.at(x), A.at(parent(x))) > 0) {
            std::swap(A.at(x), A.at(parent(x)));
            x = parent(x);
        }
    }

    template<typename T>
    inline static
    void bubbleUp(int x, std::vector<T> &A) {
        while (!isRoot(x) && A.at(x) > A.at(parent(x))) {
            std::swap(A.at(x), A.at(parent(x)));
            x = parent(x);
        }
    }

    template<typename T, typename ThreeWayComparator>
    inline static
    void bubbleDown(int x, int heapSize, std::vector<T> &A,
                    const ThreeWayComparator &comp) {
        while (hasLeft(x, heapSize)) {
            if (hasRight(x, heapSize)) {
                if (comp(A.at(left(x, heapSize)), A.at(right(x, heapSize))) >=
                    0) {
                    if (comp(A.at(left(x, heapSize)), A.at(x)) > 0) {
                        std::swap(A.at(left(x, heapSize)), A.at(x));
                        x = left(x, heapSize);
                    } else {
                        break;
                    }
                } else if (comp(A.at(right(x, heapSize)), A.at(x)) > 0) {
                    std::swap(A.at(right(x, heapSize)), A.at(x));
                    x = right(x, heapSize);
                } else {
                    break;
                }
            } else if (comp(A.at(left(x, heapSize)), A.at(x)) > 0) {
                std::swap(A.at(left(x, heapSize)), A.at(x));
                x = left(x, heapSize);
            } else {
                break;
            }
        }
    }

    template<typename T>
    inline static
    void bubbleDown(int x, int heapSize, std::vector<T> &A) {
        while (hasLeft(x, heapSize)) {
            if (hasRight(x, heapSize)) {
                if (A.at(left(x, heapSize)) >= A.at(right(x, heapSize))) {
                    if (A.at(left(x, heapSize)) > A.at(x)) {
                        std::swap(A.at(left(x, heapSize)), A.at(x));
                        x = left(x, heapSize);
                    } else {
                        break;
                    }
                } else if (A.at(right(x, heapSize)) > A.at(x)) {
                    std::swap(A.at(right(x, heapSize)), A.at(x));
                    x = right(x, heapSize);
                } else {
                    break;
                }
            } else if (A.at(left(x, heapSize)) > A.at(x)) {
                std::swap(A.at(left(x, heapSize)), A.at(x));
                x = left(x, heapSize);
            } else {
                break;
            }
        }
    }

    template<typename T>
    inline static
    void sort(std::vector<T> &A) {
        try {
            int heapSize = 1;
            int i = 1;
            int len = static_cast<int>(A.size());

            while (i < len) {
                if (heapSize < len) {
                    int x = heapSize;
                    A.at(x) = A.at(i);
                    ++heapSize;
                    bubbleUp(x, A);
                } else {
                    throw std::out_of_range("");
                }
                ++i;
            }
            i = len - 1;
            while (i > 0) {
                T largest;
                if (heapSize == 0) {
                    throw std::out_of_range("");
                } else {
                    T temp = A.at(heapSize - 1);
                    --heapSize;
                    if (heapSize == 0) {
                        largest = temp;
                    } else {
                        largest = A.at(0);
                        A.at(0) = temp;
                        bubbleDown(0, heapSize, A);
                    }
                }
                A.at(i) = largest;
                --i;
            }
        } catch (...) {}
    }

    template<typename T, typename ThreeWayComparator>
    inline static
    void sort(std::vector<T> &A, const ThreeWayComparator &comp) {
        try {

            int heapSize = 1;
            int i = 1;
            int len = static_cast<int>(A.size());

            while (i < len) {
                if (heapSize < len) {
                    int x = heapSize;
                    A.at(x) = A.at(i);
                    ++heapSize;
                    bubbleUp(x, A, comp);
                } else {
                    throw std::out_of_range("");
                }
                ++i;
            }
            i = len - 1;
            while (i > 0) {
                T largest;
                if (heapSize == 0) {
                    throw std::out_of_range("");
                } else {
                    auto temp = A.at(heapSize - 1);
                    --heapSize;
                    if (heapSize == 0) {
                        largest = temp;
                    } else {
                        largest = A.at(0);
                        A.at(0) = temp;
                        bubbleDown(0, heapSize, A, comp);
                    }
                }
                A.at(i) = largest;
                --i;
            }
        } catch (...) {}
    }
}

namespace MergeSort {
    // Merge two sorted arrays.
    template<typename T>
    std::vector<T>
    mergeTwoSortedArrays(const std::vector<T> &C1, const std::vector<T> &C2) {
        int n1 = static_cast<int>(C1.size());
        int n2 = static_cast<int>(C2.size());
        std::vector<T> D(C1.size() + C2.size());
        int i1 = 0;
        int i2 = 0;
        int j = 0;

        while ((i1 < n1) && (i2 < n2)) {
            if (C1.at(i1) <= C2.at(i2)) {
                D.at(j) = C1.at(i1);
                ++i1;
            } else {
                D.at(j) = C2.at(i2);
                ++i2;
            }
            ++j;
        }
        while (i1 < n1) {
            D.at(j) = C1.at(i1);
            ++i1;
            ++j;
        }
        while (i2 < n2) {
            D.at(j) = C2.at(i2);
            ++i2;
            ++j;
        }
        return D;
    }

    // Merge two sorted arrays. The three-way comparator is supported.
    template<typename T, typename ThreeWayComparator>
    std::vector<T>
    mergeTwoSortedArrays(const std::vector<T> &C1, const std::vector<T> &C2,
                         const ThreeWayComparator &comp) {

        int n1 = static_cast<int>(C1.size());
        int n2 = static_cast<int>(C2.size());
        std::vector<T> D(C1.size() + C2.size());
        int i1 = 0;
        int i2 = 0;
        int j = 0;

        while ((i1 < n1) && (i2 < n2)) {
            if (comp(C1.at(i1), C2.at(i2)) <= 0) {
                D.at(j) = C1.at(i1);
                ++i1;
            } else {
                D.at(j) = C2.at(i2);
                ++i2;
            }
            ++j;
        }
        while (i1 < n1) {
            D.at(j) = C1.at(i1);
            ++i1;
            ++j;
        }
        while (i2 < n2) {
            D.at(j) = C2.at(i2);
            ++i2;
            ++j;
        }
        return D;
    }

    // Main stage of merge_sort. The three-way comparator is supported.
    template<typename T, typename ThreeWayComparator>
    std::vector<T>
    merge_sort(const std::vector<T> &A, const ThreeWayComparator &comp) {
        // Case 1:
        if (A.size() <= 1) {
            return A;
        }
        // Case 2:
        int n = static_cast<int>(A.size());
        int n1, n2;

        if (n % 2 == 0) {
            n1 = n / 2;
            n2 = n / 2;
        } else {
            n1 = n / 2 + 1;
            n2 = n / 2;
        }

        std::vector<T> B2(static_cast<unsigned long>(n2));
        std::vector<T> B1(static_cast<unsigned long>(n1));
        int i = 0;

        while (i < n1) {
            B1.at(i) = A.at(i);
            ++i;
        }
        while (i < n) {
            B2.at(i - n1) = A.at(i);
            ++i;
        }
        // Recursively Sort and Merge
        auto C1 = merge_sort(B1, comp);
        auto C2 = merge_sort(B2, comp);
        return mergeTwoSortedArrays(C1, C2, comp);
    }

    // Main stage of merge_sort
    template<typename T>
    std::vector<T> merge_sort(const std::vector<T> &A) {
        // Case 1:
        if (A.size() <= 1) {
            return A;
        }
        // Case 2:
        int n = static_cast<int>(A.size());
        int n1;
        int n2;
        if (n % 2 == 0) {
            n1 = n / 2;
            n2 = n / 2;
        } else {
            n1 = n / 2 + 1;
            n2 = n / 2;
        }
        std::vector<T> B1(static_cast<unsigned long>(n1));
        std::vector<T> B2(static_cast<unsigned long>(n2));
        int i = 0;
        while (i < n1) {
            B1.at(i) = A.at(i);
            ++i;
        }
        while (i < n) {
            B2.at(i - n1) = A.at(i);
            ++i;
        }
        // Recursively Sort and Merge
        auto C1 = MergeSort::merge_sort(B1);
        auto C2 = MergeSort::merge_sort(B2);
        return mergeTwoSortedArrays(C1, C2);
    }

    template<typename T>
    void sort(std::vector<T> &A) {
        A = MergeSort::merge_sort<T>(A);
    }

    template<typename T, typename ThreeWayComparator>
    void sort(std::vector<T> &A, const ThreeWayComparator &comp) {
        A = MergeSort::merge_sort(A, comp);
    }
}

namespace BubbleSort {
    template<typename T>
    void sort(std::vector<T> &A) {
        bool flag = true; // A boolean flag indicates the array has to be sorted again if it is true
        int n = static_cast<int>(A.size());
        while (flag) {
            flag = false;
            for (int i = 0; i < n - 1; ++i) {
                if (A.at(i) > A.at(i + 1)) {
                    flag = true;
                    std::swap(A.at(i), A.at(i + 1));
                }
            }
        }
    }
    template<typename T, typename ThreeWayComparator>
    void sort(std::vector<T> &A, const ThreeWayComparator &comp) {
        bool flag = true; // A boolean flag indicates the array has to be sorted again if it is true
        int n = static_cast<int>(A.size());
        while (flag) {
            flag = false;
            for (int i = 0; i < n - 1; ++i) {
                if (comp(A.at(i), A.at(i + 1)) > 0) {
                    flag = true;
                    std::swap(A.at(i), A.at(i + 1));
                }
            }
        }
    }
}

namespace Tester {
    // List all sorting algorithms which are gonna be tested
    enum class SORTING_ALGORITHM {
        INSERTION_SORT,
        HEAP_SORT,
        MERGE_SORT,
        SELECTION_SORT,
        BUBBLE_SORT
    };
    // Initialization
    inline std::random_device dev;
    inline std::mt19937 random_generator(dev());
    inline std::uniform_int_distribution<int> dist(
            std::numeric_limits<int>::min(), std::numeric_limits<int>::max());

    // Generator
    inline static
    std::vector<int> generate_array_with_random_integers(const size_t &length) {
        std::vector<int> result;
        for (size_t i = 0; i != length; ++i) {
            result.emplace_back(dist(random_generator));
        }
        return result;
    }

    inline static
    std::vector<std::pair<int, int> >
    generate_array_with_random_pairs(const size_t &length) {
        std::vector<std::pair<int, int> > result;
        for (size_t i = 0; i != length; ++i) {
            result.emplace_back(dist(random_generator), dist(random_generator));
        }
        return result;
    }

    // Comparator
    inline static
    bool pair_comparator(const std::pair<int, int> &lhs,
                         const std::pair<int, int> &rhs) {
        if (lhs.first == rhs.first) {
            return (lhs.second < rhs.second);
        }
        return (lhs.first < rhs.first);
    }

    inline static
    int pair_three_way_comparator(const std::pair<int, int> &lhs,
                                  const std::pair<int, int> &rhs) {
        if (lhs.first == rhs.first) {
            if (lhs.second < rhs.second) {
                return -1;
            } else if (lhs.second == rhs.second) {
                return 0;
            }
            return 1;
        }
        if (lhs.first < rhs.first) {
            return -1;
        }
        return 1;
    }

    // Tester
    inline static
    void test(SORTING_ALGORITHM algorithm, int test_time = 100,
              int array_length = 1000) {

        std::vector<int> array_of_ints;
        std::vector<int> array_of_ints_copy;
        std::vector<std::pair<int, int> > array_of_pairs;
        std::vector<std::pair<int, int> > array_of_pairs_copy;

        switch (algorithm) {
            case SORTING_ALGORITHM::INSERTION_SORT:
                for (int i = 0; i < test_time; ++i) {
                    array_of_ints = generate_array_with_random_integers(
                            array_length);
                    array_of_ints_copy = array_of_ints;
                    sort(array_of_ints.begin(), array_of_ints.end());
                    InsertionSort::sort(array_of_ints_copy);
                    if (array_of_ints != array_of_ints_copy) {
                        throw;
                    }
                }
                for (int i = 0; i < test_time; ++i) {
                    array_of_pairs = generate_array_with_random_pairs(
                            array_length);
                    array_of_pairs_copy = array_of_pairs;
                    sort(array_of_pairs.begin(), array_of_pairs.end(),
                         pair_comparator);
                    InsertionSort::sort(array_of_pairs_copy,
                                        pair_three_way_comparator);
                    if (array_of_pairs != array_of_pairs_copy) {
                        throw;
                    }
                }
                break;
            case SORTING_ALGORITHM::SELECTION_SORT:
                for (int i = 0; i < test_time; ++i) {
                    array_of_ints = generate_array_with_random_integers(
                            array_length);
                    array_of_ints_copy = array_of_ints;
                    sort(array_of_ints.begin(), array_of_ints.end());
                    SelectionSort::sort(array_of_ints_copy);
                    if (array_of_ints != array_of_ints_copy) {
                        throw;
                    }
                }
                for (int i = 0; i < test_time; ++i) {
                    array_of_pairs = generate_array_with_random_pairs(
                            array_length);
                    array_of_pairs_copy = array_of_pairs;
                    sort(array_of_pairs.begin(), array_of_pairs.end(),
                         pair_comparator);
                    SelectionSort::sort(array_of_pairs_copy,
                                        pair_three_way_comparator);
                    if (array_of_pairs != array_of_pairs_copy) {
                        throw;
                    }
                }
                break;
            case SORTING_ALGORITHM::HEAP_SORT:
                for (int i = 0; i < test_time; ++i) {
                    array_of_ints = generate_array_with_random_integers(
                            array_length);
                    array_of_ints_copy = array_of_ints;
                    sort(array_of_ints.begin(), array_of_ints.end());
                    HeapSort::sort(array_of_ints_copy);
                    if (array_of_ints != array_of_ints_copy) {
                        throw;
                    }
                }
                for (int i = 0; i < test_time; ++i) {
                    array_of_pairs = generate_array_with_random_pairs(
                            array_length);
                    array_of_pairs_copy = array_of_pairs;
                    sort(array_of_pairs.begin(), array_of_pairs.end(),
                         pair_comparator);
                    HeapSort::sort(array_of_pairs_copy,
                                   pair_three_way_comparator);
                    if (array_of_pairs != array_of_pairs_copy) {
                        throw;
                    }
                }
                break;
            case SORTING_ALGORITHM::MERGE_SORT:
                for (int i = 0; i < test_time; ++i) {
                    array_of_ints = generate_array_with_random_integers(
                            array_length);
                    array_of_ints_copy = array_of_ints;
                    sort(array_of_ints.begin(), array_of_ints.end());
                    MergeSort::sort(array_of_ints_copy);
                    if (array_of_ints != array_of_ints_copy) {
                        throw;
                    }
                }
                for (int i = 0; i < test_time; ++i) {
                    array_of_pairs = generate_array_with_random_pairs(
                            array_length);
                    array_of_pairs_copy = array_of_pairs;
                    sort(array_of_pairs.begin(), array_of_pairs.end(),
                         pair_comparator);
                    MergeSort::sort(array_of_pairs_copy,
                                    pair_three_way_comparator);
                    if (array_of_pairs != array_of_pairs_copy) {
                        throw;
                    }
                }
                break;
            case SORTING_ALGORITHM::BUBBLE_SORT:
                for (int i = 0; i < test_time; ++i) {
                    array_of_ints = generate_array_with_random_integers(
                            array_length);
                    array_of_ints_copy = array_of_ints;
                    sort(array_of_ints.begin(), array_of_ints.end());
                    BubbleSort::sort(array_of_ints_copy);
                    if (array_of_ints != array_of_ints_copy) {
                        throw;
                    }
                }
                for (int i = 0; i < test_time; ++i) {
                    array_of_pairs = generate_array_with_random_pairs(
                            array_length);
                    array_of_pairs_copy = array_of_pairs;
                    sort(array_of_pairs.begin(), array_of_pairs.end(),
                         pair_comparator);
                    BubbleSort::sort(array_of_pairs_copy,
                                    pair_three_way_comparator);
                    if (array_of_pairs != array_of_pairs_copy) {
                        throw;
                    }
                }
                break;
            default:
                break;
        }
    }
}

int main() {
    Tester::test(Tester::SORTING_ALGORITHM::INSERTION_SORT);
    Tester::test(Tester::SORTING_ALGORITHM::SELECTION_SORT);
    Tester::test(Tester::SORTING_ALGORITHM::HEAP_SORT);
    Tester::test(Tester::SORTING_ALGORITHM::MERGE_SORT);
    Tester::test(Tester::SORTING_ALGORITHM::BUBBLE_SORT);
    std::cout << "No news is good news" << std::endl;
    return 0;
}
