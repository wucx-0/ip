package ben;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ben.gui.GUI;

/**
 * A GUI for Ben using FXML.
 */
public class Main extends Application {

    private GUI ben = new GUI();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Ben Chatbot");
            fxmlLoader.<MainWindow>getController().setBen(ben);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
