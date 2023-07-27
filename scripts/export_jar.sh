#!/bin/bash

# Function to compile the Java project
function compile_java_project {
  javac -d bin src/com/comunication/*.java src/com/chat/*.java
  javadoc -d docs/ -sourcepath src/ -subpackages com.comunication com.chat 
}

# Function to create the JAR file
function create_jar {
  cd bin
  jar cvf ../build/LocalChatApi.jar -C . com/comunication -C . com/chat -C .. /docs
}


# Going to the proyects root folder
cd ../
# Create a build directory if it doesn't exist
mkdir -p bin
mkdir -p build
mkdir -p docs

# Compile the Java project
compile_java_project

# Copy resources to build directory
# cp -r src/main/resources/* bin/

# Create the JAR file
create_jar 

cd ..

cp -r build/LocalChatApi.jar ../ChatServer/lib
cp -r build/LocalChatApi.jar ../ChatClient/lib

