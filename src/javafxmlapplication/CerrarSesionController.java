package javafxmlapplication;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import model.NavDAOException;
import model.Navigation;
import model.User;
import model.sub.SqliteConnection;

public class CerrarSesionController implements Initializable {

    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;

    private int hits;
    private int faults;

    private String nick;
    private String pass;
    private String email;
    private Image avatar;
    private LocalDate birthday;

    private User user;
    private Navigation nav;

    public void initUser(User u, String n, String e, String p, Image a, LocalDate dt) {
        user = u;
        nick = n;
        email = e;
        pass = p;
        avatar = a;
        birthday = dt;
        System.out.println(user + " Cerrar Sesion");
    }

    public void initSesion(int h, int f) {
        hits = h;
        faults = f;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            System.out.println(pass);
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("data.db");
            System.out.println("Base de datos encontrada");
            nav = Navigation.getInstance();
        } catch (NavDAOException ex) {
            Logger.getLogger(CerrarSesionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CerrarSesionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws Exception {
        user.addSession(hits, faults);
        Platform.exit();
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}


