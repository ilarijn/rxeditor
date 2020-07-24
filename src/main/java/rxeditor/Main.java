package rxeditor;

import rxeditor.UI.MainWindow;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("rxeditor");
        MainWindow rxeditor = new MainWindow(primaryStage);
        primaryStage.setScene(rxeditor.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
