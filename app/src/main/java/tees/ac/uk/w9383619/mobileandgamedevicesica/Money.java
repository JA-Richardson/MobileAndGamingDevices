package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Money {

    private final Context context;
    public int currentMoney;

    public Money(Context context)
    {
        this.context = context;
    }

    public void draw(Canvas canvas)
    {
        String text = "Money: " + currentMoney;

        float posX = 1500;
        float posY = 100;

        Paint paint = new Paint();
        int colour = ContextCompat.getColor(context, R.color.money);
        paint.setColor(colour);
        float textSize = 50;
        paint.setTextSize(textSize);
        canvas.drawText(text, posX, posY, paint);
    }
}
