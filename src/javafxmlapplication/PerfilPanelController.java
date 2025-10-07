package javafxmlapplication;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import model.NavDAOException;
import model.Navigation;
import model.User;
import model.sub.SqliteConnection;

public class PerfilPanelController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContrasena;
    @FXML
    private TextField tfEmail;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private ImageView ivDialogIcon;

    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;
    private Boolean pulsadoGuardar;
    private User user;
    private Navigation nav;

    public void initUser(String u, String e, String p, Image a, LocalDate dt) {
        nick = u;
        email = e;
        pass = p;
        avatar = a;
        birthday = dt;
    }

    public void mostrarinfo(String u, String e, String p, Image a, LocalDate dt) {
        tfUsuario.setText(nick);
        pfContrasena.setText(pass);
        tfEmail.setText(email);
        ivDialogIcon.setImage(avatar);
        dpFechaNacimiento.setValue(birthday);
    }

    public boolean pulsadoGuardar() {
        return pulsadoGuardar;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pulsadoGuardar = false;

        try {
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("data.db");
            System.out.println("Base de datos encontrada");
        } catch (SQLException ex) {
            System.out.println("Base de datos no encontrada");
        }

        try {
            nav = Navigation.getInstance();
            user = nav.authenticate(nick, pass);
        } catch (NavDAOException ex) {
            Logger.getLogger(PerfilPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Abre el FileChooser para cambiar el avatar */
    @FXML
    void onChooseAvatar() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecciona un avatar");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("ImÃ¡genes", "*.png", "*.jpg", "*.jpeg"));
        Window win = ivDialogIcon.getScene().getWindow();
        File f = chooser.showOpenDialog(win);
        if (f != null) {
            ivDialogIcon.setImage(new Image(f.toURI().toString()));
        }
    }

    /** Guarda los cambios (aquÃ­ solo cierra el diÃ¡logo) */
    @FXML
    void onGuardar() throws NavDAOException {
        user = nav.authenticate(nick, pass);
        if (pfContrasena.getText().length() < 8) {
            showError("La contraseÃ±a debe tener al menos 8 caracteres.");
            return;
        } else {
            user.setPassword(pfContrasena.getText());
            user.setEmail(tfEmail.getText());
            user.setAvatar(ivDialogIcon.getImage());
            user.setBirthdate(dpFechaNacimiento.getValue());
            pulsadoGuardar = true;
        }
        ivDialogIcon.getScene().getWindow().hide();
        showInfo("Cambios guardados, para su visualizaciÃ³n debe reiniciar el programa.");
    }

    /** Cancela y cierra */
    @FXML
    void onCancelar() {
        ivDialogIcon.getScene().getWindow().hide();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.initOwner(ivDialogIcon.getScene().getWindow());
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.initOwner(ivDialogIcon.getScene().getWindow());
        a.showAndWait();
    }
}


