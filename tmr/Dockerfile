## DEV

# get maven image
FROM maven:3.9.6 AS dev

# set working directory
WORKDIR /usr/src/app

# pull code from develop branch
RUN git clone --branch dev --single-branch https://github.com/rajeshsinha1997/task-manager-rest-java.git

# change working directory
WORKDIR /usr/src/app/task-manager-rest-java/tmr

# build deployebale package
RUN mvn package

# get tomcat image
FROM tomcat:10.1.19 AS serverdev

# clean webapps directory of tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# copy deployable app to tomcat's webapps directory
COPY --from=dev /usr/src/app/task-manager-rest-java/tmr/target/*.war /usr/local/tomcat/webapps/

# ------------------------------------------------------------------------------------------------

## PROD

# get maven image
FROM maven:3.9.6 AS prod

# set working directory
WORKDIR /usr/src/app

# pull code from main branch
RUN git clone --branch main --single-branch https://github.com/rajeshsinha1997/task-manager-rest-java.git

# change working directory
WORKDIR /usr/src/app/task-manager-rest-java/tmr

# build deployebale package
RUN mvn package

# get tomcat image
FROM tomcat:10.1.19 AS serverprod

# clean webapps directory of tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# copy deployable app to tomcat's webapps directory
COPY --from=prod /usr/src/app/task-manager-rest-java/tmr/target/*.war /usr/local/tomcat/webapps/