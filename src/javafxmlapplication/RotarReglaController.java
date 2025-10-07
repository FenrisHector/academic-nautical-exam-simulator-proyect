package javafxmlapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RotarReglaController {

    @FXML
    private Slider rotationSlider;
    @FXML
    private Label rotationLabel;
    @FXML
    private Text titleText;

    private double xOffset = 0;
    private double yOffset = 0;
    private ImageView ivHouse;

    public void setRuleImageView(ImageView ivHouse) {
        this.ivHouse = ivHouse;
    }

    @FXML
    public void initialize() {
        // Permite arrastrar la ventana desde el tÃ­tulo
        titleText.setOnMousePressed(event -> {
            Stage stage = (Stage) titleText.getScene().getWindow();
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        titleText.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleText.getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        // Slider de rotaciÃ³n
        rotationSlider.setMin(0);
        rotationSlider.setMax(360);
        rotationSlider.setValue(0);

        rotationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double rotationValue = newValue.doubleValue();
            rotationLabel.setText(String.format("%.2fÂ°", rotationValue));
            ivHouse.setRotate(rotationValue);
        });
    }

    @FXML
    private void closeRotationWindow(ActionEvent event) {
        Stage stage = (Stage) rotationSlider.getScene().getWindow();
        stage.close();
    }
}


