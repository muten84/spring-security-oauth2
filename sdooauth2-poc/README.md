Docker su macos:
https://spring.io/guides/gs/spring-boot-docker/
https://www.baeldung.com/dockerizing-spring-boot-application


 per esportare  DOCKER_HOST SU macos:
   export DOCKER_HOST=unix:///var/run/docker.sock
   
 per buuildare l'immagine docker:
 docker build --build-arg JAR_FILE=target/app.jar -t springio_test ./
 
 per avviare l'immagine docker appena creata:
 docker run -p 8080:8081 springio_test
 
 
 per debuggare l'app nel container:
 $ docker run -e "JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8080:8080 -p 5005:5005 -t springio/gs-spring-boot-docker
 
 prima di fare push fare commit dell'immagine
 
 fare push dell'immagine su repo
  l'immagine è salvata.
 
 docker compose:
 
 docker-compose config
 docker-compose up --build
 docker-compose down
 docker-compose up --scale oauth2-server=3
 
 

Plugin di Docker suEclipse:
 Attenzione se viene dato errore di ping sulla socket bisogna togliere il proxy manuale da eclipse e impostare quello nativo.
 
 
OAuth2:
per capire meglio i grant_type e gli attori coinvolti
https://alexbilbie.com/guide-to-oauth-2-grants/

Docker Registry:
kubectl create secret docker-registry regcred --docker-server=127.0.0.1 --docker-username=muten84 --docker-password=12345678 --docker-email=bifulcoluigi@gmail.com

Per attivare i profili di maven e spring bisogna lanciare tutto da maven

Il docker compose è: docer-compose -f file up -d per buttare giù docper-compose down

TODO: 
il docker compose non ha un volume
provare h2 console con mysql
capire meglio flybase per gli aggiornamenti