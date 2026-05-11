MAIN_CLASS = $(error Need to set MAIN_CLASS to the name of a class)

all: build run

build:
	javac -Xlint:all -Xlint:-serial -Xlint:-this-escape *.java

run:
	java $(MAIN_CLASS)

clean:
	rm -f *.class
	rm -f *~
