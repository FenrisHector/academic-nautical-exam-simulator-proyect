package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import model.Answer;
import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.User;
import model.sub.SqliteConnection;
import poiupv.Poi;

public class FXMLTrabajoController implements Initializable {

    // â€” Zoom y estructura â€”
    private Group zoomGroup;
    private final Delta dragDelta = new Delta();

    private ListView<Poi> map_listview;

    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private Pane paneCarta;

    // â€” Perfil / menÃº â€”
    @FXML
    private MenuButton mbPerfil;
    @FXML
    private MenuItem miEditarPerfil;
    @FXML
    private MenuItem menuItemEliminarMarcaTool;
    @FXML
    private MenuItem menuItemLimpiarCartaTool;

    // â€” ColorPickers y controles â€”
    @FXML
    private ColorPicker colorPickkerPunto;
    @FXML
    private ColorPicker colorPickerLinea;
    @FXML
    private ColorPicker colorPickerArco;
    @FXML
    private TextField textFieldRadioArco;
    @FXML
    private TextField textFieldTamanoTexto;
    @FXML
    private ColorPicker colorPickerTexto;
    @FXML
    private ColorPicker colorPickerEdicion;

    // â€” MenÃºs / herramientas â€”
    @FXML
    private MenuItem menuItemIniciarLinea;
    @FXML
    private MenuItem menuItemTransportador;
    @FXML
    private Menu menuMarcarPunto;
    @FXML
    private MenuItem menuItemIniciarPunto;
    @FXML
    private MenuButton mbMarcarPunto;
    @FXML
    private MenuItem menuItemMarcaCirculo;
    @FXML
    private MenuItem menuItemMarcaCruz;
    @FXML
    private MenuItem menuItemMarcaAsterisco;
    @FXML
    private MenuItem menuItemMarcaEstrella;
    @FXML
    private Menu menuTrazarLinea;
    @FXML
    private MenuItem menuGrosorLinea;
    @FXML
    private Menu menuTrazarArco;
    @FXML
    private MenuItem menuItemIniciarArco;
    @FXML
    private MenuItem menuItemRadioLibre;
    @FXML
    private MenuItem menuItemRadioFijo;
    @FXML
    private Menu menuAnotarTexto;
    @FXML
    private MenuItem menuItemAnotarTexto;
    @FXML
    private Menu menuColor;
    @FXML
    private MenuItem menuItemEditarColor;
    @FXML
    private Menu menuEliminarMarca;
    @FXML
    private Menu menuLimpiarCarta;
    @FXML
    private MenuItem btnRegla;
    @FXML
    private MenuItem menuItemLyL;
    @FXML
    private Slider sliderGrosorLinea;
    @FXML
    private Slider sliderGrosorArco;
    @FXML
    private MenuItem miCerrarAplicacion;
    @FXML
    private Menu menuHerramienta;
    @FXML
    private Menu menuAyuda;

    // â€” ImÃ¡genes/Ã­conos â€”
    @FXML
    private ImageView ivPerfil;
    @FXML
    private ImageView ivMenuMarcarPunto;
    @FXML
    private ImageView ivMenuTrazarLinea;
    @FXML
    private ImageView ivMenuTrazarArco;
    @FXML
    private ImageView ivMenuAnotarTexto;
    @FXML
    private ImageView ivMenuColor;
    @FXML
    private ImageView ivMenuEliminarMarca;
    @FXML
    private ImageView ivMenuLimpiarCarta;
    @FXML
    private ImageView ivMenuAyuda;
    @FXML
    private ImageView ivMenuHerramienta;

    // â€” Problemas â€”
    @FXML
    private TextArea taProblem;
    private Problem currentProblem;
    @FXML
    private MenuItem estadisticasButton;
    @FXML
    private Button buttonElegirProblema;
    @FXML
    private RadioButton buttonA;
    @FXML
    private ToggleGroup grupoRespuestas;
    @FXML
    private RadioButton buttonB;
    @FXML
    private RadioButton buttonC;
    @FXML
    private RadioButton buttonD;
    @FXML
    private Button buttonTerminarProblema;

    // â€” Layout raÃ­z â€”
    @FXML
    private BorderPane borderPane;

    // â€” Preview dinÃ¡mico de lÃ­nea â€”
    private Group previewLineGroup;
    private EventHandler<MouseEvent> lineDragHandler;

    // â€” HBoxes para tooltips â€”
    @FXML
    private HBox hboxMarcarPunto;
    @FXML
    private HBox hboxTrazarLinea;
    @FXML
    private HBox hboxTrazarArco;
    @FXML
    private HBox hboxAnotarTexto;
    @FXML
    private HBox hboxColor;
    @FXML
    private HBox hboxEliminarMarca;
    @FXML
    private HBox hboxLimpiarCarta;
    @FXML
    private HBox hboxHerramienta;
    @FXML
    private HBox hboxAyuda;

    // â€” Estado global herramientas â€”
    private enum Tool {
        NONE, POINT, LINE, ARC, TEXT, EDIT_COLOR, DELETE, COMPASS, RULER, PROTRACTOR, LATLON_MARKER, REGLA_VISUAL
    }

    private Tool currentTool = Tool.NONE;
    private boolean markingEnabled = false;

    // â€” Punto â€”
    private Color colorSeleccionado = Color.RED;
    private String tipoPunto = "circulo";

    // â€” LÃ­nea â€”
    private boolean lineStarted = false;
    private double lineStartX, lineStartY;
    private Color colorLinea = Color.RED;
    private double grosorLinea = 4;

    // â€” Arco â€”
    private boolean arcStarted = false;
    private double arcCenterX, arcCenterY;
    private Color colorArco = Color.RED;
    private double grosorArco = 4;
    private double radioFijo = 30;

    // â€” Texto â€”
    private double tamanoTexto = 12;
    private Color colorTexto = Color.RED;
    private String textoPendiente;

    // â€” EdiciÃ³n de color â€”
    private Color colorEdicion = Color.BLACK;

    // â€” Herramientas visuales â€”
    private Point2D compasP1, compasP2;
    private Point2D rulerP1, rulerP2;
    private ImageView ivOverlay;
    private Pane reglaContainer;
    private Group previewArcGroup;
    private EventHandler<MouseEvent> arcDragHandler;
    private Arc previewArc;
    private ImageView ivRegla;
    private Stage rotationStage;

    // â€” Modo arco â€”
    private enum ModoArco {
        RADIO_FIJO, RADIO_LIBRE
    }

    private ModoArco modoArco = ModoArco.RADIO_FIJO;

    // â€” Usuario y navegaciÃ³n â€”
    private Navigation nav;
    private String nick, email, pass;
    private Image avatar;
    private LocalDate birthday;
    private User user;

    // â€” Problemas / stats â€”
    private List<Answer> listAnswers;
    private Problem problema;
    private int indice;
    private List<model.Session> listSesions;
    private int hits;
    private int faults;

    // â€” InyecciÃ³n de datos externos â€”
    public void initUser(User u, String n, String e, String p, Image a, LocalDate dt) {
        user = u;
        nick = n;
        email = e;
        pass = p;
        avatar = a;
        birthday = dt;
        ivPerfil.imageProperty().setValue(user.getAvatar());
    }

    public void initProblema(Problem p, int i) {
        problema = p;
        indice = i;
        taProblem.setText(problema.getText());
    }

    public void initSes(int h, int f) {
        hits = h;
        faults = f;
    }

    private static class Delta {
        double x, y;
    }

    // â€”â€”â€” InicializaciÃ³n â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (taProblem == null) {
            System.out.println("TextArea 'taProblem' no ha sido inicializado correctamente.");
        } else {
            System.out.println("TextArea 'taProblem' ha sido inicializado correctamente.");
        }

        try {
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("data.db");
            System.out.println("Base de datos encontrada");
        } catch (SQLException ex) {
            System.out.println("Base de datos no encontrada");
        }

        buttonTerminarProblema.setDisable(true);

        try {
            nav = Navigation.getInstance();
        } catch (NavDAOException ex) {
            System.out.println("Nav exception");
        }

        buttonA.setVisible(false);
        buttonB.setVisible(false);
        buttonC.setVisible(false);
        buttonD.setVisible(false);

        sliderGrosorArco.setShowTickLabels(true);
        sliderGrosorArco.setShowTickMarks(true);

        // TamaÃ±os y barras del lienzo
        paneCarta.setPrefSize(2500, 1700);
        map_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        map_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        setupZoom();
        setupControls();
        setupPaneClickHandler();

        taProblem.setWrapText(true);

        // Sliders de grosor
        sliderGrosorLinea.setMin(1);
        sliderGrosorLinea.setMax(10);
        sliderGrosorLinea.setBlockIncrement(1);
        sliderGrosorLinea.setMajorTickUnit(1);
        sliderGrosorLinea.setSnapToTicks(true);
        sliderGrosorLinea.setValue(grosorLinea);
        sliderGrosorLinea.valueProperty().addListener((obs, o, nval) -> grosorLinea = nval.intValue());

        sliderGrosorArco.setMin(1);
        sliderGrosorArco.setMax(10);
        sliderGrosorArco.setBlockIncrement(1);
        sliderGrosorArco.setMajorTickUnit(1);
        sliderGrosorArco.setSnapToTicks(true);
        sliderGrosorArco.setValue(grosorArco);
        sliderGrosorArco.valueProperty().addListener((obs, o, nval) -> grosorArco = nval.intValue());

        Platform.runLater(() -> {
            colorearTickLabelsAzul(sliderGrosorLinea);
            colorearTickLabelsAzul(sliderGrosorArco);
        });

        // TextFields iniciales
        textFieldRadioArco.setText(String.format("%.0f", radioFijo));
        textFieldTamanoTexto.setText(String.format("%.0f", tamanoTexto));

        // â€” Transportador â€”
        Image rawTransportador = new Image(getClass().getResourceAsStream("/resources/transportador.png"));
        Image overlayTransportador = makeOverlayPreserveGray(rawTransportador);
        ivOverlay = new ImageView(overlayTransportador);
        ivOverlay.setMouseTransparent(false);
        ivOverlay.setPickOnBounds(true);
        ivOverlay.setVisible(false);
        paneCarta.getChildren().add(ivOverlay);

        final Delta dragDeltaOverlay = new Delta();

        ivOverlay.setOnMousePressed(e -> {
            Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            double overlayWidth = ivOverlay.getBoundsInLocal().getWidth();
            double overlayHeight = ivOverlay.getBoundsInLocal().getHeight();
            ivOverlay.setLayoutX(mouse.getX() - overlayWidth / 2);
            ivOverlay.setLayoutY(mouse.getY() - overlayHeight / 2);
            ivOverlay.setVisible(true);
            dragDeltaOverlay.x = mouse.getX() - ivOverlay.getLayoutX();
            dragDeltaOverlay.y = mouse.getY() - ivOverlay.getLayoutY();
            e.consume();
        });

        ivOverlay.setOnMouseDragged(e -> {
            Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            double candidateX = mouse.getX() - dragDeltaOverlay.x;
            double candidateY = mouse.getY() - dragDeltaOverlay.y;

            double overlayWidth = ivOverlay.getBoundsInLocal().getWidth();
            double overlayHeight = ivOverlay.getBoundsInLocal().getHeight();

            double minX = 0, minY = 0;
            double maxX = paneCarta.getWidth() - overlayWidth;
            double maxY = paneCarta.getHeight() - overlayHeight;

            candidateX = Math.max(minX, Math.min(maxX, candidateX));
            candidateY = Math.max(minY, Math.min(maxY, candidateY));

            ivOverlay.setLayoutX(candidateX);
            ivOverlay.setLayoutY(candidateY);
            e.consume();
        });

        ivOverlay.setOnMouseReleased(e -> {
            resetModes();
            markingEnabled = false;
            currentTool = Tool.NONE;
            e.consume();
        });

        // â€” Regla â€”
        Pane reglaContainer = new Pane();
        reglaContainer.setPickOnBounds(true);
        reglaContainer.setStyle("-fx-background-color: rgba(0,0,0,0);");

        Image reglaImg = new Image(getClass().getResourceAsStream("/resources/regla123.png"));
        ivRegla = new ImageView(reglaImg);
        ivRegla.setOpacity(0.85);
        ivRegla.setScaleX(0.8);
        ivRegla.setScaleY(0.8);
        ivRegla.setMouseTransparent(true);

        reglaContainer.getChildren().add(ivRegla);
        paneCarta.getChildren().add(reglaContainer);
        reglaContainer.setVisible(false);

        final Delta dragDeltaRegla = new Delta();

        reglaContainer.setOnMousePressed(e -> {
            Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            dragDeltaRegla.x = mouse.getX() - reglaContainer.getLayoutX();
            dragDeltaRegla.y = mouse.getY() - reglaContainer.getLayoutY();
            e.consume();
        });

        reglaContainer.setOnMouseDragged(event -> {
            Point2D mouse = paneCarta.sceneToLocal(event.getSceneX(), event.getSceneY());
            double candidateX = mouse.getX() - dragDeltaRegla.x;
            double candidateY = mouse.getY() - dragDeltaRegla.y;

            Bounds bounds = getBoundsAt(reglaContainer, candidateX, candidateY);

            double minX = 0, minY = 0;
            double maxX = paneCarta.getWidth();
            double maxY = paneCarta.getHeight();

            if (bounds.getMinX() < minX)
                candidateX += (minX - bounds.getMinX());
            if (bounds.getMinY() < minY)
                candidateY += (minY - bounds.getMinY());
            if (bounds.getMaxX() > maxX)
                candidateX -= (bounds.getMaxX() - maxX);
            if (bounds.getMaxY() > maxY)
                candidateY -= (bounds.getMaxY() - maxY);

            reglaContainer.setLayoutX(candidateX);
            reglaContainer.setLayoutY(candidateY);
            event.consume();
        });

        reglaContainer.setOnScroll(e -> {
            reglaContainer.setRotate(reglaContainer.getRotate() + e.getDeltaY() / 10);
            e.consume();
        });

        reglaContainer.setOnMouseReleased(e -> e.consume());

        this.reglaContainer = reglaContainer;

        // Tooltips
        instalarTooltip(hboxMarcarPunto, "Realizar marcas");
        instalarTooltip(hboxTrazarLinea, "Trazar linea");
        instalarTooltip(hboxTrazarArco, "Trazar arco");
        instalarTooltip(hboxAnotarTexto, "Escribir texto");
        instalarTooltip(hboxColor, "Cambiar de color cualquier elemento");
        instalarTooltip(hboxEliminarMarca, "Eliminar cualquier elemento");
        instalarTooltip(hboxLimpiarCarta, "Limpiar la carta de elementos");
        instalarTooltip(hboxAyuda, "Ayuda");
        instalarTooltip(hboxHerramienta, "Regla, transportador y longitud y latitud");

        // â€” Lat/Lon â€”
        paneCarta.setOnMousePressed(e -> {
            if (currentTool == Tool.LATLON_MARKER) {
                double x = e.getX(), y = e.getY();

                double ancho = 200000; // Horizontal (latitud)
                double alto = 12000; // Vertical (longitud)

                double startXH = Math.max(0, x - ancho / 2);
                double endXH = Math.min(paneCarta.getWidth(), x + ancho / 2);
                double startYV = Math.max(0, y - alto / 2);
                double endYV = Math.min(paneCarta.getHeight(), y + alto / 2);

                Line lineaHorizontal = new Line(startXH, y, endXH, y);
                Line lineaVertical = new Line(x, startYV, x, endYV);

                lineaHorizontal.setStroke(Color.RED);
                lineaVertical.setStroke(Color.RED);
                lineaHorizontal.setStrokeWidth(2);
                lineaVertical.setStrokeWidth(2);

                lineaHorizontal.setId("latlonLine");
                lineaVertical.setId("latlonLine");

                Group latLonGroup = new Group(lineaHorizontal, lineaVertical);
                latLonGroup.setId("latlonGroup");

                paneCarta.getChildren().add(latLonGroup);
            }
        });
    }

    // â€”â€”â€” Utilidades UI â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”
    private void instalarTooltip(HBox hbox, String texto) {
        Tooltip tooltip = new Tooltip(texto);
        tooltip.setShowDuration(Duration.seconds(1000));
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: normal;");
        Tooltip.install(hbox, tooltip);
    }

    private void setupZoom() {
        zoom_slider.setMin(0.2);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(0.75);

        zoom_slider.valueProperty().addListener((obs, ov, nv) -> {
            double zoomFactor = nv.doubleValue();
            zoom(zoomFactor);
        });

        Group content = new Group();
        zoomGroup = new Group();
        content.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(content);
    }

    private void zoom(double s) {
        double h = map_scrollpane.getHvalue(), v = map_scrollpane.getVvalue();
        zoomGroup.setScaleX(s);
        zoomGroup.setScaleY(s);
        map_scrollpane.setHvalue(h);
        map_scrollpane.setVvalue(v);
    }

    private void setupControls() {
        colorPickkerPunto.setOnAction(e -> colorSeleccionado = colorPickkerPunto.getValue());
        colorPickerLinea.setOnAction(e -> colorLinea = colorPickerLinea.getValue());
        colorPickerArco.setOnAction(e -> colorArco = colorPickerArco.getValue());
        colorPickerTexto.setOnAction(e -> colorTexto = colorPickerTexto.getValue());
        colorPickerEdicion.setOnAction(e -> colorEdicion = colorPickerEdicion.getValue());

        textFieldTamanoTexto.setOnAction(e -> {
            try {
                tamanoTexto = Double.parseDouble(textFieldTamanoTexto.getText());
            } catch (NumberFormatException ex) {
                textFieldTamanoTexto.setText(String.format("%.0f", tamanoTexto));
            }
        });
        textFieldTamanoTexto.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    tamanoTexto = Double.parseDouble(textFieldTamanoTexto.getText());
                } catch (NumberFormatException ex) {
                    textFieldTamanoTexto.setText(String.format("%.0f", tamanoTexto));
                }
            }
        });

        textFieldRadioArco.setOnAction(e -> {
            try {
                radioFijo = Double.parseDouble(textFieldRadioArco.getText());
            } catch (NumberFormatException ex) {
                textFieldRadioArco.setText(String.format("%.0f", radioFijo));
            }
        });
    }

    private void setupPaneClickHandler() {
        paneCarta.setOnMouseClicked(evt -> {
            if (evt.getButton() == MouseButton.SECONDARY) {
                resetModes();
                return;
            }
            if (!markingEnabled)
                return;

            double x = evt.getX(), y = evt.getY();

            switch (currentTool) {
                case POINT -> drawPoint(x, y);
                case LINE -> handleLineClick(evt);
                case ARC -> handleArcClick(evt);
                case TEXT -> handleTextClick(x, y);

                case EDIT_COLOR -> {
                    Node n = (Node) evt.getTarget();
                    if (n != paneCarta)
                        applyColor(n, colorEdicion);
                }

                case DELETE -> {
                    Node n = (Node) evt.getTarget();
                    if (n instanceof Shape || n instanceof Text) {
                        if (n.getParent() instanceof Group) {
                            paneCarta.getChildren().remove(n.getParent());
                        } else {
                            paneCarta.getChildren().remove(n);
                        }
                    }
                }

                case COMPASS -> measureCompass(x, y);

                case RULER, REGLA_VISUAL -> {
                    double clickX = evt.getX();
                    double clickY = evt.getY();

                    if (ivRegla != null) {
                        paneCarta.getChildren().remove(ivRegla);
                        ivRegla = null;
                    }

                    Image reglaImg = new Image(getClass().getResourceAsStream("/resources/regla123.png"));
                    ivRegla = new ImageView(reglaImg);

                    double escala = 1.0 / 6.0;
                    ivRegla.setFitWidth(reglaImg.getWidth() * escala);
                    ivRegla.setPreserveRatio(true);
                    ivRegla.setOpacity(1.0);
                    ivRegla.setMouseTransparent(false);
                    ivRegla.setPickOnBounds(true);

                    paneCarta.getChildren().add(ivRegla);

                    Platform.runLater(() -> {
                        ivRegla.setLayoutX(clickX - ivRegla.getBoundsInLocal().getWidth() / 2);
                        ivRegla.setLayoutY(clickY - ivRegla.getBoundsInLocal().getHeight() / 2);
                    });

                    setupReglaDragHandlers(ivRegla, paneCarta);
                    openRotationWindowNormal();
                    resetModes();
                }

                case PROTRACTOR -> {
                    ivOverlay.setLayoutX(x - ivOverlay.getImage().getWidth() / 2);
                    ivOverlay.setLayoutY(y - ivOverlay.getImage().getHeight() / 2);
                    ivOverlay.setVisible(true);
                }

                case LATLON_MARKER -> resetModes();

                default -> {
                }
            }
        });
    }

    // â€”â€”â€” Dibujo y helpers â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”
    private void drawPoint(double x, double y) {
        switch (tipoPunto) {
            case "estrella" -> {
                Text t = new Text(x - 6, y + 6, "â˜…");
                t.setFill(colorSeleccionado);
                t.setStyle("-fx-font-size: 15.4px;");
                paneCarta.getChildren().add(t);
            }
            case "asterisco" -> {
                Text t = new Text(x - 4, y + 6, "*");
                t.setFill(colorSeleccionado);
                t.setStyle("-fx-font-size: 35px;");
                paneCarta.getChildren().add(t);
            }
            case "cruz" -> {
                Text t = new Text(x - 4, y + 6, "+");
                t.setFill(colorSeleccionado);
                t.setStyle("-fx-font-size: 26.6px;");
                paneCarta.getChildren().add(t);
            }
            default -> {
                Circle c = new Circle(x, y, 5);
                c.setFill(colorSeleccionado);
                paneCarta.getChildren().add(c);
            }
        }
    }

    public void setProblem(Problem problem) {
        this.currentProblem = problem;
        mostrarEnunciado();
    }

    private void mostrarEnunciado() {
        if (currentProblem != null && taProblem != null) {
            taProblem.setText(currentProblem.getText());
        }
    }

    public void colorearTickLabelsAzul(Slider slider) {
        slider.applyCss();
        for (Node node : slider.lookupAll(".tick-label")) {
            if (node instanceof Text t) {
                t.setFill(Color.web("#0d47a1"));
            }
        }
    }

    private void handleLineClick(MouseEvent evt) {
        double x = evt.getX(), y = evt.getY();
        if (!lineStarted) {
            lineStartX = x;
            lineStartY = y;
            lineStarted = true;

            Line line = new Line(lineStartX, lineStartY, x, y);
            line.setStroke(colorLinea);
            line.setStrokeWidth(grosorLinea);

            Line h1 = new Line(lineStartX - 5, lineStartY, lineStartX + 5, lineStartY);
            Line v1 = new Line(lineStartX, lineStartY - 5, lineStartX, lineStartY + 5);
            h1.setStroke(colorLinea);
            v1.setStroke(colorLinea);

            Line h2 = new Line(x - 5, y, x + 5, y);
            Line v2 = new Line(x, y - 5, x, y + 5);
            h2.setStroke(colorLinea);
            v2.setStroke(colorLinea);

            previewLineGroup = new Group(line, h1, v1, h2, v2);
            paneCarta.getChildren().add(previewLineGroup);

            lineDragHandler = moveEvt -> {
                double ex = moveEvt.getX(), ey = moveEvt.getY();
                line.setEndX(ex);
                line.setEndY(ey);
                h2.setStartX(ex - 5);
                h2.setStartY(ey);
                h2.setEndX(ex + 5);
                h2.setEndY(ey);
                v2.setStartX(ex);
                v2.setStartY(ey - 5);
                v2.setEndX(ex);
                v2.setEndY(ey + 5);
            };
            paneCarta.addEventFilter(MouseEvent.MOUSE_MOVED, lineDragHandler);
        } else {
            paneCarta.removeEventFilter(MouseEvent.MOUSE_MOVED, lineDragHandler);
            lineStarted = false;
            resetModes();
        }
    }

    private void handleArcClick(MouseEvent evt) {
        double x = evt.getX(), y = evt.getY();

        if (modoArco == ModoArco.RADIO_FIJO) {
            if (!arcStarted) {
                arcCenterX = x;
                arcCenterY = y;
                arcStarted = true;
            } else {
                try {
                    radioFijo = Double.parseDouble(textFieldRadioArco.getText());
                } catch (NumberFormatException ex) {
                    textFieldRadioArco.setText(String.format("%.0f", radioFijo));
                }

                Arc arc = new Arc(arcCenterX, arcCenterY, radioFijo, radioFijo, 0, 180);
                arc.setType(ArcType.OPEN);
                arc.setStroke(colorArco);
                arc.setStrokeWidth(grosorArco);
                arc.setFill(Color.TRANSPARENT);

                Line h1 = new Line(arcCenterX + radioFijo - 5, arcCenterY, arcCenterX + radioFijo + 5, arcCenterY);
                Line v1 = new Line(arcCenterX + radioFijo, arcCenterY - 5, arcCenterX + radioFijo, arcCenterY + 5);
                Line h2 = new Line(arcCenterX - radioFijo - 5, arcCenterY, arcCenterX - radioFijo + 5, arcCenterY);
                Line v2 = new Line(arcCenterX - radioFijo, arcCenterY - 5, arcCenterX - radioFijo, arcCenterY + 5);
                for (Line cruz : List.of(h1, v1, h2, v2))
                    cruz.setStroke(colorArco);

                Group g = new Group(arc, h1, v1, h2, v2);
                paneCarta.getChildren().add(g);

                arcStarted = false;
                resetModes();
            }
        } else if (modoArco == ModoArco.RADIO_LIBRE) {
            handleArcLibre(evt);
        }
    }

    private void handleArcLibre(MouseEvent evt) {
        double x = evt.getX(), y = evt.getY();

        if (!arcStarted) {
            arcCenterX = x;
            arcCenterY = y;
            arcStarted = true;

            previewArc = new Arc(arcCenterX, arcCenterY, 1, 1, 0, 180);
            previewArc.setType(ArcType.OPEN);
            previewArc.setStroke(colorArco);
            previewArc.setStrokeWidth(grosorArco);
            previewArc.setFill(Color.TRANSPARENT);

            Line h1 = new Line(), v1 = new Line();
            Line h2 = new Line(), v2 = new Line();
            for (Line l : List.of(h1, v1, h2, v2))
                l.setStroke(colorArco);

            previewArcGroup = new Group(previewArc, h1, v1, h2, v2);
            paneCarta.getChildren().add(previewArcGroup);

            arcDragHandler = moveEvt -> {
                double ex = moveEvt.getX(), ey = moveEvt.getY();
                double r = Math.hypot(ex - arcCenterX, ey - arcCenterY);

                double maxRadiusX = Math.min(arcCenterX, paneCarta.getWidth() - arcCenterX);
                double maxRadiusY = Math.min(arcCenterY, paneCarta.getHeight() - arcCenterY);
                double maxRadius = Math.min(maxRadiusX, maxRadiusY);
                if (r > maxRadius)
                    r = maxRadius;

                previewArc.setRadiusX(r);
                previewArc.setRadiusY(r);

                double rightX = arcCenterX + r;
                double leftX = arcCenterX - r;
                double y0 = arcCenterY;

                h1.setStartX(rightX - 5);
                h1.setEndX(rightX + 5);
                h1.setStartY(y0);
                h1.setEndY(y0);
                v1.setStartX(rightX);
                v1.setEndX(rightX);
                v1.setStartY(y0 - 5);
                v1.setEndY(y0 + 5);

                h2.setStartX(leftX - 5);
                h2.setEndX(leftX + 5);
                h2.setStartY(y0);
                h2.setEndY(y0);
                v2.setStartX(leftX);
                v2.setEndX(leftX);
                v2.setStartY(y0 - 5);
                v2.setEndY(y0 + 5);
            };

            paneCarta.addEventFilter(MouseEvent.MOUSE_MOVED, arcDragHandler);

        } else {
            paneCarta.removeEventFilter(MouseEvent.MOUSE_MOVED, arcDragHandler);
            arcStarted = false;
            previewArc = null;
            previewArcGroup = null;
            resetModes();
        }
    }

    private void makeDraggable(Text txt) {
        final double[] d = new double[2];
        txt.setOnMousePressed(e -> {
            Point2D loc = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            d[0] = loc.getX() - txt.getX();
            d[1] = loc.getY() - txt.getY();
            e.consume();
        });
        txt.setOnMouseDragged(e -> {
            Point2D loc = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            txt.setX(loc.getX() - d[0]);
            txt.setY(loc.getY() - d[1]);
            e.consume();
        });
    }

    private Image makeOverlayPreserveGray(Image src) {
        int w = (int) src.getWidth();
        int h = (int) src.getHeight();
        javafx.scene.image.WritableImage dst = new javafx.scene.image.WritableImage(w, h);
        javafx.scene.image.PixelReader pr = src.getPixelReader();
        javafx.scene.image.PixelWriter pw = dst.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = pr.getColor(x, y);
                if (c.getOpacity() > 0.02 && c.getBrightness() < 0.95) {
                    pw.setColor(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue(), 1.0));
                } else {
                    pw.setColor(x, y, Color.TRANSPARENT);
                }
            }
        }
        return dst;
    }

    private void handleTextClick(double x, double y) {
        if (textoPendiente != null) {
            Text txt = new Text(x, y, textoPendiente);
            txt.setFill(colorTexto);
            txt.setFont(Font.font(tamanoTexto));
            makeDraggable(txt);
            paneCarta.getChildren().add(txt);
            textoPendiente = null;
        }
        resetModes();
    }

    private String toRgbString(Color c) {
        int r = (int) (c.getRed() * 255);
        int g = (int) (c.getGreen() * 255);
        int b = (int) (c.getBlue() * 255);
        return String.format("rgb(%d,%d,%d)", r, g, b);
    }

    // â€”â€”â€” Handlers @FXML â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”â€”
    @FXML
    public void seleccionarLatLonMarker(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.LATLON_MARKER;
    }

    @FXML
    private void onCerrarAplicacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaCerrarSesion.fxml"));
        Parent root = loader.load();

        CerrarSesionController ctrl = loader.getController();
        ctrl.initSesion(hits, faults);
        ctrl.initUser(user, nick, email, pass, avatar, birthday);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Cerrar SesiÃ³n");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo1.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(500);
        stage.setMinHeight(480);
        stage.show();
    }

    @FXML
    private void showPosition(MouseEvent event) {
    }

    @FXML
    private void onEstadisticas(ActionEvent event) throws IOException {
        int totalAciertos = 0;
        int totalFallos = 0;

        listSesions = user.getSessions();
        for (int i = 0; i < listSesions.size(); i++) {
            totalAciertos += listSesions.get(i).getHits();
            totalFallos += listSesions.get(i).getFaults();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Estadisticas.fxml"));
        Parent root = loader.load();
        EstadisticasController controlador2 = loader.getController();
        controlador2.initUser(nick, email, pass, avatar, birthday);
        controlador2.initEstadisticas(totalAciertos, totalFallos);

        Scene scene = new Scene(root, 650, 500);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo1.png")));
        stage.setScene(scene);
        stage.setTitle("EstadÃ­sticas");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(660);
        stage.setMinHeight(400);
        stage.show();
    }

    @FXML
    private void elegirProblemaOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/ElegirProblema.fxml"));
        Parent root = loader.load();
        ElegirProblemaController controlador2 = loader.getController();

        Scene scene = new Scene(root, 700, 370);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo1.png")));
        stage.setScene(scene);
        stage.setTitle("Elegir Problema");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(600);
        stage.setMinHeight(300);

        stage.showAndWait();

        if (controlador2.pulsadoProblema()) {
            problema = controlador2.getProblem();
            taProblem.setText(problema.getText());
            listAnswers = problema.getAnswers();

            buttonA.setText(listAnswers.get(0).getText());
            buttonB.setText(listAnswers.get(1).getText());
            buttonC.setText(listAnswers.get(2).getText());
            buttonD.setText(listAnswers.get(3).getText());

            buttonA.setVisible(true);
            buttonB.setVisible(true);
            buttonC.setVisible(true);
            buttonD.setVisible(true);
        }
        buttonTerminarProblema.setDisable(false);
    }

    @FXML
    private void terminarProblemaOnAction(ActionEvent event) {
        RadioButton seleccionado = (grupoRespuestas.getSelectedToggle() instanceof RadioButton rb) ? rb : null;

        listAnswers = problema.getAnswers();

        if (seleccionado != null) {
            String textoSeleccionado = seleccionado.getText();
            int opcionSeleccionada = -1;

            for (int i = 0; i < listAnswers.size(); i++) {
                if (listAnswers.get(i).getText().equals(textoSeleccionado)) {
                    opcionSeleccionada = i;
                    break;
                }
            }

            if (opcionSeleccionada != -1) {
                boolean esCorrecta = listAnswers.get(opcionSeleccionada).getValidity();

                if (esCorrecta) {
                    taProblem.setText("");
                    buttonA.setVisible(false);
                    buttonB.setVisible(false);
                    buttonC.setVisible(false);
                    buttonD.setVisible(false);

                    hits++;
                    listAnswers = null;

                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Respuesta");
                    alerta.setHeaderText("ACIERTO!");
                    alerta.setContentText("Pulsa aceptar para continuar haciendo ejercicios");
                    alerta.show();
                } else {
                    faults++;
                }
            } else {
                System.out.println("Respuesta seleccionada no encontrada en la lista");
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("Â¡Error!");
            alerta.setContentText("No has seleccionado nignuna respuesta");
            alerta.show();
        }
    }

    @FXML
    public void seleccionarHerramientaMarcarPunto(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.POINT;
    }

    @FXML
    public void seleccionarTrazarLinea(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.LINE;
    }

    @FXML
    public void seleccionarTrazarArco(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.ARC;
    }

    @FXML
    public void onMenuColorPicked(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.EDIT_COLOR;
        colorPickerEdicion.show();
    }

    @FXML
    public void seleccionarEliminarElemento(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.DELETE;
    }

    @FXML
    public void seleccionarLimpiarCarta(ActionEvent e) {
        paneCarta.getChildren().removeIf(n -> n instanceof Shape || n instanceof Text || n instanceof Group);
        resetModes();
    }

    public void seleccionarGrosorLinea(ActionEvent e) {
        grosorLinea = sliderGrosorLinea.getValue();
    }

    public void seleccionarGrosorArco(ActionEvent e) {
        grosorArco = sliderGrosorArco.getValue();
    }

    public void seleccionarGrosorMuyGruesoLinea(ActionEvent e) {
        grosorLinea = 6;
    }

    public void seleccionarGrosorGruesoLinea(ActionEvent e) {
        grosorLinea = 4;
    }

    public void seleccionarGrosorMedioLinea(ActionEvent e) {
        grosorLinea = 2;
    }

    public void seleccionarGrosorFinoLinea(ActionEvent e) {
        grosorLinea = 1;
    }

    public void seleccionarGrosorMuyGruesoArco(ActionEvent e) {
        grosorArco = 6;
    }

    public void seleccionarGrosorGruesoArco(ActionEvent e) {
        grosorArco = 4;
    }

    public void seleccionarGrosorMedioArco(ActionEvent e) {
        grosorArco = 2;
    }

    public void seleccionarGrosorFinoArco(ActionEvent e) {
        grosorArco = 1;
    }

    public void seleccionarCompas(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.COMPASS;
    }

    @FXML
    public void seleccionarRegla(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.RULER;
    }

    @FXML
    public void seleccionarTransportador(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.PROTRACTOR;
        ivOverlay.setVisible(true);
    }

    @FXML
    public void onEditarPerfil(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PerfilPanel.fxml"));
        Parent root = loader.load();
        Stage dialog = new Stage();
        dialog.setTitle("Editar perfil");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo1.png")));
        dialog.initOwner(paneCarta.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);

        PerfilPanelController controlador2 = loader.getController();
        controlador2.initUser(nick, email, pass, avatar, birthday);
        controlador2.mostrarinfo(nick, email, pass, avatar, birthday);

        dialog.setScene(new Scene(root));
        dialog.setMinWidth(500);
        dialog.setMinHeight(400);
        dialog.showAndWait();

        if (controlador2.pulsadoGuardar()) {
            User us = controlador2.getUser();
            Image res = us.getAvatar();
            user = us;
            ivPerfil.setImage(res);
        }
    }

    @FXML
    public void seleccionarAnotarTexto(ActionEvent e) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Anotar texto");
        dlg.setHeaderText("Introduce el texto:");
        dlg.getEditor().setPromptText("Tu texto aquÃ­");

        Optional<String> res = dlg.showAndWait();
        res.ifPresent(s -> {
            textoPendiente = s;
            resetModes();
            markingEnabled = true;
            currentTool = Tool.TEXT;
        });
    }

    private void listClicked(MouseEvent e) {
        Poi poi = map_listview.getSelectionModel().getSelectedItem();
        if (poi == null)
            return;

        double contentWidth = zoomGroup.getBoundsInLocal().getWidth() * zoom_slider.getValue();
        double contentHeight = zoomGroup.getBoundsInLocal().getHeight() * zoom_slider.getValue();
        double viewportW = map_scrollpane.getViewportBounds().getWidth();
        double viewportH = map_scrollpane.getViewportBounds().getHeight();

        double h = (poi.getX() * zoom_slider.getValue() - viewportW / 2) / (contentWidth - viewportW);
        double v = (poi.getY() * zoom_slider.getValue() - viewportH / 2) / (contentHeight - viewportH);

        h = Math.max(0, Math.min(1, h));
        v = Math.max(0, Math.min(1, v));

        map_scrollpane.setHvalue(h);
        map_scrollpane.setVvalue(v);
    }

    @FXML
    private void addPoi(MouseEvent e) {
        if (!e.isControlDown())
            return;

        Dialog<Poi> dlg = new Dialog<>();
        dlg.setTitle("Nuevo POI");
        dlg.setHeaderText("Introduce un nuevo POI");

        Stage ds = (Stage) dlg.getDialogPane().getScene().getWindow();
        ds.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

        ButtonType ok = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        TextField name = new TextField();
        name.setPromptText("Nombre del POI");
        TextArea desc = new TextArea();
        desc.setPromptText("DescripciÃ³n...");
        desc.setWrapText(true);
        desc.setPrefRowCount(5);

        dlg.getDialogPane().setContent(new javafx.scene.layout.VBox(10,
                new Label("Nombre:"), name,
                new Label("DescripciÃ³n:"), desc));

        dlg.setResultConverter(btn -> btn == ok ? new Poi(name.getText().trim(), desc.getText().trim(), 0, 0) : null);

        Optional<Poi> res = dlg.showAndWait();
        res.ifPresent(poi -> {
            Point2D lp = zoomGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
            poi.setPosition(lp);
            map_listview.getItems().add(poi);
        });
    }

    @FXML
    public void seleccionarMarcaCirculo(ActionEvent e) {
        tipoPunto = "circulo";
    }

    @FXML
    public void seleccionarMarcaEstrella(ActionEvent e) {
        tipoPunto = "estrella";
    }

    @FXML
    public void seleccionarMarcaAsterisco(ActionEvent e) {
        tipoPunto = "asterisco";
    }

    @FXML
    public void seleccionarMarcaCruz(ActionEvent e) {
        tipoPunto = "cruz";
    }

    @FXML
    public void seleccionarEditarColor(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.EDIT_COLOR;
        colorPickerEdicion.show();
    }

    @FXML
    private void about(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("IPC - 2025");
        alert.setWidth(600);
        alert.setHeight(600);
        alert.setContentText(
                "Al iniciar un elemento click izquierdo para aÃ±adir ese elemento y click derecho para cancelar.\n" +
                        "Si no se realiza un problema y se cierra sesiÃ³n las estadÃ­sticas no se actualizarÃ¡n.");
        alert.show();
    }

    @FXML
    public void zoomIn(ActionEvent e) {
        zoom_slider.setValue(zoom_slider.getValue() + 0.1);
    }

    @FXML
    public void zoomOut(ActionEvent e) {
        zoom_slider.setValue(zoom_slider.getValue() - 0.1);
    }

    private void resetModes() {
        currentTool = Tool.NONE;
        markingEnabled = false;
        lineStarted = false;
        arcStarted = false;
        compasP1 = compasP2 = rulerP1 = rulerP2 = null;
    }

    @FXML
    public void seleccionarArcoRadioFijo(ActionEvent e) {
        modoArco = ModoArco.RADIO_FIJO;
        seleccionarTrazarArco(e);
    }

    @FXML
    public void seleccionarArcoRadioLibre(ActionEvent e) {
        modoArco = ModoArco.RADIO_LIBRE;
        seleccionarTrazarArco(e);
    }

    private void openRotationWindowNormal() {
        try {
            if (rotationStage == null || !rotationStage.isShowing()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/RotarRegla.fxml"));
                Parent root = loader.load();

                rotationStage = new Stage();
                rotationStage.setTitle("Rotar Regla");
                rotationStage.setScene(new Scene(root));
                rotationStage.setAlwaysOnTop(true);
                rotationStage.initOwner(paneCarta.getScene().getWindow());

                RotarReglaController rotationController = loader.getController();
                rotationController.setRuleImageView(ivRegla);

                rotationStage.show();
            } else {
                rotationStage.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupReglaDragHandlers(ImageView ivRegla, Pane paneCarta) {
        ivRegla.setOnMousePressed(event -> {
            Point2D mouse = paneCarta.sceneToLocal(event.getSceneX(), event.getSceneY());
            dragDelta.x = mouse.getX() - ivRegla.getLayoutX();
            dragDelta.y = mouse.getY() - ivRegla.getLayoutY();
            event.consume();
        });

        ivRegla.setOnMouseDragged(event -> {
            Point2D mouse = paneCarta.sceneToLocal(event.getSceneX(), event.getSceneY());
            double candidateX = mouse.getX() - dragDelta.x;
            double candidateY = mouse.getY() - dragDelta.y;

            ivRegla.setLayoutX(candidateX);
            ivRegla.setLayoutY(candidateY);

            Bounds bounds = ivRegla.getBoundsInParent();

            double limitedX = candidateX;
            double limitedY = candidateY;

            if (bounds.getMinX() < 0)
                limitedX += (0 - bounds.getMinX());
            if (bounds.getMinY() < 0)
                limitedY += (0 - bounds.getMinY());
            if (bounds.getMaxX() > paneCarta.getWidth())
                limitedX -= (bounds.getMaxX() - paneCarta.getWidth());
            if (bounds.getMaxY() > paneCarta.getHeight())
                limitedY -= (bounds.getMaxY() - paneCarta.getHeight());

            ivRegla.setLayoutX(limitedX);
            ivRegla.setLayoutY(limitedY);

            event.consume();
        });

        ivRegla.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                ivRegla.setVisible(false);
                resetModes();
                markingEnabled = false;
                currentTool = Tool.NONE;
                event.consume();
            }
        });
    }

    private Bounds getBoundsAt(Node node, double layoutX, double layoutY) {
        double oldX = node.getLayoutX();
        double oldY = node.getLayoutY();
        node.setLayoutX(layoutX);
        node.setLayoutY(layoutY);
        Bounds bounds = node.getBoundsInParent();
        node.setLayoutX(oldX);
        node.setLayoutY(oldY);
        return bounds;
    }

    private void applyColor(Node n, Color c) {
        if (n.getParent() instanceof Group g) {
            for (Node hijo : g.getChildren()) {
                if (hijo instanceof Arc arc) {
                    arc.setStroke(c);
                    arc.setFill(Color.TRANSPARENT);
                } else if (hijo instanceof Shape s) {
                    s.setStroke(c);
                    s.setFill(c);
                } else if (hijo instanceof Text t) {
                    t.setFill(c);
                }
            }
        } else {
            if (n instanceof Arc arc) {
                arc.setStroke(c);
                arc.setFill(Color.TRANSPARENT);
            } else if (n instanceof Shape s) {
                s.setStroke(c);
                s.setFill(c);
            } else if (n instanceof Text t) {
                t.setFill(c);
            }
        }
    }

    private void measureCompass(double x, double y) {
        if (compasP1 == null) {
            compasP1 = new Point2D(x, y);
        } else {
            compasP2 = new Point2D(x, y);
            double d = compasP1.distance(compasP2);
            Line l = new Line(compasP1.getX(), compasP1.getY(), compasP2.getX(), compasP2.getY());
            paneCarta.getChildren().add(l);
            Text t = new Text((compasP1.getX() + compasP2.getX()) / 2, (compasP1.getY() + compasP2.getY()) / 2,
                    String.format("%.1f", d));
            makeDraggable(t);
            paneCarta.getChildren().add(t);
            compasP1 = compasP2 = null;
        }
    }

    private void colocarRegla(double x, double y) {
        if (ivRegla != null && paneCarta.getChildren().contains(ivRegla)) {
            paneCarta.getChildren().remove(ivRegla);
        }
        Image reglaImg = new Image(getClass().getResourceAsStream("/resources/regla123.png"));
        ivRegla = new ImageView(reglaImg);
        ivRegla.setOpacity(0.85);
        ivRegla.setScaleX(0.8);
        ivRegla.setScaleY(0.8);
        ivRegla.setMouseTransparent(false);
        ivRegla.setPickOnBounds(true);
        ivRegla.setLayoutX(x - ivRegla.getBoundsInLocal().getWidth() / 2);
        ivRegla.setLayoutY(y - ivRegla.getBoundsInLocal().getHeight() / 2);
        paneCarta.getChildren().add(ivRegla);
        setupReglaDragHandlers(ivRegla, paneCarta);
    }

    private void cerrarAplicacion(ActionEvent event) {
        if (rotationStage != null)
            rotationStage.close();
        Stage mainStage = (Stage) paneCarta.getScene().getWindow();
        mainStage.close();
    }
}


