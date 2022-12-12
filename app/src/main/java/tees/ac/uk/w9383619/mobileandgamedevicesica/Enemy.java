package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy extends GameObject{
    private static final double PIXELS_PER_SECOND = Player.PIXELS_PER_SECOND*0.6;
    private static final double MAX_SPEED = PIXELS_PER_SECOND/GameLoop.MAX_UPDATES;
    private static final double SPAWNS_PER_MINUTE = 2;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPDATES / SPAWNS_PER_MINUTE;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private final Player player;
    Bitmap frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8;
    private long startTime;
    Bitmap[] frames = new Bitmap[8];
    private int currentFrame = 0;

    public Enemy(Context ignoredContext, Player player, double posX, double posY, Resources res)
    {
        super(posX, posY);
        this.player = player;
        //loads sprites from resources folder
        frame1 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_1);
        frame2 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_2);
        frame3 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_3);
        frame4 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_4);
        frame5 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_5);
        frame6 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_6);
        frame7 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_7);
        frame8 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_8);
        //loads the sprites into the array
        frames[0] = Bitmap.createScaledBitmap(frame1, 320, 220, false);
        frames[1] = Bitmap.createScaledBitmap(frame2, 320, 220, false);
        frames[2] = Bitmap.createScaledBitmap(frame3, 320, 220, false);
        frames[3] = Bitmap.createScaledBitmap(frame4, 320, 220, false);
        frames[4] = Bitmap.createScaledBitmap(frame5, 320, 220, false);
        frames[5] = Bitmap.createScaledBitmap(frame6, 320, 220, false);
        frames[6] = Bitmap.createScaledBitmap(frame7, 320, 220, false);
        frames[7] = Bitmap.createScaledBitmap(frame8, 320, 220, false);

    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(getFrame(), (float) posX, (float) posY, new Paint());

    }
    @Override
    public void update() {
        //calc vector from enemy to player
        double distanceToPlayerX = player.getPosX() - posX;
        double distanceToPlayerY = player.getPosY() - posY;
        //calc absolute distance between enemy and player
        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);
        //calc direction to player
        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;
        //set velocity in direction of player
        velocityX = directionX*MAX_SPEED;
        velocityY = directionY*MAX_SPEED;
        //update position
        posX += velocityX;
        posY += velocityY;
    }

    //restricts enemy spawning based on the game updates and the amount of spawns allowed per minute
    public static boolean spawnReady() {
        if (updatesUntilNextSpawn <=0)//if value is below 0 it allows an enemy to spawn
        {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        }
        else//ticks down if the value is above 0
        {
            updatesUntilNextSpawn--;
            return false;
        }
    }
    //returns correct frame for animating the enemy
    private Bitmap getFrame() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if(elapsedTime >=120)
        {
            currentFrame++;
            if(currentFrame > frames.length -1)
            {
                currentFrame = 0;
            }
            startTime = System.currentTimeMillis();
        }
        return frames[currentFrame];
    }


}
