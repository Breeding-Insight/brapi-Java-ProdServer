# build image stack: `docker build -t docker-username/image-stack-name ./`
# run container (dev): `docker run --name=brapi-test-server --network=bridge -p 8081:8081 -d docker-username/image-stack-name`
# run container (prod): `docker run --name=brapi-test-server --restart always --network=brapi_net -d docker-username/image-stack-name`

FROM amazoncorretto:21

# 8080 - brapi app port | 5005 - brapi app debug port | 8008 - keycloak app port
EXPOSE 8080 5005 8008

RUN mkdir /home/brapi

COPY target/brapi-Java-TestServer-0.1.0.jar src/main/resources/ /home/brapi/

# Open up debug port on JVM
CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005 -cp "/home/brapi/:/home/brapi/brapi-Java-TestServer-0.1.0.jar" org.springframework.boot.loader.JarLauncher
