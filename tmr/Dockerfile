# get tomcat image
FROM tomcat:10.1.19

# set work directory
WORKDIR /usr/local/tomcat/webapps

# copy war file into the tomcat webapps
COPY target/*.war .

# expose port
EXPOSE 8080

# run tomcat server
CMD ["catalina.sh", "run"]