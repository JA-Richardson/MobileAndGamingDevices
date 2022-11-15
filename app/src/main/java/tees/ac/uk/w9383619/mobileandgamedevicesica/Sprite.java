package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
    private final Rect rect;
    private final SpriteSheet spriteSheet;
    public int getHeight;
    public int getWidth;

    public Sprite(SpriteSheet spriteSheet, Rect rect)
    {
        this.spriteSheet = spriteSheet;
        this.rect = rect;
    }

    public void draw(Canvas canvas, int x, int y)
    {
        canvas.drawBitmap(spriteSheet.getBitmap(), rect, new Rect(x, y, x+getWidth, y+getHeight), null);
    }

    public int getHeight()
    {
        return rect.height();
    }

    public int getWidth()
    {
        return rect.width();
    }
}
