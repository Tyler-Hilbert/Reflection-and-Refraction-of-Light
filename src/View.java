import java.text.DecimalFormat;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
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
public class View {
    final static int CANVAS_HEIGHT = 800;
    final static int CANVAS_WIDTH = 800;

    // X and Y points for light source
    private int sx = 100;
    private int sy = 100;

    GraphicsContext gc;


    public View(Stage primaryStage) {
        // Setup windows details
        primaryStage.setTitle("Light Refraction");
        primaryStage.setX(0);
        primaryStage.setY(0);

        // Draw data onto window
        showInput(primaryStage);
    }

    /**
     * Displays the view where users can input the variables of the program
     */
    private void showInput(Stage primaryStage) {
        // Set up grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 275);
        primaryStage.setScene(scene);
        primaryStage.show();


        // Create and add gui components
        Label n1Label = new Label("Index of refracion (medium 1): ");
        Label n2Label = new Label("Index of refraction (medium 2): ");
        Label aoiLabel = new Label("Angle of incidence: ");

        TextField n1Text = new TextField(Double.toString(Controller.getNI()));
        TextField n2Text = new TextField(Double.toString(Controller.getNR()));
        TextField aoiText = new TextField(Double.toString(Controller.getAOI()));

        Button submit = new Button("update");
        submit.setOnAction((ActionEvent e) -> {
            try {
                final double inputNi = Double.parseDouble(n1Text.getText());
                final double inputNr = Double.parseDouble(n2Text.getText());
                final double inputAoi = Double.parseDouble(aoiText.getText());

                // Validate the angle
                if (inputAoi < 1 || inputAoi > 85) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid input");
                    alert.setHeaderText("Please enter an angle between 1 and 85");
                    alert.showAndWait();
                } else {
                    Controller.updateModel(inputNi, inputNr, inputAoi); // Update Model
                    showLight(primaryStage); // Update View
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid input");
                alert.setHeaderText("Please enter a valid number in all text fields");
                alert.showAndWait();
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
    private void showLight(Stage primaryStage) {
        // Set up view for the light
        Group root = new Group();
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT+125);
        gc = canvas.getGraphicsContext2D();

        Button change = new Button("Change properties");
        change.setOnAction((ActionEvent e) -> {
            showInput(primaryStage);
        });

        // Draw medium and border
        gc.setFill(Color.BLUE);
        gc.fillRect(CANVAS_WIDTH/2, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0, CANVAS_HEIGHT, CANVAS_WIDTH, CANVAS_HEIGHT);


        drawLight();
        

        root.getChildren().add(canvas);
        root.getChildren().add(change);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
}


     /**
     * draws incidence line
     */
    private void drawIncidenceLine() {
        double aoi = Controller.getAOI();
        // Update light source location on screen to prevent angles from going off the screen if there is a high angle off incidence
        if (aoi > 80) {
            sx = 380;
        }else if (aoi > 70) {
            sx = 300;
        } else if (aoi > 60) {
            sx = 200;
        }
        else {
            sx = 100;
        }

        // Draws line
        gc.strokeLine(sx, sy, getMX(), getContactY());
    }

    /**
     * draws the refraction line
     */
    private void drawRefractionLine() {
        // Find point along line going through medium
        double refractionRadians = Math.toRadians(Controller.getAngleOfRefraction());
        double endingY = getMX() * Math.tan(refractionRadians) + getContactY();

        gc.strokeLine(getMX(), getContactY(), CANVAS_WIDTH, endingY);
    }

    /**
     * Draws the reflected line.
     * This only happens when the angle of incidence is over the critical angle.
     */
    private void drawReflectedLine() {
       gc.setStroke(Color.RED);

       double xr = CANVAS_WIDTH; // Large number to make sure the reflected ray is long enough
       double angleOfReflection = Controller.getAOI();       // The ray is always going to be reflected at an equal angle as the incidence ray
       double yr = Math.tan(Math.toRadians(angleOfReflection)) * xr;

       // Move the triangle created to the create location
       double xe = getMX() - xr;
       double ye = getContactY() + yr;

       gc.strokeLine(getMX(), getContactY(), xe, ye);
    }

    /**
     * Outputs the values to the view
     */
    private void outputValues() {
        gc.setFill(Color.BLACK);

        gc.fillText("Angle of incidence: " + Controller.getAOI(), 15, CANVAS_HEIGHT + 15);

        // Display angle of refraction or critical angle
        DecimalFormat format = new DecimalFormat("#.####");
        double angleOfRefraction = Controller.getAngleOfRefraction();
        if (!Double.isNaN(angleOfRefraction)) {
            gc.fillText("Angle of refraction: " + format.format(angleOfRefraction), 15, CANVAS_HEIGHT + 30);
        } else {
            drawReflectedLine();
            gc.fillText("Critical angle: " + format.format(Controller.getCriticalAngle()), 15, CANVAS_HEIGHT + 30);
        }
        gc.fillText("Index of refraction (mediums 1): " + format.format(Controller.getNI()), 15, CANVAS_HEIGHT + 45);
        gc.fillText("Index of refraction (mediums 2): " + format.format(Controller.getNR()), 15, CANVAS_HEIGHT + 60);

        gc.fillText("Reflection coefficent for P polarization: " + format.format(Controller.getPReflectionCoefficient())
                + " & S polarization " + format.format(Controller.getSReflectionCoefficient()), 15, CANVAS_HEIGHT + 75);
        gc.fillText("Transmission coefficents for P polarizatoin: " + format.format(Controller.getPTransmissionCoefficient())
                + " & S polarization" + format.format(Controller.getSTransmissionCoefficient()), 15, CANVAS_HEIGHT + 90);


        //  THESE CALCULATIONS AREN'T CORRECT AND NEED TO BE DEBUGED------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        gc.fillText("Percents: " + format.format(Controller.getPReflectionPercent()) + "% : " + format.format(Controller.getSReflectionPercent())
                + "% : " + format.format(Controller.getPTransmissionPercent()) + "% : " + format.format(Controller.getSTransmissionPercent()) + "%", 15, CANVAS_HEIGHT + 105);
    }



    /**
     * @return The y value of where the light meets between the 2 mediums
     */
    private double getContactY() {
        double displacementX = getMX() - sx; // The x distance the light will travel to hit the middle x location
        double incidenceRadians = Math.toRadians(Controller.getAOI());
        return Math.tan(incidenceRadians) * displacementX + sy;
    }


    /**
     * @return the middle of the x, where the lens center is
     */
    public int getMX() {
        return CANVAS_WIDTH / 2;
    }


    /**
     * draws the normal line
     */
    private void drawNormalLine() {
        for (int x=0; x<=CANVAS_WIDTH; x+=50) {
            gc.strokeLine(x, getContactY(), x+25, getContactY());
        }
    }

    /**
     * Draws the light going through the 2 mediums
     */
    public void drawLight() {
        gc.setStroke(Color.BLACK);

        drawIncidenceLine();
        drawRefractionLine();
        drawNormalLine();
        outputValues();
    }
}
