package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick
{
    private final int outerCirclePosX;
    private final int outerCirclePosY;
    private int innerCirclePosX;
    private int innerCirclePosY;
    private final int outerCircleRadius;
    private final int innerCircleRadius;

    private final Paint outerPaint;
    private final Paint innerPaint;

    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;

    public Joystick(int centrePosX, int centrePosY, int outerCircleRadius, int innerCircleRadius)
    {
        outerCirclePosX = centrePosX;
        outerCirclePosY = centrePosY;
        innerCirclePosX = centrePosX;
        innerCirclePosY = centrePosY;

        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        outerPaint = new Paint();
        outerPaint.setColor(Color.GRAY);
        outerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        outerPaint.setAlpha(60);

        innerPaint = new Paint();
        innerPaint.setColor(Color.DKGRAY);
        innerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCirclePosX, outerCirclePosY, outerCircleRadius, outerPaint);
        canvas.drawCircle(innerCirclePosX, innerCirclePosY, innerCircleRadius, innerPaint);
    }

    public void update()
    {
        updateInnerPos();
    }

    private void updateInnerPos()
    {
        innerCirclePosX = (int) (outerCirclePosX + actuatorX*outerCircleRadius);
        innerCirclePosY = (int) (outerCirclePosY + actuatorY*outerCircleRadius);
    }
    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isPressed(double touchPosX, double touchPosY)
    {
        double centreToTouchDistance;
        centreToTouchDistance = Math.sqrt(Math.pow(outerCirclePosX - touchPosX, 2) + Math.pow(outerCirclePosY - touchPosY,2));
        return centreToTouchDistance < outerCircleRadius;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchPosX, double touchPosY) {
        double deltaX = touchPosX - outerCirclePosX;
        double deltaY = touchPosY - outerCirclePosY;
        double deltaDistance = Math.sqrt((Math.pow(deltaX,2) + Math.pow(deltaY,2)));

        if(deltaDistance < outerCircleRadius)
        {
            actuatorX = deltaX/outerCircleRadius;
            actuatorY = deltaY/outerCircleRadius;
        }
        else
        {
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }


    public void resetActuator()
    {
        actuatorX = 0;
        actuatorY = 0;
    }

    public double getActuatorX() {
        return actuatorX;
    }
    public double getActuatorY() {
        return actuatorY;
    }
}
