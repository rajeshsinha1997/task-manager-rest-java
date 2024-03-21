# get maven docker image as base
FROM maven:3.9.6-eclipse-temurin-17-focal AS base
# set base working directory
WORKDIR /usr/src/app
# -------------------------------------------- PROD ----------------------------------------------
# get maven image
FROM base AS prod
# set working directory
WORKDIR /usr/src/app
# pull code from main branch
RUN git clone --branch main --single-branch https://github.com/rajeshsinha1997/task-manager-rest-java.git
# change working directory
WORKDIR /usr/src/app/task-manager-rest-java
# build deployebale package
RUN mvn package
# ------------------------------------------- LABEL ----------------------------------------------
LABEL maintainer="RAJESH SINHA"
LABEL language="JAVA"
LABEL framework="SERVLET"
# ------------------------------------------------------------------------------------------------
# ------------------------------------------ TOMCAT ----------------------------------------------
# get tomcat image
FROM tomcat:10.1.19 AS serveprod
# clean webapps directory of tomcat
RUN rm -rf /usr/local/tomcat/webapps/*
# copy deployable app to tomcat's webapps directory
COPY --from=prod /usr/src/app/task-manager-rest-java/target/*.war /usr/local/tomcat/webapps/
# ------------------------------------------------------------------------------------------------