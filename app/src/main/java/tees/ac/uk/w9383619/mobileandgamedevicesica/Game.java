package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
    private GameLoop gameLoop;
    private final List<Enemy> enemyList = new ArrayList<>();
    private final List<Spell> spellList = new ArrayList<>();
    private final Money money  ;
    private int enemyCount = 0;
    private int joystickPointerID = 0;
    private int numberOfSpellsCast = 0;
    private final GameOver gameOver;
    private VelocityTracker velocityTracker = null;
    private SensorManager sensorManager;
    private Sensor sensor;
    private GameDisplay gameDisplay;


    public Game(Context context) {
        super(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Gets surface holder and adds the callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        //User interface initialisation
        joystick = new Joystick(250, 800, 100, 40);
        gameOver = new GameOver(getContext());
        money = new Money(getContext());

        //Player initialisation
        SpriteSheet spriteSheet = new SpriteSheet(context);
        player = new Player(getContext(),joystick, displayMetrics.widthPixels/2, displayMetrics.heightPixels/2, getResources(), spriteSheet.getPlayer());
        sensor = new tees.ac.uk.w9383619.mobileandgamedevicesica.Sensor(context);

        //initialise gamee display and centre on player

        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }


    public void onSensorChanged(SensorEvent event){
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        //Log.d("", "Accel: " + sensor);
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
                if (joystick.getIsPressed()) //pressed before this event, so cast spell
                {
                    numberOfSpellsCast++;
                }
                else if(joystick.isPressed(event.getX(), event.getY())) //pressed during this event so set pressed to rue
                {
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                    player.isIdle(true);
                }
                else//not previous pressed and not pressed now, cast spell
                {
                    numberOfSpellsCast++;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                //Log.d("", "X Velocity: " + velocityTracker.getXVelocity(joystickPointerID));
                //Log.d("", "Y Velocity: " + velocityTracker.getYVelocity(joystickPointerID));
                if(velocityTracker.getXVelocity() > 2500)
                {
                    player.dodgeRight();
                }
                if(velocityTracker.getXVelocity() < -2500)
                {
                    player.dodgeLeft();
                }
                if(velocityTracker.getYVelocity() > 2500)
                {
                    player.dodgeDown();
                }
                if(velocityTracker.getYVelocity() < -2500)
                {
                    player.dodgeUp();
                }

                if(joystick.getIsPressed()) //previously pressed and now being moved
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
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {

        if(gameLoop.getState().equals(Thread.State.TERMINATED))
        {
            gameLoop = new GameLoop(this, surfaceHolder);
        }

        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //drawUpdates(canvas);
        //drawFrames(canvas);
        joystick.draw(canvas);
        player.draw(canvas, gameDisplay);
        money.draw(canvas);
        for(Enemy enemy : enemyList)
        {
            enemy.draw(canvas, gameDisplay);
        }
        for(Spell spell : spellList)
        {
            spell.draw(canvas,gameDisplay);
        }

        if(player.getCurrentHealth() <= 0)
        {
            gameOver.draw(canvas);
        }
    }

    /*public void drawUpdates(Canvas canvas)
    {
        String averageUpdates = Double.toString(gameLoop.getAverageUpdates());
        Paint paint = new Paint();
        int colour = ContextCompat.getColor(getContext(), R.color.red);
        paint.setColor(colour);
        paint.setTextSize(50);
        canvas.drawText("Updates: " + averageUpdates, 100, 100, paint);
    }

    public void drawFrames(Canvas canvas)
    {
        String averageFrames = Double.toString(gameLoop.getAverageFrames());
        Paint paint = new Paint();
        int colour = ContextCompat.getColor(getContext(), R.color.red);
        paint.setColor(colour);
        paint.setTextSize(50);
        canvas.drawText("Frames: " + averageFrames, 100, 200, paint);
    }*/

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

    public void update()
    {
        if (player.getCurrentHealth() <= 0)
            return;
        joystick.update();
        player.update();
        //Iterator<Spell> spellIterator = spellList.iterator();
        if (Enemy.spawnReady() && enemyCount < 5)
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
                money.currentMoney+=5;
            }
            spell.update();
        }
        if (enemyList.removeIf(enemy -> isColliding(enemy, player)))
        {
            enemyCount--;
            player.setCurrentHealth(player.getCurrentHealth() -1);
        }
        gameDisplay.update();
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
