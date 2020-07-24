package rxeditor.UI;

import java.io.File;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import rxeditor.Tools.Parsing;
import rxeditor.Tools.Storage;

/**
 * UI components, event handlers and layout settings for the main window
 * including all methods related to main UI functionality.
 */
public class MainWindow {

    //UI components.
    protected final static TabPane tabPane = new TabPane();
    protected final static TextField regexText = new TextField();
    private final BorderPane layout = new BorderPane();
    private final Scene scene = new Scene(layout, 450, 350);
    private final MenuBar menuBar = new MenuBar();
    private final Button applyButton = new Button("Apply");
    private final Button extractButton = new Button("Extract");
    private final Button replaceButton = new Button("Replace");
    private final Button csvButton = new Button("CSV");
    private final Button sqlButton = new Button("SQL");
    private final HBox bottomBar = new HBox();
    private final HBox buttonBox1 = new HBox();
    private final VBox topMenu = new VBox();
    private final Menu menuFile = new Menu("File");
    private final Menu menuRegex = new Menu("Regex");
    private final Menu menuOptions = new Menu("Options");
    private final MenuItem menuFileNew = new MenuItem("New Tab");
    private final MenuItem menuFileOpen = new MenuItem("Open");
    private final MenuItem menuFileSaveAs = new MenuItem("Save As");
    private final MenuItem menuFileExit = new MenuItem("Exit");
    private final MenuItem menuRegexSave = new MenuItem("Save Current Regex");
    private final MenuItem menuRegexLoad = new MenuItem("Load Regex");
    private final CheckMenuItem menuRegexFeedback = new CheckMenuItem("Instant Regex Feedback");
    private final MenuItem menuOptionsFontPlus = new MenuItem("Increase Font Size");
    private final MenuItem menuOptionsFontMinus = new MenuItem("Decrease Font Size");
    private static ContextMenu contextMenu = new ContextMenu();
    private final MenuItem contextMenuUndo = new MenuItem("Undo");
    private final MenuItem contextMenuCut = new MenuItem("Cut");
    private final MenuItem contextMenuCopy = new MenuItem("Copy");
    private final MenuItem contextMenuPaste = new MenuItem("Paste");
    private final MenuItem contextMenuSelectAll = new MenuItem("Select All");

    //Style fields and miscellaneous.
    protected static String defaultStyle;
    protected static String highlightStyle;
    protected static String defaultRegex;
    protected static String contextMenuStyle;
    protected static int fontSize = 14;
    private static int tabCount = 0;

    //Event handlers.
    private final EventHandler<ActionEvent> applyRegex = (ActionEvent event) -> {
        matchRegex(regexText.getText());
    };
    private final EventHandler<ActionEvent> extractMatches = (ActionEvent event) -> {
        extractRegex(regexText.getText());
    };
    private final EventHandler<ActionEvent> newTab = (ActionEvent event) -> {
        newTab("");
    };
    private final EventHandler<ActionEvent> loadFile = (ActionEvent event) -> {
        fileOperation(false);
    };
    private final EventHandler<ActionEvent> saveFile = (ActionEvent event) -> {
        fileOperation(true);
    };
    private final EventHandler<ActionEvent> saveRegex = (ActionEvent event) -> {
        if (regexText.getText().isEmpty()) {
            Alerts.regexEmptyAlert();
        } else {
            saveRegex();
        }
    };
    private final EventHandler<ActionEvent> loadRegex = (ActionEvent event) -> {
        loadRegex();
    };
    private final EventHandler<ActionEvent> matchAndReplace = (ActionEvent event) -> {
        matchReplace();
    };
    private final EventHandler<ActionEvent> generateSQL = (ActionEvent event) -> {
        generateSQL();
    };
    private final EventHandler<ActionEvent> generateCSV = (ActionEvent event) -> {
        generateCSV();
    };
    private final EventHandler<ActionEvent> fontPlus = (ActionEvent event) -> {
        fontSize++;
        InlineCssTextArea mainText = activeTabContents();
        mainText.setStyle(0, mainText.getLength(), "-fx-font-size: " + fontSize + "px;");
    };
    private final EventHandler<ActionEvent> fontMinus = (ActionEvent event) -> {
        if (fontSize > 1) {
            fontSize--;
            InlineCssTextArea mainText = activeTabContents();
            mainText.setStyle(0, mainText.getLength(), "-fx-font-size: " + fontSize + "px;");
        }
    };
    private final EventHandler<KeyEvent> instantFeedback = (KeyEvent event) -> {
        matchRegex(regexText.getText());
    };
    private final EventHandler<ActionEvent> feedbackOption = (ActionEvent event) -> {
        if (menuRegexFeedback.isSelected()) {
            regexText.setOnKeyReleased(instantFeedback);
            tabPane.setOnKeyReleased(instantFeedback);
        } else {
            regexText.setOnKeyReleased(null);
            tabPane.setOnKeyReleased(null);
        }
    };
    private final EventHandler<ActionEvent> undo = (ActionEvent event) -> {
        activeTabContents().undo();
        contextMenu.hide();
    };
    private final EventHandler<ActionEvent> cut = (ActionEvent event) -> {
        activeTabContents().cut();
        contextMenu.hide();
    };
    private final EventHandler<ActionEvent> copy = (ActionEvent event) -> {
        activeTabContents().copy();
        contextMenu.hide();
    };
    private final EventHandler<ActionEvent> paste = (ActionEvent event) -> {
        activeTabContents().paste();
        contextMenu.hide();
    };
    private final EventHandler<ActionEvent> selectAll = (ActionEvent event) -> {
        activeTabContents().selectAll();
        contextMenu.hide();
    };

    //Event handlers and layout placements are assigned in the constructor.
    public MainWindow(Stage stage) {

        scene.getStylesheets().add("editorstyle.css");
        defaultStyle = Storage.getCSSProperties(".styled-text-area .text");
        highlightStyle = Storage.getCSSProperties(".matchhighlight");
        defaultRegex = Storage.getCSSProperties(".defaultregex");
        contextMenuStyle = Storage.getCSSProperties(".contextmenu");

        menuFileNew.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        menuFileNew.setOnAction(newTab);
        menuFileOpen.setOnAction(loadFile);
        menuFileSaveAs.setOnAction(saveFile);
        menuFileExit.setOnAction((ActionEvent t) -> {
            stage.close();
        });
        menuRegexSave.setOnAction(saveRegex);
        menuRegexLoad.setOnAction(loadRegex);
        menuOptionsFontPlus.setAccelerator(new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN));
        menuOptionsFontPlus.setOnAction(fontPlus);
        menuOptionsFontMinus.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN));
        menuOptionsFontMinus.setOnAction(fontMinus);
        menuRegexFeedback.setOnAction(feedbackOption);

        regexText.setOnAction(applyRegex);
        regexText.setStyle(defaultRegex);

        applyButton.setOnAction(applyRegex);
        extractButton.setOnAction(extractMatches);
        replaceButton.setOnAction(matchAndReplace);
        csvButton.setOnAction(generateCSV);
        sqlButton.setOnAction(generateSQL);

        contextMenuUndo.setOnAction(undo);
        contextMenuCut.setOnAction(cut);
        contextMenuCopy.setOnAction(copy);
        contextMenuPaste.setOnAction(paste);
        contextMenuSelectAll.setOnAction(selectAll);

        menuFile.getItems().addAll(
                menuFileNew,
                menuFileOpen,
                menuFileSaveAs,
                menuFileExit);
        menuRegex.getItems().addAll(menuRegexSave, menuRegexLoad,
                menuRegexFeedback);
        menuOptions.getItems().addAll(menuOptionsFontPlus,
                menuOptionsFontMinus);
        menuBar.getMenus()
                .addAll(menuFile, menuRegex, menuOptions);

        contextMenu.setStyle(contextMenuStyle);
        contextMenuUndo.setStyle(contextMenuStyle);
        contextMenuCut.setStyle(contextMenuStyle);
        contextMenuCopy.setStyle(contextMenuStyle);
        contextMenuPaste.setStyle(contextMenuStyle);
        contextMenuSelectAll.setStyle(contextMenuStyle);
        contextMenu.getItems().addAll(contextMenuUndo, contextMenuCut, contextMenuCopy,
                contextMenuPaste, contextMenuSelectAll);

        buttonBox1.getChildren().addAll(applyButton, extractButton,
                replaceButton, csvButton, sqlButton);
        bottomBar.getChildren().addAll(regexText, buttonBox1);
        HBox.setHgrow(regexText, Priority.ALWAYS);
        topMenu.getChildren().add(menuBar);

        layout.setTop(topMenu);
        layout.setCenter(tabPane);
        layout.setBottom(bottomBar);

        //Create a new tab with a text area at application start.
        newTab("");
    }

    public MainWindow() {
    }

    /**
     * Creates a new Tab containing an InlineCssTextArea inside a
     * VirtualizedScrollPane to enable scrolling and places the tab in the
     * TabPane.
     *
     * @param contents Text to be placed inside the new tab.
     * @return The created tab.
     */
    protected static Tab newTab(String contents) {
        InlineCssTextArea mainText = new InlineCssTextArea(contents);
        mainText.setWrapText(true);
        mainText.setParagraphGraphicFactory(LineNumberFactory.get(mainText));
        setContextMenu(mainText);
        tabCount++;
        Tab tab = new Tab("untitled " + tabCount, new VirtualizedScrollPane(mainText));
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        return tab;
    }

    /**
     * Set context menu handlers. InlineCssTextArea consumes mouse events and
     * custom handlers are needed.
     *
     * @param textarea Textarea for which handlers should be set.
     */
    private static void setContextMenu(InlineCssTextArea textarea) {
        Nodes.addInputMap(textarea,
                InputMap.consume(
                        EventPattern.mouseClicked(MouseButton.SECONDARY),
                        e -> {
                            contextMenu.show(textarea, e.getScreenX(), e.getScreenY());
                        }
                )
        );
        Nodes.addInputMap(textarea,
                InputMap.consume(
                        EventPattern.mouseClicked(MouseButton.PRIMARY),
                        e -> {
                            if (contextMenu.isShowing()) {
                                contextMenu.hide();
                            }
                        }
                )
        );
    }

    /**
     * Gets the index ranges of regex matches in the InlineCssTextArea of the
     * currently active tab using Parsing.matchRanges() and highlights the
     * ranges.
     *
     * @param regex Regular expression to be used for matching.
     */
    protected static void matchRegex(String regex) {
        InlineCssTextArea mainText = activeTabContents();
        mainText.setStyle(0, mainText.getLength(), defaultStyle);
        String sourceText = mainText.getText();
        HashMap<Integer, Integer> ranges = Parsing.matchRanges(regex, sourceText);
        if (ranges != null) {
            for (Integer i : ranges.keySet()) {
                mainText.setStyle(i, ranges.get(i), highlightStyle);
            }
        }
    }

    /**
     * Opens a new tab containing regex matches from the previously active tab
     * separated by newlines.
     *
     * @param regex Regular expression to be used for matching.
     */
    private void extractRegex(String regex) {
        InlineCssTextArea mainText = activeTabContents();
        String source = mainText.getText();
        if (!source.isEmpty() && !regexText.getText().isEmpty()) {
            String result = Parsing.extractMatches(regex, source);
            if (result.isEmpty() || result == null) {
                Alerts.noMatchesFound();
            } else {
                newTab(result);
            }
        } else if (source.isEmpty()) {
            Alerts.emptyTextExtract();
        } else {
            Alerts.regexMissing();
        }
    }

    /**
     * Returns the InlineCssTextArea contained in the currently active tab.
     *
     * @return The InlineCssTextArea contained in the scrollpane of currently
     * active tab.
     */
    public static InlineCssTextArea activeTabContents() {
        VirtualizedScrollPane scrollPane = (VirtualizedScrollPane) tabPane.getSelectionModel().getSelectedItem().getContent();
        return (InlineCssTextArea) scrollPane.getContent();
    }

    /**
     * Method for opening a dialog window for saving the contents of the active
     * tab or opening the contents of a file.
     *
     * @param save Opens a save dialog if true or a load dialog if false.
     */
    private void fileOperation(boolean save) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("SQL", "*.sql"),
                new ExtensionFilter("CSV", "*.csv"),
                new ExtensionFilter("All Files", "*.*"));
        Stage newWindow = new Stage();
        if (save) {
            fileChooser.setTitle("Save File");
            File file = fileChooser.showSaveDialog(newWindow);
            String content = activeTabContents().getText();
            if (file != null) {
                Storage.writeFile(file, content, false);
            }
        } else {
            fileChooser.setTitle("Open File");
            File file = fileChooser.showOpenDialog(newWindow);
            if (file != null) {
                String contents = Storage.readFile(file);
                Tab tab = newTab(contents);
                tab.setText(file.getName());
            }
        }
    }

    /*
    Methods for opening each of the dialog windows.
     */
    private void saveRegex() {
        String regex = regexText.getText();
        if (!regex.isEmpty()) {
            if (Parsing.tryPatternCompile(regex)) {
                Stage newWindow = new Stage();
                SaveWindow saveDialog = new SaveWindow(newWindow);
                newWindow.setTitle("Save Regex");
                newWindow.setScene(saveDialog.getScene());
                newWindow.show();
            }
            else {
                Alerts.invalidRegex();
            }
        } else {
            Alerts.regexMissing();
        }
    }

    private void loadRegex() {
        Stage newWindow = new Stage();
        LoadWindow loadDialog = new LoadWindow(newWindow);
        newWindow.setTitle("Load Regex");
        newWindow.setScene(loadDialog.getScene());
        newWindow.show();
    }

    private void matchReplace() {
        Stage newWindow = new Stage();
        String sourceText = activeTabContents().getText();
        ReplaceWindow csvDialog = new ReplaceWindow(newWindow, sourceText, tabPane);
        newWindow.setTitle("Match and Replace");
        newWindow.setScene(csvDialog.getScene());
        newWindow.show();
    }

    private void generateSQL() {
        Stage newWindow = new Stage();
        String sourceText = activeTabContents().getText();
        SQLWindow sqlDialog = new SQLWindow(newWindow, sourceText, tabPane);
        newWindow.setTitle("SQL Insert Statements");
        newWindow.setScene(sqlDialog.getScene());
        newWindow.show();
    }

    private void generateCSV() {
        Stage newWindow = new Stage();
        String sourceText = activeTabContents().getText();
        CSVWindow csvDialog = new CSVWindow(newWindow, sourceText, tabPane);
        newWindow.setTitle("Generate CSV");
        newWindow.setScene(csvDialog.getScene());
        newWindow.show();
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public Scene getScene() {
        return scene;
    }
}
