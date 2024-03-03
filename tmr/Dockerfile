# set base image
FROM maven:3.9.6-eclipse-temurin-17-alpine AS base

# set work directory
WORKDIR /usr/app

# copy maven project file (POM.XML)
COPY ./pom.xml .

# download dependencies
RUN ["mvn", "clean", "install", "-f", "/usr/app/pom.xml", "-DskipTests=true"]

# copy source files
COPY ./src .

# build war file
RUN ["mvn", "package"]

# get tomcat image
FROM tomcat:10.1.19 AS tomcat

# expose port
EXPOSE 8080

# copy the generated war file into the tomcat working directory
COPY --from=base /usr/app/target/*.war /usr/local/tomcat/webapps/

# run tomcat server
CMD ["catalina.sh", "run"]