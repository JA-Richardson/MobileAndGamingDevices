package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class SpriteSheet {
    private Bitmap bitmap;

    public SpriteSheet(Context context)
    {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.witch_run_sheet, bitmapOptions);
    }

    public Sprite getPlayer()
    {
        return new Sprite(this, new Rect(0, 0, 32, 12));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
