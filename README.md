# blog

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

## Testen des Projekts
Sobald das Projekt gestartet wurde, wird in der Konsole folgende Meldung angezeigt:
![quarkus-tests.png](readme-images%2Fquarkus-tests.png)

Mit der Eingabe von "r" werden die Tests ausgeführt. Das Resultat kann dann mit "o" angesehen werden.

## Probleme
Aktuell werden neu angelegte Kommentare nicht auf dem Blog gespeichert. Ich habe einiges versucht und mit Google versucht das Problem zu lösen. Allerdings ohne Erfolg.

Der Post-Request für das Erstellen eines Kommentars wird richtig abgesetzt und die richtigen Methoden werden auch aufgerufen. Wenn ich dann aber die Kommentare zu einem Post abfrage, erhalte ich immer eine leere Liste zurück.

@Simeon: Hast du vielleicht eine Idee woran das liegen könnte? Die wichtigsten Klassen und Methoden:
- CommentService#addComment
- CommentService#getComments
- Blog.java
- Comment.java