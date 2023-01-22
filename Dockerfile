FROM amazoncorretto:11
WORKDIR /app
ARG JAR_FILE=ecosigner.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]