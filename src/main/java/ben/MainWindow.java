package ben;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ben.gui.GUI;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private GUI ben;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image benImage = new Image(this.getClass().getResourceAsStream("/images/Ben.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        dialogContainer.prefWidthProperty().bind(scrollPane.widthProperty().subtract(2));

        // Add welcome message
        dialogContainer.getChildren().add(
                DialogBox.getBenDialog("Hello! I'm Ben\nWhat can I do for you?", benImage)
        );
    }

    /** Injects the Ben instance */
    public void setBen(GUI b) {
        ben = b;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Ben's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = ben.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBenDialog(response, benImage)
        );
        userInput.clear();

        // Handle exit command
        if (input.trim().equalsIgnoreCase("bye")) {
            // Close application after showing goodbye message
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(2000); // Show goodbye message for 2 seconds
                    javafx.application.Platform.exit();
                } catch (InterruptedException e) {
                    javafx.application.Platform.exit();
                }
            });
        }
    }
}