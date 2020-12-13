{-
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Assignment 3
    Notice before grading: All functions used but not defined here are built-in or imported functions.
-}

module A3 where

-- Question 1
{-
    A well-formed formula (wff) of Propositional Logic is one of the following:

    *   a propositional variable (with a name of type String)
    *   the negation of a wff (not)
    *   the conjunction of two wffs (and)
    *   the disjunction of two wffs (or)

    Except for the above there are no other wffs.
-}

{-
    Declare a Haskell algebraic type Formula to represent wffs.
-}
data Formula = Variable String | Not Formula | And Formula Formula | Or Formula Formula

{-
    Purpose: represent the formula ¬(¬p ^ ¬q)
-}
expr :: Formula
expr = Not (And (Not (Variable "p")) (Not (Variable "q")))

{-
    (a) Purpose: return the string representation of the Formula instance given. Notice that '~' indicates the symbol
        of negation here instead of '¬' since it is not in the range of ASCII.
    (b) Parameter: the Formula instance given in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: a String instance represents the Formula instance
-}
showFormula :: Formula -> String
showFormula (Variable s) = s
showFormula (Not p) = "~" ++ "(" ++ (showFormula p) ++ ")"
showFormula (And l r) = "(" ++ (showFormula l) ++ ")" ++ "^" ++ "(" ++ (showFormula r) ++ ")"
showFormula (Or l r) = "(" ++ (showFormula l) ++ ")" ++ "v" ++ "(" ++ (showFormula r) ++ ")"

{-
    (a) Purpose: return a wff f' such that f' is logically equivalent to the Formula instance given and f' is in NNF.
    (b) Parameter: the Formula instance given in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the Formula instance f'
-}
rewrite :: Formula -> Formula
rewrite (Variable s) = Variable s
rewrite (Not (Not p)) = rewrite p
rewrite (And p q) = And (rewrite p) (rewrite q)
rewrite (Or p q) = Or (rewrite p) (rewrite q)
rewrite (Not (And p q)) = Or (rewrite (Not p)) (rewrite (Not q))
rewrite (Not (Or p q)) = And (rewrite (Not p)) (rewrite (Not q))
rewrite (Not p) = Not (rewrite p)

-- Question 3
{-
    Use map/filter and/or foldr/foldr1 to implement the following 3 Haskell functions.
    And the use of anonymous functions should be demonstrated in the implementation of at least 1 of 3 functions.
-}

{-
    (a) Purpose: return the last element of the list given, an exception will be thrown if the list is empty.
    (b) Parameter: the list 'xs'
    (c) Preconditions/assumptions: -
    (d) Return value: the last element in 'xs'
-}
lastElm :: [a] -> a
lastElm xs = foldr1 (\_ -> \x -> x) xs

{-
    (a) Purpose: the function takes a list of predicates and an entity as arguments, then applies every predicate to the
        entity, finally returns 'True' if and only if every predicate in the list returns 'True'. Return 'True' if the
        list is empty.
    (b) Parameter: the list 'xs' of predicates and an entity 'x'
    (c) Preconditions/assumptions: -
    (d) Return value: a boolean value
-}
unanimous :: [a -> Bool] -> a -> Bool
unanimous xs x = foldr (&&) True (map (\f -> f x) xs)

{-
    (a) Purpose: the function takes a predicate, a transformer and a list, then filters all the elements in the list
        each satisfies the predicate and then do the conversion by the transformer.
    (b) Parameter: the predicate 'p', transformer 'f' and the list 'xs'
    (c) Preconditions/assumptions: -
    (d) Return value: the list after the transformation
-}
selectiveMap :: (a -> Bool) -> (a -> b) -> [a] -> [b]
selectiveMap p f xs = map f (filter p xs)

-- Question 4
{-
    (a) Define a function 'iter' so that

        iter n f x = f(f(f...(f x)...))

        where 'f' occurs 'n' times on the right-hand side of the equation. And 'iter 0 f x' should return x.

    (b) Using 'iter' in (a) and 'double' defined in the textbook to define a function which on input 'n' returns 2^n,
        which indicates one multiplied by two 'n' times.
-}

{-
    (a) Purpose: return the result where 'f' occurs 'n' times on 'x', an error message will be shown if n < 0.
    (b) Parameter: the integer 'n', function 'f' and 'x'
    (c) Preconditions/assumptions: -
    (d) Return value: the result where 'f' applies 'n' times on 'x'
-}
iter :: Integer -> (a -> a) -> a -> a
iter n f x
  | n < 0 = error "The time of iterations should be a non-negative integer!"
  | n == 0 = x
  | otherwise = iter (n - 1) f (f x)

{-
    (a) Purpose: return the twice of the argument given. The definition comes from P217 of the textbook.
    (b) Parameter: the argument 'x'
    (c) Preconditions/assumptions: -
    (d) Return value: the twice of 'x'
-}
double :: Num a => a -> a
double x = 2 * x

{-
    (a) Purpose: return the power of 2 with the exponent given, an error message will be shown if the exponent < 0.
    (b) Parameter: the exponent 'n'
    (c) Preconditions/assumptions: -
    (d) Return value: the power of 2 with the exponent 'n'
-}
powerOfTwo :: Integer -> Integer
powerOfTwo n
  | n < 0 = error "The exponent should be a non-negative integer!"
  | otherwise = iter n double 1
