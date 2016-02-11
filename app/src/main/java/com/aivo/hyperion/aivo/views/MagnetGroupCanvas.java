
package com.aivo.hyperion.aivo.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;

/**
 * Created by corpp on 6.1.2016.
 */
public class MagnetGroupCanvas {

    private static final float MAGNET_RADIUS = 24;
    private static final float MAGNET_WIDTH = MAGNET_RADIUS * 4;
    private static final float MAGNET_HEIGHT = MAGNET_WIDTH * 2;
    private static final float BORDER_THICKNESS = MAGNET_RADIUS;

    private Canvas mCanvas;
    private Paint mPaint;

    private MagnetGroup mMagnetGroup;
    private RectF outerRectF;

    public MagnetGroupCanvas() {
        this.mCanvas = null;
        this.mPaint = null;

        this.mMagnetGroup = null;
    }

    public void setMagnetGroup(MagnetGroup magnetGroup) {
        this.mMagnetGroup = magnetGroup;

        int sizeModifier = 2;
        float deltaX = sizeModifier * BORDER_THICKNESS + sizeModifier * MAGNET_WIDTH;
        float deltaY = sizeModifier * BORDER_THICKNESS + sizeModifier * MAGNET_HEIGHT;
        this.outerRectF = new RectF(
                magnetGroup.getPoint().x - deltaX,
                magnetGroup.getPoint().y - deltaY,
                magnetGroup.getPoint().x + deltaX,
                magnetGroup.getPoint().y + deltaY);
    }

    public void draw(Canvas canvas, Paint paint) {
        mCanvas = canvas;
        mPaint = paint;

        paint.setColor(Color.CYAN);
        canvas.drawRoundRect(outerRectF, 24, 24, paint);

//        for(Magnet magnet : mMagnetGroup.getMagnets()) {
//           // drawMagnet("Title", new PointF(mMagnetGroup.getX(), mMagnetGroup.getY()));
//        }

        mCanvas = null;
        mPaint = null;
    }

    // 2
    private void drawMagnet(String title, PointF pointF) {
        drawMagnetCenter(pointF);
        drawMagnetTop(pointF);
        drawMagnetBottom(pointF);

        drawMagnetRight(pointF);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mCanvas.drawText(title, pointF.x, pointF.y, mPaint);
    }

    // 3
    private void drawMagnetCenter(PointF pointF) {
        mPaint.setColor(Color.YELLOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCanvas.drawRoundRect(
                    pointF.x - MAGNET_WIDTH, pointF.y - MAGNET_HEIGHT,
                    pointF.x + MAGNET_WIDTH, pointF.y + MAGNET_HEIGHT,
                    MAGNET_RADIUS, MAGNET_RADIUS,
                    mPaint);
        } else {
            mCanvas.drawRoundRect(
                    new RectF(
                            pointF.x - MAGNET_WIDTH, pointF.y - MAGNET_HEIGHT,
                            pointF.x + MAGNET_WIDTH, pointF.y + MAGNET_HEIGHT),
                    MAGNET_RADIUS, MAGNET_RADIUS,
                    mPaint);
        }
    }

    // 4
    private void drawMagnetTop(PointF pointF) {
        mPaint.setColor(Color.RED);
        mCanvas.drawCircle(pointF.x, pointF.y - MAGNET_HEIGHT, MAGNET_RADIUS, mPaint);
    }

    // 5
    private void drawMagnetBottom(PointF pointF) {
        float y = pointF.y + MAGNET_HEIGHT;
        mPaint.setColor(Color.WHITE);
        mCanvas.drawCircle(pointF.x, y, MAGNET_RADIUS, mPaint);
        mPaint.setColor(Color.BLACK);
        mCanvas.drawLine(
                pointF.x - MAGNET_RADIUS, y,
                pointF.x + MAGNET_RADIUS, y,
                mPaint);
        mCanvas.drawLine(
                pointF.x, y - MAGNET_RADIUS,
                pointF.x, y + MAGNET_RADIUS,
                mPaint);
    }

    private void drawMagnetRight(PointF pointF) {
        boolean hasVid = true;
        boolean hasImg = true;

        if (hasVid) {

            mPaint.setColor(Color.BLUE);
            mCanvas.drawRect(
                    pointF.x + MAGNET_WIDTH + BORDER_THICKNESS,
                    pointF.y - MAGNET_HEIGHT,
                    pointF.x + MAGNET_WIDTH + MAGNET_RADIUS + BORDER_THICKNESS + MAGNET_RADIUS,
                    pointF.y + MAGNET_HEIGHT / 2,
                    mPaint
                    );
        }

        if (hasImg) {

            mPaint.setColor(Color.GREEN);
            mCanvas.drawRect(
                    pointF.x + MAGNET_WIDTH + BORDER_THICKNESS,
                    pointF.y + MAGNET_HEIGHT / 2,
                    pointF.x + MAGNET_WIDTH + MAGNET_RADIUS + BORDER_THICKNESS + MAGNET_RADIUS,
                    pointF.y + MAGNET_HEIGHT,
                    mPaint
            );
        }
    }
}
