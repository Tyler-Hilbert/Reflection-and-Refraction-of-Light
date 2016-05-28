/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tyler
 */
public class Model {
        // Declares default values and creates input view
        double ni = 2; // Index of refaction for medium 1
        double nr = 1.5; // Index of refraction for medium 2;
        double aoi = 30; // Angle of incidence
                
        public double getNI() {
            return ni;
        }
       
        public double getNR() {
            return nr;
        }
        
        public double getAOI() {
            return aoi;
        }
        
        public void setNI(double ni) {
            this.ni = ni;
        }
 
        public void setNR(double nr) {
            this.nr = nr;
        }
        
        public void setAOI(double aoi) {
            this.aoi = aoi;
        }
        
        /**
            * @return The angle that the light will travel through the second medium relative to the normal line.
        */
        public double getAngleOfRefraction() {
            double incidenceRadians = Math.toRadians(aoi);
            double sin = ni * Math.sin(incidenceRadians) / nr;
            double refractionRadians = Math.asin(sin);
            return Math.toDegrees(refractionRadians);
        }
        
        /**
            * @return The critical angle
        */
        public double getCriticalAngle() {
            return Math.toDegrees(Math.asin(nr/ni));
        }
}
