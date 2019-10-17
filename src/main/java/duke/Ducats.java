package duke;

import duke.uicomponents.CommandLine;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Ducats extends Application {

    Stage window;
    Scene scene;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setTitle("Ducats");

        CommandLine commandLine = new CommandLine("", "Enter a command...");
        commandLine.setId("commandLine");
        commandLine.requestFocus();
        commandLine.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                System.out.println(commandLine.getText());
                commandLine.clear();
                commandLine.setPromptText("Enter command here...");
                commandLine.setFocusTraversable(false);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().add(commandLine);

        scene = new Scene(layout, 300, 250);

        //applying stylesheet to the scene
        scene.getStylesheets().add("style/ducats.css");
        scene.getStylesheets().add("style/persistent-prompt.css");
        window.setScene(scene);
        window.setMinWidth(1024);
        window.setMinHeight(760);

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        window.show();
    }

    private void closeProgram() {
        //Logic to execute before closing the program
        System.out.println("Program about to close");
        window.close();
    }
}
