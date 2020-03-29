import com.lockboxlocal.entity.Model;
import com.lockboxlocal.view.HomeView;
import com.lockboxlocal.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();
        View view = new View(model, primaryStage);
        primaryStage.setTitle("Lockbox Local");
        primaryStage.setScene(view.getHomeView());
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
