{-
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Assignment 4
    Notice before grading: All functions used but not defined here are built-in or imported functions.
-}

module A4 where

-- Question 1
{-
    A polynomial in one variable is defined inductively as follows:

    *   a constant of type Int is a polynomial
    *   the variable is a polynomial
    *   if P and Q are polynomials, then P + Q is a polynomial
    *   if P and Q are polynomials, then P × Q is a polynomial

    Except for the above there is no other polynomial.

    A function 'f' of type Int -> Int is the denotation of a Poly P iff 'f' is a single-argument function that evaluates
    P at the argument. For example, (\x -> x + x) is the denotation of (PAdd PVar PVar). Develop a denotational compiler
    for Poly:

    compilePoly :: Poly -> (Int -> Int)

    such that (compilePoly P) returns the denotation of P.
-}

{-
    Define the algebraic type that satisfies the question's description
-}
data Poly = PConst Int | PVar | PAdd Poly Poly | PMul Poly Poly

{-
    (a) Purpose: return the denotation of a poly P given
    (b) Parameter: the poly P given in different patterns
    (c) Preconditions/assumptions: the function must be primitively recursive
    (d) Return value: the denotation of the poly P
-}
compilePoly :: Poly -> (Int -> Int)
compilePoly (PConst c) = \_ -> c
compilePoly (PVar) = \x -> x
compilePoly (PAdd lhs rhs) = \x -> ((compilePoly lhs) x) + ((compilePoly rhs) x)
compilePoly (PMul lhs rhs) = \x -> ((compilePoly lhs) x) * ((compilePoly rhs) x)

-- Question 4
{-
    Define the function

    runningSums :: [Int] -> [Int]

    which calculates the running sums

    [0, a0, a0 + a1, a0 + a1 + a2, ...]

    of a list

    [a0, a1, a2, ...]
-}

{-
    (a) Purpose: return the running sums of a list given
    (b) Parameter: the list 'xs'
    (c) Preconditions/assumptions: -
    (d) Return value: the running sums of 'xs'
-}
runningSums :: [Int] -> [Int]
runningSums xs = scanl (+) 0 xs