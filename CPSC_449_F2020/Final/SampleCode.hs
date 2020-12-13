-- Sample Code for Question 3
crunch :: [Integer] -> [Integer] -> Integer
crunch [] []     = 0
crunch (x:xs) [] = crunch xs [x]
crunch [] (y:ys) = y + crunch [] ys
crunch (x:xs) (y:ys)
  | even x       = crunch xs (x:y:ys)
  | otherwise    = crunch ((x+y):xs) ys


-- Sample Code for Question 4

type VarName = Char

data Expr = Lit Integer
          | Var VarName
          | Add Expr Expr

type Binding = (VarName, Expr)
type Substitution = [Binding]

ex :: Expr
ex = Add (Add (Var 'x') (Lit 3)) (Var 'y')

sub :: Substitution
sub = [('x', Lit 7), ('y', Var 'z'), ('z', Lit 0)]

showExpr :: Expr -> String
showExpr (Lit n) = "(Lit " ++ (show n) ++ ")"
showExpr (Var name) = "(Var " ++ (show name) ++ ")"
showExpr (Add e1 e2) = "(Add " ++ (showExpr e1) ++ " " ++ (showExpr e2) ++ ")"

showSub :: Substitution -> String
showSub [] = "[]"
showSub ((ch, e):ss) =
    "[(" ++ (show ch) ++ ", " ++ (showExpr e) ++ ")" ++
    (concat [", (" ++ (show ch2) ++ ", " ++ (showExpr e2) ++ ")" |
             (ch2, e2) <- ss]) ++
    "]"


