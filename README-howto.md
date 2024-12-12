# Wie ein robustes messaging aufgebaut werden sollte

## Retries in den Clients

    mp.messaging.connector.smallrye-amqp.reconnect-interval=1

- nach einem Verbindungsfehler wird jeder Sekunde erneut getestet, ob AMQ wieder erreichbar ist
- längere Intervalle verlängern die Zeit bis zum reconnect unnötig

## Cluster können messages übernehmen

jede Artemis Instanz hat zunächst nur einen privaten Speicher für die empfangenen messages

