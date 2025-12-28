#!/usr/bin/env bash

main_dir="/home/mxz-schwarz/JSON-parser"

cd "$main_dir"

git clone https://github.com/briandfoy/json-acceptance-tests

echo "import io.github.mxz_schwarz.parser.*;
import java.nio.file.Path;

public class Test {
    public static void main(String[] args) throws JSONException {
        for (String file : args) {
            try {
                Obj json = JSON.parse(Path.of(file));
                if (!(file.contains(\"pass\") || file.contains(\"fail1.json\") || file.contains(\"fail18.json\"))) {
                    IO.println(\"File: \"+file+\" should not have passed\");
                    return;
                }
                IO.println(\"File: \"+file+\" successfully parsed\");
                IO.println(json);
            } catch (JSONException je) {
                if (file.contains(\"pass\") || file.contains(\"fail1.json\") || file.contains(\"fail18.json\")) {
                    IO.println(\"File: \"+file);
                    throw je;
                }
            }
        }
    }
}" > Test.java

java -cp jar-stuff/parser.jar Test.java json-acceptance-tests/json-checker/*

rm  Test.java
rm -rf json-acceptance-tests