rm -rf bin/*
javac -sourcepath src -d bin src/ir/ramtung/coolserver/*.java

if [ $? -eq 0 ]; then
    cd bin
    jar cvf ../coolserver.jar *
fi
