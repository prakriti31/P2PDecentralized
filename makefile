# Variables
SRC_DIR = src/main/java
TEST_DIR = src/test/java
BUILD_DIR = target
JAVAC = javac
JAVA = java
TEST_RUNNER = org.junit.runner.JUnitCore
CLASSPATH = $(BUILD_DIR):$(BUILD_DIR)/lib/*

# Compile all Java files
all: compile test

compile:
	@echo "Compiling Java files..."
	$(JAVAC) -d $(BUILD_DIR) $(SRC_DIR)/**/*.java $(TEST_DIR)/**/*.java

# Run all tests
test: compile
	@echo "Running tests..."
	$(JAVA) -cp $(CLASSPATH) $(TEST_RUNNER) $(TEST_DIR)/**/*.class

# Clean the build directory
clean:
	@echo "Cleaning up..."
	rm -rf $(BUILD_DIR)/*.class $(BUILD_DIR)/lib/*

.PHONY: all compile test clean
