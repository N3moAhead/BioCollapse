# BioCollapse

## Program Architecture (Idea)

### Programmstruktur

```
src
└── com
    └── biocollapse
        ├── controller
        │   ├── SimulationController.java
        │   └── WindowController.java
        ├── main
        │   └── Main.java
        ├── model
        │   ├── Hospital.java
        │   ├── Human.java
        │   ├── Map.java
        │   └── Virus.java
        ├── service
        │   ├── InfectionService.java
        │   ├── MovementService.java
        │   └── SimulationService.java
        ├── util
        │   └── HelperUtils.java
        └── view
            ├── ConfigScreen.java
            ├── Homescreen.java
            ├── SimulationScreen.java
            └── StatisticsScreen.java
```

### Klassenbeschreibungen

1. **SimulationController.java**: Diese Klasse ist für die Steuerung und Koordination der Simulationslogik verantwortlich. Sie interagiert mit den Modell-Klassen und den Service-Klassen, um die Simulation auszuführen.
2. **WindowController.java**: Diese Klasse ist für das Management des grafischen Benutzerinterfaces (GUI) zuständig. Sie erstellt und verwaltet die Hauptfenster-Komponente und koordiniert die Interaktion zwischen den Ansichts-Klassen (View-Klassen) und dem Simulationscontroller.
3. **Main.java**: Diese Klasse ist der Einstiegspunkt der Anwendung. Sie erstellt eine Instanz des WindowControllers, um die Simulation zu starten.
4. **Hospital.java**, **Human.java**, **Map.java** und **Virus.java**: Diese Modell-Klassen repräsentieren die Schlüsselkomponenten der Virusausbreitungs-Simulation.
5. **InfectionService.java**, **MovementService.java** und **SimulationService.java**: Diese Service-Klassen kapseln die Logik für die Infektionsausbreitung, die Bewegung der Menschen und die Gesamtsteuerung der Simulation.
6. **HelperUtils.java**: Diese Hilfsklasse enthält nützliche Funktionen und Methoden, die von anderen Teilen der Anwendung verwendet werden können.
7. **SimulationScreen.java**: Diese Ansichts-Klasse stellt die grafische Darstellung der Simulation dar, z.B. die Visualisierung der Ausbreitung des Virus auf der Karte.
8. **ConfigScreen.java**: Diese Ansichts-Klasse stellt die Konfigurationsseite für die darauffolgende Simulation dar.
9. **StatisticsScreen.java**: Diese Ansichts-Klasse ist für die Darstellung der Statistik-Seite zuständig.
10. **HomeScreen.java** Diese Ansichts-Klasse ist für die Darstellung des Hauptmenüs zuständig.
