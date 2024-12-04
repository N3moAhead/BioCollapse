OS := $(shell uname -s)

ifeq ($(OS), Windows_NT)
    RM = del /S /Q
    SEP = \\
else
    RM = rm -rf
    SEP = /
endif

default: run

clean:
	$(RM) src$(SEP)**.class

build: clean
	javac src$(SEP)com$(SEP)biocollapse$(SEP)main$(SEP)Main.java

run: build
	java src$(SEP)com$(SEP)biocollapse$(SEP)main$(SEP)Main
