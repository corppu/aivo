
package com.aivo.hyperion.aivo.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import com.aivo.hyperion.aivo.models.MagnetGroup;

/**
 * Created by corpp on 6.1.2016.
 */
public class MagnetGroupCanvas {

    private static final float MAGNET_RADIUS = 24;
    private static final float MAGNET_WIDTH = MAGNET_RADIUS * 4;
    private static final float MAGNET_HEIGHT = MAGNET_WIDTH * 2;
    private static final float BORDER_THICKNESS = MAGNET_RADIUS;

    private MagnetGroup magnetGroup;
    private RectF outerRectF;

    public MagnetGroupCanvas() {
        this.magnetGroup = null;
    }

    public void setMagnetGroup(MagnetGroup magnetGroup) {
        this.magnetGroup = magnetGroup;

        int sizeModifier = 2;
        float deltaX = sizeModifier * BORDER_THICKNESS + sizeModifier * MAGNET_WIDTH;
        float deltaY = sizeModifier * BORDER_THICKNESS + sizeModifier * MAGNET_HEIGHT;
        this.outerRectF = new RectF(
                magnetGroup.getX() - deltaX,
                magnetGroup.getY() - deltaY,
                magnetGroup.getX() + deltaX,
                magnetGroup.getY() + deltaY);
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.CYAN);
        canvas.drawRoundRect(outerRectF, 24, 24, paint);
    }
}
