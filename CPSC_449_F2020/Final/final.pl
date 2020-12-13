/*
    Name:   Haohu Shen
    UCID:   30063099
    CPSC449 Final Exam
    Notice before grading: All functions used but not defined here are built-in or imported functions.
*/

/*
    A helper predicate that takes two lists and generates its Cartesian Product as 2-length tuples.
*/
product([U|_], L2, (U, V)) :- member(V, L2).
product([_|L1], L2, X) :- product(L1, L2, X).

/*
    A helper predicate that generates all possible edges base on the point set given, self loop is ignored.
*/
get_edges(S, (U, V)) :-
    product(S, S, (U, V)),
    \+ U = V.

/*
    A helper predicate that checks if any edge generated from the point set given exists in the graph given.
*/
include_any_edge(G, S, E) :- 
    get_edges(S, E), 
    member(E, G).

/*
    A helper predicate that unpacks all two-length tuples in the list.
*/
unpack_tuples_in_the_list([],M,R) :- R = M.
unpack_tuples_in_the_list([H | L],A, R) :-
    (Fst, Snd) = H,
    append([Fst, Snd], A, A2),
    unpack_tuples_in_the_list(L, A2, R).

/*
    A helper predicate that gets all subsets.
*/
get_all_subsets([], []).
get_all_subsets([_ | T], T2) :- get_all_subsets(T, T2).
get_all_subsets([H | T], [H | T2]) :- get_all_subsets(T, T2).

/*
    A helper predicate that gets all combinations of point set of the graph
*/
get_all_comb(G, V) :-
    collect_vertices(G, V2),
    get_all_subsets(V2, V).

/*
    Question (a):
    The predicate asserts that the list 'S' is a socially distant set for the undirected graph 'G'. Note that both 'G'
    and 'S' are assumed to be fully instantiated.
*/
check_soc_dist(G, S) :- \+ include_any_edge(G, S, _).

/*
    Question (b):
    The predicate receives a graph G and asserts that the list V is the list of all vertices in the graph.
    The predicate will generate only one possible value for Vs, but the order is not guaranteed.
*/
collect_vertices(G, V) :-
    unpack_tuples_in_the_list(G, [], X),    % Unpack the edges of list into a list of nodes
    list_to_set(X, V).                      % Convert the list to a set in order to remove duplicates

/*
    Question (c):
    The predicate asserts that the list S is a socially distant set for the undirected graph G. S is a variable when the
    predicate is called. The predicate will generate all possible socially distant sets in turn.
    From my understanding, [4,2,1] and [1,2,4] here should be consider as the same socially distant set since there is
    no order in the concept of set here.
*/
gen_soc_dist(G, S) :-
    get_all_comb(G, S),
    check_soc_dist(G, S).