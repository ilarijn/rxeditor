package rxeditor.UI;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * Superclass for dialog windows using GridPane.
 */
public abstract class GridpaneWindow {

    //Common UI components.
    protected GridPane layout = new GridPane();
    protected Button cancelButton = new Button("Cancel");
    protected Scene scene;

    public GridpaneWindow(Stage stage, int width, int height) {

        scene = new Scene(layout, width, height);

        //Common event handlers.
        cancelButton.setOnAction((ActionEvent t) -> {
            stage.close();
        });

        //Padding and style settings.
        layout.setHgap(5);
        layout.setVgap(5);
        layout.setPadding(new Insets(10));

        //Column constraints.
        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);
        layout.getColumnConstraints().addAll(cons1, cons2);

        //Row constraints.
        RowConstraints rcons1 = new RowConstraints(25);
        rcons1.setVgrow(Priority.ALWAYS);
        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.NEVER);
        RowConstraints rcons3 = new RowConstraints();
        rcons3.setVgrow(Priority.NEVER);
        layout.getRowConstraints().addAll(rcons1, rcons2, rcons3);
    }

    public Scene getScene() {
        return scene;
    }
}
