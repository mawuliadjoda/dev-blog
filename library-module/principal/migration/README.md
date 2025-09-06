
cd principal
docker build -t adjodamawuli/my_migration:1.0.0 -f migration/Dockerfile .

docker run --rm adjodamawuli/my_migration:1.0.0



#  
     docker run --rm \
     -e LIQUIBASE_COMMAND=update \
     -e LIQUIBASE_LOG_LEVEL=info \
     -e LIQUIBASE_COMMAND_URL=jdbc:postgresql://postgres:5432/person \
     -e LIQUIBASE_COMMAND_USERNAME=root \
     -e LIQUIBASE_COMMAND_PASSWORD=root123 \
     adjodamawuli/my_migration:1.0.0