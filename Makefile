serv-build:
	ant -buildfile server/build.xml

.PHONY: serv
serv:
	@java -jar server/dist/server.jar

client-build:
	ant -buildfile client/build.xml

.PHONY: client
client:
	@java -jar client/dist/client.jar
