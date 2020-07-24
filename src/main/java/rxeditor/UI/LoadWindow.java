package rxeditor.UI;

import java.util.HashMap;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rxeditor.Tools.Storage;

/**
 * Dialog window for loading expressions.
 */
public class LoadWindow {

    private final HBox layout = new HBox();
    private final VBox rightBox = new VBox();
    private final Scene scene = new Scene(layout, 300, 250);
    private final Button loadButton = new Button("Load");
    private final Button cancelButton = new Button("Cancel");
    private final Text showRegex = new Text();
    private final HBox textBox = new HBox(showRegex);
    private final HBox buttonBox = new HBox(loadButton, cancelButton);
    private final TreeItem<String> root = new TreeItem<String>("root");
    private final TreeView<String> treeView = new TreeView<String>(root);
    private final HashMap<String, String> regexValues = new HashMap<>();

    //EventHandler for getting the correct regular expression
    //when an item in the TreeView is selected.
    //Since only the name and category strings of saved expressions appear
    //in the TreeView, the actual expressions are retrieved from HashMap regexValues.
    private final EventHandler<MouseEvent> clickName = (MouseEvent event) -> {
        TreeItem selected = treeView.getSelectionModel().getSelectedItem();
        showRegex.setText("");
        if (selected instanceof TreeItem && (selected.getParent() != root || selected.getChildren().isEmpty())) {
            if (selected.getParent() != root) {
                showRegex.setText(regexValues.get(selected.getParent().getValue() + selected.getValue().toString()));
            } else {
                showRegex.setText(regexValues.get(selected.getValue().toString()));
            }
        }
    };

    public LoadWindow(Stage stage) {

        //Load previously saved expressions and populate the TreeView 
        //according to category and name. Category can be empty, in which
        //case the expression appears at the root.
        Storage.loadRegexFile("regex.txt").forEach(entry -> {
            regexValues.put(entry.getCategory() + entry.getName(), entry.getRegex());
            if (!entry.getCategory().isEmpty()) {
                boolean found = false;
                for (TreeItem ti : root.getChildren()) {
                    if (entry.getCategory().equals(ti.getValue())) {
                        ti.getChildren().add(new TreeItem<String>(entry.getName()));
                        found = true;
                    }
                }
                if (!found) {
                    TreeItem<String> newCategory = new TreeItem<String>(entry.getCategory());
                    newCategory.getChildren().add(new TreeItem<String>(entry.getName()));
                    root.getChildren().add(newCategory);
                }
            } else {
                root.getChildren().add(new TreeItem<String>(entry.getName()));
            }
        });

        //Assign previously set up event handlers and set up new ones that 
        //require parameters from constructor.
        treeView.setOnMouseClicked(clickName);
        loadButton.setOnAction((event) -> {
            String regex = showRegex.getText();
            if (!regex.isEmpty()) {
                MainWindow.regexText.setText(regex);
                stage.close();
            }
            else {
                Alerts.noValueSelected();
            }
        });
        cancelButton.setOnAction((event) -> {
            stage.close();
        });

        //Set up layout.
        treeView.setShowRoot(false);
        showRegex.setStyle(MainWindow.defaultRegex);

        rightBox.getChildren().addAll(textBox, buttonBox);
        textBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        textBox.setPrefHeight(rightBox.getHeight() / 2);
        buttonBox.setPrefHeight(rightBox.getHeight() / 2);
        VBox.setVgrow(textBox, Priority.ALWAYS);
        VBox.setVgrow(buttonBox, Priority.ALWAYS);
        layout.getChildren().addAll(treeView, rightBox);
        treeView.setPrefWidth(scene.getWidth() / 2);
        rightBox.setPrefWidth(scene.getWidth() / 2);
        HBox.setHgrow(treeView, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

    }

    public Scene getScene() {
        return scene;
    }
}
