package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Spell extends GameObject {
    public static final double PIXELS_PER_SECOND = 600;
    public static final double MAX_SPELL_SPEED = PIXELS_PER_SECOND/GameLoop.MAX_UPDATES;
    Bitmap sprite;

    public Spell(Context ignoredContext, Player player, double posX, double posY, Resources res) {
        super(posX, posY);

        velocityX = player.getDirectionX()*MAX_SPELL_SPEED;
        velocityY = player.getDirectionY()*MAX_SPELL_SPEED;

        sprite = BitmapFactory.decodeResource(res, R.drawable.roomba);
        sprite = Bitmap.createScaledBitmap(sprite, 160,83, false);

    }

    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        canvas.drawBitmap(sprite, (float) gameDisplay.displayCoordsX((float) posX), (float) gameDisplay.displayCoordsY((float) posY+250), new Paint());

    }

    @Override
    public void update() {
        posX += velocityX;
        posY+= velocityY;

    }
}
