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
  - 중지 : docker stop #CONTAINER ID#
  - 삭제 : docker container rm #CONTAINER ID#
- 네트워크
  - 목록 : docker network ls  
  - 할당 : docker network create --gateway 172.18.0.1 --subnet 172.18.0.0/16 ecommerce-network
  - 상세 : docker network inspect ecommerce-network
- RabbitMQ
  - 등록 : docker run -d --name rabbitmq --network ecommerce-network -p 15672:15672 -p 5672:5672 -p 15671:15671 -p 5671:5671 -p 4369:4369 -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:management
- MariaDB
  - docker pull mariadb
  - docker run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mariadb mariadb:latest
  - docker exec -it mariadb /bin/bash
  - mysql -uroot -p -h127.0.0.1
  - docker stop mariadb / docker start mariadb
- Service
  - Config
    - Dockerfile 경로에서 아래 명령어 실행
    - docker build -t 1992choi/config-service:1.0 .
    - docker push 1992choi/config-service:1.0
    - docker run -d -p 8888:8888 --network ecommerce-network -e "spring.rabbitmq.host=rabbitmq" -e "spring.profiles.active=default" --name config-service 1992choi/config-service:1.0
  - Eureka
    - Dockerfile 경로에서 아래 명령어 실행
    - docker build -t 1992choi/discovery-service:1.0 .
    - docker push 1992choi/discovery-service:1.0
    - docker run -d -p 8761:8761 --network ecommerce-network -e "spring.cloud.config.uri=http://config-service:8888" --name discovery-service 1992choi/discovery-service:1.0
  - User
    - Dockerfile 경로에서 아래 명령어 실행
    - docker build -t 1992choi/user-service:1.0 .
    - docker push 1992choi/user-service:1.0
    - docker run 1992choi/user-service:1.0 (기본 실행)
    - docker run -d 1992choi/user-service:1.0 (백그라운드 실행)
