.PHONY: build
build:
	@ant -buildfile server/build.xml > /dev/null
	@java -jar server/dist/server.jar

.PHONY: client
client:
	@mvn -f client/pom.xml compile
	mvn exec:java -Dexec.mainClass="com.assembleurnational.javachatclient.App" -f client/pom.xml
