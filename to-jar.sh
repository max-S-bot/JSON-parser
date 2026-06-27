#!/usr/bin/env bash

mkdir bin
javac -d bin io/github/mxz_schwarz/parser/*.java
cd bin
jar cvf "../parser.jar" io/github/mxz_schwarz/parser/*.class
cd ..
rm -rf bin
