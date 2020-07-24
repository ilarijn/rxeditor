package rxeditor.UI;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rxeditor.Models.Entry;
import rxeditor.Tools.Storage;

/**
 * A dialog window for saving expressions.
 */
public class SaveWindow extends GridpaneWindow {

    private final Button saveButton = new Button("Save");
    private final TextField name = new TextField();
    private final TextField category = new TextField();
    private final Label nameLabel = new Label("Name");
    private final Label categoryLabel = new Label("Category");
    private final Text showRegex = new Text();

    public SaveWindow(Stage stage) {

        super(stage, 300, 150);

        //Event handlers.
        saveButton.setOnAction((ActionEvent t) -> {
            List<Entry> existing = Storage.loadRegexFile("regex.txt");
            Entry entry = new Entry(category.getText(), name.getText(), showRegex.getText());
            if (!existing.contains(entry)) {
                Storage.saveRegexToFile(entry, "regex.txt");
                stage.close();
            } else {
                Alerts.regexAlreadyExists();
            }
        });

        showRegex.setText(MainWindow.regexText.getText());
        showRegex.setStyle(MainWindow.defaultRegex);

        //Node placement in GridPane.
        layout.add(nameLabel, 0, 1);
        layout.add(name, 1, 1);
        layout.add(categoryLabel, 0, 2);
        layout.add(category, 1, 2);
        layout.add(showRegex, 0, 0, 2, 1);
        layout.add(saveButton, 1, 4, 1, 1);
        layout.add(cancelButton, 1, 4, 1, 1);
        GridPane.setHalignment(showRegex, HPos.CENTER);
        GridPane.setHalignment(saveButton, HPos.LEFT);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
    }
}
