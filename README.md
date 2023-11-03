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
