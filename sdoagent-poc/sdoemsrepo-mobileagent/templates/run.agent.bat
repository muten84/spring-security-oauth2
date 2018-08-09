@echo off
@echo Running the batch %1

set AGENT_CP=.;..\lib\@batchJarName@.jar;@classpath@
java @java_opts@ -cp %AGENT_CP%  it.eng.areas.ems.mobileagent.AgentApp @app_opts@
