package cpsc331.assignment1;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

// Test Suite for the Method SGrindelwald.sGrin

public class test_sGrin {

  // Test of the behaviour of sGrin on input -2
  // Expected Results: An IllegalArgumentException is thrown
  
  @Test (expected = IllegalArgumentException.class)
  public void test_sGrin_neg2() {
    int solution = SGrindelwald.sGrin(-2);
  }

  // Test of the behaviour of sGrin on input -1
  // Expected Results: An IllegalArgumentException is thrown
  
  @Test (expected = IllegalArgumentException.class)
  public void test_sGrin_neg1() {
    int solution = SGrindelwald.sGrin(-1);
  }
  
  // Test of the behaviour of sGrin on input 0
  // Expected Results: The integer 1 is returned
  
  @Test
  public void test_sGrin_0 () {
    assertTrue(SGrindelwald.sGrin(0) == 1);
  }
  
  // Test of the behaviour of sGrin on input 1
  // Expected Results: The integer 2 is returned
  
  @Test
  public void test_sGrin_1 () {
    assertTrue(SGrindelwald.sGrin(1) == 2);
  }
  
  // Test of the behaviour of sGrin on input 2
  // Expected Results: The integer 3 is returned
  
  @Test
  public void test_sGrin_2 () {
    assertTrue(SGrindelwald.sGrin(2) == 3);
  }
  
  // Test of the behaviour of sGrin on input 3
  // Expected Results: The integer 4 is returned
  
  @Test
  public void test_sGrin_3 () {
    assertTrue(SGrindelwald.sGrin(3) == 4);
  }
  
  // Test of the behaviour of sGrin on input 4
  // Expected Results: The integer 5 is returned
  
  @Test
  public void test_sGrin_4 () {
    assertTrue(SGrindelwald.sGrin(4) == 5);
  }
  
  // Test of the behaviour of sGrin on input 5
  // Expected Results: The integer 6 is returned
  
  @Test
  public void test_sGrin_5 () {
    assertTrue(SGrindelwald.sGrin(5) == 6);
  }
  
  // Test of the behaviour of sGrin on input 6
  // Expected Results: The integer 7 is returned
  
  @Test
  public void test_sGrin_6 () {
    assertTrue(SGrindelwald.sGrin(6) == 7);
  }
  
  // Test of the behaviour of sGrin on input 7
  // Expected Results: The integer 8 is returned
  
  @Test
  public void test_sGrin_7 () {
    assertTrue(SGrindelwald.sGrin(7) == 8);
  }
  
  // Test of the behaviour of sGrin on input 8
  // Expected Results: The integer 9 is returned
  
  @Test
  public void test_sGrin_8 () {
    assertTrue(SGrindelwald.sGrin(8) == 9);
  }
  
  // Test of the behaviour of sGrin on input 9
  // Expected Results: The integer 10 is returned
  
  @Test
  public void test_sGrin_9 () {
    assertTrue(SGrindelwald.sGrin(9) == 10);
  }
  
  // Test of the behaviour of sGrin on input 10
  // Expected Results: The integer 11 is returned
  
  @Test
  public void test_sGrin_10 () {
    assertTrue(SGrindelwald.sGrin(10) == 11);
  }
  
  // Test of the behaviour of sGrin on input 11
  // Expected Results: The integer 12 is returned
  
  @Test
  public void test_sGrin_11 () {
    assertTrue(SGrindelwald.sGrin(11) == 12);
  }
  
}