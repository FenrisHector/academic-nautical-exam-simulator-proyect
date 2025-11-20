# Academic Nautical Exam Simulator (JavaFX)

**Proyecto de práctica y simulación de ejercicios de carta náutica desarrollado en Java + JavaFX con herramientas interactivas y módulo de evaluación.**

## Descripción

Este proyecto permite trabajar sobre una carta náutica digital simulando la experiencia de un cuaderno de ejercicios real. La aplicación ofrece una suite de herramientas de dibujo (puntos, líneas, arcos, texto) e instrumental de navegación (transportador y regla rotatoria) para resolver problemas de navegación costera.

El desarrollo fue realizado en equipo junto a dos compañeros (**Unai Soler** y **Jordi Encabo**). El trabajo fue colaborativo, abarcando desde el diseño de vistas en Scene Builder hasta la lógica de los controladores y la persistencia de datos. El objetivo principal fue construir una experiencia de práctica fluida utilizando la arquitectura **JavaFX (FXML + Controladores)** y gestionar usuarios y sesiones mediante **SQLite**.

Se implementó una interfaz para la resolución de problemas tipo test (enunciado + 4 opciones), validación de soluciones y almacenamiento de estadísticas de aciertos y fallos. Durante el desarrollo se utilizó soporte puntual de inteligencia artificial (Copilot) para sugerencias de sintaxis, pero la arquitectura, la lógica de negocio y la depuración fueron realizadas manualmente.

## Objetivos

* **Simulación de Carta:** Practicar conceptos básicos de navegación utilizando herramientas visuales precisas sobre un lienzo digital.
* **Módulo de Evaluación:** Resolver problemas tipo test y registrar automáticamente los aciertos y fallos por sesión.
* **Gestión de Usuarios:** Permitir la creación, autenticación y edición de perfiles (avatar, credenciales, datos personales).
* **Experiencia de Usuario (UX):** Mantener una interfaz intuitiva con funcionalidades de zoom, arrastre (*pan*), rotación de instrumentos y limpieza rápida del área de trabajo.

## Funcionalidades Principales

* **Dibujo Técnico:** Trazado de puntos (círculo, cruz, asterisco), líneas con grosor ajustable y arcos (radio fijo o libre) con previsualización en tiempo real.
* **Edición:** Herramientas de selección, cambio de color, borrado individual y limpieza total del lienzo.
* **Instrumental:**
    * **Transportador:** Arrastrable con overlay transparente.
    * **Regla:** Ventana auxiliar con controles de rotación y arrastre.
    * **Coordenadas:** Marcador visual de Latitud/Longitud.
* **Sistema de Problemas:** Selección aleatoria o manual de ejercicios, validación inmediata y *feedback* visual.
* **Estadísticas:** Panel de consulta de resultados acumulados por usuario.

## Datos y Recursos

* **Base de Datos:** SQLite (`data.db` en la raíz). Gestiona las entidades: `User`, `Problem`, `Answer`, `Session`.
* **Recursos:** Iconos, imágenes de instrumental (regla, transportador) y hojas de estilo CSS ubicados en `resources/`.
* **Vistas:** Archivos FXML diseñados con Scene Builder ubicados en `vista/`.

> **Nota:** Si no se incluye la base de datos real en el repositorio, se proporciona un `data.example.db`. Renómbralo a `data.db` para iniciar la aplicación.

## Metodología

### 1. Arquitectura JavaFX
Implementación del patrón MVC mediante FXML para la vista y Clases Java para el control (`Controllers`). Separación lógica entre la interfaz gráfica y el modelo de datos.

### 2. Gestión del Lienzo (Canvas)
Uso de un `ScrollPane` conteniendo un `Group` para permitir un zoom continuo y navegación fluida sobre la imagen de la carta náutica de alta resolución.

### 3. Lógica de Herramientas
Gestión de estados mediante un `enum` para controlar el comportamiento del ratón (primer clic, arrastre, segundo clic) y las previsualizaciones de trazo.

### 4. Validación con Usuarios
Se realizaron pruebas informales de usabilidad con un grupo de control de aprox. **50 personas** (entorno local). El feedback permitió refinar la UX:
* Mejora en la previsualización de líneas y arcos.
* Tooltips más descriptivos en los botones.
* Ajuste de la ventana de rotación de la regla a modo *"Always on Top"*.

## Estructura del repositorio

```text
POI_UPV/
├── src/
│   ├── javafxmlapplication/       # Controladores de la UI
│   │   ├── FXMLTrabajoController.java
│   │   ├── FXMLAutenticarseController.java
│   │   ├── EstadisticasController.java
│   │   └── ...
│   └── poiupv/                    # Main y lógica de arranque
│       ├── PoiUPVApp.java
│       ├── Poi.java
│       └── ...
├── vista/                         # Archivos FXML (Scene Builder)
│   ├── FXMLTrabajo.fxml
│   ├── VentanaRegistro.fxml
│   └── ...
├── resources/                     # Assets gráficos y CSS
│   ├── regla123.png
│   ├── transportador.png
│   ├── estiloTrabajo.css
│   └── ...
├── lib/lib/                       # Librerías (sqlite-jdbc)
├── data.db                        # Base de datos SQLite
└── README.md                      # Documentación
