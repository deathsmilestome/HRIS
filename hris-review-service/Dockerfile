FROM gradle:8.5.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM --platform=linux/amd64 amazoncorretto:21-alpine as run
WORKDIR /usr/local/bin/hris-review-service
COPY --from=build app/build/libs/hris-review-service-all.jar ./hris-review-service-all.jar
RUN chmod +x hris-review-service-all.jar
ENTRYPOINT ["java","-jar","hris-review-service-all.jar"]