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
	find src -name "*.class" -exec $(RM) {} +

build: clean
	javac src$(SEP)com$(SEP)biocollapse$(SEP)main$(SEP)Main.java

compile:
	$(JAVAC) -d $(BIN) $(SRC)/com/biocollapse/main/Main.java

run: build
	java src$(SEP)com$(SEP)biocollapse$(SEP)main$(SEP)Main

jar: compile
	echo "Manifest-Version: 1.0" > MANIFEST.MF
	echo "Main-Class: $(MAIN_CLASS)" >> MANIFEST.MF
	$(JAR) cfm $(JAR_FILE) MANIFEST.MF -C $(BIN) .
	rm -f MANIFEST.MF
