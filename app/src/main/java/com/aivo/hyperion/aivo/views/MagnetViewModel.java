package com.aivo.hyperion.aivo.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.main.MainActivity;


/**
 * Created by corpp on 1.2.2016.
 */
public class MagnetViewModel {
    static public final int BORDER_COLOR = Color.BLACK;
    static public final int BORDER_SIZE = 5;
    static public final int CIRLCE_RADIUS = 24;
    static public final int ROUND_RADIUS = CIRLCE_RADIUS / 2;
    static public final int HALF_WIDTH = CIRLCE_RADIUS + CIRLCE_RADIUS;
    static public final int HALF_HEIGHT = HALF_WIDTH;
    static public final int HIGHLIGHT_BORDER_SIZE = CIRLCE_RADIUS + BORDER_SIZE * 2;
    static public final int HIGHLIGHT_BORDER_COLOR = Color.BLUE;
    static public final int ICON_COLOR = Color.YELLOW;
    static public final int CONTENT_COLOR = Color.LTGRAY;
    static public final int TEXT_SIZE = 26;
    static public final int TEXT_COLOR = Color.BLACK;
    static public final String TITLE = "Preview";
    static public final Bitmap VID_ICON = BitmapFactory.decodeResource(MainActivity.getContext().getResources(), R.mipmap.ic_launcher);
    static public final Bitmap IMG_ICON = BitmapFactory.decodeResource(MainActivity.getContext().getResources(), R.mipmap.ic_launcher);

    static public boolean checkCollisions(PointF pointF1, PointF pointF2) {
        RectF rectF1 = new RectF(
                pointF1.x - HALF_WIDTH - HIGHLIGHT_BORDER_SIZE, pointF1.y - HALF_HEIGHT - HIGHLIGHT_BORDER_SIZE,
                pointF1.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE, pointF1.y + HALF_HEIGHT + HIGHLIGHT_BORDER_SIZE);
        RectF rectF2 = new RectF(
                pointF2.x - HALF_WIDTH - HIGHLIGHT_BORDER_SIZE, pointF2.y - HALF_HEIGHT - HIGHLIGHT_BORDER_SIZE,
                pointF2.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE, pointF2.y + HALF_HEIGHT + HIGHLIGHT_BORDER_SIZE);

        return rectF1.intersect(rectF2);
    }
    static public boolean checkSelection(PointF pointF, PointF selectionPointF) {
        RectF rectF = new RectF(
                pointF.x - HALF_WIDTH - HIGHLIGHT_BORDER_SIZE, pointF.y - HALF_HEIGHT - HIGHLIGHT_BORDER_SIZE,
                pointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE, pointF.y + HALF_HEIGHT + HIGHLIGHT_BORDER_SIZE);
        return rectF.contains(selectionPointF.x, selectionPointF.y);
    }
    static public void draw( MagnetViewModel viewModel,
                     Canvas canvas,
                     Paint paint
                     ) {

        if (viewModel.isSelected) {
            // Draw highlight borders
            paint.setColor(HIGHLIGHT_BORDER_COLOR);
            canvas.drawRoundRect( new RectF(
                    viewModel.pointF.x - HALF_WIDTH - HIGHLIGHT_BORDER_SIZE, viewModel.pointF.y - HALF_HEIGHT - HIGHLIGHT_BORDER_SIZE,
                    viewModel.pointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE, viewModel.pointF.y + HALF_HEIGHT + HIGHLIGHT_BORDER_SIZE),
                    ROUND_RADIUS, ROUND_RADIUS,
                    paint);
        }
        // Draw borders
        paint.setColor(BORDER_COLOR);
        canvas.drawRoundRect(new RectF(
                        viewModel.pointF.x - HALF_WIDTH - BORDER_SIZE, viewModel.pointF.y - HALF_HEIGHT - BORDER_SIZE,
                        viewModel.pointF.x + HALF_WIDTH + BORDER_SIZE, viewModel.pointF.y + HALF_HEIGHT + BORDER_SIZE),
                ROUND_RADIUS, ROUND_RADIUS,
                paint);

        // Draw content
        paint.setColor(CONTENT_COLOR);
        canvas.drawRoundRect(new RectF(
                        viewModel.pointF.x - HALF_WIDTH, viewModel.pointF.y - HALF_HEIGHT,
                        viewModel.pointF.x + HALF_WIDTH, viewModel.pointF.y + HALF_HEIGHT),
                ROUND_RADIUS, ROUND_RADIUS,
                paint);

        // Draw top icon borders
        paint.setColor(BORDER_COLOR);
        canvas.drawCircle(viewModel.pointF.x, viewModel.pointF.y - HALF_HEIGHT, CIRLCE_RADIUS + BORDER_SIZE, paint);

        // Draw top icon

        canvas.drawCircle(viewModel.pointF.x, viewModel.pointF.y - HALF_HEIGHT, CIRLCE_RADIUS, paint);
        //canvas.drawBitmap(viewModel.topIcon, viewModel.pointF.x - CIRLCE_RADIUS, viewModel.pointF.y - HALF_HEIGHT - CIRLCE_RADIUS, paint);

        // Draw bottom icon borders
        paint.setColor(BORDER_COLOR);
        canvas.drawCircle(viewModel.pointF.x, viewModel.pointF.y + HALF_HEIGHT, CIRLCE_RADIUS + BORDER_SIZE, paint);

        // Draw bottom icon
        paint.setColor(CONTENT_COLOR);
        canvas.drawCircle(viewModel.pointF.x, viewModel.pointF.y + HALF_HEIGHT, CIRLCE_RADIUS, paint);

        // Draw text
        paint.setTextSize(TEXT_SIZE);
        paint.setColor(TEXT_COLOR);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(TITLE, viewModel.pointF.x, viewModel.pointF.y + CIRLCE_RADIUS / 2, paint);

        if (viewModel.hasVid) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(VID_ICON, HIGHLIGHT_BORDER_SIZE, HIGHLIGHT_BORDER_SIZE, true),
                    viewModel.pointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE,
                    viewModel.pointF.y - HALF_HEIGHT,
                    null);
        }

        if (viewModel.hasImg) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(IMG_ICON, HIGHLIGHT_BORDER_SIZE, HIGHLIGHT_BORDER_SIZE, true),
                    viewModel.pointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE,
                    viewModel.pointF.y,
                    null);
        }
    }

    public PointF pointF = new PointF();
    public boolean hasImg = false;
    public boolean hasVid = false;
    public boolean isSelected = false;
    public boolean topIconPressed = false;
    public boolean bottomIconPressed = false;
    public String title = "Preview";
    public Bitmap topIcon = VID_ICON;
}
