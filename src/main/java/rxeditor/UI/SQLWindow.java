package rxeditor.UI;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rxeditor.Models.Datatype;
import rxeditor.Tools.Parsing;

/**
 * Dialog window for generating SQL insert statements.
 */
public class SQLWindow extends GridpaneWindow {

    private final Button generateButton = new Button("Generate");
    private final Button datatypesButton = new Button("Datatypes");
    private final HBox buttonBox = new HBox(generateButton, datatypesButton, cancelButton);
    private final TextField tableName = new TextField();
    private final TextField delimiter = new TextField();
    private final TextField columns = new TextField();
    private final Label tableNameLabel = new Label("Table name");
    private final Label delimiterLabel = new Label("Delimiter");
    private final Label columnsLabel = new Label("Number of columns");
    private final CheckBox serialBox = new CheckBox("Serial");
    private final List<Datatype> columnTypes = new ArrayList<>();
    private final Label columnTypesText = new Label();

    public SQLWindow(Stage stage, String source, TabPane tabPane) {

        super(stage, 350, 155);

        //Event handlers.
        generateButton.setOnAction((ActionEvent t) -> {
            if (!delimiter.getText().isEmpty() && Parsing.tryParseInt(columns.getText())) {
                if (columnTypes.size() < Integer.parseInt(columns.getText())) {
                    Alerts.columnValueAlert();
                } else {
                    String sql = Parsing.generateSQL(source, delimiter.getText(),
                            tableName.getText(), Integer.parseInt(columns.getText()), serialBox.isSelected(), columnTypes);
                    MainWindow.newTab(sql);
                    stage.close();
                }
            } else {
                Alerts.missingDelimiterOrColumns();
            }
        });
        datatypesButton.setOnAction((event) -> {
            if (Parsing.tryParseInt(columns.getText())) {
                if (Integer.parseInt(columns.getText()) > 0) {
                    Stage newWindow = new Stage();
                    SQLColumnWindow columnDialog = new SQLColumnWindow(newWindow,
                            Integer.parseInt(columns.getText()), columnTypes, columnTypesText);
                    newWindow.setScene(columnDialog.getScene());
                    newWindow.setTitle("Set Column Datatypes");
                    newWindow.show();
                } else {
                    Alerts.columnNumberAlert();
                }
            } else {
                Alerts.columnNumberAlert();
            }
        });

        buttonBox.setSpacing(10);
        columnTypesText.setText("[NO DATATYPES SELECTED]");

        //Node placement in GridPane.
        layout.add(tableNameLabel, 0, 0);
        layout.add(tableName, 1, 0);
        layout.add(columnsLabel, 0, 1);
        layout.add(columns, 1, 1);
        layout.add(delimiterLabel, 0, 2);
        layout.add(delimiter, 1, 2);
        layout.add(serialBox, 0, 3);
        layout.add(buttonBox, 1, 3, 1, 1);
        layout.add(columnTypesText, 1, 4);
        GridPane.setHalignment(buttonBox, HPos.LEFT);
     }

    public List<Datatype> getColumnTypes() {
        return columnTypes;
    }

}
