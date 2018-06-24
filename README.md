## Demo Projekt im Softwarepraktikum 2018

Dieses Projekt wird in der Vorlesung kontinuierlich erweitert und wird die wichtigsten Frameworks für die Implementierung beinhalten.

Letzte Änderungen:
* Bootstrap als CSS-Framework eingebunden
* JPA-Beispiel hinzugefügt: Entities, Repositories und Services für die Verwaltung von Usern und Rollen
* DataLoader hinzugefügt: Wird bei jedem Applikationsstart durchgeführt und kann verwendet werden, um Testdaten zu generieren

### Authentifizierung
* ILIAS-Authentifizierung: Authentifizierung läuft über SOAP-Request an die ILIAS-Schnittstelle. Bei gültigen Logindaten wird eine Session-ID zurückgegeben, die für weitere SOAP-Requests verwendet werden kann. Siehe master-branch.
* Standardauthentifizierung: Benutzername und Passwort werden in lokaler Datenbank gespeichert und überprüft. Beispielprojekt: https://gitlab.com/aschmid/sopra-demo/tree/spring_security_standard


