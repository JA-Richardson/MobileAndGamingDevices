package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread
{
    private SurfaceHolder surfaceHolder;
    private Game game;
    private boolean isRunning = false;
    private double averageUpdates;
    private double averageFrames;
    public static final double MAX_UPDATES = 60;
    private static final double UPDATE_PERIOD = 1E+3/ MAX_UPDATES;

    public GameLoop(Game game, SurfaceHolder surfaceHolder)
    {
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    public double getAverageUpdates() {
        return averageUpdates;
    }

    public double getAverageFrames() {
        return averageFrames;
    }

    public void startLoop()
    {
        isRunning = true;
        start();
    }

    @Override
    public void run()
    {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        //game loop
        Canvas canvas;
        startTime = System.currentTimeMillis();
        while (isRunning)
        {
            //update and render game
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    game.update();
                    updateCount++;
                    game.draw(canvas);
                }

                surfaceHolder.unlockCanvasAndPost(canvas);
                frameCount++;
            }
            catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPDATE_PERIOD - elapsedTime);
            if(sleepTime > 0)
            {
                try
                {
                    sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            while (sleepTime < 0 && updateCount < MAX_UPDATES -1)
            {
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPDATE_PERIOD - elapsedTime);
            }
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000)
            {
                averageUpdates = updateCount / (1E-3 * elapsedTime);
                averageFrames = frameCount / (1E-3 * elapsedTime);
                updateCount =0;
                frameCount =0;
                startTime = System.currentTimeMillis();
            }


        }
    }
}
