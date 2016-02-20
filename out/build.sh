rm -rf ./bin/*
find -name "*.java" > sources.txt
javac -sourcepath src -d bin -classpath ./lib/coolserver.jar @sources.txt
echo Main-Class: server/Server > manifest.txt
echo Class-Path: lib/coolserver.jar >> manifest.txt
cd bin
jar cvfm ../CA2.jar ../manifest.txt *
