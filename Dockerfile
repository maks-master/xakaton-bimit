FROM tomcat:8.5-jdk16

ENV CATALINA_OPTS=" -Duser.timezone=Europe/Moscow -Dfile.encoding=UTF-8 -Dspring.profiles.active=prod"

COPY xakaton.war /usr/local/tomcat/webapps/
COPY version /

EXPOSE 8080

ENV JAVA_OPTS="-Xmx1024M  -server "

VOLUME ["/usr/local/tomcat/logs"]

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]