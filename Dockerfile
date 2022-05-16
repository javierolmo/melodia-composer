# Install melodia-core and package melodia-composer
FROM maven:3.8.1-jdk-11

ARG github_token
ENV GITHUB_TOKEN=$github_token
RUN echo $github_token

COPY config/maven /root/.m2
RUN sed "s/SUPERSECRETTOKEN/$github_token/g" /root/.m2/settings.prod.xml > /root/.m2/settings.xml
RUN cp /root/.m2/settings.local.xml /root/.m2/settings.xml 2>&1; exit 0
RUN cat /root/.m2/settings.xml

COPY ./src /app/melodia-composer/src
COPY ./pom.xml /app/melodia-composer/pom.xml
RUN cd /app/melodia-composer && mvn package -DskipTests -P github

# Run melodia-composer
FROM openjdk:11-jre AS melodia-composer
RUN apt-get update && apt-get install -y musescore
ENV QT_QPA_PLATFORM=offscreen
WORKDIR /app
COPY --from=0 /app/melodia-composer/target/melodia-composer.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]