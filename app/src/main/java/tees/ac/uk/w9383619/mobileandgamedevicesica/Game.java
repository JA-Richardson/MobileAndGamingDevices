package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private final GameLoop gameLoop;
    private final List<Enemy> enemyList = new ArrayList<>();
    private final List<Spell> spellList = new ArrayList<>();
    private final Money money  ;
    private int enemyCount = 0;
    private int joystickPointerID = 0;
    private int numberOfSpellsCast = 0;
    private final GameOver gameOver;
    private VelocityTracker velocityTracker = null;
    private final Sensor sensor;
    public Bitmap bg;


    public Game(Context context) {
        super(context);
        //Gets surface holder and adds the callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        gameLoop = new GameLoop(this, surfaceHolder);
        //User interface initialisation
        joystick = new Joystick(250, 800, 100, 40);
        gameOver = new GameOver(getContext());
        money = new Money(getContext());
        //Player initialisation
        player = new Player(getContext(),joystick, 500, 500, getResources());
        sensor = new tees.ac.uk.w9383619.mobileandgamedevicesica.Sensor(context);
        //background initialisoon
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.pixelbg);
        bg = Bitmap.createScaledBitmap(bg, dm.widthPixels, dm.heightPixels, false);
        setFocusable(true);
        audio();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                if(velocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the
                    // velocity of a motion.
                    velocityTracker = VelocityTracker.obtain();
                }
                else {
                    // Reset the velocity tracker back to its initial state.
                    velocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                velocityTracker.addMovement(event);
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed()) //checks if joystick is pressed and allows player to fire
                {
                    numberOfSpellsCast++;
                }
                else if(joystick.isPressed(event.getX(), event.getY())) //sets joystick to the pressed state
                {
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                    player.isIdle(true);
                }
                else//if joystick isn't pressed, still allows player to fire
                {
                    numberOfSpellsCast++;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                Log.d("", "X Velocity: " + velocityTracker.getXVelocity(joystickPointerID));
                Log.d("", "Y Velocity: " + velocityTracker.getYVelocity(joystickPointerID));
                if(velocityTracker.getXVelocity() > 2500)
                {
                    player.dodge = 1;
                    player.dodge();
                }
                if(velocityTracker.getXVelocity() < -2500)
                {
                    player.dodge = 2;
                    player.dodge();
                }
                if(velocityTracker.getYVelocity() > 2500)
                {
                    player.dodge = 3;
                    player.dodge();
                }
                if(velocityTracker.getYVelocity() < -2500)
                {
                    player.dodge = 4;
                    player.dodge();
                }

                if(joystick.getIsPressed()) //checks for joystick being pressed then moves the player
                {
                    joystick.setActuator(event.getX(), event.getY());
                    player.isIdle(false);
                    player.isMoving(true);

                }
                return true;
            case MotionEvent.ACTION_UP:
                //velocityTracker.recycle();
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerID == event.getPointerId((event.getActionIndex())))
                {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    player.isMoving(false);
                    player.isIdle(true);
                }
                case MotionEvent.ACTION_CANCEL:
                    //velocityTracker.recycle();
                    return true;


        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {gameLoop.startLoop();}

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}

    @Override
    //draw function
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(bg,0, 0, null);

        joystick.draw(canvas);
        player.draw(canvas);
        money.draw(canvas);
        for(Enemy enemy : enemyList)
        {
            enemy.draw(canvas);
        }
        for(Spell spell : spellList)
        {
            spell.draw(canvas);
        }

        if(player.getCurrentHealth() <= 0)
        {
            gameOver.draw(canvas);

        }
    }
    //update function
    //checks if player is still alive, then checks for enemies to spawn
    //checks if any spells are ready to be cast, then performs all the update actions, then removes spells/enemies based on hit
    //final check is to see if the ult charge sensor is full
    public void update()
    {

        if (player.getCurrentHealth() <= 0)
            return;
        joystick.update();
        player.update();
        if (Enemy.spawnReady() && enemyCount < 5) //allows an enemy to be spawned if the value is true and there are less than 5 alive
        {
            enemyList.add(new Enemy(getContext(), player, Math.random()*1000, Math.random()*1000, getResources()));
            enemyCount++;
        }
        while (numberOfSpellsCast > 0)
        {
            spellList.add(new Spell(getContext(), player, player.posX, player.posY, getResources()));
            numberOfSpellsCast--;
        }
        for (Enemy enemy : enemyList)
        {
            enemy.update();
        }
        for (Spell spell : spellList)
        {
            if (enemyList.removeIf(enemy -> isSpellColliding(enemy, spell)))
            {
                enemyCount--;
                money.currentMoney+=5; //if enemy is killed by the player spell, adds 5 to the players gold
            }
            spell.update();
        }
        if (enemyList.removeIf(enemy -> isColliding(enemy, player)))
        {
            enemyCount--;
            player.setCurrentHealth(player.getCurrentHealth() -1);
        }
        if (sensor.chargeFull)
        {
            player.ultimate();
            sensor.charge = 0;
            sensor.chargeFull = false;
        }
    }

    static double getDistanceBetweenObjects(Enemy enemy, Player player)
    {
        return Math.sqrt(Math.pow(enemy.getPosX()-player.getPosX(), 2) + Math.pow(enemy.getPosY() - player.getPosY(),2));
    }
    static double getDistanceBetweenObjects(Enemy enemy, Spell spell)
    {
        return Math.sqrt(Math.pow(enemy.getPosX()-spell.getPosX(), 2) + Math.pow(enemy.getPosY() - spell.getPosY(),2));
    }
    private boolean isColliding(Enemy next, Player player)
    {
        double distance = getDistanceBetweenObjects(next, player);
        int collisionDistanceWidth = (player.frame1.getWidth() + next.frame1.getWidth())/4;
        int collisionDistanceHeight = (player.frame1.getHeight() + next.frame1.getHeight())/4;
        if (distance < collisionDistanceWidth) return true;
        return distance < collisionDistanceHeight;
    }
    private boolean isSpellColliding(Enemy next, Spell spell)
    {
        double distance = getDistanceBetweenObjects(next, spell);
        int collisionDistanceWidth = (spell.sprite.getWidth() + next.frame1.getWidth())/4;
        int collisionDistanceHeight = (spell.sprite.getHeight() + next.frame1.getHeight())/4;
        if (distance < collisionDistanceWidth) return true;
        return distance < collisionDistanceHeight;
    }
    public void pause()
    {
        gameLoop.stopLoop();
    }
    //loads and starts the bgm during gameplay
    public void audio()
    {
         MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.vilegrove);
        mediaPlayer.start();
    }


}
