{-
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Assignment 2
    Notice before grading: All functions used but not defined here are built-in or imported functions.
-}

module A2 where

import Data.List (dropWhileEnd)

-- Question 1
{-
    We represent the single-variate polynomials as lists of integer coefficients in the ascending order. Specifically,
    the zero polynomial is  represented by the empty list and a non-zero polynomial is represented by a list of integers
    for which the last element is non-zero.

    You are required to develop the Haskell functions below with primitive and/or general recursions:

    (a) Return the sum of two polynomials:
    addPoly :: Poly -> Poly -> Poly

    (b) Return the multiplication of two polynomials:
    multPoly :: Poly -> Poly -> Poly
-}

{-
    Define the type aliases for the single-variate polynomial.
-}
type Poly = [Integer]

{-
    (a) Purpose: return the sum of two polynomials
    (b) Parameter: two polynomials in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the sum of two polynomials
-}
addpoly :: Poly -> Poly -> Poly
addpoly xs [] = xs
addpoly [] ys = ys
addpoly xs ys
  | length xs < length ys = addpoly xs init_ys ++ [last ys]
  | length xs > length ys = addpoly init_xs ys ++ [last xs]
  | otherwise = if sum_last == 0 then addpoly init_xs init_ys else addpoly init_xs init_ys ++ [sum_last]
  where
    init_xs = init xs
    init_ys = init ys
    sum_last = last xs + last ys

{-
    (a) Purpose: return the sum of two polynomials without trimming any items with zero coefficients
    (b) Parameter: two polynomials in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the sum of two polynomials whose length is the maximal length of two polynomials given
-}
addpoly' :: Poly -> Poly -> Poly
addpoly' xs [] = xs
addpoly' [] ys = ys
addpoly' (x : xs) (y : ys) = (x + y) : addpoly' xs ys

{-
    (a) Purpose: return the multiplication of two polynomials
    (b) Parameter: two polynomials in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the multiplication of two polynomials
-}
multpoly :: Poly -> Poly -> Poly
multpoly _ [] = []
multpoly [] _ = []
multpoly (x : xs) ys = dropWhileEnd (== 0) (addpoly' (map (\z -> z * x) ys) (0 : (multpoly xs ys)))

-- Question 2
{-
    You are required to develop the Haskell functions below:

    (a) Merge two sorted list of integers in ascending order into a list in ascending order as well:
    mergeLists :: [Integer] -> [Integer] -> [Integer]

    (b) Splits a given list of integers into two lists such that the first returned list contains those elements of the
    argument list at an even index, and the second list contains those at the odd index. Indices are zero based.
    splitList :: [Integer] -> ([Integer], [Integer])

    (c) Implement a function that performs merge sort such that it must call 'mergeLists' and 'splitList'
    mSort :: [Integer] -> [Integer]

-}

{-
    (a) Purpose: merge two lists into a new list in ascending order
    (b) Parameter: two list of integers in different patterns
    (c) Preconditions/assumptions: two lists are sorted in ascending order
    (d) Return value: the new merged list
-}
mergeLists :: [Integer] -> [Integer] -> [Integer]
mergeLists xs [] = xs
mergeLists [] ys = ys
mergeLists (x : xs) (y : ys)
  | x <= y = x : mergeLists xs (y : ys)
  | otherwise = y : mergeLists (x : xs) ys

{-
    (a) Purpose: obtain the elements in the list with even indices into a new list
    (b) Parameter: a list of integers in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the new list
-}
getEvenIndexItems :: [Integer] -> [Integer]
getEvenIndexItems [] = []
getEvenIndexItems (x : _ : xs) = x : getEvenIndexItems xs
getEvenIndexItems (x : xs) = x : getEvenIndexItems xs

{-
    (a) Purpose: obtain the elements in the list with odd indices into a new list
    (b) Parameter: a list of integers in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the new list
-}
getOddIndexItems :: [Integer] -> [Integer]
getOddIndexItems [] = []
getOddIndexItems (_ : x : xs) = x : getOddIndexItems xs
getOddIndexItems (_ : xs) = getOddIndexItems xs

{-
    (a) Purpose: splits a list of integers of into two lists such that the first returned list contains those elements
        of the argument list at an even index, and the second list contains those at the odd index
    (b) Parameter: a list of integers in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the new list
-}
splitList :: [Integer] -> ([Integer], [Integer])
splitList [] = ([], [])
splitList xs = (getEvenIndexItems xs, getOddIndexItems xs)

{-
    (a) Purpose: sort a list of integers in ascending order
    (b) Parameter: a list of integers in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the sorted list
-}
mSort :: [Integer] -> [Integer]
mSort [] = []
mSort [x] = [x]
mSort xs = mergeLists (mSort ys) (mSort zs)
  where
    (ys, zs) = splitList xs