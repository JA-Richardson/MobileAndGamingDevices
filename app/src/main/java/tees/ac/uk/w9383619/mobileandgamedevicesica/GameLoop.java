package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread
{
    private final SurfaceHolder surfaceHolder;
    private final Game game;
    private boolean isRunning = false;
    public static final double MAX_UPDATES = 1000;
    private static final double UPDATE_PERIOD = 1E3/ MAX_UPDATES;

    public GameLoop(Game game, SurfaceHolder surfaceHolder)
    {
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }
    //starts the thread the games runs
    public void startLoop()
    {
        isRunning = true;
        start();
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run()
    {
        super.run();

        int updateCount = 0;

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
            }
            catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPDATE_PERIOD - elapsedTime);
            //sleeps the thread if trying to update too often
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
            //if thread isn't sleeping, updates the game and resets the sleep counter
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
                updateCount =0;
                startTime = System.currentTimeMillis();
            }


        }
    }
    //stops the thread
    public void stopLoop()
    {
        isRunning = false;
        try
        {
            join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
