import java.text.DecimalFormat;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This is the model for all the calculations of where points are and angles.
 * It also draws to the light view.
 */
public class Calculator {
    final private double ni; // index of refraction for first object
    final private double nr; // index of refraction for second object
    final private double angleOfIncidence; // The angle the light has going into the 2nd medium relative to the normal line.
    
    // X and Y points for light source
    private int sx = 100;
    private int sy = 100;
    
    private GraphicsContext gc; // The view
    
    
    public Calculator(double ni, double nr, double angleOfIncidence, GraphicsContext gc) {
        this.ni = ni;
        this.nr = nr;
        this.angleOfIncidence = angleOfIncidence;
        this.gc = gc;
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
    
    /**
     * draws the normal line
     */
    private void drawNormalLine() {
        for (int x=0; x<=Main.CANVAS_WIDTH; x+=50) {
            gc.strokeLine(x, getContactY(), x+25, getContactY());
        }
    }
    
    /** 
     * draws incidence line
     */
    private void drawIncidenceLine() {
        // Update light source location on screen to prevent angles from going off the screen if there is a high angle off incidence
        if (angleOfIncidence > 80) {
            sx = 380;
        }else if (angleOfIncidence > 70) {
            sx = 300;
        } else if (angleOfIncidence > 60) {
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
        double refractionRadians = Math.toRadians(getAngleOfRefraction());
        double endingY = getMX() * Math.tan(refractionRadians) + getContactY();
        
        gc.strokeLine(getMX(), getContactY(), Main.CANVAS_WIDTH, endingY);
    }
    
    /**
     * Draws the reflected line.
     * This only happens when the angle of incidence is over the critical angle.
     */
    private void drawReflectedLine() {
       gc.setStroke(Color.RED);
            
       // Creaete triangle and use pythagrom theroy to draw reflected line
       double xr = Main.CANVAS_WIDTH; // Large number to make sure the reflected ray is long enough
       // The ray is always going to be reflected at an equal angle as the incidence ray
       double angleOfReflection = angleOfIncidence;
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
        
        gc.fillText("Angle of incidence: " + angleOfIncidence, 15, Main.CANVAS_HEIGHT + 15);
        
        // Display angle of refraction or critical angle
        DecimalFormat format = new DecimalFormat("###.###");
        if (!Double.isNaN(getAngleOfRefraction())) {
            gc.fillText("Angle of refraction: " + format.format(getAngleOfRefraction()), 15, Main.CANVAS_HEIGHT + 30);
        } else {
            drawReflectedLine();
            gc.fillText("Critical angle: " + format.format(getCriticalAngle()), 15, Main.CANVAS_HEIGHT + 30);
        }
        gc.fillText("Index of refraction (mediums 1): " + ni, 15, Main.CANVAS_HEIGHT + 45);
        gc.fillText("Index of refraction (mediums 2): " + nr, 15, Main.CANVAS_HEIGHT + 60);
        
    }
    
    /**
     * @return The critical angle
     */
    private double getCriticalAngle() {
            return Math.toDegrees(Math.asin(nr/ni));
    }
    
    /** 
     * @return The y value of where the light meets between the 2 mediums
     */
    private double getContactY() {
        double displacementX = getMX() - sx;
        double incidenceRadians = Math.toRadians(angleOfIncidence);
        return Math.tan(incidenceRadians) * displacementX + sy;
    }
    
    /**
     * @return The angle that the light will travel through the second medium relative to the normal line.
     */
    public double getAngleOfRefraction() {
        double incidenceRadians = Math.toRadians(angleOfIncidence);
        double sin = ni * Math.sin(incidenceRadians) / nr;
        double refractionRadians = Math.asin(sin);
        return Math.toDegrees(refractionRadians);
    }
    
    /**
     * @return the middle of the x, where the lens center is
     */
    public int getMX() {
        return Main.CANVAS_WIDTH / 2;
    }
}
