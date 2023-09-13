#!/bin/bash

. ./declare_build_vars.sh

# Function to compile the Java project
function compile_java_project {
  javac -d $OUTPUT_FOLDER $COMPILATION_PATH src/module-info.java
  javadoc -d $DOCS_PATH -sourcepath $SOURCE_PATH -subpackages $PKG_CONTROLLER $PKG_CHAT $PKG_API $PKG_CONTROLLER_HANDLERS $PKG_DATA
}

# Function to create the JAR file
function create_jar {
  cd $OUTPUT_FOLDER
  jar cvf ../$BUILD_PATH -C . com/api -C . com/chat -C . com/controller -C . com/controller/handlers -C . com/data  -C .. /docs
}

# Function to go to the root folder
function go_to_root {
  cd ../
}

go_to_root
# Create a build directory if it doesn't exist
mkdir -p $OUTPUT_FOLDER
mkdir -p $BUILD_FOLDER
mkdir -p $DOCS_FOLDER

# Compile the Java project
compile_java_project

# Copy resources to build directory
# cp -r src/main/resources/* bin/

# Create the JAR file
create_jar

cd ..

cp -r $BUILD_PATH ../ChatServer/lib
cp -r $BUILD_PATH ../ChatClient/lib
