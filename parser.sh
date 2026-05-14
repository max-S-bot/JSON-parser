#!/bin/bash

git clone https://github.com/max-S-bot/JSON-parser

cd JSON-parser

bash ./to-jar.sh

mv ./parser.jar ../

cd ../

rm -rf JSON-parser