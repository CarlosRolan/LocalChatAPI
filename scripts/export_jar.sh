#!/bin/bash

# Function to check if the current directory contains a Java project
function check_java_project {
  if [ -f "../src/com/ApiCodes.java" ]; then
    return 0
  else
    return 1
  fi
}

# Function to compile the Java project
function compile_java_project {
  javac -d ../bin/com/*.jar
}

# Function to create the JAR file
function create_jar {
  jar cvf "$1" -C ../bin/com .
}

# Check if the current directory is a Java project
if check_java_project; then
  # Create a build directory if it doesn't exist
  mkdir -p bin

  # Compile the Java project
  compile_java_project

  # Copy resources to build directory
  # cp -r src/main/resources/* bin/

  # Get the given path for the JAR file from the user
  $export_path = ../bin/com

  # Check if the given path ends with '.jar' extension
  if [[ "$export_path" == *.jar ]]; then
    # Create the JAR file
    create_jar "$export_path"
    echo "JAR file successfully exported to: $export_path"
  else
    echo "Invalid file name. The path should end with '.jar' extension."
  fi
else
  echo "Error: The current directory does not appear to be a Java project."
fi
