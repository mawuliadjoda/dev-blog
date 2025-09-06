#!/bin/sh


export LIQUIBASE_COMMAND_URL="jdbc:postgresql://host.docker.internal:5432/person" # car postgresql est lancer dans docker

export LIQUIBASE_COMMAND_USERNAME="root"
export LIQUIBASE_COMMAND_PASSWORD="root123"
#export LIQUIBASE_COMMAND_PASSWORD=${DB_PASSWORD }

export LIQUIBASE_COMMAND_DEFAULT_SCHEMA_NAME="person_schema"

export LIQUIBASE_COMMAND_CHANGELOG_FILE="db/changelog/db.changelog-master.yml"

export LIQUIBASE_COMMAND_CONTEXTS="remote"

liquibase update