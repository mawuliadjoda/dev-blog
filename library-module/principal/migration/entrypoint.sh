#!/bin/sh


export LIQUIBASE_COMMAND_URL="jdbc:postgresql://host.docker.internal:5432/postgres" # car postgresql est lancer dans docker

export LIQUIBASE_COMMAND_USERNAME="root"
export LIQUIBASE_COMMAND_PASSWORD="root123"

export LIQUIBASE_DEFAULT_SCHEMA_NAME="public"

export LIQUIBASE_COMMAND_CHANGELOG_FILE="db/changelog/db.changelog-master.yml"

#export LIQUIBASE_CONTEXTS="remote"

liquibase update
