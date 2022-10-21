package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
//main character for the game, which is an extension of the GameObject class
public class Player extends GameObject
{
    public static final double PIXELS_PER_SECOND = 200;

    private final Paint paint;
    public static final double MAX_SPEED = PIXELS_PER_SECOND/GameLoop.MAX_UPDATES;
    private final Joystick joystick;
    Bitmap frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8,
            idleFrame1, idleFrame2, idleFrame3, idleFrame4, idleFrame5, idleFrame6;
    int frameCounter =0;
    int idleFrameCounter =0;
    public boolean moving = false;
    public boolean idle = true;
    private long startTime;

    public Player(Context ignoredContext, Joystick joystick, double posX, double posY, Resources res)
    {
        super(posX, posY);
        this.joystick = joystick;

        startTime = System.currentTimeMillis();

        frame1 = BitmapFactory.decodeResource(res, R.drawable.tile000);
        frame2 = BitmapFactory.decodeResource(res, R.drawable.tile001);
        frame3 = BitmapFactory.decodeResource(res, R.drawable.tile002);
        frame4 = BitmapFactory.decodeResource(res, R.drawable.tile003);
        frame5 = BitmapFactory.decodeResource(res, R.drawable.tile004);
        frame6 = BitmapFactory.decodeResource(res, R.drawable.tile005);
        frame7 = BitmapFactory.decodeResource(res, R.drawable.tile006);
        frame8 = BitmapFactory.decodeResource(res, R.drawable.tile007);

        idleFrame1 = BitmapFactory.decodeResource(res, R.drawable.idle1);
        idleFrame2 = BitmapFactory.decodeResource(res, R.drawable.idle2);
        idleFrame3 = BitmapFactory.decodeResource(res, R.drawable.idle3);
        idleFrame4 = BitmapFactory.decodeResource(res, R.drawable.idle4);
        idleFrame5 = BitmapFactory.decodeResource(res, R.drawable.idle5);
        idleFrame6 = BitmapFactory.decodeResource(res, R.drawable.idle6);

        frame1 = Bitmap.createScaledBitmap(frame1, 192, 288, false);
        frame2 = Bitmap.createScaledBitmap(frame2, 192, 288, false);
        frame3 = Bitmap.createScaledBitmap(frame3, 192, 288, false);
        frame4 = Bitmap.createScaledBitmap(frame4, 192, 288, false);
        frame5 = Bitmap.createScaledBitmap(frame5, 192, 288, false);
        frame6 = Bitmap.createScaledBitmap(frame6, 192, 288, false);
        frame7 = Bitmap.createScaledBitmap(frame7, 192, 288, false);
        frame8 = Bitmap.createScaledBitmap(frame8, 192, 288, false);

        idleFrame1 = Bitmap.createScaledBitmap(idleFrame1, 192, 288, false);
        idleFrame2 = Bitmap.createScaledBitmap(idleFrame2, 192, 288, false);
        idleFrame3 = Bitmap.createScaledBitmap(idleFrame3, 192, 288, false);
        idleFrame4 = Bitmap.createScaledBitmap(idleFrame4, 192, 288, false);
        idleFrame5 = Bitmap.createScaledBitmap(idleFrame5, 192, 288, false);
        idleFrame6 = Bitmap.createScaledBitmap(idleFrame6, 192, 288, false);

        paint = new Paint();
    }
    public void draw(Canvas canvas)
    {
        if(getIsMoving())
        {
            canvas.drawBitmap(getWalkFrame(), (float) posX, (float) posY, paint);
        }
        else
        {
            canvas.drawBitmap(getIdleFrame(), (float) posX, (float) posY, paint);
        }
    }

    //sets the idle animation for the player
    private Bitmap getIdleFrame()
    {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if(elapsedTime >=120)
        {
            idleFrameCounter+=1;
            startTime = System.currentTimeMillis();
        }
        if (idleFrameCounter == 0 && getIsIdle())
        {
            return idleFrame2;
        }
        if (idleFrameCounter ==1 && getIsIdle())
        {
            return idleFrame3;
        }
        if (idleFrameCounter ==2 && getIsIdle())
        {
            return idleFrame4;
        }
        if (idleFrameCounter ==3 && getIsIdle())
        {
            return idleFrame5;
        }
        if (idleFrameCounter ==4 && getIsIdle())
        {
            return idleFrame6;
        }
        idleFrameCounter-=4;
        return idleFrame1;
    }
    //sets the walking animation for the player
    private Bitmap getWalkFrame()
    {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if(elapsedTime >=120)
        {
            frameCounter+=1;
            startTime = System.currentTimeMillis();
        }
        if (frameCounter ==0 && getIsMoving())
        {
            return frame2;
        }
        if (frameCounter ==1 && getIsMoving())
        {
            return frame3;
        }
        if (frameCounter ==2 && getIsMoving())
        {
            return frame4;
        }
        if (frameCounter ==3 && getIsMoving())
        {
            return frame5;
        }
        if (frameCounter ==4 && getIsMoving())
        {
            return frame6;
        }
        if (frameCounter ==5 && getIsMoving())
        {
            return frame7;
        }
        if (frameCounter ==6 && getIsMoving())
        {
            return frame8;
        }
        frameCounter-=6;
        return frame1;
    }

    public void update()
    {
        //updates velocity based on joystick inputs
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        //updates player position
        posX += velocityX;
        posY += velocityY;
    }

    public void isMoving(boolean moving) { this.moving = moving; }
    public void isIdle(boolean idle)
    {
        this.idle = idle;
    }
    public boolean getIsMoving()
    {
        return moving;
    }
    public boolean getIsIdle() { return  idle; }
}
