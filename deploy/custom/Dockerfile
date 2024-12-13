FROM registry.redhat.io/amq7/amq-broker-rhel8:7.12.3

COPY broker.xml /opt/amq/conf/broker.xml
COPY log4j2.properties /opt/amq/conf/log4j2.properties

ENV AMQ_CLUSTERED true
ENV AMQ_USER quarkus
ENV AMQ_PASSWORD quarkus