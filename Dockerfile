#FROM adoptopenjdk:11-jre-hotspot
#VOLUME /tmp
#RUN ./mvnw install -DskipTests
#COPY target/*.jar app.jar
#EXPOSE 8085 587
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM  openjdk:11-jdk as build
#WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

#RUN ./mvnw install -DskipTests
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM  openjdk:11-jdk
VOLUME /tmp
EXPOSE 8085 587
#ARG DEPENDENCY=/workspace/app/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY target/*.jar app.jar
#ENTRYPOINT ["java","-cp","app:app/lib/*","Mail.Application"]
ENTRYPOINT ["java","-jar","/app.jar"]
