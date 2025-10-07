package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.User;
import model.sub.SqliteConnection;

public class FXMLAutenticarseController implements Initializable {

    @FXML
    private Label emailError;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private TextField usuarioField;
    @FXML
    private Button RegistrarseButton;

    private int hits = 0;
    private int faults = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("data.db");
            System.out.println("Base de datos encontrada");
            try {
                Navigation.getInstance();
            } catch (NavDAOException ex) {
                System.out.println("hola");
            }
        } catch (SQLException ex) {
            System.out.println("Base de datos no encontrada");
        }
    }

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws IOException, NavDAOException {
        String nick = usuarioField.getText();
        String pass = passwordField.getText();
        Navigation nav = Navigation.getInstance();

        if (nav.exitsNickName(nick)) {
            User user = nav.authenticate(nick, pass);
            if (user.chekCredentials(nick, pass)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLTrabajo.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo1.png")));
                stage.setScene(scene);
                stage.setTitle("Trabajo");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setMinWidth(900);
                stage.setMinHeight(630);

                FXMLTrabajoController controlador1 = loader.getController();
                controlador1.initUser(user, user.getNickName(), user.getEmail(), user.getPassword(), user.getAvatar(),
                        user.getBirthdate());
                controlador1.initSes(hits, faults);
                System.out.println(user + " Autenticarse");

                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                stage.show();
            }
        }
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void RegistrarseButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaRegistro.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 500);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo1.png")));
        stage.setScene(scene);
        stage.setTitle("Registrarse");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}


