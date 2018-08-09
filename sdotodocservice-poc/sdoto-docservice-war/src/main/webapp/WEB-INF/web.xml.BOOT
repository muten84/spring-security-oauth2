<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
	id="sdotodocservice" version="3.0">
	<display-name>sdoto-docservice-webpp</display-name>
	<context-param>
		<param-name>logbackConfigLocation</param-name>
		<param-value>/WEB-INF/logback.xml</param-value>
	</context-param>
	<context-param>
		<param-name>webAppRootKey</param-name>
		<param-value>sdoto-docservice-webpp.root</param-value>
	</context-param>
	<context-param>
		<param-name>contextClass</param-name>
		<param-value>
          org.springframework.web.context.support.AnnotationConfigWebApplicationContext
      </param-value>
	</context-param>

	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>it.eng.areas.to.sdoto.docservice.war.WebAppConfiguration</param-value>
	</context-param>

	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

	<context-param>
		<param-name>confPath</param-name>
		<param-value>classpath:/it.eng.areas.to.sdoto.docservice/conf/</param-value>
	</context-param>
	<servlet>
		<servlet-name>SpringMVC</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>		
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet>
		<servlet-name>jersey-serlvet</servlet-name>
		<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
		<init-param>
			<param-name>javax.ws.rs.Application</param-name>
			<param-value>it.eng.areas.to.sdoto.docservice.rest.JerseyConfig</param-value>
		</init-param>
		<load-on-startup>2</load-on-startup>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>jersey-serlvet</servlet-name>
		<url-pattern>/rest/*</url-pattern>
	</servlet-mapping>
	<servlet-mapping>
		<servlet-name>SpringMVC</servlet-name>
		<url-pattern>/*</url-pattern>
	</servlet-mapping>

</web-app>