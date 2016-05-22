import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * The main for the program.
 * Also has 2 views, input and light, within it. 
 * Acts as the controller for the program.
 */
public class Controller extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Declares default values and creates input view
        Model model = new Model();        
        // Declares default values and creates input view
        double ni = model.getNI();
        double nr = model.getNR();
        double aoi = model.getAOI();
        View view = new View(primaryStage, ni, nr, aoi);
    }
       
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
