@echo off

taskkill /f /im explorer.exe
start "" javaw -jar mobileagent-launcher.jar mobile-agent run.bat /esel/terminal/web /esel/terminal/download/EE