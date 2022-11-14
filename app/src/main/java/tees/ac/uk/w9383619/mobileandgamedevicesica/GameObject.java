package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.graphics.Canvas;

public abstract class GameObject
{

    protected double posX;
    protected double posY;
    protected double velocityX;
    protected double velocityY;
    protected double directionX;
    protected double directionY;

    public GameObject(double posX, double posY)
    {
        this.posX = posX;
        this.posY = posY;
    }

    protected static double getDistanceBetweenObjects(GameObject object1, GameObject object2)
    {
        return Math.sqrt(Math.pow(object2.getPosX()-object1.getPosX(), 2) + Math.pow(object2.getPosY() - object1.getPosY(),2));
    }

    public abstract void draw(Canvas canvas, GameDisplay gameDisplay);

    public abstract void update();


    protected double getPosX() {return posX;}
    protected double getPosY() {return posY;}

    protected double getDirectionX() {
        return directionX;
    }

    protected double getDirectionY() {
        return directionY;
    }
}
