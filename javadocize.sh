#!/usr/bin/env bash

cd /home/mxz-schwarz/JSON-parser

git clone -b main --single-branch https://github.com/max-S-bot/JSON-parser

javadoc -d /home/mxz-schwarz/JSON-parser -sourcepath /home/mxz-schwarz/JSON-parser/JSON-parser -subpackages io.github.mxz_schwarz.parser

rm -rf JSON-parser