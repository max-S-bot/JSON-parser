#!/usr/bin/env bash

git clone -b main --single-branch https://github.com/max-S-bot/JSON-parser

javadoc -d ./ -sourcepath ./JSON-parser -subpackages io.github.mxz_schwarz.parser

rm -rf JSON-parser