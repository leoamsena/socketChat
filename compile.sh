rm -rf bin/
find -name "*.java" > sources.txt;
javac -d bin @sources.txt 
cp -r src/client/asets/ bin/src/client/
rm sources.txt