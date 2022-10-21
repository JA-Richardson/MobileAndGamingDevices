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
    private int frameCounter;

    public Enemy(Context ignoredContext, Player player, double posX, double posY, Resources res)
    {
        super(posX, posY);
        this.player = player;

        frame1 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_1);
        frame2 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_2);
        frame3 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_3);
        frame4 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_4);
        frame5 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_5);
        frame6 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_6);
        frame7 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_7);
        frame8 = BitmapFactory.decodeResource(res, R.drawable.warrior_run_8);

        frame1 = Bitmap.createScaledBitmap(frame1, 320, 220, false);
        frame2 = Bitmap.createScaledBitmap(frame2, 320, 220, false);
        frame3 = Bitmap.createScaledBitmap(frame3, 320, 220, false);
        frame4 = Bitmap.createScaledBitmap(frame4, 320, 220, false);
        frame5 = Bitmap.createScaledBitmap(frame5, 320, 220, false);
        frame6 = Bitmap.createScaledBitmap(frame6, 320, 220, false);
        frame7 = Bitmap.createScaledBitmap(frame7, 320, 220, false);
        frame8 = Bitmap.createScaledBitmap(frame8, 320, 220, false);


        
    }






    public static boolean spawnReady() {
        if (updatesUntilNextSpawn <=0)
        {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        }
        else{
            updatesUntilNextSpawn--;
            return false;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(getFrame(), (float) posX, (float) posY, new Paint());

    }

    private Bitmap getFrame() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if(elapsedTime >=120)
        {
            frameCounter+=1;
            startTime = System.currentTimeMillis();
        }
        if (frameCounter ==0)
        {
            return frame2;
        }
        if (frameCounter ==1)
        {
            return frame3;
        }
        if (frameCounter ==2)
        {
            return frame4;
        }
        if (frameCounter ==3)
        {
            return frame5;
        }
        if (frameCounter ==4)
        {
            return frame6;
        }
        if (frameCounter ==5)
        {
            return frame7;
        }
        if (frameCounter ==6)
        {
            return frame8;
        }
        frameCounter-=6;
        return frame1;
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
}
