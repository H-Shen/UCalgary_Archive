package cpsc331.assignment2;

import java.util.NoSuchElementException;
import cpsc331.collections.ElementFoundException;
import java.util.ArrayList;
import cpsc331.assignment2.TwoThreeTree;

/**
*
* Provides utilities that can be used to test a TwoThreeTree
*
*/

public class TwoThreeUtilities<E extends Comparable<E>> {

  /**
  *
  * Reports whether an input tree is a 2-3 tree.
  *
  * @param T the tree to be tested
  * @param verbose indicates whether output is to be displayed
  *        when errors are detected
  * @return true if the input tree is a 2-3 tree, and false
  *        otherwise
  *
  */

  public boolean isTwoThreeTree(TwoThreeTree<E> T, boolean verbose) {

    return (hasTwoThreeShape(T, verbose)
             && consistentHeight(T, verbose)
             && elementsOrdered(T, verbose));

  }

  /**
  *
  * Checks whether an input tree is a 2-3 tree, providing a
  * command-line description of the testing being performed
  * and additional documentation if an error is found.
  *
  * @param T the tree to be tested
  *
  */

  public void testTree(TwoThreeTree<E> T) {

    if (isTwoThreeTree(T, false)) {
      System.out.println("Result is a valid 2-3 tree.");
    } else {
      System.out.println("Result is not a valid 2-3 tree.");
      System.out.println("");
      boolean result = isTwoThreeTree(T, true);
    };
    System.out.println("");

  }

  /**
  *
  * Checks whether an input tree has the shape of a 2-3 tree.
  *
  * @param T the tree to be tested
  * @param verbose indicates whether output is to be displayed
  *        when errors are detected
  * @return true if this tree has the shape of a 2-3 tree, and
  *        false otherwise
  *
  */

  public boolean hasTwoThreeShape(TwoThreeTree<E> T, boolean verbose) {

    if (T.root() != null) {

      TwoThreeTree<E>.TwoThreeNode x = T.root();
      boolean result = true;

      if ((x.numberChildren() == 0)
            || (x.numberChildren() == 2)
            || (x.numberChildren() == 3)) {

        if (x.numberChildren == 0) {

          if (x.element() == null) {
            if (verbose) {
              System.out.print("Node with zero children ");
              System.out.println("stores a null element.");
            };
            result = false;
          };

          if (x.firstChild() != null) {
            if (verbose) {
              System.out.println("Leaf has a non-null first child.");
            };
            result = false;
          };

          if (x.firstMax() != null) {
            if (verbose) {
              System.out.println("Leaf has a non-null first maximum.");
            };
            result = false;
          };

          if (x.secondChild() != null) {
            if (verbose) {
              System.out.println("Leaf has a non-null second child.");
            };
            result = false;
          };

          if (x.secondMax() != null) {
            if (verbose) {
              System.out.println("Leaf has a non-null second maximum.");
            };
            result = false;
          };

          if (x.thirdChild() != null) {
            if (verbose) {
              System.out.println("Leaf has a non-null third child.");
            };
            result = false;
          };

          if (x.thirdMax() != null) {
            if (verbose) {
              System.out.println("Leaf has a non-null third maximum.");
            };
            result = false;
          }

        } else if (x.numberChildren == 2) {

          if (x.element() != null) {
            if (verbose) {
              System.out.print("Node with two children ");
              System.out.println(" stores a non-null element.");
              result = false;
            };
          };

          if (x.firstChild() == null) {
            if (verbose) {
              System.out.print("Node with two children ");
              System.out.println(" has a null first child.");
            };
            result = false;
          } else {
            if (!x.equals(x.firstChild().parent())) {
              if (verbose) {
                System.out.print("Inconsistent firstChild and ");
                System.out.println("parent references.");
              };
              result = false;
            }
          };

          if (x.firstMax() == null) {
            if (verbose) {
              System.out.print("Node with two children ");
              System.out.println("has a null first maximum.");
            };
            result = false;
          };

          if (x.secondChild() == null) {
            if (verbose) {
              System.out.print("Node with two children ");
              System.out.println(" has a null second child.");
            };
            result = false;
          } else {
           if (!x.equals(x.secondChild().parent())) {
             if (verbose) {
               System.out.print("Inconsistent secondChild and ");
               System.out.println("parent references.");
             };
             result = false;
            }
          };

          if (x.secondMax() == null) {
            if (verbose) {
              System.out.print("Node with two children ");
              System.out.println("has a null second maximum.");
            };
            result = false;
          };

          if (x.thirdChild() != null) {
            if (verbose) {
              System.out.print("Node with two children ");
              System.out.println(" has a null third child.");
            };
            result = false;
          };

          if (x.thirdMax() != null) {
            if (verbose) {
              System.out.print("Node with two children ");
              System.out.println("has a null third maximum.");
            };
            result = false;
          }

        } else {

          if (x.element() != null) {
            if (verbose) {
              System.out.print("Node with three children ");
              System.out.println("store a non-null element.");
            };
            result = false;
          };

          if (x.firstChild() == null) {
            if (verbose) {
              System.out.print("Node with three children ");
              System.out.println(" has a null first child.");
            };
            result = false;
          } else {
            if (!x.equals(x.firstChild().parent())) {
              if (verbose) {
                System.out.print("Inconsistent firstChild and ");
                System.out.println("parent references.");
              };
              result = false;
            }
          };

          if (x.firstMax() == null) {
            if (verbose) {
              System.out.print("Node with three children ");
              System.out.println(" has a null first maximum.");
            };
            result = false;
          };

          if (x.secondChild() == null) {
            if (verbose) {
              System.out.print("Node with three children ");
              System.out.println(" has a null second child.");
            };
            result = false;
          } else {
            if (!x.equals(x.secondChild().parent())) {
              if (verbose) {
                System.out.print("Inconsistent secondChile and ");
                System.out.println("parent references.");
              };
              result = false;
            }
          };

          if (x.secondMax() == null) {
            if (verbose) {
              System.out.print("Node with three children ");
              System.out.println("has a null second maximum.");
            };
            result = false;
          };

          if (x.thirdChild() == null) {
            if (verbose) {
              System.out.print("Node with three children ");
              System.out.println(" has a null third child.");
            };
            result = false;
          } else {
            if (!x.equals(x.thirdChild().parent())) {
              if (verbose) {
                System.out.print("Inconsistent thirdChild and ");
                System.out.println("parent references.");
              };
              result = false;
            }
          };

          if (x.thirdMax() == null) {
            if (verbose) {
              System.out.print("Node with three children ");
              System.out.println("has a null third maximum.");
            };
            result = false;
          }

        };

        if (x.fourthChild() != null) {
          if (verbose) {
            System.out.println("Node has a non-null fourth child.");
          };
          result = false;
        };

        if (x.fourthMax() != null) {
          if (verbose) {
           System.out.println("Node has a non-null fourth maximum.");
          };
          result = false;
        };

        return result;

      } else {

        if (verbose) {
          System.out.println("Node has an incorrect number of children.");
        };

        return false;

      }

    } else {

      return true;

    }

  }

  /**
  *
  * Checks whether the leaves of a tree T, that has the same shape as
  * a 2-3 tree, all have the same distance from the root.
  *
  * @param T the tree to be tested
  * @param verbose indicates whether output is to be displayed
  *        when errors are detected
  * @return true if all leaves in this tree have the same distance
  *        from the root, and false otherwise
  *
  */

  public boolean consistentHeight(TwoThreeTree<E> T, boolean verbose) {

    if (T.root() != null) {

      try {
        int h = height(T.root(), verbose);
        return true;
      } catch (IllegalArgumentException ex) {
        return false;
      }

    } else {

      return true;

    }

  }

  // Returns the height of a node in a tree with the shape of a 2-3 tree -
  // throwning an IllegalArgumentException if it is discovered that the leaves
  // in the subtree with root x do not all have the same distance from x

  private int height(TwoThreeTree<E>.TwoThreeNode x, boolean verbose)
                                             throws IllegalArgumentException {

    if (x.numberChildren() == 0){

      return 0;

    } else if (x.numberChildren() == 2) {

      int h1 = height(x.firstChild(), verbose);
      int h2 = height(x.secondChild(), verbose);
      if (h1 == h2) {
        return h1 + 1;
      } else {
        if (verbose) {
          System.out.println("Depth of subtrees disagree.");
          System.out.print("Depth of first subtree: ");
          System.out.println(h1);
          System.out.print("Depth of second subtree: ");
          System.out.print(h2);
        };
        throw new IllegalArgumentException("Leaves at different levels.");
      }

    } else {

      int h1 = height(x.firstChild(), verbose);
      int h2 = height(x.secondChild(), verbose);
      int h3 = height(x.thirdChild(), verbose);
      if ((h1 == h2) && (h2 == h3)) {
        return h1 + 1;
      } else {
        if (verbose) {
          System.out.println("Depth of subtrees disagree.");
          System.out.print("Depth of first subtree: ");
          System.out.println(h1);
          System.out.print("Depth of second subtree: ");
          System.out.println(h2);
          System.out.print("Depth of third subtree: ");
          System.out.println(h3);
        };
        throw new IllegalArgumentException("Leaves at different levels.");
      }

    }

  }

  /**
  *
  * Checks whether elements of E are stored at the leaves of this tree
  * in the required order, assuming it has been confirmed that this tree
  * has the shape of a 2-3 tree.
  *
  * @param T the tree to be tested
  * @param verbose indicates whether output is to be displayed
  *        when errors are detected
  * @return true if leaves store elements of E in increasing order, and
  *        false otherwise
  *
  */

  public boolean elementsOrdered(TwoThreeTree<E> T, boolean verbose) {

    if (T.root() != null) {

      ArrayList<E> entries = new ArrayList<E>();
      addSubtree(T.root(), entries);
      int i = 0;
      boolean result = true;
      while ((i < entries.size() - 1) && result) {
        if ((entries.get(i)).compareTo(entries.get(i+1)) < 0) {
          i = i + 1;
        } else {
        if (verbose) {
          System.out.print((entries.get(i)).toString() + " is listed ");
          System.out.println("before " + (entries.get(i+1)).toString() + ".");
        };
          result = false;
        }
      }
      return result;

    } else {

      return true;

    }

  }

  // Adds the elements of the subtree with a given node as root to the
  // ArrayList being maintained

  private void addSubtree(TwoThreeTree<E>.TwoThreeNode x,
                                        ArrayList<E> entries ) {

    if (x.numberChildren() == 0) {
      entries.add(x.element());
    } else if (x.numberChildren() == 2) {
      addSubtree(x.firstChild(), entries);
      addSubtree(x.secondChild(), entries);
    } else {
      addSubtree(x.firstChild(), entries);
      addSubtree(x.secondChild(), entries);
      addSubtree(x.thirdChild(), entries);
    }

  }

  /**
  *
  * Tests a search for an element.
  *
  * @param T the 2-3 tree in which the search takes place
  * @param x the element being searched for
  * @param success true if the element should be found, and
  *          false otherwise
  *
  */

  public void testSearch(TwoThreeTree<E> T,
                          E x,
                          boolean success) {

    try {

      System.out.print("Searching for \"");
      System.out.println(x.toString() + "\"");
      TwoThreeTree<E>.TwoThreeNode y = T.search(x);
      if (success) {
        if (y.numberChildren == 0) {
          E e = y.element();
          if (e.equals(x)) {
            System.out.print("Search was successful, ");
            System.out.println("as expected.");
          } else {
            System.out.print("Leaf storing \"");
            System.out.print(e.toString());
            System.out.println("\" was incorrectly returned.");
          };
        } else {
          System.out.print("An internal node was ");
          System.out.println("incorrectly returned.");
        };
      } else {
        System.out.print("A \"NoSuchElementException\" was ");
        System.out.println("expected but not thrown.");
        if (y.numberChildren == 0) {
          E e = y.element();
          System.out.print("Leaf storing \"");
          System.out.print(e.toString());
          System.out.println("\" was incorrectly returned.");
        } else {
          System.out.print("An internal node was ");
          System.out.println("incorrectly returned.");
        };
      };
      testTree(T);

    } catch (NoSuchElementException ex) {

      if (success) {
        System.out.print("A \"NoSuchElementException\"");
        System.out.println(" was incorrectly thrown.");
      } else {
        System.out.print("A \"NoSuchElementException\"");
        System.out.println(" was correctly thrown.");
      };
      testTree(T);

    }

  }

  /**
  *
  * Tests the insertion of an element.
  *
  * @param T the 2-3 tree in which the insertion is performed
  * @param x the element to be inserted
  * @param success true if the search successful, and false if
  *      an ElementFoundException should be thrown instead
  *
  */

  public void testInsert(TwoThreeTree<E> T,
                         E x,
                         boolean success) {

    try {

      System.out.print("Trying to insert \"");
      System.out.println(x.toString() + "\"");
      T.insert(x);
      if (success) {
        System.out.println("Operation succeeded, as expected.");
      } else {
        System.out.print("An \"ElementFoundException\" was ");
        System.out.println("expected but not thrown.");
      };
      testTree(T);

    } catch (ElementFoundException ex) {

      if (success) {
        System.out.print("An \"ElementFoundException\" was ");
        System.out.println("expected but not thrown.");
      } else {
        System.out.print("An \"ElementFoundException\" was ");
        System.out.println("correctly thrown.");
      };
      testTree(T);

    };

  }

  /**
  *
  * Tests the deletion of an element.
  *
  * @param T the 2-3 tree for which the deletion should be performed
  * @param x the element to be deleted
  * @param success true if the deletion was successful, and false
  *     if a NoSuchElementException should be thrown instead
  *
  */

  public void testDelete(TwoThreeTree<E> T,
                         E x,
                         boolean success) {

    try {

      System.out.print("Trying to delete \"");
      System.out.println(x.toString() + "\"");
      T.delete(x);
      if (success) {
        if (contains(T, x)) {
          System.out.print("Operation failed: ");
          System.out.println("Element was not removed.");
        } else {
          System.out.println("Operation succeeded, as expected.");
        };
      } else {
        System.out.print("The expected \"NoSuchElementException\" ");
        System.out.println("has not been thrown.");
        if (contains(T, x)) {
          System.out.print("This element has evidently been inserted ");
          System.out.println("into the tree.");
        };
      };
      testTree(T);

    } catch (NoSuchElementException ex) {

      if (success) {
        System.out.print("A \"NoSuchElementException\" has ");
        System.out.println("incorrectly been thrown.");
        if (contains(T, x)) {
          System.out.print("The element is still stored ");
          System.out.println("in this tree.");
        } else {
          System.out.print("The element has, evidently, been ");
          System.out.println("removed from this tree.");
        };
      } else {
        System.out.print("A \"NoSuchElementException\" has ");
        System.out.println("been thrown, as expected.");
        if (contains(T, x)) {
          System.out.print("Operation failed: The element has, ");
          System.out.println("evidently, been inserted into this tree.");
        } else {
          System.out.println("Operation succeeded.");
        };
      };
      testTree(T);

    }

  }

  // Checks whether a given 2-3 tree stores a given key

  private boolean contains(TwoThreeTree<E> T, E x) {

    try {
      TwoThreeTree<E>.TwoThreeNode y = T.search(x);
      return true;
    } catch (NoSuchElementException ex) {
      return false;
    }

  }

}
