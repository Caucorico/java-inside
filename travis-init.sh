#!/bin/bash

set -e # exit if return != 0
if [ "$DIR" = "lab6" ]; then
    # download jdk-14
    if [ "$TRAVIS_OS_NAME" = "linux" ]; then
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-linux.tar.gz
        tar xvzf jdk-14-loom-linux.tar.gz
    elif [ "$TRAVIS_OS_NAME" = "osx" ]; then
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-osx.tar.gz
        tar xvzf jdk-14-loom-osx.tar.gz
    fi
    # export new environment variable JAVA_HOME
    export JAVA_HOME=$(pwd)/jdk-14-loom/
fi

# for all lab
cd $DIR
mvn package
