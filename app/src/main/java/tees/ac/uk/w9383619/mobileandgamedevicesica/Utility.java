package tees.ac.uk.w9383619.mobileandgamedevicesica;

public class Utility {
    public static double distanceBetweenPoints(double p1x, double p1y, double p2x, double p2y) {
        return Math.sqrt(Math.pow(p1x - p2x, 2) + Math.pow(p1y - p2y, 2));
    }
}
