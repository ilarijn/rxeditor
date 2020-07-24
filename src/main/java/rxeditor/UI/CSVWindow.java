package rxeditor.UI;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rxeditor.Tools.Parsing;

/**
 * Dialog window for CSV generation.
 *
 */
public class CSVWindow extends GridpaneWindow {

    private final Button generateButton = new Button("Generate");
    private final TextField match = new TextField();
    private final TextField delimiter = new TextField();
    private final Label matchLabel = new Label("Match");
    private final Label delimiterLabel = new Label("Delimiter");
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final RadioButton beginningButton = new RadioButton("Beginning");
    private final RadioButton endButton = new RadioButton("End");
    private final RadioButton bothButton = new RadioButton("Both");
    private final HBox buttonBox = new HBox(endButton, bothButton);

    public CSVWindow(Stage stage, String sourceText, TabPane tabPane) {

        super(stage, 300, 130);

        toggleGroup.getToggles().addAll(beginningButton, endButton, bothButton);
        endButton.setSelected(true);

        //Event handlers.
        generateButton.setOnAction((ActionEvent t) -> {
            if (!delimiter.getText().isEmpty() && !match.getText().isEmpty()) {
                String csv = Parsing.generateCSV(sourceText, match.getText(), delimiter.getText(),
                        beginningButton.isSelected(), bothButton.isSelected());
                MainWindow.newTab(csv);
                stage.close();
            } else {
                Alerts.csvDelimiterOrMatchEmpty();
            }
        });

        //Padding and style settings specific to this window.
        endButton.setStyle("-fx-padding: 0 10 0 0;");

        //Node placement in GridPane.
        layout.add(matchLabel, 0, 0);
        layout.add(match, 1, 0);
        layout.add(delimiterLabel, 0, 1);
        layout.add(delimiter, 1, 1);
        layout.add(beginningButton, 0, 2);
        layout.add(buttonBox, 1, 2);
        layout.add(generateButton, 0, 3, 1, 1);
        layout.add(cancelButton, 1, 3, 1, 1);
        GridPane.setHalignment(generateButton, HPos.LEFT);
        GridPane.setHalignment(cancelButton, HPos.LEFT);
        match.setText(MainWindow.regexText.getText());
        delimiter.setText(",");
    }
}
