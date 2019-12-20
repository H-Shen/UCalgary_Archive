#!/bin/bash

set -ex

if [[ "$OSTYPE" == "linux-gnu" ]]; then
  g++ -std=c++17 -Wall -Ofast -lGL -lGLU -lglut -lGLEW assignment1_legacy_openGL.cpp -o assignment1_legacy_openGL.out -lstdc++fs
  ./assignment1_legacy_openGL.out
fi
