{-
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Assignment 1
    Notice before grading: All functions used but not defined here are built-in functions.
-}

module A1 where

-- Question 1
{-
    Implement a function 'myLog' that computes $\log_b{x}$

    (a) Purpose: calculate the log of 'x' with base 'b'
    (b) Parameter: an integer 'b' as the base and an integer 'x' whose logarithm is to be computed
    (c) Preconditions/assumptions: 'b' and 'x' should be positive and 'b' is at least 2
    (d) Return value: the biggest non-negative integer 'y' such that b^y is not more than 'x'
-}
myLog :: Integer -> Integer -> Integer
myLog b x
  | b > x = 0
  | otherwise = 1 + myLog b x'
  where
    x' = x `div` b

-- Question 2
{-
    Discuss how you would implement the database functions had you used the representation
    [(Person, [Book])] rather than [(Person, Book)] for the database.

    Contrary to the statement of the exercise in the textbook (which asks only for a “discussion”),
    you are required to provide the Haskell implementation of all the database functions, namely:

    books :: Database -> Person -> [Book]
    borrowers :: Database -> Book -> [Person]
    borrowed :: Database -> Book -> Bool
    numBorrowed :: Database -> Person -> Int
    makeLoan :: Database -> Person -> Book -> Database
    returnLoan :: Database -> Person -> Book -> Database

    These additional requirements should be satisfied:
    (1) If a person has not checked out any book, her name shall not appear in the database at all.
    (2) A person may check out a book at most once, his/her requests to have the same book to check out will be ignored.
-}

{-
    Define type aliases for the question.
-}
type Person = String

type Book = String

type Database = [(Person, [Book])]

{-
    (a) Purpose: find the books that the person given borrowed, if any
    (b) Parameter: the database 'db' and the person 'per'
    (c) Preconditions/assumptions: -
    (d) Return value: a list of books that the person borrowed
-}
books :: Database -> Person -> [Book]
books db per = concat [bList | (p, bList) <- db, p == per]

{-
    (a) Purpose: find the borrower(s) of the book given, if any
    (b) Parameter: the database 'db' and the book 'bk'
    (c) Preconditions/assumptions: -
    (d) Return value: a list of persons who borrowed the book
-}
borrowers :: Database -> Book -> [Person]
borrowers db bk = [p | (p, bList) <- db, elem bk bList]

{-
    (a) Purpose: check if a book given is borrowed
    (b) Parameter: the database 'db' and the book 'bk'
    (c) Preconditions/assumptions: -
    (d) Return value: 'True' if 'bk' is borrowed, otherwise 'False'
-}
borrowed :: Database -> Book -> Bool
borrowed db bk = not (null (borrowers db bk))

{-
    (a) Purpose: find the number of books that the person given borrowed
    (b) Parameter: the database 'db' and the person 'per'
    (c) Preconditions/assumptions: -
    (d) Return value: the number of books that 'per' borrowed
-}
numBorrowed :: Database -> Person -> Int
numBorrowed db per = length (books db per)

{-
    (a) Purpose: add the book to the head item of the database
    (b) Parameter: the database, the book 'bk'
    (c) Preconditions/assumptions: the database is not empty
    (d) Return value: the updated database
-}
addBookToHead :: Database -> Book -> Database
addBookToHead ((x, y) : xs) bk = (x, (bk : y)) : xs
addBookToHead [] _ = error "The precondition is violated."

{-
    (a) Purpose: update the database if a person makes a loan, ignore if the person checks out the same book
    (b) Parameter: the database 'db', the person 'per', the book 'bk'
    (c) Preconditions/assumptions: -
    (d) Return value: the updated database
-}
makeLoan :: Database -> Person -> Book -> Database
makeLoan db per bk
  | per `elem` (borrowers db bk) = db
  | numBorrowed db per == 0 = (per, [bk]) : db
  | otherwise = (addBookToHead l bk) ++ l'
  where
    l = [(p, bkList) | (p, bkList) <- db, p == per]
    l' = [(p, bkList) | (p, bkList) <- db, p /= per]

{-
    (a) Purpose: remove all occurrences of a book in a book list
    (b) Parameter: the book 'bk', the book list 'bkList'
    (c) Preconditions/assumptions: -
    (d) Return value: the updated book list
-}
remove :: Book -> [Book] -> [Book]
remove bk bkList = [i | i <- bkList, i /= bk]

{-
    (a) Purpose: remove all occurrences of a book in the database no matter who borrows it,
        the person has not checked out any book after the update will also be removed.
    (b) Parameter: the book 'bk', the book list 'bkList'
    (c) Preconditions/assumptions: -
    (d) Return value: the updated book list
-}
removeBook :: Database -> Book -> Database
removeBook db bk = [(p', bkList') | (p', bkList') <- [(p, remove bk bkList) | (p, bkList) <- db], not (null bkList')]

{-
    (a) Purpose: update the database if a person returns a loan, remove the person if he/she
        has not checked out any book.
    (b) Parameter: the database 'db', the person 'per', the book 'bk'
    (c) Preconditions/assumptions: -
    (d) Return value: the updated database
-}
returnLoan :: Database -> Person -> Book -> Database
returnLoan db per bk
  | per `notElem` (borrowers db bk) = db
  | numBorrowed db per == 0 = db
  | otherwise = (removeBook l bk) ++ l' ++ l''
  where
    l = [(p, bkList) | (p, bkList) <- db, p == per, bk `elem` bkList]
    l' = [(p, bkList) | (p, bkList) <- db, p == per, bk `notElem` bkList]
    l'' = [(p, bkList) | (p, bkList) <- db, p /= per]

-- Question 3
{-
    Define the function 'scale' that scales the input picture by the integer given, return an empty picture if the
    integer is zero or negative.
-}
{-
    Define the type alias for this question.
-}
type Picture = [[Char]]

{-
    (a) Purpose: scales the input picture by the integer given, return an empty picture if the integer not positive
    (b) Parameter: the picture 'p', an integer 'n' as the scale factor
    (c) Preconditions/assumptions: -
    (d) Return value: the picture after scaling
-}
scale :: Picture -> Int -> Picture
scale p n = concat [replicate n s' | s' <- [concat [replicate n ch | ch <- s] | s <- p]]

-- Question 4
{-
    A social graph is represented as a list of un-directed edges using pairs. Inside the pair one is a friend of each
    other. If edges (x, y) and (y, z) exists then 'x' and 'z' are also friends. Develop a Haskell function that returns
    the list of all common friends of two given persons using list comprehension instead of recursion.
-}

{-
    Define the type alias for this question.
-}
type Graph = [(Int, Int)]

{-
    (a) Purpose: take the first 'n' elements from the list
    (b) Parameter: the integer 'n', the list 'l'
    (c) Preconditions/assumptions: -
    (d) Return value: the new list
-}
myTake :: Int -> [Int] -> [Int]
myTake n l = [element | (index, element) <- zip [0 ..] l, index < n]

{-
    (a) Purpose: remove all duplicates of elements in a list and remain its first occurrence only
    (b) Parameter: the list 'l'
    (c) Preconditions/assumptions: -
    (d) Return value: the new list after removing duplicated elements
-}
removeDuplicates :: [Int] -> [Int]
removeDuplicates l = [element | (index, element) <- zip [0 ..] l, element `notElem` (myTake index l)]

{-
    (a) Purpose: obtain friends from a graph of a person given, no matter if the friend is identical to the person
    (b) Parameter: the social graph 'g', the person 'x'
    (c) Preconditions/assumptions: -
    (d) Return value: the list of friends of 'x'
-}
obtainFriends :: Graph -> Int -> [Int]
obtainFriends g x = [i | (i, _) <- l] ++ [j | (_, j) <- l]
  where
    l = [(u, v) | (u, v) <- g, v == x || u == x]

{-
    (a) Purpose: generate the common elements of the two lists without duplicates
    (b) Parameter: two lists 'a' and 'b'
    (c) Preconditions/assumptions: -
    (d) Return value: the new list
-}
intersectWithoutDuplicates :: [Int] -> [Int] -> [Int]
intersectWithoutDuplicates a b = removeDuplicates [x | x <- a, x `elem` b]

{-
    (a) Purpose: generate the list of all common friends shared by two persons in a social graph
    (b) Parameter: the social graph 'g', two persons 'x' and 'y'
    (c) Preconditions/assumptions: -
    (d) Return value: the list of common friends of 'x' and 'y'
-}
commonFriends :: Graph -> Int -> Int -> [Int]
commonFriends g x y = [i | i <- intersectWithoutDuplicates (obtainFriends g x) (obtainFriends g y), i /= x, i /= y]