#!/bin/sh
set -e

export LIQUIBASE_COMMAND_URL="jdbc:postgresql://host.docker.internal:5432/person"
export LIQUIBASE_COMMAND_USERNAME="root"
export LIQUIBASE_COMMAND_PASSWORD="root123"
export LIQUIBASE_COMMAND_DEFAULT_SCHEMA_NAME="person_schema"
export LIQUIBASE_COMMAND_CHANGELOG_FILE="db/changelog/db.changelog-master.yml"
export LIQUIBASE_COMMAND_CONTEXTS="remote"

# préciser le search-path évite les surprises de chemin
#liquibase --search-path=/db update


liquibase update
