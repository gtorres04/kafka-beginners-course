# kafka-beginners-course
Paso a Paso del curso de Kafka de Udemy
## Instalacion y ejecucion de Kafka (Docker)
referencia: https://www.conduktor.io/kafka/how-to-start-kafka-using-docker

```
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-beginners-course % git clone https://github.com/conduktor/kafka-stack-docker-compose.git
Cloning into 'kafka-stack-docker-compose'...
remote: Enumerating objects: 590, done.
remote: Counting objects: 100% (258/258), done.
remote: Compressing objects: 100% (93/93), done.
remote: Total 590 (delta 177), reused 204 (delta 154), pack-reused 332
Receiving objects: 100% (590/590), 115.98 KiB | 1.76 MiB/s, done.
Resolving deltas: 100% (407/407), done.
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-beginners-course % cd kafka-stack-docker-compose 
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % ls -l
total 104
-rw-r--r--  1 gerlinorlandotorres  staff  11345 Jun 21 23:25 LICENSE
-rw-r--r--  1 gerlinorlandotorres  staff   7408 Jun 21 23:25 README.md
-rw-r--r--  1 gerlinorlandotorres  staff   4686 Jun 21 23:25 full-stack.yml
-rwxr-xr-x  1 gerlinorlandotorres  staff   2150 Jun 21 23:25 test.sh
-rw-r--r--  1 gerlinorlandotorres  staff   3948 Jun 21 23:25 zk-multiple-kafka-multiple-schema-registry.yml
-rw-r--r--  1 gerlinorlandotorres  staff   3411 Jun 21 23:25 zk-multiple-kafka-multiple.yml
-rw-r--r--  1 gerlinorlandotorres  staff   1959 Jun 21 23:25 zk-multiple-kafka-single.yml
-rw-r--r--  1 gerlinorlandotorres  staff   2689 Jun 21 23:25 zk-single-kafka-multiple.yml
-rw-r--r--  1 gerlinorlandotorres  staff   1327 Jun 21 23:25 zk-single-kafka-single.yml
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker-compose -f zk-single-kafka-multiple.yml up -d          
[+] Running 4/4
 ⠿ Container zoo1    Started                                                                                                                                                                                                                                                                                       0.5s
 ⠿ Container kafka3  Started                                                                                                                                                                                                                                                                                       1.2s
 ⠿ Container kafka2  Started                                                                                                                                                                                                                                                                                       1.0s
 ⠿ Container kafka1  Started                                                                                                                                                                                                                                                                                       1.1s
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker-compose -f zk-single-kafka-multiple.yml ps   
NAME                COMMAND                  SERVICE             STATUS              PORTS
kafka1              "/etc/confluent/dock…"   kafka1              running             0.0.0.0:9092->9092/tcp
kafka2              "/etc/confluent/dock…"   kafka2              running             0.0.0.0:9093->9093/tcp
kafka3              "/etc/confluent/dock…"   kafka3              running             0.0.0.0:9094->9094/tcp
zoo1                "/etc/confluent/dock…"   zoo1                running             0.0.0.0:2181->2181/tcp
```
Se instala kafka
```
brew install kafka
```
Desde el comando de la instalacion de kafka en local se accede al kafka dockerizado
```
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % kafka-topics --bootstrap-server localhost:9092 --version                     
3.2.0 (Commit:38103ffaa962ef50)
```
Ingresar al contenedor Kafka
```
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker exec -it kafka1 kafka-topics --version       
7.1.1-ccs (Commit:947fac5beb61836d)
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker exec -it kafka1 /bin/bash                    
[appuser@kafka1 ~]$ kafka-topics --version
7.1.1-ccs (Commit:947fac5beb61836d)
[appuser@kafka1 ~]$ exit
exit
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker exec -it kafka2 kafka-topics --version      
7.1.1-ccs (Commit:947fac5beb61836d)
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker exec -it kafka2 /bin/bash            
[appuser@kafka2 ~]$ kafka-topics --version
7.1.1-ccs (Commit:947fac5beb61836d)
[appuser@kafka2 ~]$ exit
exit
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker exec -it kafka3 kafka-topics --version
7.1.1-ccs (Commit:947fac5beb61836d)
gerlinorlandotorres@MacBook-Pro-de-Gerlin kafka-stack-docker-compose % docker exec -it kafka3 /bin/bash            
[appuser@kafka3 ~]$ kafka-topics --version
7.1.1-ccs (Commit:947fac5beb61836d)
[appuser@kafka3 ~]$ exit
exit
```