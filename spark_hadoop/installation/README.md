---

docker compose up -d

docker exec -it namenode bash

ls

ls data-local/

# Créer un repertoire /data pour contenir les fichiers parquet à déposer dans hadoop
hdfs dfs -mkdir -p /data

hdfs dfs -put /data-local/*.parquet /data/


# Note:
Pour déposer ou insérer de fichier, la ligne correspondante suivant doit être égal à : datanode
# HDFS_CONF_dfs_datanode_hostname=datanode
# delete file
hdfs dfs -rm /data/annotationPermanente.parquet

# copy all file at the time
hdfs dfs -put /data-local/*.parquet /data/