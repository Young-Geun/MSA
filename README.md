# MSA

### Eureka
- http://localhost:8761/
  
### MariaDB
- mysql.server start
- sudo mysql -u root

### Kafka
- ./zookeeper-server-start.sh ../config/zookeeper.properties
- ./kafka-server-start.sh ../config/server.properties
- ./bin/connect-distributed ./etc/kafka/connect-distributed.properties

### Zipkin
- java -jar zipkin.jar
- http://localhost:9411/

### Prometheus
- ./prometheus
- http://localhost:9090/

### Grafana
- ./grafana-server
- http://localhost:3000/
- ID/PW = admin/admin

### Docker
- 이미지
  - 검색 : docker image ls
- 컨테이너
  - 검색 : docker container ls
  - 상태 : docker ps -a
  - 로그 : docker logs #CONTAINER ID#
  - 삭제 : docker container rm #CONTAINER ID#
- MariaDB
  - docker pull mariadb
  - docker run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mariadb mariadb:latest
  - docker exec -it mariadb /bin/bash
  - mysql -uroot -p -h127.0.0.1
  - docker stop mariadb / docker start mariadb
