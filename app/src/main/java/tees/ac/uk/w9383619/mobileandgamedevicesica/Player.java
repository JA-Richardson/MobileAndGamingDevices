package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    Bitmap frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8;
    int width, height, frameCounter = 0;
    public Player(Context context, double posX, double posY, double radius, Resources res)
    {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        //width = frame1.getWidth();
        //height = frame1.getHeight();

        frame1 = BitmapFactory.decodeResource(res, R.drawable.tile000);
        frame2 = BitmapFactory.decodeResource(res, R.drawable.tile001);
        frame3 = BitmapFactory.decodeResource(res, R.drawable.tile002);
        frame4 = BitmapFactory.decodeResource(res, R.drawable.tile003);
        frame5 = BitmapFactory.decodeResource(res, R.drawable.tile004);
        frame6 = BitmapFactory.decodeResource(res, R.drawable.tile005);
        frame7 = BitmapFactory.decodeResource(res, R.drawable.tile006);
        frame8 = BitmapFactory.decodeResource(res, R.drawable.tile007);

        frame1 = Bitmap.createScaledBitmap(frame1, 128, 192, false);
        frame2 = Bitmap.createScaledBitmap(frame2, 128, 192, false);
        frame3 = Bitmap.createScaledBitmap(frame3, 128, 192, false);
        frame4 = Bitmap.createScaledBitmap(frame4, 128, 192, false);
        frame5 = Bitmap.createScaledBitmap(frame5, 128, 192, false);
        frame6 = Bitmap.createScaledBitmap(frame6, 128, 192, false);
        frame7 = Bitmap.createScaledBitmap(frame7, 128, 192, false);
        frame8 = Bitmap.createScaledBitmap(frame8, 128, 192, false);

        paint = new Paint();
        int colour = ContextCompat.getColor(context, R.color.player);
        paint.setColor(colour);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(getFrame(), (float) posX, (float) posY, paint);
    }

    private Bitmap getFrame()
    {
        if (frameCounter ==0)
        {
            frameCounter++;
            return frame2;
        }
        if (frameCounter ==1)
        {
            frameCounter++;
            return frame3;
        }
        if (frameCounter ==2)
        {
            frameCounter++;
            return frame4;
        }
        if (frameCounter ==3)
        {
            frameCounter++;
            return frame5;
        }
        if (frameCounter ==4)
        {
            frameCounter++;
            return frame6;
        }
        if (frameCounter ==5)
        {
            frameCounter++;
            return frame7;
        }
        if (frameCounter ==6)
        {
            frameCounter++;
            return frame8;
        }
        frameCounter-=6;
        return frame1;


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
