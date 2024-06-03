# Kontakte-Verwaltung

Java, Swing und PostgreSQL (Docker)
funktioniert auch ohne Docker, dann aber PostgreSQL installieren auf den Rechner, oder Cloud Dienst nutzen und anpassen

(Clean Code and Best Practices in Java) 

## MVC - Design Pattern

* **Model:** Die Contact Klasse in src/model/Contact.java repräsentiert das Datenmodell. 
* Sie enthält die Daten und die Logik, die mit diesen Daten verbunden ist.
*
* **View:** Die Klassen ContactGUI, AddContactDialog und EditContactDialog in src/view/ repräsentieren die Ansicht. 
* Sie sind verantwortlich für die Darstellung der Daten an den Benutzer und die Sammlung der Benutzereingaben.  
*
* **Controller:** Die ContactController Klasse in src/controller/ContactController.java repräsentiert den Controller. 
* Sie verarbeitet die Benutzereingaben, interagiert mit dem Datenmodell und aktualisiert die Ansicht entsprechend.


## DAO - Data Access Object

Die ContactDAO Schnittstelle und ihre Implementierung PostgreSQLContactDAO in src/dao/ stellen eine Abstraktionsschicht 
für den Zugriff auf die Datenquelle (in diesem Fall eine PostgreSQL-Datenbank) dar. 
Dies ist ein weiteres gängiges Designmuster, das Data Access Object (DAO) genannt wird. 
Es ist nicht Teil des MVC-Musters, aber es wird oft zusammen mit MVC in Anwendungen verwendet, 
die mit Datenbanken interagieren.