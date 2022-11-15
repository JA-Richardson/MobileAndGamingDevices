package tees.ac.uk.w9383619.mobileandgamedevicesica;

public class GameDisplay {
    private double offsetX;
    private double offsetY;
    private double displayCentreX;
    private double displayCentreY;
    private double gameCentreX;
    private double gameCentreY;
    private GameObject centreObject;

    public GameDisplay(int width, int height, GameObject centreObject)
    {
        this.centreObject = centreObject;
        displayCentreX = width/2.0;
        displayCentreY = height/2.0;
    }

    public void update()
    {
        gameCentreX = centreObject.getPosX();
        gameCentreY = centreObject.getPosY();

        offsetX = displayCentreX - gameCentreX;
        offsetY = displayCentreY - gameCentreY;
    }

    public double displayCoordsX(double X) {
        return X + offsetX;
    }

    public double displayCoordsY(double Y) {
        return Y + offsetY;
    }
}
