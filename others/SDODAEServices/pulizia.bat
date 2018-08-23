SET days=3

echo Pulizia file più vecchi di %days% giorni

SET log_path=C:\esel\area118\tools\tomcat60-std\logs\

echo Pulizia file %log_path%

forfiles -p %log_path% -s -m *.* /d -%days% -c "cmd /c del @path"

SET log_path=C:\esel\area118\tools\tomcat60-geo\logs\

forfiles -p %log_path% -s -m *.*  /d -%days% -c "cmd /c del @path"

SET log_path=C:\esel\area118\tools\tomcat7bis\logs\

forfiles -p %log_path%  -s -m *.*  /d -%days% -c "cmd /c del @path"


SET log_path=C:\deploy\esel\area118\tools\tomcat7bis\logs\

forfiles -p %log_path%  -s -m *.*  /d -%days% -c "cmd /c del @path"