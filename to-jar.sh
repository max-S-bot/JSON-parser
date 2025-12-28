#!/usr/bin/env bash

cd /home/mxz-schwarz/JSON-parser

rm -f parser.jar
mkdir  bin
javac -d bin io/github/mxz_schwarz/parser/*.java
cd bin
jar cvf /home/mxz-schwarz/JSON-parser/parser.jar io/github/mxz_schwarz/parser/*.class
cd ..
rm -rf bin