package tees.ac.uk.w9383619.mobileandgamedevicesica;

public class GameDisplay {
    private float offsetX;
    private float offsetY;
    private float displayCentreX;
    private float displayCentreY;
    private float gameCentreX;
    private float gameCentreY;
    private GameObject centreObject;

    public GameDisplay(int width, int height, GameObject centreObject)
    {
        this.centreObject = centreObject;
        displayCentreX = width/2.0f;
        displayCentreY = height/2.0f;
    }

    public void update()
    {
        gameCentreX = (float) centreObject.getPosX();
        gameCentreY = (float) centreObject.getPosY();

        offsetX = displayCentreX - gameCentreX;
        offsetY = displayCentreY - gameCentreY;
    }

    public Object displayCoordsX(float   X) {
        return X + offsetX;
    }

    public Object displayCoordsY(float Y) {
        return Y + offsetY;
    }
}
