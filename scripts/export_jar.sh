#!/bin/bash

# Function to compile the Java project
function compile_java_project {
  javac -d ../bin/ ../src/com/*.java ../src/com/chat/*.java
}

# Function to create the JAR file
function create_jar {
  cd ../bin
  jar cvf ../build/LocalChatApi.jar -C . /com -C . /com/chat
}

# Check if the current directory is a Java project

  # Create a build directory if it doesn't exist
  mkdir -p ../bin
  mkdir -p ../build

  # Compile the Java project
  compile_java_project

  # Copy resources to build directory
  # cp -r src/main/resources/* bin/

  # Create the JAR file
  create_jar 

  cp -r ../build/LocalChatApi.jar ../../ChatServer/lib
  cp -r ../build/LocalChatApi.jar ../../ChatClient/lib

