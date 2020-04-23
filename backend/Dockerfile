FROM openjdk:15-alpine
RUN addgroup -S application && adduser -S application -G application
USER application:application
WORKDIR /opt/app
COPY target/todo-backend-*.jar backend.jar
ENTRYPOINT [ "java","-jar","backend.jar" ]
