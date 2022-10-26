package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Experience {

    private final Context context;
    public float currentExp;
    private float expToNextLevel;
    private Player player;


    public Experience(Context context)
    {
        this.context = context;
    }

    public void draw(Canvas canvas)
    {
        String text = "Money: " + currentExp;

        float posX = 1500;
        float posY = 100;
        Paint paint = new Paint();
        int colour = ContextCompat.getColor(context, R.color.money);
        paint.setColor(colour);
        float textSize = 50;
        paint.setTextSize(textSize);
        canvas.drawText(text, posX, posY, paint);
    }

    public void expToNextLevel()
    {
        expToNextLevel = player.getCurrentLevel() * 10;
    }
}
