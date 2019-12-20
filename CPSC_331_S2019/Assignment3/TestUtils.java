package cpsc331.assignment3;

import cpsc331.assignment3.Array;

public class TestUtils<T extends Comparable<T>> {

  public Array<T> SelectionSort(Array<T> A) {

    Array<T> B = new Array<T>(A.length());

    int i = 0;

    // Copy the contents of A into B

    while (i < A.length()) {

      B.set(i, A.get(i));
      i = i + 1;

    };

    i = 0;

    while (i < B.length() - 1) {

      int minIndex = i;
      T minValue = B.get(i);

      int j = i + 1;
      while (j < B.length()) {
        if (B.get(j).compareTo(minValue) < 0) {
          minIndex = j;
          minValue = B.get(j);
        };
        j = j + 1;
      }
      T tmp = B.get(i);
      B.set(i, B.get(minIndex));
      B.set(minIndex, tmp);

      i = i + 1;

    };

    return B;

  }

  public boolean isEqual (Array<T> A, Array<T> B) {

    if (A.length() == B.length()) {

      int i = 0;
      while (i < A.length()) {
        if (A.get(i).compareTo(B.get(i)) != 0) {
          return false;
        };
        i = i + 1;
      };
      return true;

    } else {

      return false;

    }

  }

}
