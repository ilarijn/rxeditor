package rxeditor.UI;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rxeditor.Models.Datatype;

/**
 * Dialog window for setting the data types of columns defined in SQLWindow.
 * Automatically generates a ComboBox for each column containing as selections
 * the data types defined in enum class Datatype. These values are saved in the
 * columnTypes list of the originating SQLWindow given in the constructor.
 */
public class SQLColumnWindow extends GridpaneWindow {

    private final Button setButton = new Button("Set");
    private final List<ComboBox> boxList = new ArrayList<>();

    public SQLColumnWindow(Stage stage, int columns, List<Datatype> columnTypes, Label infotext) {
        
        super(stage, 300, 60 + ((int) Math.round(columns/2.0) * 30));
      
        int i = 0;
        int row = 0;
        int column = 0;
        while (i < columns) {
            HBox newBox = generateComboBox(i);
            layout.add(newBox, column, row);
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
            i++;
        }
        if (column != 0) {
            row++;
        }

        layout.add(setButton, 0, row);
        layout.add(cancelButton, 1, row);
        setButton.setMinWidth(60);

        setButton.setOnAction((event) -> {
            populateList(columnTypes, boxList);
            if (columnTypes.size() < columns) {
                Alerts.columnValueAlert();
            } else {
                infotext.setText(columnTypes.toString());
                stage.close();
            }
        });
    }

    private HBox generateComboBox(Integer columnnumber) {
        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "VARCHAR",
                        "INT",
                        "FLOAT",
                        "DATE",
                        "BOOLEAN"
                );
        ComboBox comboBox = new ComboBox(options);
        Label label = new Label("" + (columnnumber + 1));
        HBox hbox = new HBox(label, comboBox);
        hbox.setSpacing(10);
        boxList.add(comboBox);
        return hbox;
    }

    private void populateList(List<Datatype> columnTypes, List<ComboBox> boxList) {
        columnTypes.clear();
        for (ComboBox box : boxList) {
            Object item = box.getSelectionModel().getSelectedItem();
            if (item!= null) {
                switch (item.toString()) {
                    case "VARCHAR":
                        columnTypes.add(Datatype.VARCHAR);
                        break;
                    case "INT":
                        columnTypes.add(Datatype.INT);
                        break;
                    case "FLOAT":
                        columnTypes.add(Datatype.FLOAT);
                        break;
                    case "DATE":
                        columnTypes.add(Datatype.DATE);
                        break;
                    case "BOOLEAN":
                        columnTypes.add(Datatype.BOOLEAN);
                        break;
                }
            }
        }
    }

    public Scene getScene() {
        return scene;
    }
}
