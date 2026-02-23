package ip;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// IP Week 4 Level-10: Add GUI
// Chat with Monika from Doki Doki Literature Club
public class Main extends Application {

    private final Monika monika = new Monika();

    public Main() throws IOException {}

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setMonika(monika);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*
        UI ui = new UI();
        greeting(ui);
        Storage storage = new Storage();
        TaskMgr tasks = storage.loadTasks();
        Parser parser = new Parser();
        Executor exe = new Executor();
        while (true) {
            Command cmd = parser.parse(ui.input());
            String s = exe.execute(cmd, tasks);
            if (s == null) {
                break;
            } else {
                ui.say(s);
            }
        }
        storage.storeTasks(tasks);
        ui.say(" Bye. Hope to see you again soon!");
         */
        Application.launch(args);
    }
}
