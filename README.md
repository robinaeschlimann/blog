# Blog

In diesem Projekt können Blog-Posts erstellt und geladen werden. Zu den Blog-Posts gehören natürlich auch die Kommentare, welche zu einem Blog erfasst werden können.

## Starten des Projekts
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

### Keycloak starten und authentifizieren
> **_WICHTIG:_**  Der Keycloak muss bereits konfiguriert sein. Konfiguration siehe: https://moodle.hftm.ch/mod/page/view.php?id=206167

Für die Authentifizierung wird Keycloak verwendet. Dieser muss ebenfalls gestartet werden, damit man sich an den Endpoints authentifizieren kann. Gestartet wird er mit folgendem Befehl:
```shell
docker run --name keycloak --network blog-nw -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -e KC_HTTP_PORT=8180 -e KC_HOSTNAME_URL=http://keycloak:8180 -p 8180:8180 -d quay.io/keycloak/keycloak:22.0.1 start-dev
```
Nachdem das Projekt und der Keycloak aufgestartet ist, kann man die Seite http://localhost:8080/q/dev-v1/ aufrufen. Dort findet man folgende Übersicht:
![quarkus-overview.png](readme-images%2Fquarkus-overview.png)

Unter OpenID Connect kann man dann auf "keycloak" klicken und sich dann da einloggen. Für das Testing kann der User "robin" mit dem Passwort "Test1234" verwendet werden.

Nach dem Login findet man diese Ansicht:
![oidc-overview.png](readme-images%2Foidc-overview.png)

In dieser Ansicht kann man dann auf "View Access Token" klicken und das Access Token kopieren.

Auf der DEV-UI (siehe Bild oben) kann mann dann im Kasten "SmallRye OpenAPI" auf "Swagger UI" klicken. Beim Klick auf "Authorize" im Swagger UI kann dann das Access Token eingegeben werden.

Nach diesem Schritt können die Requests aus dem Swagger UI inkl. Authentifizierung ausgeführt werden.

## Testen des Projekts
Sobald das Projekt gestartet wurde, wird in der Konsole folgende Meldung angezeigt:
![quarkus-tests.png](readme-images%2Fquarkus-tests.png)

Mit der Eingabe von "r" werden die Tests ausgeführt. Das Resultat kann dann mit "o" angesehen werden.

## Berechtigungskonzept

Die Methode für das Laden von Blogs und Kommentaren benötigt keine Berechtigung. Für die anderen Funktionen braucht es eine bestimmte Rolle:

| Funktion           | Rolle           | Beschränkung                                   |
|-------------------|-----------------|-----------------------------------------------|
| Blog erstellen    | User & create-blog | -                                             |
| Blog bearbeiten   | User & create-blog | Der Benutzer kann nur seine eigenen Blogs bearbeiten |
| Kommentar schreiben | User           | -                                             |

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
## Blog-App starten
```shell
docker run -itd --name blog -p 8080:8080 --network=blog-nw ghcr.io/robinaeschlimann/blog-backend-ra:latest
```

## Probleme
### Kommentare werden nicht gespeichert
Neu angelegte Kommentare werden nicht auf dem Blog gespeichert. Ich habe einiges versucht und mit Google versucht das Problem zu lösen. Allerdings ohne Erfolg.
Der Post-Request für das Erstellen eines Kommentars wird richtig abgesetzt und die richtigen Methoden werden auch aufgerufen. Wenn ich dann aber die Kommentare zu einem Post abfrage, erhalte ich immer eine leere Liste zurück.
</br>**Lösung**: Ich habe die Kommentare in der Datenbank nicht richtig gespeichert. Ich habe das Property `cascade` in der Annotation `@OneToMany` vergessen. Nachdem ich dieses hinzugefügt habe, hat es funktioniert.
