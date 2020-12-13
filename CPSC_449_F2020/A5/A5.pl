/*
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Assignment 5
*/

% Question 1
/*
    The Java language supports single inheritance with the class hierarchy, but multiple inheritance with its interface
    with its interface hierarchy.

    *   A class extends its immediate superclass. Every class has at most 1 immediate superclass.
    *   A class implements its immediate super-interfaces. Every class has zero or more immediate superinterfaces.
    *   An interface extends its immediate super-interfaces. Every interface has zero or more immediate superinterfaces.

    We represent a Java classes/interfaces and their inheritance hierarchy using 4 Prolog predicates.

    *   class(X) iff X is a class
    *   interface(X) iff X is an interface
    *   extends(X, Y) iff either 
            (i) class X extends class Y
            (ii) interface X extends interface Y
    *   implements(X, Y) iff class X implements interface Y

    It is assumed that we are given a database populated with facts specifying the relationships between some Java
    classes and interfaces.
*/

/*
    (a) Define a Prolog predicate 'subclass(?X, ?Y)', which succeeds iff X and Y are both classes, and X is related to
        Y via the transitive closure of the extends relation. For example, subclass(a, c) holds if the following facts
        are in the database:

        class(a)
        class(b)
        class(c)
        extends(a, b)
        extends(b, c)

        In the above example, 'a' is said to be a subclass of 'c', and 'c' is said to be a superclass of 'a'. A trivial
        case of subclassing is when one class extends another class.
*/

subclass(X, Y) :-
    class(X),
    extends(X, Y).
    
subclass(X, Y) :-
    class(X),
    extends(X, Z),
    subclass(Z, Y).

/*
    (b) Define a Prolog predicate 'superinterface(?Y, ?X)', which succeeds iff 
        
        *   Y is an interface
        *   X is a class
        *   either X or a superclass of X implements an interface Z, such that either Z is Y, or Z is related to Y via
            the transitive closure of extends. For example, superinterface(f, a) holds if the following facts are in the
            database.

            class(a)
            class(b)
            class(c)
            interface(d)
            interface(e)
            interface(f)
            extends(a, b)
            extends(b, c)
            implements(c, d)
            extends(d, e)
            extends(e, f)

        A trivial case in which superinterface(Y, X) holds is when X implements Y.
*/

/*
    A helper predicate 'subinterface(?X, ?Y)', which succeeds iff

    *   X and Y are interfaces.
    *   X is related to Y via the transitive closure of extends.
*/
subinterface(X, Y) :-
    interface(X),
    extends(X, Y).
subinterface(X, Y) :-
    interface(X),
    extends(X, Z),
    subinterface(Z, Y).

% Case 1: X implements an interface Z, Z is Y
superinterface(Y, X) :-
    class(X),
    implements(X, Z),
    Z = Y.

% Case 2: X implements an interface Z, Z is related to Y via the transitive closure of extends
superinterface(Y, X) :-
    class(X),
    implements(X, Z),
    subinterface(Z, Y).

% Case 3: a superclass of X implements an interface Z, Z is Y
superinterface(Y, X) :-
    class(X),
    subclass(X, W),
    implements(W, Z),
    Z = Y.

% Case 4: a superclass of X implements an interface Z, Z is related to Y via the transitive closure of extends
superinterface(Y, X) :-
    class(X),
    subclass(X, W),
    implements(W, Z),
    subinterface(Z, Y).

% Question 2
/*
    Given an undirected graph G = (V, E), a subset S \subset V of vertices is called a vertex cover of G if there
    does not exists an edge uv in E such that neither u nor v is in S. People are interested in finding out if a given
    graph G has a vertex cover of size k or less, for some pararmeter k.

    One can represent an undirected graph in Prolog as a list of pairs, each pair encoding an undirected edge. As an
    example, consider the following Prolog list:

        [(1,3), (2,3), (3,4), (4,5), (4,6), (5,6)]

    The list represents the graph below:

                       1       5
                        \     /|
                         3---4 |
                        /     \|
                       2       6

    A vertex cover can be represented as a list of vertices. For example, the following is a vertex cover of the graph
    above 
                        [3,5,6]

    In fact, the vertex cover above is one of the smallest vertex covers for the graph in question. The following is a
    not a vertex cover, because the edge (5,6) is not covered.

                        [3,4]

    Develop a Prolog predicate, vertex_cover(+Graph, ?Cover), which asserts that the list Cover is a vertex cover of
    undirected graph Graph. Your implementation shall satisfy the following requirements:

        *   If Cover is not fully instantiated, then the implementation shall generate all possible vertex covers that
            unify with Cover.
        *   It is okay to end up generating the same vertex cover multiple times. (Requiring uniqueness will significantly
            increase the difficulty of the question.)
        *   The implementation shall not perform a naive generate-and-test (e.g,generate every subset of the vertex set
            of Graph, and then test if the generated subset is a vertex cover). Early pruning of the search space shall
            be attempted if possible. A naive generate-and-test solution is only worth 66% of the marks.

    The predicate above can be used for checking if there exists a vertex cover for a given graph G where the cover is
    of size no bigger than some positive integer constant k. For example, the following query does exactly this:

    ?- length(Cover, k), vertex_cover(G, Cover).

    Alternatively, if k is small, say 3, one can also issue the following query to check if there is a vertex cover of
    size 3:

    ?- vertex-cover(G, [V1, V2, V3]).
*/


/*
    Obtain the length of the list.
*/
get_list_length([], 0).
get_list_length([_ | L], N) :- get_list_length(L, N1), N is N1 + 1.

/*
    Unpack all two-length tuples in the list.
*/
unpack_tuples_in_the_list([],M,R) :- R = M.
unpack_tuples_in_the_list([H | L],A, R) :-
    (Fst, Snd) = H,
    append([Fst, Snd], A, A2),
    unpack_tuples_in_the_list(L, A2, R).

/*
    Obtain the minimal value of 'X' and 'Y' and stored in 'Output'.
*/
get_min(X, Y, Output) :-
    (
        X < Y -> Output = X;
        Output = Y
    ).

/*
    If 'Cover' is instantiated, then obtain the minimal value between the size of 'Cover' and the total number of nodes
    in the graph. Otherwise, return the number of nodes in the graph.
*/
get_upperbound(Cover, N, Upperbound) :-
    (
        var(Cover) -> Upperbound = N;
        length(Cover, K),
        get_min(N, K, Upperbound)
    ).

vertex_cover(Graph, Cover) :-
    unpack_tuples_in_the_list(Graph, [], X),    % Unpack the edges of list into a list of nodes
    list_to_set(X, Y),                          % Convert the list to the set to remove duplicates
    get_list_length(Y, N),                      % Get the length of the set, that is, the number of nodes in the graph
    get_upperbound(Cover, N, Upperbound),
    (
        % Case 1: If 'Cover' is not fully instantiated, then the implementation shall generate all possible vertex
        % covers that unify with Cover.
        var(Cover) -> foreach(between(1, N, I), vertex_cover_of_size_k(N, Graph, I, Cover); true);
        % Case 2: Otherwise, check if there exists a vertex cover for a given graph G where the cover is of size no
        % bigger than some positive integer constant k.
        sort(Cover, CoverSort), vertex_cover_of_size_k(N, Graph, Upperbound, CoverSort)
    ).

/*
    Search if there any cover has the size of 'K'.
*/
vertex_cover_of_size_k(N, Graph, K, Cover) :-
    numlist(1, N, L), 
    combination(K, L, Cover),
    covers(Cover, Graph).

/*
    A recursive function checks if the Cover is in the graph.
*/
covers(_, []).
covers(Cover, [H | T]) :- 
    includes(Cover, H), 
    covers(Cover, T).

/*
    Check if the Graph includes the edge (X, Y).
*/
includes([H | T], (X, Y)) :-  
    (( H = X ; H = Y ) -> true; 
    includes(T, (X, Y)) 
    ).

/*
    Calculate the combination from 1 to N numbers recursively.
*/
combination(0,_,[]).
combination(N,[H | T],[H | T2]):-    
    N > 0,
    N1 is N - 1,
    combination(N1, T, T2).
combination(N,[_ | T], T2):-        
    N > 0,
    combination(N, T, T2).
