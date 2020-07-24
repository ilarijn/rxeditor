package rxeditor.UI;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * General class for alerts in the editor.
 */
public class Alerts {

    public static void columnValueAlert() {
        Alert alert = createAlert("Please set a datatype for each column.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void columnNumberAlert() {
        Alert alert = createAlert("Invalid number of columns.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void missingDelimiterOrColumns() {
        Alert alert = createAlert("Please set a delimiter and/or a valid number of columns.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }
    
    public static void matchConditionMissing() {
        Alert alert = createAlert("Please set a match pattern.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }
    
    public static void regexEmptyAlert() {
        Alert alert = createAlert("Regex value is empty.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void csvDelimiterOrMatchEmpty() {
        Alert alert = createAlert("Please enter a delimiter character and a match pattern.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void emptyTextExtract() {
        Alert alert = createAlert("Text area is empty.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void regexMissing() {
        Alert alert = createAlert("Regex missing.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void invalidRegex() {
        Alert alert = createAlert("Invalid regex.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void noValueSelected() {
        Alert alert = createAlert("No value selected.",
                Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    public static void noMatchesFound() {
        Alert alert = createAlert("No matches found.",
                Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    public static void regexAlreadyExists() {
        Alert alert = createAlert("An entry with this name and category already exists.",
                Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    private static Alert createAlert(String text, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Error");
        alert.setContentText(text);
        return alert;
    }

}
