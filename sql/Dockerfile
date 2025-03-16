# docker build -t mysql-utf8 .
# docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=memeTagger mysql-utf8
FROM mysql:latest
COPY encode.cnf /etc/mysql/conf.d/encode.cnf