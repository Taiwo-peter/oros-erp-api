#!/bin/sh
set -e

MAVEN_WRAPPER_JAR="$(dirname "$0")/.mvn/wrapper/maven-wrapper.jar"
MAVEN_HOME="${MAVEN_HOME:-"${HOME}/.m2/wrapper"}"
MAVEN_VERSION="3.9.6"

if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
    mkdir -p "$(dirname "$MAVEN_WRAPPER_JAR")"
    if command -v wget > /dev/null; then
        wget -q "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" -O "$MAVEN_WRAPPER_JAR"
    elif command -v curl > /dev/null; then
        curl -sL "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" -o "$MAVEN_WRAPPER_JAR"
    fi
fi

MAVEN_DIR="${MAVEN_HOME}/apache-maven-${MAVEN_VERSION}"
if [ ! -d "$MAVEN_DIR" ]; then
    mkdir -p "$MAVEN_HOME"
    if command -v wget > /dev/null; then
        wget -q "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.tar.gz" -O - | tar xz -C "$MAVEN_HOME"
    elif command -v curl > /dev/null; then
        curl -sL "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.tar.gz" | tar xz -C "$MAVEN_HOME"
    fi
fi

exec "$MAVEN_DIR/bin/mvn" "$@"
