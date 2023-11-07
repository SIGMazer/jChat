#!/bin/sh


run() {
    set -xe 
    javac Server.java Client.java
    java Server
}
clean(){
    set -xe 
    rm  *.class 
}
if [[ $1 == "run" ]]; then
    run
elif [[ $1 == "clean" ]]; then
    clean
else
    echo "Usage: ./run.sh [run|clean]"
fi
