#!/bin/bash

set -e

# Directory containing the programs
PROGRAM_DIR=$(dirname "$0")

# Function to validate output
validate_output() {
  while IFS= read -r line; do
    if [[ "$line" =~ expected:\ ([0-9]+),\ actual:\ ([0-9]+) ]]; then
      expected="${BASH_REMATCH[1]}"
      actual="${BASH_REMATCH[2]}"
      if [[ "$expected" -ne "$actual" ]]; then
        echo "Validation failed: $line"
        exit 1
      fi
    fi
  done
}

# Run all Java programs directly
for java_file in "$PROGRAM_DIR"/*.java; do
  echo "Running $java_file..."
  
  # Capture and validate output
  output=$(java "$java_file")
  echo "$output"
  echo "$output" | validate_output
done

# Run all PHP programs
for php_file in "$PROGRAM_DIR"/*.php; do
  echo "Running $php_file..."
  
  # Capture and validate output
  output=$(php "$php_file")
  echo "$output"
  echo "$output" | validate_output
done

echo "All programs executed successfully and validated!"

