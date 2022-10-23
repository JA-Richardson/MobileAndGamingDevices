package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;

public class Spell extends GameObject {
    private final Player player;

    public Spell(Context ignoredContext, Player player, double posX, double posY, Resources res) {
        super(posX, posY);

        this.player = player;

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }
}
