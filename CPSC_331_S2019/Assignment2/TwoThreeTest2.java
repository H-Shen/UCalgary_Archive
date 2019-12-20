package cpsc331.assignment2;

import java.util.NoSuchElementException;
import cpsc331.collections.ElementFoundException;
import cpsc331.assignment2.TwoThreeTree;
import cpsc331.assignment2.TwoThreeUtilities;

public class TwoThreeTest2 {

  public static void main(String[] args) {

    System.out.println("");
    System.out.print("Initializing a new 2-3 Tree ");
    System.out.println("storing integers.");
    TwoThreeTree<Integer> T = new TwoThreeTree<Integer>();
    TwoThreeUtilities<Integer> Test = new TwoThreeUtilities<Integer>();
    Test.testTree(T);

    Test.testInsert(T, 1, true);
    Test.testInsert(T, 2, true);
    Test.testInsert(T, 3, true);
    Test.testInsert(T, 4, true);
    Test.testInsert(T, 5, true);
    Test.testInsert(T, 6, true);
    Test.testInsert(T, 7, true);
    Test.testInsert(T, 8, true);
    Test.testInsert(T, 9, true);
    Test.testInsert(T, 10, true);
    Test.testInsert(T, 21, true);
    Test.testInsert(T, 22, true);
    Test.testInsert(T, 23, true);
    Test.testInsert(T, 24, true);
    Test.testInsert(T, 25, true);
    Test.testInsert(T, 26, true);
    Test.testInsert(T, 27, true);
    Test.testInsert(T, 28, true);
    Test.testInsert(T, 29, true);
    Test.testInsert(T, 30, true);
    Test.testInsert(T, 10, false);

    Test.testSearch(T, -1, false);
    Test.testSearch(T, 1, true);
    Test.testSearch(T, 2, true);
    Test.testSearch(T, 3, true);
    Test.testSearch(T, 4, true);
    Test.testSearch(T, 5, true);
    Test.testSearch(T, 6, true);
    Test.testSearch(T, 7, true);
    Test.testSearch(T, 8, true);
    Test.testSearch(T, 9, true);
    Test.testSearch(T, 10, true);
    Test.testSearch(T, 11, false);
    Test.testSearch(T, 15, false);
    Test.testSearch(T, 20, false);
    Test.testSearch(T, 21, true);
    Test.testSearch(T, 22, true);
    Test.testSearch(T, 23, true);
    Test.testSearch(T, 24, true);
    Test.testSearch(T, 25, true);
    Test.testSearch(T, 26, true);
    Test.testSearch(T, 27, true);
    Test.testSearch(T, 28, true);
    Test.testSearch(T, 29, true);
    Test.testSearch(T, 30, true);
    Test.testSearch(T, 31, false);

    Test.testDelete(T, -1, false);
    Test.testDelete(T, 5, true);
    Test.testInsert(T, 5, true);
    Test.testDelete(T, 1, true);
    Test.testDelete(T, 2, true);
    Test.testDelete(T, 3, true);
    Test.testDelete(T, 4, true);
    Test.testDelete(T, 5, true);
    Test.testDelete(T, 30, true);
    Test.testDelete(T, 22, true);
    Test.testDelete(T, 6, true);
    Test.testDelete(T, 7, true);
    Test.testDelete(T, 8, true);
    Test.testDelete(T, 9, true);
    Test.testDelete(T, 10, true);
    Test.testDelete(T, 21, true);
    Test.testDelete(T, 23, true);
    Test.testDelete(T, 24, true);
    Test.testDelete(T, 25, true);
    Test.testDelete(T, 26, true);
    Test.testDelete(T, 27, true);
    Test.testDelete(T, 28, true);
    Test.testDelete(T, 29, true);

    Test.testInsert(T, 5, true);

  }

}