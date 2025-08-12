# Camunda 7 Prozess Instanz Migration

https://docs.camunda.org/manual/latest/user-guide/process-engine/process-instance-migration/
https://docs.camunda.io/docs/components/best-practices/operations/versioning-process-definitions/
https://docs.camunda.org/rest/camunda-bpm-platform/7.24-SNAPSHOT/#tag/Migration

## Kontext

Wann immer Änderungen am Prozess vorgenommen wurden, wird dieser als neue Version in Camunda deployed. 
Bestehende Instanzen dieses Prozesses laufen in der Version weiter, in der sie gestartet wurden. 
Neue Prozesse werden mit der neuesten Version gestartet. 
Ein Migrationsplan kann über die Camunda Schnittstellen erstellt werden (sowohl über Rest, als auch über direkt über Java Beans)

## Überlegungen

**Laufende Instanzen sollten auf die neueste Prozessdefinition migriert werden, wenn:**

- Fehlerbehebungen oder Patches für das bestehende Modell notwendig sind.
- Betriebskomplexität durch parallele Modellversionen vermieden werden soll (z.B. zur Reduktion von Support- und Wartungsaufwand).
    - Bei Änderungen der Schnittstellen
- Sich die Business-Logik geändert hat oder es rechtliche Gründe dafür gibt

**Eine parallele Ausführung mehrerer Versionen kann sinnvoll sein, wenn:**

- es sich um Entwicklungs- oder Testumgebungen handelt, in denen alte Instanzen keine Rolle spielen,
- bestehende Instanzen aus rechtlichen oder fachlichen Gründen exakt mit ihrer ursprünglichen Modellversion abgeschlossen werden müssen,
- die Migration aufgrund der Komplexität, des Aufwands oder eines geringen Nutzens nicht wirtschaftlich ist,
- Prozessinstanzen eine sehr kurze Lebensdauer haben (z.B. < 1 Tag) und somit bald natürlich auslaufen.
- Es rechtliche Gründe dafür gibt

Ziel ist es, **Risiken zu minimieren, Stabilität zu gewährleisten** und gleichzeitig eine flexible Weiterentwicklung der Prozesse zu ermöglichen.

## Best Practices

- Vermeide es davon abhängige Ressourcen zu versionieren (Implementierungen)
- Je größer das Prozessmodell wird, desto aufwendiger wird die Migration.
    - Nutze Call-Activities bei komplexen Prozessen (→ Austauschen von Teilen)
    - [Schließe Prozess Instanzen schneller ab](https://docs.camunda.io/docs/components/best-practices/operations/versioning-process-definitions/#cutting-very-long-running-processes-into-pieces)
        - Anstatt bspw. auf eine Vertragsaktualisierung in x Monaten zu warten
        - Lege über einen Service-Task einen Reminder an, der Periodisch von einem zweiten Prozess abgefragt wird
- Dump von der Prod-Datenbank ziehen und die Migration lokal testen (Testdaten decken das in der Regel nie zu 100% ab)