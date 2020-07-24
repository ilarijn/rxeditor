package rxeditor.UI;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rxeditor.Tools.Parsing;

/**
 * A dialog window for matching and replacing.
 */
public class ReplaceWindow extends GridpaneWindow {

    private final Button matchButton = new Button("Match");
    private final Button replaceButton = new Button("Replace");
    private final TextField replacement = new TextField();
    private final TextField matchPattern = new TextField();
    private final Label replacementLabel = new Label("Replace with");
    private final Label splitterLabel = new Label("Match");

    public ReplaceWindow(Stage stage, String sourceText, TabPane tabPane) {

        super(stage, 300, 110);

        //Event handlers.
        matchButton.setOnAction((ActionEvent t) -> {
            MainWindow.matchRegex(matchPattern.getText());
        });
        replaceButton.setOnAction((ActionEvent t) -> {
            if (!matchPattern.getText().isEmpty()) {
                String csv = Parsing.matchAndReplace(sourceText, matchPattern.getText(), replacement.getText());
                MainWindow.newTab(csv);
                stage.close();
            } else {
                Alerts.matchConditionMissing();
            }
        });

        //Node placement in GridPane.
        layout.add(splitterLabel, 0, 0);
        layout.add(matchPattern, 1, 0);
        layout.add(replacementLabel, 0, 1);
        layout.add(replacement, 1, 1);
        layout.add(matchButton, 0, 3, 1, 1);
        layout.add(replaceButton, 1, 3, 1, 1);
        layout.add(cancelButton, 1, 3, 1, 1);
        GridPane.setHalignment(matchButton, HPos.LEFT);
        GridPane.setHalignment(replaceButton, HPos.LEFT);
        GridPane.setHalignment(cancelButton, HPos.RIGHT);

        matchPattern.setText(MainWindow.regexText.getText());
    }
}
