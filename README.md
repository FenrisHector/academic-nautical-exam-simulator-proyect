# Academic Nautical Exam Simulator (JavaFX)

**Practice and simulation project for nautical chart exercises developed in Java + JavaFX with interactive tools and evaluation module.**

## Description

This project allows working on a digital nautical chart simulating the experience of a real exercise workbook. The application offers a suite of drawing tools (points, lines, arcs, text) and navigation instruments (protractor and rotating ruler) to solve coastal navigation problems.

The development was carried out as a team together with two colleagues (**Unai Soler** and **Jordi Encabo**). The work was collaborative, covering everything from view design in Scene Builder to controller logic and data persistence. The main goal was to build a smooth practice experience using the **JavaFX architecture (FXML + Controllers)** and manage users and sessions through **SQLite**.

An interface for solving multiple-choice questions (statement + 4 options), solution validation, and storage of correct/incorrect statistics was implemented. During development, occasional AI assistance (Copilot) was used for syntax suggestions, but the architecture, business logic, and debugging were all done manually.

## Objectives

* **Chart Simulation:** Practice basic navigation concepts using precise visual tools on a digital canvas.
* **Evaluation Module:** Solve multiple-choice problems and automatically record correct and incorrect answers per session.
* **User Management:** Allow creation, authentication, and editing of profiles (avatar, credentials, personal data).
* **User Experience (UX):** Maintain an intuitive interface with zoom, panning, instrument rotation, and quick workspace clearing.

## Main Features

* **Technical Drawing:** Plotting of points (circle, cross, asterisk), lines with adjustable thickness, and arcs (fixed or free radius) with real-time preview.
* **Editing:** Selection tools, color change, individual erase, and full canvas cleaning.
* **Instruments:**
    * **Protractor:** Draggable with transparent overlay.
    * **Ruler:** Auxiliary window with rotation and drag controls.
    * **Coordinates:** Visual marker for Latitude/Longitude.
* **Problem System:** Random or manual selection of exercises, immediate validation, and visual feedback.
* **Statistics:** Panel to view accumulated results per user.

## Data and Resources

* **Database:** SQLite (`data.db` in the root). Manages the entities: `User`, `Problem`, `Answer`, `Session`.
* **Resources:** Icons, instrument images (ruler, protractor), and CSS stylesheets located in `resources/`.
* **Views:** FXML files designed with Scene Builder located in `vista/`.

> **Note:** If the real database is not included in the repository, a `data.example.db` is provided. Rename it to `data.db` to launch the application.

## Methodology

### 1. JavaFX Architecture
Implementation of the MVC pattern using FXML for the view and Java Classes for control (`Controllers`). Clear separation between the graphical interface and the data model.

### 2. Canvas Management
Use of a `ScrollPane` containing a `Group` to allow continuous zoom and smooth navigation over the high-resolution nautical chart image.

### 3. Tool Logic
State management using an `enum` to control mouse behavior (first click, drag, second click) and stroke previews.

### 4. User Validation
Informal usability tests were conducted with a control group of approx. **50 people** (local environment). The feedback helped refine the UX:
* Improved preview of lines and arcs.
* More descriptive tooltips on buttons.
* Adjustment of the ruler rotation window to *"Always on Top"* mode.

## Repository Structure

```text
POI_UPV/
├── src/
│   ├── javafxmlapplication/       # UI Controllers
│   │   ├── FXMLTrabajoController.java
│   │   ├── FXMLAutenticarseController.java
│   │   ├── EstadisticasController.java
│   │   └── ...
│   └── poiupv/                    # Main and startup logic
│       ├── PoiUPVApp.java
│       ├── Poi.java
│       └── ...
├── vista/                         # FXML Files (Scene Builder)
│   ├── FXMLTrabajo.fxml
│   ├── VentanaRegistro.fxml
│   └── ...
├── resources/                     # Graphic assets and CSS
│   ├── regla123.png
│   ├── transportador.png
│   ├── estiloTrabajo.css
│   └── ...
├── lib/lib/                       # Libraries (sqlite-jdbc)
├── data.db                        # SQLite Database
└── README.md                      # Documentation
