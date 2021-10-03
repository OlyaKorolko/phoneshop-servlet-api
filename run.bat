set MAVEN_OPTS=-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000
mvn -Dmaven.test.skip=true tomcat7:run-war
rem mvn tomcat7:run