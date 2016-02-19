rm -rf bin/*
javac -classpath ./lib/coolserver.jar ./src/Server/Server.java

if [ $? -eq 0 ]; then
    echo "Starting Server"
    java -classpath ./bin:../lib/coolserver.jar CalcServer
fi
