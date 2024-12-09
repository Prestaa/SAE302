build:
	ant -buildfile server/build.xml

.PHONY: serv
serv:
	@java -jar server/dist/server.jar

