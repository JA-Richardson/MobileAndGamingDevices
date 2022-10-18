package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Player
{
    private static final double PIXELS_PER_SECOND = 200;
    private double posX;
    private double posY;
    private double radius;
    private Paint paint;
    private double velocityX;
    private double velocityY;
    private static final double MAX_SPEED = PIXELS_PER_SECOND/GameLoop.MAX_UPDATES;


    public Player(Context context, double posX, double posY, double radius)
    {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;

        paint = new Paint();
        int colour = ContextCompat.getColor(context, R.color.player);
        paint.setColor(colour);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle((float)posX,(float) posY, (float)radius, paint);
    }

    public void update(Joystick joystick)
    {
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
        posX += velocityX;
        posY += velocityY;
    }

    public void setPosition(double posX, double posY)
    {
        this.posX = posX;
        this.posY = posY;
    }
}
