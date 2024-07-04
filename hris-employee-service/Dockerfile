FROM gradle:8.5.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM --platform=linux/amd64 amazoncorretto:21-alpine
WORKDIR /usr/local/bin/hris-employee-service
COPY --from=build app/build/libs/hris-employee-service-all.jar ./hris-employee-service-all.jar
RUN chmod +x hris-employee-service-all.jar
ENTRYPOINT ["java","-jar","hris-employee-service-all.jar"]
