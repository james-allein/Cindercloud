 FROM openjdk:8-jdk-alpine
VOLUME /tmp

WORKDIR /cindercloud
VOLUME /cindercloud

ENV JAVA_OPTS="-Dspring.profiles.active=prod"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar web.jar