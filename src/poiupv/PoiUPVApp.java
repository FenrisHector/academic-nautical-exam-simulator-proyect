package poiupv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PoiUPVApp extends Application {

    private static Stage Pstage;
    private static PoiUPVApp instance;

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        Pstage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaAutenticacion.fxml"));
        Parent root = loader.load();
        Pstage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo1.png")));
        Scene scene = new Scene(root);
        Pstage.setTitle("Bienvenido");
        Pstage.setScene(scene);
        Pstage.setMinWidth(660);
        Pstage.setMinHeight(400);
        Pstage.show();
    }

    public static void reiniciarApp() throws Exception {
        // Crea un nuevo Stage y vuelve a lanzar start()
        Stage newStage = new Stage();
        instance.start(newStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


