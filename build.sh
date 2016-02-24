rm -rf ./bin/*
find -name "*.java" > sources.txt
javac -sourcepath src -d bin -classpath ./lib/coolserver.jar @sources.txt
#echo Main-Class: CA2 > manifest.txt
#echo Class-Path: lib/coolserver.jar >> manifest.txt
cd bin
mv CA3.class ../
jar cvfm ../CA3.jar ../manifest.txt *
#echo "Starting Server"
#java -classpath "../zipfile/":../lib/coolserver.jar:CA3.jar CA3
cd ..
rm *.txt
