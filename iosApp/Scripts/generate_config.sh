#!/bin/bash

# Define paths
INPUT_FILE="${PROJECT_DIR}/../config/app.properties"
OUTPUT_FILE="${PROJECT_DIR}/Generated.xcconfig"

echo "Generating xcconfig from $INPUT_FILE..."

# 1. Add a header to the xcconfig file
echo "// This file is auto-generated. Do not modify." > "$OUTPUT_FILE"

# 2. Parse the properties file
# - grep -v '^#': Ignore comments
# - sed 's/[[:space:]]*=[[:space:]]*/=/g': Remove spaces around =
# - sed 's/\./_/g': Replace dots with underscores (optional, but safer for Xcode vars)
if [ -f "$INPUT_FILE" ]; then
    grep -v '^#' "$INPUT_FILE" | \
    sed 's/[[:space:]]*=[[:space:]]*/=/g' | \
    sed -e ':a' -e 's/^\([^=]*\)\./\1_/' -e 'ta' >> "$OUTPUT_FILE"
else
    echo "Warning: $INPUT_FILE not found. Skipping config generation."
fi
