package light;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This is the model for all the calculations of where points are and angles.
 * 
 */
public class Calculator {
    final private double ni; // index of refraction for first object
    final private double nr; // index of refraction for second object
    final private double angleOfIncidence;   
    
    // X and Y points for light source
    final private int sx = 100;
    final private int sy = 100;

    
    private GraphicsContext gc = Light.gc;
    
    
    public Calculator(double ni, double nr, double angleOfIncidence) {
        this.ni = ni;
        this.nr = nr;
        this.angleOfIncidence = angleOfIncidence;
    }
    
    
    /**
     * Draws the light going through the 2 mediums
     */
    public void drawLight() {
        gc.setStroke(Color.BLACK);
                
        drawIncidenceLine();
        drawRefractionLine();
        drawNormalLine();
    }
    
    /**
     * draws the normal line
     */
    private void drawNormalLine() {
        for (int x=0; x<=Light.CANVAS_WIDTH; x+=50) {
            gc.strokeLine(x, getContactY(), x+25, getContactY());
        }
    }
    
    /** 
     * draws incidence line
     */
    private void drawIncidenceLine() {
        gc.strokeLine(sx, sy, getMX(), getContactY());
    }
    
    /**
     * draws the refraction line
     */
    private void drawRefractionLine() {
        // Find point along line going through medium
        double refractionRadians = Math.toRadians(getAngleOfRefraction());
        double endingY = getMX() * Math.tan(refractionRadians) + getContactY();
        
        gc.strokeLine(getMX(), getContactY(), Light.CANVAS_WIDTH, endingY);
    }
    
    
    private double getContactY() {
        double displacementX = getMX() - sx;
        double incidenceRadians = Math.toRadians(angleOfIncidence);
        return Math.tan(incidenceRadians) * displacementX + sy;
    }
    
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
        return Light.CANVAS_WIDTH / 2;
    }
}
