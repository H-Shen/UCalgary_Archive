{-
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Assignment 4
    Notice before grading: All functions used but not defined here are built-in or imported functions.
-}

-- Question 3
{-
    Complete the implementation of the 'Expr' parser as presented in the lecture slides. Your code should be based on
    parser.hs, which has been posted at the course website.

    Important: You are not allowed to modify the code that is already given in parser.hs. Specifically, you need to
    provide the implementation for the following functions:

    *   isOp
    *   charToOp
    *   makeExpr
    *   optional
    *   neList
    *   stringToExpr
-}

{-
    (a) Purpose: check if the given character is a valid operator
    (b) Parameter: the character given
    (c) Preconditions/assumptions: -
    (d) Return value: return true if the character is a valid operator, otherwise return false
-}
isOp :: Char -> Bool
isOp ch = ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%'

{-
    (a) Purpose: return the operator that associates its relative character given
    (b) Parameter: the character given
    (c) Preconditions/assumptions: -
    (d) Return value: return the corresponding operator (an 'Ops' instance)
-}
charToOp :: Char -> Ops
charToOp '+' = Add
charToOp '-' = Sub
charToOp '*' = Mul
charToOp '/' = Div
charToOp '%' = Mod
charToOp _ = error "Invalid operator"

{-
    (a) Purpose: generate an 'Expr' instance by the pattern of the expression given
    (b) Parameter: the pattern of the expression
    (c) Preconditions/assumptions: -
    (d) Return value: return the new 'Expr' instance
-}
makeExpr :: (a, (Expr, (Char, (Expr, b)))) -> Expr
makeExpr (_, (e1, (bop, (e2, _)))) = Op (charToOp bop) e1 e2

{-
    (a) Purpose: the function recognizes a non-empty list of objects which are recognized by the argument given
    (b) Parameter: the argument 'p'
    (c) Preconditions/assumptions: -
    (d) Return value: a parser for a non-empty list of objects
-}
neList :: Parse a b -> Parse a [b]
neList p = alt (build p (: [])) (build (sqn p (list p)) (uncurry (:)))

{-
    (a) Purpose: the function recognizes a non-empty list of objects which are recognized by the argument given
    (b) Parameter: the argument 'p' optionally, that is, it may recognize an object or succeed immediately.
    (c) Preconditions/assumptions: -
    (d) Return value: a parser for a non-empty list of objects
-}
optional :: Parse a b -> Parse a [b]
optional p = alt (succeed []) (build p (: []))

{-
    (a) Purpose: a helper function that converts a string to an integer
    (b) Parameter: the string of integers in different patterns
    (c) Preconditions/assumptions: the string is valid and can be convert to an integer
    (d) Return value: an integer
-}
stoi :: String -> Int
stoi ('~' : xs) = - (stoi xs)
stoi [] = 0
stoi s = read s :: Int

{-
    (a) Purpose: the function is used to parsing literal expressions, so that

        stringToExpr "234" --> Lit 234
        stringToExpr "~98" --> Lit (-98)

    (b) Parameter: the string of expressions in different patterns
    (c) Preconditions/assumptions: -
    (d) Return value: an 'Expr' instance that indicates the string of expressions
-}
stringToExpr :: [Char] -> Expr
stringToExpr s = Lit (stoi s)