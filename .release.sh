#!/bin/bash

# Script to update Maven project version across all modules
# Usage: ./.release.sh <new-version>

set -e

# Check if version argument is provided
if [ $# -eq 0 ]; then
    echo "Error: No version specified"
    echo "Usage: $0 <new-version>"
    echo "Example: $0 v2.5.0"
    exit 1
fi

NEW_VERSION="v$1"

update_readme_version() {
    local version="$1"
    echo "Updating README.md version to: $version"
    sed -i.bak "s|<version>v[^<]*</version>|<version>$version</version>|g" README.md
    rm -f README.md.bak
}

echo "Updating Maven project version to: $NEW_VERSION"

# Update root pom.xml version
echo "Updating root pom.xml..."
mvn versions:set -DnewVersion="$NEW_VERSION" -DgenerateBackupPoms=false

# Update README.md version
echo "Updating readme.md..."
update_readme_version "$NEW_VERSION"

echo "Version update completed successfully!"
echo "New version: $NEW_VERSION"

mvn -q -DperformRelease=true \
          -Dcentral.username="${CENTRAL_USERNAME}" \
          -Dcentral.password="${CENTRAL_TOKEN}" \
          -Dgpg.passphrase="${GPG_PASSPHRASE}" \
          dokka:javadocJar gpg:sign deploy

# Create release artifacts directory and collect ZIP files
echo "Collecting release artifacts..."
mkdir -p release-artifacts
find . -type f -path "*/target/*.zip" -exec cp {} release-artifacts/ \;
