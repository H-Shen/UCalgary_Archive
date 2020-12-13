{-
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Final Exam
    Notice before grading: All functions used but not defined here are built-in or imported functions.
-}

module Final where

-- Question 1
{-
    Define the binary relation over Integer
-}
type BinRel = [(Integer, Integer)]

{-
    (a) Purpose: Obtain the composition of two BinRels r1 and r2
    (b) Parameter: Two BinRels r1 and r2
    (c) Preconditions/assumptions: The solution shall NOT involve explicit recursion.
                                   Use list comprehension instead.
    (d) Return value: The composition of r1 and r2
-}
bin_rel_comp :: BinRel -> BinRel -> BinRel
bin_rel_comp r1 r2 = [(x, z) | (x, y1) <- r1, (y2, z) <- r2, y1 == y2]

-- Question 2
{-
    Define the bit vector
-}
type BitVec = [Bool]

{-
    (a) Purpose: Reverse the BitVec instance 'xs' from the least significant to the most significant
    (b) Parameter: The BitVec instance 'xs' in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: The reversed BitVec instance 'xs'
-}
myReverse :: BitVec -> BitVec
myReverse [] = []
myReverse (x : xs) = myReverse xs ++ [x]

{-
    (a) Purpose: Remove all left trailing 'False's in the BitVec instance given
    (b) Parameter: The BitVec instance 'xs' in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: The BitVec instance without left trailing 'False's
-}
removeLeftTrailingFalses :: BitVec -> BitVec
removeLeftTrailingFalses [] = []
removeLeftTrailingFalses (x : xs)
  | x = x : xs
  | otherwise = removeLeftTrailingFalses xs

{-
    (a) Purpose: Remove all right trailing 'False's in the BitVec instance given
    (b) Parameter: The BitVec instance 'xs' in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: The BitVec instance without right trailing 'False's
-}
removeRightTrailingFalses :: BitVec -> BitVec
removeRightTrailingFalses xs = myReverse (removeLeftTrailingFalses (myReverse xs))

{-
    (a) Purpose: Left-padding the BitVec instance with a number of 'False's
    (b) Parameter: The BitVec instance 'xs' and the number 'n' in different patterns
    (c) Preconditions/assumptions: 'n' is a non-negative integer
    (d) Return value: The BitVec instance without right trailing 'False's
-}
paddingLeftFalses :: BitVec -> Int -> BitVec
paddingLeftFalses xs 0 = xs
paddingLeftFalses xs n = False : paddingLeftFalses xs (n - 1)

{-
    (a) Purpose: Right-padding the BitVec instance with a number of 'False's
    (b) Parameter: The BitVec instance 'xs' and the number 'n' in different patterns
    (c) Preconditions/assumptions: 'n' is a non-negative integer
    (d) Return value: The BitVec instance without right trailing 'False's
-}
paddingRightFalses :: BitVec -> Int -> BitVec
paddingRightFalses xs n = myReverse (paddingLeftFalses (myReverse xs) n)

{-
    (a) Purpose: The helper function of 'addbv', which implements the main logic of the summation
    (b) Parameter: Two BitVec instances adder and the carry in different patterns
    (c) Preconditions/assumptions: Two BitVec instances have the same length
    (d) Return value: A BitVec instance
-}
addbvHelper :: BitVec -> BitVec -> Int -> BitVec
addbvHelper [] [] carry = if carry == 1 then [True] else [False]
addbvHelper (x : xs) (y : ys) carry = val : addbvHelper xs ys newCarry
  where
    valX = if x then 1 else 0
    valY = if y then 1 else 0
    temp = valX + valY + carry
    val = odd temp
    newCarry = temp `div` 2

{-
    (a) Purpose: Obtain the sum of two BitVec instances given as a BitVec instance
    (b) Parameter: Two BitVec instances in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: A BitVec instance
-}
addbv :: BitVec -> BitVec -> BitVec
addbv [] xs = removeRightTrailingFalses xs
addbv xs [] = removeRightTrailingFalses xs
addbv xs ys
  | length_xs == length_ys = removeRightTrailingFalses (addbvHelper xs ys 0)
  | otherwise = removeRightTrailingFalses (addbvHelper xs' ys' 0)
  where
    length_xs = length xs
    length_ys = length ys
    max_length = max length_xs length_ys
    xs' = paddingRightFalses xs (max_length - length_xs)
    ys' = paddingRightFalses ys (max_length - length_ys)

-- Question 4

{-
    Define the type aliases and the data type for the simple expression language.
-}
type VarName = Char
data Expr = Lit Integer
          | Var VarName
          | Add Expr Expr
type Binding = (VarName, Expr)
type Substitution = [Binding]

{-
    An example of an 'Expr' instance
-}
ex :: Expr
ex = Add (Add (Var 'x') (Lit 3)) (Var 'y')

{-
    An example of a'Substitution' instance
-}
sub :: Substitution
sub = [('x', Lit 7), ('y', Var 'z'), ('z', Lit 0)]

{-
    The function converts the 'Expr' instance into the 'String' instance
-}
showExpr :: Expr -> String
showExpr (Lit n) = "(Lit " ++ (show n) ++ ")"
showExpr (Var name) = "(Var " ++ (show name) ++ ")"
showExpr (Add e1 e2) = "(Add " ++ (showExpr e1) ++ " " ++ (showExpr e2) ++ ")"

{-
    The function converts the 'Substitution' instance into the 'String' instance
-}
showSub :: Substitution -> String
showSub [] = "[]"
showSub ((ch, e):ss) =
    "[(" ++ (show ch) ++ ", " ++ (showExpr e) ++ ")" ++
    (concat [", (" ++ (show ch2) ++ ", " ++ (showExpr e2) ++ ")" |
             (ch2, e2) <- ss]) ++ "]"

{-
    (a) Purpose: The function takes a substitution 's' and an expression 'e' as arguments, and returns an expression
                 obtained by applying 's' to 'e'. The following is the type signature of the function.
    (b) Parameter: A substitution 's' and an expression 'e' in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: the expression 'e' after applying
-}
substitute :: Substitution -> Expr -> Expr
substitute _ (Lit n) = Lit n
substitute s (Var v)
    | not (null temp) = head temp
    | otherwise = Var v
    where temp = [j | (i, j) <- s, i == v]
substitute s (Add e1 e2) = Add (substitute s e1) (substitute s e2)
 
-- Question 6
{-
    Use map, filter, and/or foldr/foldr1 to implement the following Haskell functions. You shall demonstrate the use of
    anonymous functions (i.e., lambda abstractions) in the implementation of at least one of following functions.
    Failing to demonstrate the use of anonymous functions will lead to a 3% mark deduction.
-}

{-
    (a) Purpose: Returns the sum of the squares of the even members of the input list
    (b) Parameter: The input list 'xs'
    (c) Preconditions/assumptions: -
    (d) Return value: the sum of the squares of the even members of the input list
-}
sum_sq_even :: [Integer] -> Integer
sum_sq_even xs = foldr ((+) . (\x -> x * x)) 0 (filter (\y -> y `mod` 2 == 0) xs)

{-
    (a) Purpose: Returns a list almost identical to the input list, except that the last element of the input list is
                 now the first element of the returned list.
    (b) Parameter: The input list 'xs'
    (c) Preconditions/assumptions: -
    (d) Return value: the sum of the squares of the even members of the input list
-}
bubble_up :: [Integer] -> [Integer]
bubble_up xs = if l < 1 then xs else foldr1 (\_ x -> x) xs : (foldr (\y ys -> [] : map (y :) ys) [[]] xs) !! (l - 1)
  where
    l = foldr (\_ z -> z + 1) 0 xs

{-
    (a) Purpose: The function takes a non-empty list of integer functions [f_1,f_2,...,f(k)] as input and returns
                 a function obtained by composing the boosts of the input functions f_1↑◦f_2↑◦···◦f_k↑
    (b) Parameter: the list 'xs' of predicates and an entity 'x'
    (c) Preconditions/assumptions: -
    (d) Return value: a boolean value
-}
boost_all :: [Integer -> Integer] -> (Integer -> Integer)
boost_all xs = foldr (.) (\x -> x) (map (\f -> (\n -> if f n > n then f n else n)) xs)

-- Question 7
{-
    (a) Purpose: Convert an integer to its corresponding BitVec instance, where the leftmost bit is the most
                 significant bit.
    (b) Parameter: The BitVec instance given by different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: a BitVec instance that maps to the Integer instance given
-}
dec2Bin :: Integer -> BitVec
dec2Bin 0 = [False]
dec2Bin 1 = [True]
dec2Bin n
  | even n = dec2Bin (n `div` 2) ++ [False]
  | otherwise = dec2Bin (n `div` 2) ++ [True]

{-
    (a) Purpose: A helper function that generate a tuple base on the tuple given according to some rules, such a tuple
                 can be converted into a BitVec instance.
    (b) Parameter: the tuple given
    (c) Preconditions/assumptions: -
    (d) Return value: the next tuple generated
-}
nextItem :: (Integer, Integer, Integer) -> (Integer, Integer, Integer)
nextItem (val, bit, maxValue)
  | val == maxValue = (0, newBit, 2 ^ newBit - 1)
  | otherwise = (val + 1, bit, maxValue)
  where
    newBit = bit + 1

{-
    (a) Purpose: A helper function that maps a positive integer to a tuple, such a tuple
                 can be converted into a BitVec instance.
    (b) Parameter: an integer
    (c) Preconditions/assumptions: the integer must be positive
    (d) Return value: the mapping tuple
-}
from :: Integer -> (Integer, Integer, Integer)
from 1 = (0, 1, 1)
from n = nextItem (from (n - 1))

{-
    (a) Purpose: A helper function that maps a positive integer to a tuple, such a tuple
                 can be converted into a BitVec instance.
    (b) Parameter: an integer
    (c) Preconditions/assumptions: the integer must be positive
    (d) Return value: the mapping tuple
-}
tuple2BitVec :: (Integer, Integer, Integer) -> BitVec
tuple2BitVec (val, bit, _) = result
  where
    temp = dec2Bin val
    length_temp = toInteger (length temp)
    result = if length_temp < bit then leftPadding ++ temp else temp
    leftPadding = map (const False) [1 .. (bit - length_temp)]

{-
    (a) Purpose: Return 'True' if the BitVec instance given is elegant, otherwise return 'False'
    (b) Parameter: The BitVec instance 'bv'
    (c) Preconditions/assumptions: -
    (d) Return value: 'True' or 'False'
-}
isElegent :: BitVec -> Bool
isElegent bv = (bv == reverse bv) && odd (length bv)

{-
    'elegant' is an infinite list that contains all bit vectors that are elegant. That is, every elegant bit vector
    appears at least once in elegant, while none of the inelegant bit vectors appears in elegant.
-}
elegant :: [BitVec]
elegant = filter isElegent [tuple2BitVec (from i) | i <- [1 ..]]
