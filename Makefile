SRC_DIR = src
BUILD_DIR = build

MAIN_CLASS = app.Main

.PHONY: build run clean

all: build run

build:
	@echo "=== Compilando ==="
	mkdir -p $(BUILD_DIR)
	find $(SRC_DIR) -name "*.java" > sources.txt
	javac -d $(BUILD_DIR) @sources.txt
	rm sources.txt
	@echo "=== Feito ==="

run:
	@echo "=== Executando ==="
	java -cp $(BUILD_DIR) $(MAIN_CLASS)
	@echo "=== Feito ==="

clean:
	@echo "=== Limpando ==="
	rm -rf $(BUILD_DIR)
	@echo "=== Feito ==="
