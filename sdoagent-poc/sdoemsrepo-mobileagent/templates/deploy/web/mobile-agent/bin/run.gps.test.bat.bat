set AGENT_CP=.;..\lib\mobile-agent.jar;../lib\perstcdc-1.0.0.jar;../lib\tinylog-1.3.2.jar;../lib\jsondb-core-1.0.36.jar;../lib\jcl-over-slf4j-1.7.21.jar;../lib\log4j-api-2.6.2.jar;../lib\commons-beanutils-1.9.2.jar;../lib\commons-logging-1.1.1.jar;../lib\commons-collections-3.2.1.jar;../lib\javassist-3.20.0-GA.jar;../lib\commons-jxpath-1.3.jar;../lib\slf4j-api-1.7.21.jar;../lib\guava-19.0.jar;../lib\jackson-core-2.8.3.jar;../lib\reflections-0.9.10.jar;../lib\annotations-2.0.1.jar;../lib\log4j-slf4j-impl-2.6.2.jar;../lib\log4j-core-2.6.2.jar;../lib\slf4j-simple-1.7.7.jar;../lib\rxtx-2.1.jar;../lib\mqtt-client-1.12.jar;../lib\hawtdispatch-transport-1.21.jar;../lib\hawtdispatch-1.21.jar;../lib\hawtbuf-1.10.jar;../lib\jackson-databind-2.4.1.2.jar;../lib\log4j-1.2.17.jar;../lib\spark-core-2.6.0.jar;../lib\jetty-webapp-9.4.4.v20170414.jar;../lib\jetty-xml-9.4.4.v20170414.jar;../lib\websocket-servlet-9.4.4.v20170414.jar;../lib\sundial-2.1.3.jar;../lib\okhttp-3.9.0.jar;../lib\okio-1.13.0.jar;../lib\sdoemsrepo-api-0.0.1-SNAPSHOT.jar;../lib\swagger-annotations-1.5.10.jar;../lib\logback-classic-1.1.11.jar;../lib\logback-core-1.1.11.jar;../lib\commons-lang3-3.2.1.jar;../lib\junit-4.12.jar;../lib\hamcrest-core-1.3.jar;../lib\commons-io-2.4.jar;../lib\commons-cli-1.2.jar;../lib\oshi-core-3.4.3.jar;../lib\threetenbp-1.3.4.jar;../lib\javax.servlet-api-3.1.0.jar;../lib\websocket-common-9.4.6.v20170531.jar;../lib\websocket-api-9.4.6.v20170531.jar;../lib\jetty-io-9.4.6.v20170531.jar;../lib\websocket-server-9.4.6.v20170531.jar;../lib\websocket-client-9.4.6.v20170531.jar;../lib\jetty-client-9.4.6.v20170531.jar;../lib\jetty-servlet-9.4.6.v20170531.jar;../lib\jetty-security-9.4.6.v20170531.jar;../lib\jetty-http-9.4.6.v20170531.jar;../lib\jetty-util-9.4.6.v20170531.jar;../lib\jetty-server-9.4.6.v20170531.jar;../lib\jackson-annotations-2.8.8.jar;../lib\json-path-2.3.0.jar;../lib\json-smart-2.3.jar;../lib\accessors-smart-1.2.jar;../lib\asm-5.0.4.jar;../lib\dagger-2.14.1.jar;../lib\javax.inject-1.jar;../lib\jna-4.5.1.jar;../lib\jna-platform-4.5.1.jar
java -Djava.library.path=/esel/terminal/web/mobile-agent/bin -Dtinylog.configuration=/esel/terminal/web/etc/tinylog.properties -Xms10m -Xmx1024m -cp %AGENT_CP%  it.eng.areas.ems.mobileagent.jnative.win32.Win32Device 300000
