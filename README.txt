# Academic Nautical Exam Simulator (JavaFX)
Proyecto de práctica y simulación de ejercicios de carta náutica en **Java + JavaFX** con herramientas interactivas (puntos, líneas, arcos, texto, transportador y regla) y un módulo de **test** con seguimiento de aciertos/fallos.

## Descripción
Este proyecto permite trabajar sobre una carta náutica como si fuera un cuaderno de ejercicios: marcar puntos con distintos símbolos, trazar líneas y arcos (radio fijo o libre), anotar texto, usar transportador y regla con rotación, y limpiar/editar elementos.

Se realizó en equipo por Héctor Zamorano García junto con dos compañeros. El desarrollo fue colaborativo: diseño de vistas en Scene Builder, controladores JavaFX y lógica de

prácticas/estadísticas.
Incluye una interfaz para **elegir problemas** (enunciado + 4 respuestas), validar la solución y **guardar resultados** para consultar estadísticas por usuario.

El enfoque fue construir una experiencia de práctica fluida con JavaFX (FXML + controladores), gestionar usuarios/sesiones en **SQLite** y mantener el código claro. Se usó apoyo puntual de autocompletado/IA (Copilot) para sugerencias, pero las decisiones y la depuración fueron manuales.

## Objetivos
- Practicar conceptos básicos de **navegación en carta** con herramientas visuales.
- Resolver **problemas tipo test** y registrar **aciertos/fallos** por sesión.
- Permitir **edición de perfil** (avatar, email, contraseña, fecha).
- Mantener una **experiencia sencilla**: zoom, arrastre, rotación de regla, transportador, limpieza rápida del lienzo.

## Datos
- **Base de datos**: SQLite (`data.db` en la raíz del proyecto).
- Entidades manejadas en `model/` (por ejemplo: `User`, `Problem`, `Answer`, `Session`).
- Si no subes la BD real, añade `data.example.db` y en local cópiala/renómbrala a `data.db`.

## Variables / Recursos
- Recursos gráficos en `resources/` (iconos, regla, transportador, css).
- Vistas FXML en `vista/` (Scene Builder).

## Metodología
1. **Arquitectura JavaFX** con FXML + controladores por vista.
2. **Lienzo** en `ScrollPane` con `Group` para **zoom** continuo.
3. **Herramientas** gestionadas por un `enum` y manejadores de ratón (primer/segundo clic, previsualizaciones).
4. **Persistencia** en SQLite para usuarios, sesiones y problemas.
5. **UI** con Scene Builder, diálogos modales y tooltips.

## Validación / Pruebas con usuarios
Realizamos pruebas informales de uso con familiares y amigos (≈ **50** personas entre los tres autores). Buscábamos fricción en la interacción: entender herramientas, rotación de la regla, limpieza del lienzo y flujo de problemas. A partir del feedback hicimos pequeños ajustes de UX (previsualización de línea/arco, tooltips más descriptivos y ventana de rotación “always on top”).
No se recogieron datos personales; fueron sesiones locales y cualitativas.

## Funcionalidades principales
- **Dibujo:** puntos (círculo/cruz/asterisco/estrella), líneas con previsualización y **grosor** ajustable, arcos (radio **fijo** o **libre**) con previsualización, anotación de **texto** arrastrable.
- **Edición:** cambio de **color**, **eliminar** elementos, **limpiar** la carta.
- **Instrumental:** **transportador** arrastrable (overlay), **regla** con arrastre y **rotación** (ventana auxiliar), marcador visual **Lat/Lon**.
- **Problemas y resultados:** elección de problema, validación de respuesta, contador de **aciertos/fallos**.
- **Estadísticas:** totales acumulados por usuario.
- **Perfil:** edición de email, contraseña, fecha de nacimiento y **avatar** (archivo o predeterminado).

## Estructura del repositorio
POI_UPV/
├── src/
│ ├── javafxmlapplication/ # controladores JavaFX
│ │ ├── FXMLTrabajoController.java
│ │ ├── FXMLAutenticarseController.java
│ │ ├── FXMLRegistrarseController.java
│ │ ├── CerrarSesionController.java
│ │ ├── CerrarAplicacionController.java
│ │ ├── ElegirProblemaController.java
│ │ └── EstadisticasController.java
│ └── poiupv/
│ ├── PoiUPVApp.java # main
│ └── Poi.java
├── vista/ # FXML (Scene Builder)
│ ├── VentanaAutenticacion.fxml
│ ├── VentanaRegistro.fxml
│ ├── FXMLTrabajo.fxml
│ ├── PerfilPanel.fxml
│ ├── RotarRegla.fxml
│ ├── ElegirProblema.fxml
│ └── Estadisticas.fxml
├── resources/ # imágenes / css
│ ├── regla123.png
│ ├── transportador.png
│ ├── logo1.png
│ └── estiloTrabajo.css
├── lib/lib/ # librerías externas (sqlite-jdbc, etc.)
├── data.db # base de datos (ver nota)
├── build.xml, manifest.mf # proyecto Ant (opcional conservar)
└── README.md


## Tecnologías
- **Java 17+**
- **JavaFX** (FXML + controles)
- **SQLite** (sqlite-jdbc)
- **VS Code** (Extension Pack for Java) / NetBeans (opcional)
- **Git / GitHub**

## Ejecución
**VS Code**:
1. Abrir la carpeta del proyecto.
2. Instalar **Extension Pack for Java**.
3. Asegurar Java 17+.
4. Ejecutar `poiupv.PoiUPVApp` (botón *Run* sobre el `main`).
5. Si VS Code no detecta las librerías, usar `.vscode/settings.json`:
   ```json
   { "java.project.referencedLibraries": ["lib/lib/**/*.jar"] }

AR (si lo generas con Ant/NetBeans):
java -jar dist/POI_UPV_Bueno.jar

## Créditos / Autores
- **Héctor Zamorano García** (coautor). GitHub: [@FenrisHector](https://github.com/FenrisHector)
- **Unai Soler Gómez** (coautor)
- **Jordi Encabo Bataller** (coautor)

## Trabajo futuro / Mejoras
- **Arquitectura (refactor pendiente):** `FXMLTrabajoController` actúa como “god class”. El plan es extraer la lógica de cada herramienta a componentes dedicados (p. ej., `ToolPoint`, `ToolLine`, `ToolArc`, `ToolText`, `ToolColor`, `ToolEraser`, `ToolRuler`, `ToolProtractor`) con interfaces comunes (inicializar, manejar clic/drag, limpiar estado). Beneficios: **SRP**, menor acoplamiento, pruebas unitarias por herramienta, y facilidad para añadir/activar/desactivar herramientas sin tocar el controlador principal. Este refactor está identificado y pendiente de ejecución.

- **Tests:** actualmente no hay una batería formal de pruebas. Próximo paso: añadir **tests unitarios** para el modelo (`User`, `Problem`, `Answer`, `Session`), **tests de integración** de la capa SQLite con dobles/mocks y, si es posible, **tests UI** con TestFX (interacciones básicas: selección de herramienta, trazado y borrado, validación de respuesta). Objetivo: asegurar regresiones cero tras el refactor y documentar la cobertura en CI.

