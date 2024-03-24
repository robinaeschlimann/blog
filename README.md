# Blog

In diesem Projekt können Blog-Posts erstellt und geladen werden. Zu den Blog-Posts gehören natürlich auch die Kommentare, welche zu einem Blog erfasst werden können.
Auch eine Suche wurde mit Hibernate Search eingebaut. Diese kann über eine eigene API verwendet werden.

## Services

Folgende Services sind in diesem Projekt vorhanden:

![Architektur.png](readme-images%2FArchitektur.png)

Diese werden mit dem Docker-Compose-File gestartet (siehe nächster Abschnitt).

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