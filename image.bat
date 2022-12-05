call mvn clean install -DskipTests=true -Dfile.encoding=UTF8
docker build -t example:0.0.1 .