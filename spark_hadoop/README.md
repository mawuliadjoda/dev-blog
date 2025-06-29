# PREREQUIS: 
  A tester sur linux, car des souci de windows avec spark
  Java 17 car spark n'est pas encore compatible avec Java 21

1. generer le fichier parquet => ParquetFileWriter.java generate()
2. copier le fichier généré dans /installation/data-local
3. mettre à jour le fichier docker-compose.yml => ajouter  volume: - ./data-local:/data-local
4. Relancer docker compose


adjoda@NucBoxM3Plus:/mnt/d/dev/git/dev-blog/spark_hadoop/installation$ docker compose down
adjoda@NucBoxM3Plus:/mnt/d/dev/git/dev-blog/spark_hadoop/installation$ docker compose up -d
[+] Running 3/3
✔ Network installation_hadoop  Created                                                                                                                                                0.1s
✔ Container namenode           Started                                                                                                                                                0.3s
✔ Container datanode           Started                                                                                                                                                0.4s


5.  entrer dans le container
adjoda@NucBoxM3Plus:/mnt/d/dev/git/dev-blog/spark_hadoop/installation$ docker exec -it namenode bash
6. Verifier si le volume "data-local" existe
root@b079b8565da8:/# ls
KEYS  bin  boot  data-local  dev  entrypoint.sh  etc  hadoop  hadoop-data  home  lib  lib64  media  mnt  opt  proc  root  run  run.sh  sbin  srv  sys  tmp  usr  var
root@b079b8565da8:/# ls data-local/
_SUCCESS  part-00000-dc46302e-c7e6-4bd2-af56-07e01a70a7f7-c000.snappy.parquet
root@b079b8565da8:/# hdfs dfs -mkdir -p /data
7. 	Copier un fichier depuis le système local vers HDFS
root@b079b8565da8:/# hdfs dfs -put /data-local/part-00000-*.parquet /data/
2025-06-29 19:31:27,830 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
root@b079b8565da8:/#

8. http://localhost:9870/ visualiser le systeme de fichier hdfs
   http://localhost:9870/ aller dans utilities > browse the filesystem > on voit bien le repertoir "data"
   Le fichier "part-00000-dc46302e-c7e6-4bd2-af56-07e01a70a7f7-c000.snappy.parquet" existe bien dans le repertoire "data"
   http://localhost:9870/explorer.html#/data