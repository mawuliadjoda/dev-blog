FROM liquibase:4.27-alpine
WORKDIR /db

# tout ce qui est sous principal/src/... est visible
COPY src/main/resources/db ./db
COPY migration/entrypoint.sh /db/entrypoint.sh

USER root
RUN chmod +x /db/entrypoint.sh
USER liquibase

ENTRYPOINT ["./entrypoint.sh"]