package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class GameOver {

    private final Context context;

    public GameOver(Context context)
    {
        this.context = context;
    }

    public void draw(Canvas canvas)
    {
        String text = "Game Over";

        float posX = 100;
        float posY = 100;

        Paint paint = new Paint();
        int colour = ContextCompat.getColor(context, R.color.gameOver);
        paint.setColor(colour);
        float textSize = 150;
        paint.setTextSize(textSize);
        canvas.drawText(text, posX, posY, paint);
    }


}
