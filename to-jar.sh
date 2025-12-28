#!/usr/bin/env bash

main_dir="/home/mxz-schwarz/JSON-parser"

cd "$main_dir"

rm -f parser.jar
mkdir  bin
javac -d bin io/github/mxz_schwarz/parser/*.java
cd bin
jar cvf "$main_dir/parser.jar" io/github/mxz_schwarz/parser/*.class
cd ..
rm -rf bin