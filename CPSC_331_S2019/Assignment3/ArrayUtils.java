package cpsc331.assignment3;

import cpsc331.assignment3.Array;

import java.util.NoSuchElementException;

/**
 * A class for solving the 'Sorting in Place' problem.
 *
 * <p>
 * Sorting an array in nondecreasing order when array A with length >= 1,
 * storing elements from some ordered type T using heapsort.
 * </p>
 *
 * @author Haohu Shen (UCID: 30063099)
 * @date 2019/06/17
 */

public class ArrayUtils<T extends Comparable<T>> {

    // A boolean method reports if x is the root of the heap when an
    // array-based representation of the heap is being used.
    //
    // Citation:
    // Page 33 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private boolean isRoot(int x) {
        if (x == 0) {
            return true;
        } else {
            return false;
        }
    }

    // A method returns the parent of x whose value is floor((x-1)/2) or
    // throws a NoSuchElementException if x is the root of the heap when
    // an array-based representation of the heap is being used.
    //
    // Citation:
    // Page 33 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private int parent(int x) throws NoSuchElementException {
        if (x == 0) {
            throw new NoSuchElementException();
        } else {
            return (x - 1) / 2;
        }
    }

    // An algorithm solves the 'Maxheap restoration after insertion' computational
    // problem by bubbling x up toward the root of the heap when an array-based
    // representation of the heap is being used. The algorithm takes node x and A
    // as arguments and it is similar to the one provided on the lecture but
    // a while loop is used to replace the tail calls.
    //
    // Precondition:
    // a) A binary tree H with size > 0, whose nodes store values from some ordered
    //    type T is represented using the array A and H is accessed and modified
    //    as global data.
    // b) x is a node in H as input
    // c) For each node y in H except for x, if y is not the root of the heap, then
    //    the value stored at node y is less than or equal to the value stored at
    //    the parent of y and the value stored at node y is also less than or equal
    //    to the value stored at the grandparent of y if y has a grandparent.
    //
    // Postcondition:
    // a) The values at the nodes in H either have been exchanged between nodes or
    //    remain unchanged. The multiset S represented by H is unchanged.
    // b) H is a MaxHeap
    //
    // Citation:
    // Page 28-34 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private void bubbleUp(int x, Array<T> A) {

        // Loop Invariant:
        // 1. A is an array with length A.length >= 1, storing values of some
        //    ordered type T and is given as input.
        // 2. x is a node in the binary tree H, which represents as an index integer in A
        // 3. The multiset S represented by A is unchanged
        //
        // Bound Function for Loop: the level of x
        //
        // let T_{bubbleUp}(k) is the number of steps executed by the algorithm in the worst case such that
        // the input node x has level k >= 0, then the upperbound of running times
        // T_{bubbleUp}(k) = O(k)

        while (!isRoot(x)) {
            if (A.get(x).compareTo(A.get(parent(x))) > 0) {
                T temp = A.get(x);
                A.set(x, A.get(parent(x)));
                A.set(parent(x), temp);
                x = parent(x);
            } else {
                break;
            }
        }
    }

    // A boolean method reports if x has a non-null left child when
    // an array-based representation of the heap is being used.
    //
    // Citation:
    // Page 48 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private boolean hasLeft(int x, int heapSize) {
        if (2 * x + 1 < heapSize) {
            return true;
        } else {
            return false;
        }
    }

    // A method returns the left child of x whose value is 2*x+1 or
    // throws a NoSuchElementException if the left child of x is null
    // when an array-based representation of the heap is being used.
    //
    // Citation:
    // Page 48 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private int left(int x, int heapSize) throws NoSuchElementException {
        if (hasLeft(x, heapSize)) {
            return 2 * x + 1;
        } else {
            throw new NoSuchElementException();
        }
    }

    // A boolean method reports if x has a non-null right child when
    // an array-based representation of the heap is being used.
    //
    // Citation:
    // Page 49 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private boolean hasRight(int x, int heapSize) {
        if (2 * x + 2 < heapSize) {
            return true;
        } else {
            return false;
        }
    }

    // A method returns the right child of x whose value is 2*x+2 or
    // throws a NoSuchElementException if the right child of x is null
    // when an array-based representation of the heap is being used.
    //
    // Citation:
    // Page 49 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private int right(int x, int heapSize) throws NoSuchElementException {
        if (hasRight(x, heapSize)) {
            return 2 * x + 2;
        } else {
            throw new NoSuchElementException();
        }
    }

    // An algorithm solves the 'Maxheap restoration after deletion' computational
    // problem by bubbling x down toward the leaves of the heap when an array-based
    // representation of the heap is being used. The algorithm takes the node x, the
    // size of the heap represented using A and A itself as arguments, it is similar
    // to the one provided in the lecture but a while loop is used to replace the tail
    // calls.
    //
    // Precondition:
    // a) A binary tree H with size > 0, whose nodes store values from some ordered
    //    type T is represented using the array A and H is accessed and modified
    //    as global data.
    // b) x is a node in H as input
    // c) For each node y in H except for x, if z is a child of y, then the value
    //    stored at y is greater than or equal to the value stored at z and the
    //    values stored at any children of z (if z has any children).
    //
    // Postcondition:
    // a) The values at the nodes in H either have been exchanged between nodes or
    //    remain unchanged. The multiset S represented by H is unchanged.
    // b) H is a MaxHeap
    //
    // Citation:
    // P43-44, 50-51 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L17/L17_binary_heaps.pdf

    private void bubbleDown(int x, int heapSize, Array<T> A) {

        // Loop Invariant:
        // 1. A is an array with length A.length >= 1, storing values of some
        //    ordered type T and is given as input.
        // 2. x is a node in the binary tree H, which represents as an index integer in A
        // 3. heapSize is an integer represents the size of the heap such that 0 <= heapSize <= A.length
        // 4. The multiset S represented by A is unchanged
        //
        // Bound Function for Loop: the height of x
        //
        // Let T_{bubbleDown}(k) is the number of steps executed by the algorithm in the worst case such that
        // the input node x has height k >= 0, then the upperbound of running times
        // T_{bubbleDown}(k) = O(k)

        while (hasLeft(x, heapSize)) {
            if (hasRight(x, heapSize)) {
                if (A.get(left(x, heapSize)).compareTo(A.get(right(x, heapSize))) >= 0) {
                    if (A.get(left(x, heapSize)).compareTo(A.get(x)) > 0) {
                        T temp = A.get(left(x, heapSize));
                        A.set(left(x, heapSize), A.get(x));
                        A.set(x, temp);
                        x = left(x, heapSize);
                    } else {
                        break;
                    }
                } else if (A.get(right(x, heapSize)).compareTo(A.get(x)) > 0) {
                    T temp = A.get(right(x, heapSize));
                    A.set(right(x, heapSize), A.get(x));
                    A.set(x, temp);
                    x = right(x, heapSize);
                } else {
                    break;
                }
            } else {
                if (A.get(left(x, heapSize)).compareTo(A.get(x)) > 0) {
                    T temp = A.get(left(x, heapSize));
                    A.set(left(x, heapSize), A.get(x));
                    A.set(x, temp);
                    x = left(x, heapSize);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * An implementation of the algorithm using heapsort to solve the 'Sorting in Place' computation problem on an
     * array A with length >= 0, storing elements from some ordered type T.
     * <p>
     * Citation:
     * [1] Page 37 in http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L15/L15_basic.pdf
     * [2] Page 4, 22, 25 in
     * http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/6_Sorting/L18/L18_heap_applications.pdf
     *
     * @param A An array whose length >= 0, storing elements from some ordered type T.
     */

    // Precondition
    // a) An array A with its length A.length() >= 1, storing elements from some ordered type T
    //
    // Postcondition
    // If A_pre is the input array before the computational problem is solved and
    // A_post is the input array after the computational problem is solved, then
    // a) A_post is a reordering of A_pre
    // b) A_post.get(i) <= A_post.get(i+1) for all integers i such that 0 <= i <= A.length() - 2
    //
    // Suppose n is the size of A, then
    // a) Let T_{sort}(n) be the number of steps executed by the algorithm in the worst case,
    // then the upperbound of running times T_{sort}(n) = O(nlogn)
    //
    // b) Let S_{sort}(n) be the storage space required by the algorithm,
    // then the upperbound of storage space S_{sort}(n) = O(1)

    public void sort(Array<T> A) {

        try {

            int heapSize = 1;
            int i        = 1;

            // Loop Invariant for the First Loop:
            // 1. A is an array with length A.length >= 1, storing values of some
            //    ordered type T and is given as input.
            // 2. The entries of A have been reordered but are otherwise unchanged.
            // 3. i is an integer variable such that 1 <= i <= A.length
            // 4. A represents a Maxheap with size i
            //
            // Let n be the length of A, thus:
            //
            // Bound Function for Loop: f(n, i) = n - i
            //
            // Also let T_{firstLoop}(n) be the number of steps executed by the algorithm in the worst case,
            // then the upperbound of running times T_{firstLoop}(n) = O(nlogn)

            while (i < A.length()) {
                if (heapSize < A.length()) {
                    int x = heapSize;
                    A.set(x, A.get(i));
                    heapSize = heapSize + 1;
                    bubbleUp(x, A);
                } else {
                    throw new IllegalStateException();
                }

                i = i + 1;
            }
            // Assertion: A Maxheap representing a finite multiset S of values from an ordered type T is now
            //            represented by the array A.

            i = A.length() - 1;

            // Loop Invariant for the Second Loop:
            // 1. A is an array with length A.length >= 1, storing values of some
            //    ordered type T and is given as input.
            // 2. The entries of A have been reordered but are otherwise unchanged.
            // 3. i is an integer variable such that 0 <= i <= A.length - 1
            // 4. A represents a Maxheap with size i + 1
            // 5. If i <= A.length - 2 then A[h] <= A[h + 1] for every integer h such that 0 <= h <= i
            // 6. A[h] <= A[h + 1] for every integer h such that i + 1 <= h <= A.length - 2
            //
            // Bound Function for Loop: f(i) = i
            //
            // Also let T_{secondLoop}(n) be the number of steps executed by the algorithm in the worst case,
            // then the upperbound of running times T_{secondLoop}(n) = O(nlogn)

            while (i > 0) {

                T largest = null;
                if (heapSize == 0) {
                    throw new NoSuchElementException();
                } else {
                    T temp = A.get(heapSize - 1);
                    heapSize = heapSize - 1;
                    if (heapSize == 0) {
                        largest = temp;
                    } else {
                        largest = A.get(0);
                        A.set(0, temp);
                        bubbleDown(0, heapSize, A);
                    }
                }

                A.set(i, largest);
                i = i - 1;
            }
            // Assertion: A.get(i) <= A.get(i+1) for all integers i such that 0 <= i <= A.length() - 2
        } catch (Exception ex) {
            return;
        }
    }
}
