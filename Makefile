build:
	@ant -buildfile server/build.xml > /dev/null
	@java -jar server/dist/server.jar



