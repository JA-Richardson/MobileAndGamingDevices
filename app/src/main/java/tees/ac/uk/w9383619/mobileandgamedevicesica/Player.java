package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

//main character for the game, which is an extension of the GameObject class
public class Player extends GameObject {
    public static final double PIXELS_PER_SECOND = 200;
    public static final int MAX_HEALTH = 100;
    private Sensor sensor;

    private final Paint paint;
    public static final double MAX_SPEED = PIXELS_PER_SECOND / GameLoop.MAX_UPDATES;
    private final Joystick joystick;

    Bitmap frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8,
            idleFrame1, idleFrame2, idleFrame3, idleFrame4, idleFrame5, idleFrame6;

    public boolean moving = false;
    public boolean idle = true;
    public boolean ultActive = false;
    private long startTime;
    private final HealthBar healthBar;
    private int currentHealth;
    //private final int currentLevel = 1;
    private long dodgeCooldown = 5000;
    private long ultCooldown = 300000;
    private int screenheight;
    private int screenwidth;
    Bitmap[] walkFrames = new Bitmap[8];
    Bitmap[] idleFrames = new Bitmap[6];
    private int currentwalkframe = 0;
    private int currentidleframe = 0;

    public Player(Context ignoredContext, Joystick joystick, double posX, double posY, Resources res) {
        super(posX, posY);
        this.joystick = joystick;
        this.healthBar = new HealthBar(ignoredContext, this);
        this.currentHealth = MAX_HEALTH;
        sensor = new Sensor(ignoredContext);

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

        walkFrames[0] = Bitmap.createScaledBitmap(frame1, 192, 288, false);
        walkFrames[1] = Bitmap.createScaledBitmap(frame2, 192, 288, false);
        walkFrames[2] = Bitmap.createScaledBitmap(frame3, 192, 288, false);
        walkFrames[3] = Bitmap.createScaledBitmap(frame4, 192, 288, false);
        walkFrames[4] = Bitmap.createScaledBitmap(frame5, 192, 288, false);
        walkFrames[5] = Bitmap.createScaledBitmap(frame6, 192, 288, false);
        walkFrames[6] = Bitmap.createScaledBitmap(frame7, 192, 288, false);
        walkFrames[7] = Bitmap.createScaledBitmap(frame8, 192, 288, false);

        idleFrames[0] = Bitmap.createScaledBitmap(idleFrame1, 192, 288, false);
        idleFrames[1] = Bitmap.createScaledBitmap(idleFrame2, 192, 288, false);
        idleFrames[2] = Bitmap.createScaledBitmap(idleFrame3, 192, 288, false);
        idleFrames[3] = Bitmap.createScaledBitmap(idleFrame4, 192, 288, false);
        idleFrames[4] = Bitmap.createScaledBitmap(idleFrame5, 192, 288, false);
        idleFrames[5] = Bitmap.createScaledBitmap(idleFrame6, 192, 288, false);

        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        healthBar.draw(canvas);
        if (getIsMoving()) {
            canvas.drawBitmap(getWalkFrame(), (float) posX, (float) posY, paint);
        } else {
            canvas.drawBitmap(getIdleFrame(), (float) posX, (float) posY, paint);
        }
        screenheight = canvas.getHeight();
        screenwidth = canvas.getWidth();
    }

    //sets the idle animation for the player
    private Bitmap getIdleFrame()
    {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime >= 120)
        {
            currentidleframe++;
            if(currentidleframe > idleFrames.length-1)
            {
                currentidleframe = 0;
            }
            startTime = System.currentTimeMillis();
        }
        return idleFrames[currentidleframe];
    }

    //sets the walking animation for the player
    private Bitmap getWalkFrame()
    {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime >= 120)
        {
            currentwalkframe++;
            if(currentwalkframe > walkFrames.length-1)
            {
                currentwalkframe = 0;
            }
            startTime = System.currentTimeMillis();
        }
        return walkFrames[currentwalkframe];
    }

    public void update() {
        //updates velocity based on joystick inputs
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        //updates player position
        posX += velocityX;
        posY += velocityY;
        //update direction based on velocity
        if (velocityX != 0 || velocityY != 0) {
            double distance = Utility.distanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX / distance;
            directionY = velocityY / distance;
        }
        if(posX <= 0)
        {
            posX = 0;
        }
        if (posX > screenwidth - 192)
        {
            posX = screenwidth - 192;
        }
        if (posY <=0)
        {
            posY = 0;
        }
        if (posY > screenheight - 288)
        {
            posY = screenheight - 288;
        }
    }

    public void isMoving(boolean moving) {
        this.moving = moving;
    }

    public void isIdle(boolean idle) {
        this.idle = idle;
    }

    public boolean getIsMoving() {
        return moving;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void dodgeRight()
    {
        startTime = System.currentTimeMillis();
        if (startTime - dodgeCooldown >= 5000)
        {
            posX += 500;
            dodgeCooldown = startTime;
        }
        else
            System.out.println("Dodge cooldown has: " + (5000 - (startTime - dodgeCooldown)) + "ms to go" );

    }

    public void dodgeLeft()
    {
        startTime = System.currentTimeMillis();
        if (startTime - dodgeCooldown >= 5000)
        {
            posX -= 500;
            dodgeCooldown = startTime;
        }
        else
            System.out.println("Dodge cooldown has: " + (5000 - (startTime - dodgeCooldown)) + "ms to go" );

    }

    public void dodgeUp()
    {
        startTime = System.currentTimeMillis();
        if (startTime - dodgeCooldown >= 5000)
        {
            posY -= 500;
            dodgeCooldown = startTime;
        }
        else
            System.out.println("Dodge cooldown has: " + (5000 - (startTime - dodgeCooldown)) + "ms to go" );

    }
    public void dodgeDown()
    {
        startTime = System.currentTimeMillis();
        if (startTime - dodgeCooldown >= 5000)
        {
            posY += 500;
            dodgeCooldown = startTime;
        }
        else
            System.out.println("Dodge cooldown has: " + (5000 - (startTime - dodgeCooldown)) + "ms to go" );

    }

    public void ultimate()
    {
        startTime = System.currentTimeMillis();
        if(startTime - ultCooldown >= 300000)
        {
            currentHealth = 300;
            ultCooldown = startTime;
        }
        else
            System.out.println("Ultimate cooldown has: " + (300000 - (startTime - ultCooldown))+ "ms to go");
    }
}
