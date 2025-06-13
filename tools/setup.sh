#!/bin/bash

RED='\033[0;1;31m'
NC='\033[0m' # No Color

GIT_DIR=$(git rev-parse --git-dir 2> /dev/null)
GIT_ROOT=$(git rev-parse --show-toplevel 2> /dev/null)

echo "Installing git commit-message hook"
echo
curl -sSLo "${GIT_DIR}/hooks/commit-msg" \
    "https://gerrit-review.googlesource.com/tools/hooks/commit-msg" \
  && chmod +x "${GIT_DIR}/hooks/commit-msg"

echo "Installing git pre-push hook"
echo
mkdir -p "${GIT_DIR}/hooks/"
cp "${GIT_ROOT}/tools/pre-push" "${GIT_DIR}/hooks/pre-push" \
  && chmod +x "${GIT_DIR}/hooks/pre-push"

cat <<-EOF
Checking the following settings helps avoid miscellaneous issues:
  * Settings -> Editor -> General -> Remove trailing spaces on: Modified lines
  * Settings -> Editor -> General -> Ensure every saved file ends with a line break
  * Settings -> Editor -> General -> Auto Import -> Optimize imports on the fly (for both Kotlin\
 and Java)
EOF
