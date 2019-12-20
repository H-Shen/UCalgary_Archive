package cpsc331.assignment2;

import java.util.NoSuchElementException;
import cpsc331.collections.ElementFoundException;
import cpsc331.assignment2.TwoThreeTree;
import cpsc331.assignment2.TwoThreeUtilities;

public class TwoThreeTest1 {

  public static void main(String[] args) {

    System.out.println("");
    System.out.print("Initializing a new 2-3 Tree ");
    System.out.println("storing integers.");
    TwoThreeTree<Integer> T = new TwoThreeTree<Integer>();
    TwoThreeUtilities<Integer> Test = new TwoThreeUtilities<Integer>();
    Test.testTree(T);

    Test.testSearch(T, 10, false);

    Test.testInsert(T, 5, true);
    Test.testInsert(T, 5, false);
    Test.testInsert(T, 10, true);
    Test.testInsert(T, 15, true);
    Test.testInsert(T, 20, true);
    Test.testInsert(T, 25, true);
    Test.testInsert(T, 30, true);
    Test.testInsert(T, 35, true);
    Test.testInsert(T, 40, true);
    Test.testInsert(T, 45, true);
    Test.testInsert(T, 50, true);

    Test.testInsert(T, 48, true);
    Test.testInsert(T, 43, true);
    Test.testInsert(T, 38, true);
    Test.testInsert(T, 33, true);
    Test.testInsert(T, 28, true);
    Test.testInsert(T, 23, true);
    Test.testInsert(T, 18, true);
    Test.testInsert(T, 13, true);
    Test.testInsert(T, 8, true);
    Test.testInsert(T, 3, true);
    Test.testInsert(T, 18, false);

    Test.testInsert(T, 1, true);
    Test.testInsert(T, 6, true);
    Test.testInsert(T, 11, true);
    Test.testInsert(T, 16, true);
    Test.testInsert(T, 21, true);
    Test.testInsert(T, 26, true);
    Test.testInsert(T, 31, true);
    Test.testInsert(T, 36, true);
    Test.testInsert(T, 41, true);
    Test.testInsert(T, 46, true);

    Test.testInsert(T, 49, true);
    Test.testInsert(T, 44, true);
    Test.testInsert(T, 39, true);
    Test.testInsert(T, 34, true);
    Test.testInsert(T, 29, true);
    Test.testInsert(T, 24, true);
    Test.testInsert(T, 19, true);
    Test.testInsert(T, 14, true);
    Test.testInsert(T, 9, true);
    Test.testInsert(T, 4, true);

    Test.testSearch(T, 0, false);

    Test.testSearch(T, 1, true);
    Test.testSearch(T, 2, false);
    Test.testSearch(T, 3, true);
    Test.testSearch(T, 4, true);
    Test.testSearch(T, 5, true);
    Test.testSearch(T, 6, true);
    Test.testSearch(T, 7, false);
    Test.testSearch(T, 8, true);
    Test.testSearch(T, 9, true);
    Test.testSearch(T, 10, true);

    Test.testSearch(T, 11, true);
    Test.testSearch(T, 12, false);
    Test.testSearch(T, 13, true);
    Test.testSearch(T, 14, true);
    Test.testSearch(T, 15, true);
    Test.testSearch(T, 16, true);
    Test.testSearch(T, 17, false);
    Test.testSearch(T, 18, true);
    Test.testSearch(T, 19, true);
    Test.testSearch(T, 20, true);

    Test.testSearch(T, 21, true);
    Test.testSearch(T, 22, false);
    Test.testSearch(T, 23, true);
    Test.testSearch(T, 24, true);
    Test.testSearch(T, 25, true);
    Test.testSearch(T, 26, true);
    Test.testSearch(T, 27, false);
    Test.testSearch(T, 28, true);
    Test.testSearch(T, 29, true);
    Test.testSearch(T, 30, true);

    Test.testSearch(T, 31, true);
    Test.testSearch(T, 32, false);
    Test.testSearch(T, 33, true);
    Test.testSearch(T, 34, true);
    Test.testSearch(T, 35, true);
    Test.testSearch(T, 36, true);
    Test.testSearch(T, 37, false);
    Test.testSearch(T, 38, true);
    Test.testSearch(T, 39, true);
    Test.testSearch(T, 40, true);

    Test.testSearch(T, 41, true);
    Test.testSearch(T, 42, false);
    Test.testSearch(T, 43, true);
    Test.testSearch(T, 44, true);
    Test.testSearch(T, 45, true);
    Test.testSearch(T, 46, true);
    Test.testSearch(T, 47, false);
    Test.testSearch(T, 48, true);
    Test.testSearch(T, 49, true);
    Test.testSearch(T, 50, true);

    Test.testSearch(T, 51, false);

    Test.testSearch(T, 1, true);
    Test.testSearch(T, 2, false);
    Test.testSearch(T, 49, true);
    Test.testSearch(T, 50, true);
    Test.testSearch(T, 51, false);

  }

}