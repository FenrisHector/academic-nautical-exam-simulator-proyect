package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.User;
import model.sub.SqliteConnection;

public class ElegirProblemaController implements Initializable {

    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;

    @FXML
    private Label labelProblemas;
    @FXML
    private Button botonModoAleatorio;
    @FXML
    private ListView<String> listaProblemas;

    private javafx.scene.control.MenuButton mbPerfil; // se usa en onCerrarSesion

    private List<Problem> list;
    private ObservableList<Problem> ol;
    private Navigation nav;
    private User user;

    private Problem selectedProblem;
    private int indice;
    private int hits;
    private int faults;
    private boolean problemaElegido;

    private ImageView ivPerfil;

    public void initUser(User u, String n, String e, String p, Image a, LocalDate dt) {
        user = u;
        nick = n;
        email = e;
        pass = p;
        avatar = a;
        birthday = dt;
        System.out.println(user.toString() + "Elegir Problema");
    }

    public void initSes(int h, int f) {
        hits = h;
        faults = f;
    }

    public boolean pulsadoProblema() {
        return problemaElegido;
    }

    public Problem getProblem() {
        return selectedProblem;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nav = Navigation.getInstance();
            user = nav.authenticate(nick, pass);
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("data.db");
            System.out.println("Base de datos encontrada");
        } catch (NavDAOException ex) {
            Logger.getLogger(ElegirProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ElegirProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        problemaElegido = false;
        list = nav.getProblems();
        ol = FXCollections.observableList(list);

        listaProblemas.getItems().addAll(
                "Problema 1", "Problema 2", "Problema 3", "Problema 4", "Problema 5", "Problema 6",
                "Problema 7", "Problema 8", "Problema 9", "Problema 10", "Problema 11", "Problema 12",
                "Problema 13", "Problema 14", "Problema 15", "Problema 16", "Problema 17", "Problema 18");

        listaProblemas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                indice = listaProblemas.getSelectionModel().getSelectedIndex();
                selectedProblem = ol.get(indice);
                if (selectedProblem != null) {
                    problemaElegido = true;
                    listaProblemas.getScene().getWindow().hide();
                }
            }
        });
    }

    private void onCerrarSesion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaCerrarSesion.fxml"));
        Parent root = loader.load();

        CerrarSesionController ctrl = loader.getController();
        // Pasamos el Stage principal (ventana que queremos cerrar)
        Stage mainStage = (Stage) mbPerfil.getScene().getWindow();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.setTitle("Cerrar SesiÃ³n");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void onInicioButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 650, 500));
        stage.setTitle("Pantalla de Inicio");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.show();
    }

    private void onEditarPerfil(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PerfilPanel.fxml"));
        Parent root = loader.load();
        Stage dialog = new Stage();
        dialog.setTitle("Editar perfil");

        dialog.initModality(Modality.APPLICATION_MODAL);
        PerfilPanelController controlador2 = loader.getController();
        controlador2.initUser(nick, email, pass, avatar, birthday);
        controlador2.mostrarinfo(nick, email, pass, avatar, birthday);
        dialog.setScene(new Scene(root));
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        dialog.showAndWait();

        if (controlador2.pulsadoGuardar()) {
            User user = controlador2.getUser();
            Image res = user.getAvatar();
            ivPerfil.setImage(res);
        }
    }

    private void onCerrarAplicacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaCerrarAplicacion.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Cerrar AplicaciÃ³n");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void botonModoAleatorioOnAction(ActionEvent event) {
        if (list != null && !list.isEmpty()) {
            int indiceRandom = (int) (Math.random() * list.size());
            listaProblemas.getSelectionModel().select(indiceRandom);
            selectedProblem = ol.get(indiceRandom);

            if (selectedProblem != null) {
                problemaElegido = true;
                listaProblemas.scrollTo(indiceRandom);
                listaProblemas.getScene().getWindow().hide();
            }
        }
    }
}


