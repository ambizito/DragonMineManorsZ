#!/usr/bin/env bash
set -euo pipefail

root_dir="$(cd "$(dirname "$0")/.." && pwd)"
find "$root_dir/config/dragonminez" -type f -name '*.json' -print0 | xargs -0 -n1 jq empty
echo "JSON válido."
