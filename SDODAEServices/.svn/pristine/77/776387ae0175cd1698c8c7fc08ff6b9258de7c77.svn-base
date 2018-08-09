cd ..
call mvn clean -Dmaven.test.skip=true

cd SDODAEServices-data-layer
call mvn install -Dmaven.test.skip=true

cd ..
cd SDODAEServices-delegate-layer
call mvn install -Dmaven.test.skip=true

cd ..
cd SDODAEServices-rest-layer
call mvn install -Dmaven.test.skip=true

cd ..
cd SDODAEServices-webapp
call mvn spring-boot:run -Dcatalina.base=logs
