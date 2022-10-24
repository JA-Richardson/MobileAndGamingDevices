package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private final GameLoop gameLoop;
    private final List<Enemy> enemyList = new ArrayList<>();
    private final List<Spell> spellList = new ArrayList<>();
    private int enemyCount = 0;


    public Game(Context context) {
        super(context);

        //Gets surface holder and adds the callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        joystick = new Joystick(250, 800, 100, 40);
        gameLoop = new GameLoop(this, surfaceHolder);

        player = new Player(getContext(),joystick, 500, 500, getResources());

        setFocusable(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (joystick.getIsPressed()) //pressed before this event, so cast spell
                {
                    spellList.add(new Spell(getContext(), player, player.posX, player.posY, getResources()));
                }
                else if(joystick.isPressed(event.getX(), event.getY())) //pressed during this event so set pressed to rue
                {
                    joystick.setIsPressed(true);
                    player.isIdle(true);
                }
                else//not previous pressed and not pressed now, cast spell
                {
                    spellList.add(new Spell(getContext(), player, player.posX, player.posY, getResources()));
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()) //previously pressed and now being moved
                {
                    joystick.setActuator(event.getX(), event.getY());
                    player.isIdle(false);
                    player.isMoving(true);
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                player.isMoving(false);
                player.isIdle(true);
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
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
        drawUpdates(canvas);
        drawFrames(canvas);
        joystick.draw(canvas);
        player.draw(canvas);
        for(Enemy enemy : enemyList)
        {
            enemy.draw(canvas);
        }
        for(Spell spell : spellList)
        {
            spell.draw(canvas);
        }
    }

    public void drawUpdates(Canvas canvas)
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

    public void update()
    {
        joystick.update();
        player.update();
        //Iterator<Spell> spellIterator = spellList.iterator();
        if (Enemy.spawnReady() && enemyCount < 5)
        {
            enemyList.add(new Enemy(getContext(), player, Math.random()*1000, Math.random()*1000, getResources()));
            enemyCount++;
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

            }

            spell.update();




        }
        if (enemyList.removeIf(enemy -> isColliding(enemy, player)))
        {
            enemyCount--;
        }

    }
}
