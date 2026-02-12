#!/bin/bash

# Define paths
DEFAULT_FILE="${PROJECT_DIR}/../config/app.properties"
PROD_FILE="${PROJECT_DIR}/../config/app.properties.prod"
OUTPUT_FILE="${PROJECT_DIR}/Generated.xcconfig"

INPUT_FILE=""
if [ -f "$PROD_FILE" ]; then
    INPUT_FILE="$PROD_FILE"
    echo "✅ Found Production config. Using: $INPUT_FILE"
else
    INPUT_FILE="$DEFAULT_FILE"
    echo "ℹ️ Production config not found. Using default: $INPUT_FILE"
fi

echo "// This file is auto-generated from $(basename "$INPUT_FILE"). Do not modify." > "$OUTPUT_FILE"

if [ -f "$INPUT_FILE" ]; then
    grep -v '^#' "$INPUT_FILE" | \
    sed 's/[[:space:]]*=[[:space:]]*/=/g' | \
    sed -e ':a' -e 's/^\([^=]*\)\./\1_/' -e 'ta' >> "$OUTPUT_FILE"
else
    echo "Warning: $INPUT_FILE not found. Skipping config generation."
fi
