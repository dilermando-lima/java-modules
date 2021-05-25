#!/bin/bash

# compiling ======================
 javac -d /built-docker  --module-source-path /src-docker --module example.readfile 
 mkdir /built-docker/example.readfile/resource/ 
 cp -r /src-docker/example.readfile/resource/* /built-docker/example.readfile/resource/

# creating =======================
rm -rf jre-custom-docker

jlink --module-path $JAVA_HOME/jmods:/built-docker --add-modules example.readfile,java.base  --output jre-custom-docker  --launcher example-readfile=example.readfile/example.readfile.run.ReadFile

# removing old jre  ==============
rm -r /opt/openjdk-16/*
