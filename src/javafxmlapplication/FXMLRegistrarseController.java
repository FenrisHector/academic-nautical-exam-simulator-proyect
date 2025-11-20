package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.YEARS;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.LocalDateStringConverter;

import model.NavDAOException;
import model.Navigation;
import model.User;
import model.sub.SqliteConnection;

public class FXMLRegistrarseController implements Initializable {

    @FXML
    private Label emailError;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private DatePicker dateField;
    @FXML
    private Label dateError;

    private BooleanProperty validEmail;
    private BooleanProperty validPassword;
    private BooleanProperty validDate;
    private BooleanProperty validUser;

    private ChangeListener<String> listenerEmail;
    private ChangeListener<String> listenerPassword;
    private ChangeListener<LocalDate> listenerDate;
    private ChangeListener<String> listenerUser;

    private Image avatar;

    @FXML
    private TextField usuarioField;
    @FXML
    private Label usuarioError;
    @FXML
    private Button PredAvatarButton;
    @FXML
    private Button subirAvatarButton;

    private void checkUser() throws NavDAOException {
        String user = usuarioField.getText();
        Navigation nav = Navigation.getInstance();
        boolean isValid = nav.exitsNickName(user);
        validUser.set(isValid);
        showError(!isValid, usuarioField, usuarioError);
    }

    private void checkEmail() {
        String email = emailField.getText();
        boolean isValid = email.matches(
                "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        validEmail.set(isValid);
        showError(isValid, emailField, emailError);
    }

    private void checkPassword() {
        String password = passwordField.getText();
        boolean isValid = password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,15}$");
        validPassword.set(isValid);
        showError(isValid, passwordField, passwordError);
    }

    private void checkDate() {
        LocalDate value = dateField.getValue();
        boolean isValid = value.isBefore(LocalDate.now().minus(16, YEARS));
        validDate.set(isValid);
        showError(isValid, dateField, dateError);
    }

    private void showError(boolean isValid, Node field, Node errorMessage) {
        errorMessage.setVisible(!isValid);
        field.setStyle(isValid ? "" : "-fx-background-color: #FCE5E0");
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

        validUser = new SimpleBooleanProperty(false);
        usuarioField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    checkUser();
                    if (validUser.get()) {
                        if (listenerUser != null) {
                            listenerUser = (a, b, c) -> {
                                try {
                                    checkUser();
                                } catch (NavDAOException ignored) {
                                }
                            };
                            usuarioField.textProperty().addListener(listenerUser);
                        }
                    }
                } catch (NavDAOException ignored) {
                }
            }
        });

        validEmail = new SimpleBooleanProperty(false);
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkEmail();
                if (!validEmail.get()) {
                    if (listenerEmail == null) {
                        listenerEmail = (a, b, c) -> checkEmail();
                        emailField.textProperty().addListener(listenerEmail);
                    }
                }
            }
        });

        validPassword = new SimpleBooleanProperty(false);
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkPassword();
                if (!validPassword.get()) {
                    if (listenerPassword == null) {
                        listenerPassword = (a, b, c) -> checkPassword();
                        passwordField.textProperty().addListener(listenerPassword);
                    }
                }
            }
        });

        validDate = new SimpleBooleanProperty(false);
        dateField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkDate();
                if (!validDate.get()) {
                    if (listenerDate == null) {
                        listenerDate = (a, b, c) -> checkDate();
                        dateField.valueProperty().addListener(listenerDate);
                    }
                }
            }
        });

        LocalDateStringConverter localDateStringConverter = new LocalDateStringConverter() {
            @Override
            public LocalDate fromString(String value) {
                try {
                    return super.fromString(value);
                } catch (Exception e) {
                    System.out.println("Exception in fromString");
                    return LocalDate.now();
                }
            }

            @Override
            public String toString(LocalDate value) {
                return super.toString(value);
            }
        };
        dateField.setConverter(localDateStringConverter);

        BooleanBinding validFields = Bindings
                .and(validEmail, validPassword)
                .and(validDate)
                .and(validUser.not());

        bAccept.disableProperty().bind(Bindings.not(validFields));
    }

    @FXML
    private void handleBAcceptOnAction(javafx.event.ActionEvent event) throws IOException, NavDAOException {
        String user = usuarioField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate birthdate = dateField.getValue();

        Navigation nav = Navigation.getInstance();
        User res = nav.registerUser(user, email, password, avatar, birthdate);
        System.out.println("Usuario Registrado");

        ((javafx.stage.Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void handleBCanceltOnAction(javafx.event.ActionEvent event) {
        ((javafx.stage.Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void subirAvatar(javafx.event.ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Abrir Fichero");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("ImÃ¡genes", "*.png", "*.jpg"),
                new ExtensionFilter("Todos", "*.*"));
        File fichero = fc.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (fichero != null) {
            String ruta = fichero.toURI().toString();
            System.out.println("Ruta URI: " + ruta);
            avatar = new Image(ruta);
            System.out.println("avatar modificado");
        }
    }

    @FXML
    private void predAvatarAction(javafx.event.ActionEvent event) {
        avatar = new Image(getClass().getResourceAsStream("/resources/usuario.png"));
    }
}


