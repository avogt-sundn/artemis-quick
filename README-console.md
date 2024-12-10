# Die GUI zu Artemis

Im [docker-compose.yml](deploy/simple/docker-compose.yml) sind die Ports auf den Host gemappt und jolokia CORS Prüfung abgeschwächt:

    environment:
      AMQ_EXTRA_ARGS: "--relax-jolokia"

    ports:
      - "8161:8161"



Dann kann unter
die console aufgerufen werden:

- im Browser http://localhost:8161
- login mit `admin` : `admin`
- erscheint:![alt text](.images/SCR-20241210-o5j.png)