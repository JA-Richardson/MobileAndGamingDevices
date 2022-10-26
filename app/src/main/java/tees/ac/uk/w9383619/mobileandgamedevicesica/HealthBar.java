package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class HealthBar {
    private final float margin;
    private final Player player;
    private final Paint paintBorder;
    private final Paint paintFill;
    private final float width;
    private final float height;

    public HealthBar(Context context, Player player) {
        this.player = player;
        this.width = 25;
        this.height = 250;
        this.margin = 2.5f;

        this.paintBorder = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.purple_500);
        paintBorder.setColor(borderColor);

        this.paintFill = new Paint();
        int fillColor = ContextCompat.getColor(context, R.color.red);
        paintFill.setColor(fillColor);
    }

    public void draw(Canvas canvas) {
        float x = (float) player.getPosX();
        float y = (float) player.getPosY();
        float distanceToPlayer = -225;

        float healthPercent = (float) player.getCurrentHealth() / Player.MAX_HEALTH;

        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x - width / 2;
        borderRight = x + width / 2;
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBottom, paintBorder);

        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
        healthWidth = width - 2 * margin;
        healthHeight = height - 2 * margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight * healthPercent;
        canvas.drawRect(healthLeft, healthTop, healthRight, healthBottom, paintFill);


    }
}
