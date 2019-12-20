package cpsc331.assignment1;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

// Test Suite for the Method FGrindelwald.fGrin

public class test_fGrin {

  // Test of the behaviour of fGrin on input -2
  // Expected Results: An IllegalArgumentException is thrown
  
  @Test (expected = IllegalArgumentException.class)
  public void test_fGrin_neg2() {
    int solution = FGrindelwald.fGrin(-2);
  }

  // Test of the behaviour of fGrin on input -1
  // Expected Results: An IllegalArgumentException is thrown
  
  @Test (expected = IllegalArgumentException.class)
  public void test_fGrin_neg1() {
    int solution = FGrindelwald.fGrin(-1);
  }
  
  // Test of the behaviour of fGrin on input 0
  // Expected Results: The integer 1 is returned
  
  @Test
  public void test_fGrin_0 () {
    assertTrue(FGrindelwald.fGrin(0) == 1);
  }
  
  // Test of the behaviour of fGrin on input 1
  // Expected Results: The integer 2 is returned
  
  @Test
  public void test_fGrin_1 () {
    assertTrue(FGrindelwald.fGrin(1) == 2);
  }
  
  // Test of the behaviour of fGrin on input 2
  // Expected Results: The integer 3 is returned
  
  @Test
  public void test_fGrin_2 () {
    assertTrue(FGrindelwald.fGrin(2) == 3);
  }
  
  // Test of the behaviour of fGrin on input 3
  // Expected Results: The integer 4 is returned
  
  @Test
  public void test_fGrin_3 () {
    assertTrue(FGrindelwald.fGrin(3) == 4);
  }
  
  // Test of the behaviour of fGrin on input 4
  // Expected Results: The integer 5 is returned
  
  @Test
  public void test_fGrin_4 () {
    assertTrue(FGrindelwald.fGrin(4) == 5);
  }
  
  // Test of the behaviour of fGrin on input 5
  // Expected Results: The integer 6 is returned
  
  @Test
  public void test_fGrin_5 () {
    assertTrue(FGrindelwald.fGrin(5) == 6);
  }
  
  // Test of the behaviour of fGrin on input 6
  // Expected Results: The integer 7 is returned
  
  @Test
  public void test_fGrin_6 () {
    assertTrue(FGrindelwald.fGrin(6) == 7);
  }
  
  // Test of the behaviour of fGrin on input 7
  // Expected Results: The integer 8 is returned
  
  @Test
  public void test_fGrin_7 () {
    assertTrue(FGrindelwald.fGrin(7) == 8);
  }
  
  // Test of the behaviour of fGrin on input 8
  // Expected Results: The integer 9 is returned
  
  @Test
  public void test_fGrin_8 () {
    assertTrue(FGrindelwald.fGrin(8) == 9);
  }
  
  // Test of the behaviour of fGrin on input 9
  // Expected Results: The integer 10 is returned
  
  @Test
  public void test_fGrin_9 () {
    assertTrue(FGrindelwald.fGrin(9) == 10);
  }
  
  // Test of the behaviour of fGrin on input 10
  // Expected Results: The integer 11 is returned
  
  @Test
  public void test_fGrin_10 () {
    assertTrue(FGrindelwald.fGrin(10) == 11);
  }
  
  // Test of the behaviour of fGrin on input 11
  // Expected Results: The integer 12 is returned
  
  @Test
  public void test_fGrin_11 () {
    assertTrue(FGrindelwald.fGrin(11) == 12);
  }
  
}
