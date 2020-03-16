import com.lockboxlocal.entity.Model;
import com.lockboxlocal.view.HomeView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();
        VBox root = new VBox(7);
        HomeView hv = new HomeView(root);
        primaryStage.setTitle("Lockbox Local");
        primaryStage.setScene(hv);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
