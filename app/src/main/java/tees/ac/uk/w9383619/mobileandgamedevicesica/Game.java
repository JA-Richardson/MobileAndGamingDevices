package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private GameLoop gameLoop;


    public Game(Context context) {
        super(context);

        //Gets surafce holder and adds the callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        joystick = new Joystick(100, 700, 75, 30);
        gameLoop = new GameLoop(this, surfaceHolder);

        player = new Player(getContext(), 500, 500, 30);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                    if(joystick.isPressed((double)event.getX(), (double)event.getY()));
            {
                joystick.setIsPressed(true);
            }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed())
                {
                    joystick.setActuator((double)event.getX(), (double)event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
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
    public void update()
    {
        joystick.update();
        player.update(joystick);
    }
}

