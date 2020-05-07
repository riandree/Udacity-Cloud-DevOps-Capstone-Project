FROM openjdk:15-alpine
RUN addgroup -S application && adduser -S application -G application
USER application:application
# this will trigger the linter
# RUN cd /opt/app  
WORKDIR /opt/app
COPY target/todo-backend-*.jar backend.jar
ENTRYPOINT [ "java","-jar","backend.jar" ]
