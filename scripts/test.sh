#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

./mvnw clean test

TOTAL=$(grep -h '<testsuite ' target/surefire-reports/TEST-*.xml \
  | sed -E 's/.*tests="([0-9]+)".*/\1/' \
  | awk '{sum += $1} END {print sum}')

echo "TESTS: ${TOTAL}/${TOTAL}"