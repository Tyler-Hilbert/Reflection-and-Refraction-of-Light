import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
public class Main extends Application {
    final static int CANVAS_HEIGHT = 800;
    final static int CANVAS_WIDTH = 800;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Light Refraction");
        
        // Declares default values and creates input view
        double ni = 1;
        double nr = 1.5;
        double aoi = 30;
        showInput(primaryStage, ni, nr, aoi);
    }
    
    /**
     * Displays the view where users can input the variables of the program
     */
    private void showInput(Stage primaryStage, double ni, double nr, double aoi) {
        // Set up grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        
        // Create and add gui components 
        Label n1Label = new Label("Index of refracion (medium 1): ");
        Label n2Label = new Label("Index of refraction (medium 2): ");
        Label aoiLabel = new Label("Angle of incidence: ");
        
        TextField n1Text = new TextField(Double.toString(ni));
        TextField n2Text = new TextField(Double.toString(nr));
        TextField aoiText = new TextField(Double.toString(aoi));
        
        Button submit = new Button("update");
        submit.setOnAction((ActionEvent e) -> {
            try {
                final double inputNi = Double.parseDouble(n1Text.getText());
                final double inputNr = Double.parseDouble(n2Text.getText());
                final double inputAoi = Double.parseDouble(aoiText.getText());
                showLight(primaryStage, inputNi, inputNr, inputAoi);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        grid.add(n1Label, 0, 0);
        grid.add(n2Label, 0, 1);
        grid.add(aoiLabel, 0, 2);
        grid.add(n1Text, 1, 0);
        grid.add(n2Text, 1, 1);
        grid.add(aoiText, 1, 2);
        grid.add(submit, 0, 3);
    }
    
    /**
     * Displays the view where the light moves through the mediums
     */
    private void showLight(Stage primaryStage, double ni, double nr, double aoi) {
        // Set up view for the light
        GraphicsContext gc;
        Group root = new Group();    
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT+100);
        gc = canvas.getGraphicsContext2D();
        
        Button change = new Button("Change properties");
        change.setOnAction((ActionEvent e) -> {
            showInput(primaryStage, ni, nr, aoi);
        });
        
        // Draw medium and border
        gc.setFill(Color.BLUE);
        gc.fillRect(CANVAS_WIDTH/2, 0, Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0, CANVAS_HEIGHT, CANVAS_WIDTH, CANVAS_HEIGHT);
        
                
        
        // Perform calculations and output to the view
        Calculator calc = new Calculator(ni, nr, aoi, gc);
        calc.drawLight();
        

        root.getChildren().add(canvas);  
        root.getChildren().add(change);
        primaryStage.setScene(new Scene(root));   
        primaryStage.show();
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
