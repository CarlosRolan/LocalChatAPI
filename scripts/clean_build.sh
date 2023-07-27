#!/bin/bash

. ./declare_build_vars.sh

cd ..

echo "Deleting last build..."


if [ -f $OUTPUT_FOLDER ]; then
    rm -R $OUTPUT_FOLDER

else
    echo The folder ./$OUTPUT_FOLDER is alredy deleted
fi

if [ -d $DOCS_FOLDER ]; then
    rm -R $DOCS_FOLDER
else
    echo The folder ./$DOCS_FOLDER is alredy deleted
fi

if [ -d $BUILD_FOLDER ]; then
    rm -R $BUILD_FOLDER
else
    echo The folder ./$BUILD_FOLDER is alredy deleted
fi

if [ -d $LIB_FOLDER ]; then
    rm -R $LIB_FOLDER
else
    echo The folder ./$LIB_FOLDER is alredy deleted
fi
