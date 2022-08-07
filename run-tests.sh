#!/bin/bash

if [[ "$#" == "0"  ]]; then
    echo "Please specify language type"
    exit 1
fi

echo "Building modules of type '$1'"

for dir in */; do
  # echo $dir
  cd $dir

  if [[ "$1" == "java" ]]; then
      if [ -f "pom.xml" ]; then
        ./mvnw clean verify
      fi

      if [ -f "build.gradle" ]; then
        ./gradlew clean build
      fi
  fi

  if [[ "$1" == "golang" ]]; then
    if [ -f "go.mod" ]; then
      go test ./...
    fi
  fi

  cd ..
done