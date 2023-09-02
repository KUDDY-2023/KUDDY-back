FROM openjdk:11 as builder

COPY ./ ./

RUN chmod +x ./gradlew

RUN ./gradlew clean :noti-server:build -x test


FROM openjdk:11

WORKDIR /app

COPY --from=builder noti-server/build/libs/*.jar ./noti-server.jar

ENTRYPOINT ["java","-jar","./noti-server.jar"]
