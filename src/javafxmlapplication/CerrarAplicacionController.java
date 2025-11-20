package javafxmlapplication;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CerrarAplicacionController implements Initializable {

    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nada que inicializar por ahora
    }

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).hide();
    }
}


