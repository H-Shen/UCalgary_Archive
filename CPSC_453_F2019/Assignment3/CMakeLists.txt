cmake_minimum_required(VERSION 3.14)
project(CPSC453_Assignment3)

IF (NOT "${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
    MESSAGE(STATUS "You are using : ")
    MESSAGE(${CMAKE_CXX_COMPILER_ID})
    MESSAGE(FATAL_ERROR "Please use GCC instead of other compilers!")
ENDIF ()

set(CMAKE_CXX_STANDARD 17)

add_executable(assignment3.out
        main.cpp
        )


target_link_libraries(assignment3.out
        stdc++fs)

set(CMAKE_CXX_FLAGS "-Wall -Ofast")