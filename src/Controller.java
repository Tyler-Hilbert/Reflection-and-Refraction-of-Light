import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;


/**
 * The main for the program.
 * Also has 2 views, input and light, within it. ///// Is this still correct?
 * Acts as the controller for the program.
 */
public class Controller extends Application {
    static Model model;
    
    
    @Override
    public void start(Stage primaryStage) {
        // Declares default values and creates input view
        model = new Model();        
        new View(primaryStage);
    }
    
    /**
     * Updates the model with the newest values
     * @param ni updated ni
     * @param nr updated nr
     * @param aoi updated aoi
     */
    public static void updateModel(double ni, double nr, double aoi) {
        model.setNI(ni);
        model.setNR(nr);
        model.setAOI(aoi);
    }
    
    public static double getNI() {
        return model.getNI();
    }
       
   public static double getNR() {
        return model.getNR();
    }
        
    public static double getAOI() {
        return model.getAOI();
    }

    public static double getAngleOfRefraction() {
        return model.getAngleOfRefraction();
    }
    
    public static double getCriticalAngle() {
        return model.getCriticalAngle();
    }
    
    public static double getPReflection() {
        return model.getPReflection();
    }
    
    public static double getSReflection() {
        return model.getSReflection();
    }
    
    public static double getPTransmission() {
        return model.getPTransmission();
    }
    
    public static double getSTransmission() {
        return model.getSTransmission();
    }
       
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
