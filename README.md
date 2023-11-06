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
  - mysql 원본 디렉토리를 별도의 디렉토리(db_data)로 복사한다.
    - Ex) cp -R /opt/homebrew/var/mysql ./db_data
  - db_data 동등레벨에 Dockerfile를 생성한다. 
  - ```
    Dockerfile
    
    FROM mariadb
    ENV MYSQL_ROOT_PASSWORD test1357
    ENV MYSQL_DATABASE mydb
    COPY ./mysql /var/lib/mysql
    EXPOSE 3306
    CMD ["--user=root"]
    ```
  - docker build -t 1992choi/mariadb:1.0 .
  - docker run -d -p 3306:3306 --network ecommerce-network --name mariadb 1992choi/mariadb:1.0
  - docker exec -it mariadb /bin/bash
  - mariadb -uroot -p
- Kafka
  - git clone https://github.com/wurstmeister/kafka-docker.git
  - docker-compose-single-broker.yml 수정
  - ```
    version: '2'
    services:
      zookeeper:
        image: wurstmeister/zookeeper
        ports:
          - "2181:2181"
        networks:
          my-network:
            ipv4_address: 172.18.0.100
      kafka:
        #build: .
        image: wurstmeister/kafka
        ports:
          - "9092:9092"
        environment:
          KAFKA_ADVERTISED_HOST_NAME: 172.18.0.101
          KAFKA_CREATE_TOPICS: "test:1:1"
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
        volumes:
          - /var/run/docker.sock:/var/run/docker.sock
        depends_on:
          - zookeeper
        networks:
          my-network:
            ipv4_address: 172.18.0.101
    
    networks:
      my-network:
        name: ecommerce-network
        external: true
    ```
  - 실행 : docker-compose -f docker-compose-single-broker.yml up -d
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
  - Gateway
    - Dockerfile 경로에서 아래 명령어 실행
    - docker build -t 1992choi/gateway-service:1.0 .
    - docker push 1992choi/gateway-service:1.0
    - docker run -d -p 8000:8000 --network ecommerce-network -e "spring.cloud.config.uri=http://config-service:8888" -e "spring.rabbitmq.host=rabbitmq" -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" --name gateway-service 1992choi/gateway-service:1.0
  - User
    - Dockerfile 경로에서 아래 명령어 실행
    - docker build -t 1992choi/user-service:1.0 .
    - docker push 1992choi/user-service:1.0
    - docker run 1992choi/user-service:1.0 (기본 실행)
    - docker run -d 1992choi/user-service:1.0 (백그라운드 실행)
