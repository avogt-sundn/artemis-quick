## Konfigurieren des Artemis Broker

### deploy/simple: Einfacher Cluster mit Auto discovery

Wechsel auf der Kommandozeile in das Verzeichnis mit dem einfachen docker Setup für einen Cluster mit 2 Knoten:

````bash
cd deploy/simple
````

Darin findet sich das [docker-compose.yml](./deploy/simple/docker-compose.yml):
````yaml

services:

  artemis:
    image: quay.io/artemiscloud/activemq-artemis-broker
    environment:
        AMQ_USER: quarkus
        AMQ_PASSWORD: quarkus
        AMQ_CLUSTERED: true
````

Starten mit

````bash
docker compose up -d
docker compose logs -f artemis
`````

### Mit dem Cluster experimentieren:

````bash
docker compose scale artemis 1
# bringt den Cluster auf einen Knoten
docker compose scale artemis 2
# bringt den Cluster auf zwei Knoten

`````

Im Log von Artemis wird bei jeder Änderung im Cluster die neue connection ausgegeben:

`# artemis-1  | 2024-12-10 12:11:11,812 INFO  [org.apache.activemq.artemis.core.server] AMQ221027: Bridge ClusterConnectionBridge@7c992ddf [name=$.artemis.internal.sf.my-cluster.d800935e-b6ef-11ef-90d2-0242ac120003, queue=QueueImpl[name=$.artemis.internal.sf.my-cluster.d800935e-b6ef-11ef-90d2-0242ac120003, postOffice=PostOfficeImpl [server=ActiveMQServerImpl::name=broker], temp=false]@ded9eb2 targetConnector=ServerLocatorImpl (identity=(Cluster-connection-bridge::ClusterConnectionBridge@7c992ddf [name=$.artemis.internal.sf.my-cluster.d800935e-b6ef-11ef-90d2-0242ac120003, queue=QueueImpl[name=$.artemis.internal.sf.my-cluster.d800935e-b6ef-11ef-90d2-0242ac120003, postOffice=PostOfficeImpl [server=ActiveMQServerImpl::name=broker], temp=false]@ded9eb2 targetConnector=ServerLocatorImpl [initialConnectors=[TransportConfiguration(name=artemis, factory=org-apache-activemq-artemis-core-remoting-impl-netty-NettyConnectorFactory)?port=61616&host=172-18-0-3], discoveryGroupConfiguration=null]]::ClusterConnectionImpl@721395199[nodeUUID=d80e00c7-b6ef-11ef-a43f-0242ac120004, connector=TransportConfiguration(name=artemis, factory=org-apache-activemq-artemis-core-remoting-impl-netty-NettyConnectorFactory)?port=61616&host=172-18-0-4, address=, server=ActiveMQServerImpl::name=broker])) [initialConnectors=[TransportConfiguration(name=artemis, factory=org-apache-activemq-artemis-core-remoting-impl-netty-NettyConnectorFactory)?port=61616&host=172-18-0-3], discoveryGroupConfiguration=null]] is connected`



Im Image wird Artemis mit diesem Skript gestartet:


    artemis-1  | /opt/amq/bin/launch.sh: line 49: /home/jboss/broker/bin/artemis: No such file or directory

## Initialisierung des Containers

### Logging

Im Init Script wird die log4j Konfiguration von der Datei

      echo 'JAVA_ARGS=" $JAVA_ARGS -Dlog4j2.configurationFile=$ARTEMIS_HOME/conf/log4j2.properties "' >> ${instanceDir}/etc/artemis.profile

### AMQ_CLUSTERED=true

Die Variable AMQ_CLUSTERD wird beim Starten des Containers umgesetzt zu:


      <cluster-user>cluster_user</cluster-user>

      <cluster-password>cluster_password</cluster-password>

      <broadcast-groups>
         <broadcast-group name="bg-group1">
            <group-address>231.7.7.7</group-address>
            <group-port>9876</group-port>
            <broadcast-period>5000</broadcast-period>
            <connector-ref>artemis</connector-ref>
         </broadcast-group>
      </broadcast-groups>

      <discovery-groups>
         <discovery-group name="dg-group1">
            <group-address>231.7.7.7</group-address>
            <group-port>9876</group-port>
            <refresh-timeout>10000</refresh-timeout>
         </discovery-group>
      </discovery-groups>

      <cluster-connections>
         <cluster-connection name="my-cluster">
            <connector-ref>artemis</connector-ref>
            <message-load-balancing>ON_DEMAND</message-load-balancing>
            <max-hops>0</max-hops>
            <discovery-group-ref discovery-group-name="dg-group1"/>
         </cluster-connection>
      </cluster-connections>
## Links

- Red Hat Dokumentation
  - https://docs.redhat.com/en/documentation/red_hat_amq/7.3/html-single/configuring_amq_broker/index#broker-configuration-files-location-configuring
- Quarkus amq extension properties
  - https://quarkus.io/guides/amqp-reference

- Wie startet der container und parametrisiert Artemis?
  -  https://github.com/amq-broker-hub/amq-on-openshift/tree/main/init-container
  -  https://github.com/artemiscloud/activemq-artemis-broker-kubernetes-image/blob/main/modules/activemq-artemis-launch/added/launch.sh
  -