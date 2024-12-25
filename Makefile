OS := $(shell uname -s)

ifeq ($(OS), Windows_NT)
    RM = del /S /Q
    SEP = \\
else
    RM = rm -rf
    SEP = /
endif

# Variablen
SRC_DIR = src
BIN_DIR = bin
JAR_FILE = app.jar
MAIN_CLASS = src.com.biocollapse.main.Main
MANIFEST = MANIFEST.MF

# Default-Ziel
default: run

# Säubert alte Dateien
clean:
	$(RM) $(BIN_DIR) $(JAR_FILE)

# Kompiliert den Code
compile: clean
	mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) $(shell find $(SRC_DIR) -name "*.java")

# Erstellt die JAR-Datei
jar: compile
	echo "Manifest-Version: 1.0" > $(MANIFEST)
	echo "Main-Class: $(MAIN_CLASS)" >> $(MANIFEST)
	jar cfm $(JAR_FILE) $(MANIFEST) -C $(BIN_DIR) . -C $(SRC_DIR) com/biocollapse/ressources

# Führt die JAR-Datei aus
run: jar
	java -jar $(JAR_FILE)