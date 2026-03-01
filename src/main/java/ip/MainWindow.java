package ip;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

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

    private Monika monika;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private final Image monikaImage = new Image(this.getClass().getResourceAsStream("/images/Monika.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Monika instance */
    public void setMonika(Monika m) throws IOException {
        monika = m;
        say(monika.greeting());
    }

    /** simply message to user */
    public void say(String msg) {
        assert msg != null : "Response is null";
        dialogContainer.getChildren().add(DialogBox.getMonikaDialog(msg, monikaImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() throws IOException, InterruptedException {
        String input = userInput.getText();
        String response = monika.getResponse(input);
        if (!input.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));
        }
        dialogContainer.getChildren().add(DialogBox.getMonikaDialog(response, monikaImage));
        userInput.clear();
    }
}
