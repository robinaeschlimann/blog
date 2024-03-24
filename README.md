# Blog

In diesem Projekt können Blog-Posts erstellt und geladen werden. Zu den Blog-Posts gehören natürlich auch die Kommentare, welche zu einem Blog erfasst werden können.
Auch eine Suche wurde mit Hibernate Search eingebaut. Diese kann über eine eigene API verwendet werden.

## Services



## Applikation im Prod-Modus starten
Um die Applikation mit allen Services im Prod-Modus zu starten, kann das docker-compose.yml im src/main/docker Ordner verwendet werden. Dieses kann man herunterladen und danach starten: 
```shell
docker compose up
```

## Lokales Starten des Projekts
Das Projekt kann entweder mit Maven oder mit IntelliJ gestartet werden.
Um das Projekt mit Maven zu starten, braucht es den mvnw (Maven-Wrapper). Dafür muss man in der Konsole ins Projekt navigieren und danach den folgenden Befehl ausführen:
```shell
.\mvnw quarkus:dev
```

Im IntelliJ müssen folgende Plugins installiert werden:<br>
![intellij-quarkus-plugins.png](readme-images%2Fintellij-quarkus-plugins.png)

Danach wird automatisch diese Runtime-Config angelegt, über welche das Projekt gestartet werden kann:
![intellij-quarkus-runtime-config.png](readme-images%2Fintellij-quarkus-runtime-config.png)

Das Projekt kann dann mit diesen Buttons gestartet oder gedebugged werden:<br>
![intellij-start.png](readme-images%2Fintellij-start.png)

## Testen des Projekts
Sobald das Projekt gestartet wurde, wird in der Konsole folgende Meldung angezeigt:
![quarkus-tests.png](readme-images%2Fquarkus-tests.png)

Mit der Eingabe von "r" werden die Tests ausgeführt. Das Resultat kann dann mit "o" angesehen werden.

# Containerisierung
Diese Applikation kann auch in einem Container laufen gelassen werden. Ein Image dafür steht auf den GitHub-Packages zur Verfügung (https://www.github.com/robinaeschlimann/packages). Mit den folgenden Schritten kann die Applikation in einem Container gestartet werden:

## Docker Network erstellen
```shell
docker network create blog-nw
```
## Keycloak starten

```shell
docker run --name keycloak --network blog-nw -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -e KC_HTTP_PORT=8180 -e KC_HOSTNAME_URL=http://keycloak:8180 -p 8180:8180 -d quay.io/keycloak/keycloak:22.0.1 start-dev
```

Falls der Keycloak bereits vorhanden ist, kann dieser auch mit folgendem Befehl gestartet werden:
```shell
docker start keycloak
docker network connect blog-nw keycloak
```

## Blog-App image erzeugen
```shell
./mwnw package
docker build -f src/main/docker/Dockerfile.jvm -t ghcr.io/robinaeschlimann/blog-backend-ra:latest .

```

## Blog-App starten
```shell
docker run -itd --name blog -p 8080:8080 --network=blog-nw ghcr.io/robinaeschlimann/blog-backend-ra:latest
```

## Probleme
### Kommentare werden nicht gespeichert
Neu angelegte Kommentare werden nicht auf dem Blog gespeichert. Ich habe einiges versucht und mit Google versucht das Problem zu lösen. Allerdings ohne Erfolg.
Der Post-Request für das Erstellen eines Kommentars wird richtig abgesetzt und die richtigen Methoden werden auch aufgerufen. Wenn ich dann aber die Kommentare zu einem Post abfrage, erhalte ich immer eine leere Liste zurück.
</br>**Lösung**: Ich habe die Kommentare in der Datenbank nicht richtig gespeichert. Ich habe das Property `cascade` in der Annotation `@OneToMany` vergessen. Nachdem ich dieses hinzugefügt habe, hat es funktioniert.
