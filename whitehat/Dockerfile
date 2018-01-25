FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/whitehat.jar app.jar
ENV JAVA_OPTS="-D spring.profiles.active=prod"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar