package javafxmlapplication;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;

import model.NavDAOException;
import model.Navigation;
import model.Session;
import model.User;
import model.sub.SqliteConnection;

public class EstadisticasController implements Initializable {

    @FXML
    private ToggleGroup Grupotiempo;
    @FXML
    private ToggleGroup Grupodificultad;
    @FXML
    private ToggleGroup Grupotematica;

    @FXML
    private Label labelProblemas;
    @FXML
    private Label labelPorAciertos;
    @FXML
    private Label labelNumAciertos;
    @FXML
    private Label labelPorFallos;
    @FXML
    private Label labelNumFallos;

    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;

    private Session session;
    private int totalAciertos;
    private int totalFallos;

    public void initUser(String u, String e, String p, Image a, LocalDate dt) {
        nick = u;
        email = e;
        pass = p;
        avatar = a;
        birthday = dt;
    }

    public void initEstadisticas(int tA, int tF) {
        totalAciertos = tA;
        totalFallos = tF;

        double numProblemas = totalAciertos + totalFallos;
        double lPorAciertos = ((totalAciertos / numProblemas) * 100);
        double lPorFallos = ((totalFallos / numProblemas) * 100);

        labelProblemas.setText(numProblemas + "");
        labelPorAciertos.setText(lPorAciertos + "%");
        labelNumAciertos.setText(totalAciertos + "");
        labelPorFallos.setText(lPorFallos + "%");
        labelNumFallos.setText(totalFallos + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("data.db");
            System.out.println("Base de datos encontrada");
        } catch (SQLException ex) {
            System.out.println("Base de datos no encontrada");
        }

        try {
            Navigation nav = Navigation.getInstance();
            User res = nav.authenticate(nick, pass);
        } catch (NavDAOException ex) {
            Logger.getLogger(EstadisticasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


