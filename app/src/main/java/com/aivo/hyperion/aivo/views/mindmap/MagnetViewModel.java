package com.aivo.hyperion.aivo.views.mindmap;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.main.MainActivity;
import com.aivo.hyperion.aivo.models.Magnet;



/**
 * Created by corppu on 23.2.2016.
 */
public class MagnetViewModel {
    public void refresh() {
        mColor = mMagnet.getColor();
        mTitle = mMagnet.getTitle();
        mHasImage = mMagnet.hasImage();
        mHasVideo = mMagnet.hasVideo();
    }

    // Retrieve view constants from dimensions.xml
    private Context mContext;

    // View model state stuff
    private boolean mIsGhost;
    private boolean mIsSelected;

    public boolean getIsGhost() { return mIsGhost; }
    public void setIsGhost(boolean isGhost) { mIsGhost = isGhost; }
    public boolean getIsSelected() { return  mIsSelected; }
    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }

    // Model related
    private int mColor;
    private String mTitle;
    private boolean mHasImage;
    private boolean mHasVideo;
    private Magnet mMagnet;

    public Magnet getModel() {
        return mMagnet;
    }

    public MagnetViewModel() {
        mIsGhost = true;
        mIsSelected = true;
        mColor = ICON_COLOR;
        mTitle = TITLE;
        mHasVideo = false;
        mHasImage = false;
        mMagnet = null;
    }

    public MagnetViewModel(Magnet magnet) {
        mIsGhost = false;
        mIsSelected = false;
        mColor = magnet.getColor();
        mTitle = magnet.getTitle();
        mHasImage = magnet.hasImage();
        mHasVideo = magnet.hasVideo();
        mMagnet = magnet;
    }


    public void setTopLeftPointF(PointF topLeftPointF) {

        this.mCenterPointF = new PointF(
                topLeftPointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE,
                topLeftPointF.y + HALF_HEIGHT + HIGHLIGHT_BORDER_SIZE);
        mOuterRectF = new RectF(
                mCenterPointF.x - HALF_WIDTH - HIGHLIGHT_BORDER_SIZE, mCenterPointF.y - HALF_HEIGHT - HIGHLIGHT_BORDER_SIZE,
                mCenterPointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE, mCenterPointF.y + HALF_HEIGHT + HIGHLIGHT_BORDER_SIZE);


        mBottomIconCenterPointF = new PointF(mCenterPointF.x, mCenterPointF.y + HALF_HEIGHT);
        mTopIconCenterPointF = new PointF(mCenterPointF.x, mCenterPointF.y - HALF_HEIGHT);
    }


    public void getOuterRectF(RectF outerRectF) {
        outerRectF.set(mOuterRectF);
    }
    public void getCenterPointF(PointF centerPointF) {
        centerPointF.set(mCenterPointF);
    }

    static public final Bitmap VID_ICON = BitmapFactory.decodeResource(MainActivity.getContext().getResources(), R.mipmap.ic_launcher);
    static public final Bitmap IMG_ICON = BitmapFactory.decodeResource(MainActivity.getContext().getResources(), R.mipmap.ic_launcher);

    private boolean mTopIconIsPressed = false;
    private boolean mBottomIconIsPressed = false;



    private RectF mOuterRectF;
    private PointF mCenterPointF;
    private PointF mTopIconCenterPointF;
    private PointF mBottomIconCenterPointF;
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
    static public final int INDICATOR_ICON_SIZE = HIGHLIGHT_BORDER_SIZE;
    static public final int GHOST_ALPHA = 100;
    static public final int OUTER_HALF_WIDTH_WITH_INDICATOR_SIZE = HALF_WIDTH + HIGHLIGHT_BORDER_SIZE + INDICATOR_ICON_SIZE;
    static public final int OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE = HALF_HEIGHT + HIGHLIGHT_BORDER_SIZE + INDICATOR_ICON_SIZE;


    public float getCenterX() {
        return mCenterPointF.x;
    }

    public float getCenterY() {
        return mCenterPointF.y;
    }

    static public void draw(MagnetViewModel magnetViewModel, Canvas canvas, Paint paint) {
        if (magnetViewModel.mIsSelected || magnetViewModel.mIsGhost) {
            // Draw highlight borders
            paint.setColor(HIGHLIGHT_BORDER_COLOR);
            if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);
            canvas.drawRoundRect(
                    magnetViewModel.mOuterRectF,
                    ROUND_RADIUS, ROUND_RADIUS,
                    paint);
        }

        // Draw borders
        paint.setColor(BORDER_COLOR);
        if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);

        RectF drawRectF = new RectF(
                magnetViewModel.mOuterRectF.left + HIGHLIGHT_BORDER_SIZE,
                magnetViewModel.mOuterRectF.top + HIGHLIGHT_BORDER_SIZE,
                magnetViewModel.mOuterRectF.right - HIGHLIGHT_BORDER_SIZE,
                magnetViewModel.mOuterRectF.bottom - HIGHLIGHT_BORDER_SIZE);
        canvas.drawRoundRect(
                drawRectF,
                ROUND_RADIUS, ROUND_RADIUS,
                paint);

        // Draw content
        paint.setColor(CONTENT_COLOR);
        if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);

        drawRectF.left += BORDER_SIZE;
        drawRectF.top += BORDER_SIZE;
        drawRectF.right -= BORDER_SIZE;
        drawRectF.bottom -= BORDER_SIZE;

        canvas.drawRoundRect(
                drawRectF,
                ROUND_RADIUS, ROUND_RADIUS,
                paint);



        // Draw top icon borders
        paint.setColor(BORDER_COLOR);
        if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);
        canvas.drawCircle(magnetViewModel.mTopIconCenterPointF.x, magnetViewModel.mTopIconCenterPointF.y, CIRLCE_RADIUS + BORDER_SIZE, paint);

        // Draw top icon
        paint.setColor(magnetViewModel.mColor);
        //paint.setColor(Color.argb(255, MainActivity.getRandom().nextInt(255), MainActivity.getRandom().nextInt(255), MainActivity.getRandom().nextInt(255)));
        if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);

        canvas.drawCircle(magnetViewModel.mTopIconCenterPointF.x, magnetViewModel.mTopIconCenterPointF.y, CIRLCE_RADIUS, paint);
//        canvas.drawBitmap(viewModel.topIcon, mCenterPointF.x - CIRLCE_RADIUS, viewModel.pointF.y - HALF_HEIGHT - CIRLCE_RADIUS, paint);

        // Draw bottom icon borders
        if(magnetViewModel.mIsSelected && magnetViewModel.getModel() != null && magnetViewModel.getModel().getMagnetGroup().getMagnetCount() == 1) {
            paint.setColor(BORDER_COLOR);
            if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);

            canvas.drawCircle(magnetViewModel.mBottomIconCenterPointF.x, magnetViewModel.mBottomIconCenterPointF.y, CIRLCE_RADIUS + BORDER_SIZE, paint);

            // Draw bottom icon
            paint.setColor(CONTENT_COLOR);
            if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);
            canvas.drawCircle(magnetViewModel.mBottomIconCenterPointF.x, magnetViewModel.mBottomIconCenterPointF.y, CIRLCE_RADIUS, paint);

            paint.setColor(BORDER_COLOR);
            if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);

            // Draw cross or bitmap icon
            paint.setStrokeWidth(BORDER_SIZE);
            canvas.drawLine(
                    magnetViewModel.mBottomIconCenterPointF.x - CIRLCE_RADIUS,
                    magnetViewModel.mBottomIconCenterPointF.y,
                    magnetViewModel.mBottomIconCenterPointF.x + CIRLCE_RADIUS,
                    magnetViewModel.mBottomIconCenterPointF.y, paint);

            canvas.drawLine(
                    magnetViewModel.mBottomIconCenterPointF.x,
                    magnetViewModel.mBottomIconCenterPointF.y - CIRLCE_RADIUS,
                    magnetViewModel.mBottomIconCenterPointF.x,
                    magnetViewModel.mBottomIconCenterPointF.y + CIRLCE_RADIUS, paint);
        }

        // Draw text
        RectF rectF = new RectF();
        rectF.set(magnetViewModel.mTopIconCenterPointF.x - HALF_WIDTH - BORDER_SIZE, magnetViewModel.mTopIconCenterPointF.y + CIRLCE_RADIUS, magnetViewModel.mTopIconCenterPointF.x + HALF_WIDTH + BORDER_SIZE, magnetViewModel.mBottomIconCenterPointF.y - CIRLCE_RADIUS);

        paint.setColor(BORDER_COLOR);
        canvas.drawRoundRect(rectF,
                ROUND_RADIUS,
                ROUND_RADIUS,
                paint);

        paint.setColor(CONTENT_COLOR);
        if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);

        rectF.left += BORDER_SIZE;
        rectF.top += BORDER_SIZE;
        rectF.bottom -= BORDER_SIZE;
        rectF.right -= BORDER_SIZE;
        canvas.drawRoundRect(rectF,
                ROUND_RADIUS,
                ROUND_RADIUS,
                paint);

        paint.setTextSize(TEXT_SIZE);
        paint.setColor(TEXT_COLOR);
        if (magnetViewModel.mIsGhost) paint.setAlpha(GHOST_ALPHA);

        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(magnetViewModel.mTitle, magnetViewModel.mCenterPointF.x, magnetViewModel.mCenterPointF.y + CIRLCE_RADIUS / 2, paint);

        if (magnetViewModel.mHasVideo) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(VID_ICON, HIGHLIGHT_BORDER_SIZE, HIGHLIGHT_BORDER_SIZE, true),
                    magnetViewModel.mCenterPointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE,
                    magnetViewModel.mCenterPointF.y - HALF_HEIGHT,
                    null);
        }

        if (magnetViewModel.mHasImage) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(IMG_ICON, HIGHLIGHT_BORDER_SIZE, HIGHLIGHT_BORDER_SIZE, true),
                    magnetViewModel.mCenterPointF.x + HALF_WIDTH + HIGHLIGHT_BORDER_SIZE,
                    magnetViewModel.mCenterPointF.y,
                    null);
        }
    }

    public static boolean topIconPressed(MagnetViewModel viewModel, float x, float y) {
        if (x > viewModel.mTopIconCenterPointF.x - CIRLCE_RADIUS &&
                x < viewModel.mTopIconCenterPointF.x + CIRLCE_RADIUS &&
                y > viewModel.mTopIconCenterPointF.y - CIRLCE_RADIUS &&
                y < viewModel.mTopIconCenterPointF.y + CIRLCE_RADIUS) {
            viewModel.mTopIconIsPressed = true;
            return true;
        }
        return false;
    }

    public static boolean bottomIconPressed(MagnetViewModel viewModel, float x, float y) {
        if (x > viewModel.mBottomIconCenterPointF.x - CIRLCE_RADIUS &&
                x < viewModel.mBottomIconCenterPointF.x + CIRLCE_RADIUS &&
                y > viewModel.mBottomIconCenterPointF.y - CIRLCE_RADIUS &&
                y < viewModel.mBottomIconCenterPointF.y + CIRLCE_RADIUS) {
            viewModel.mBottomIconIsPressed = true;
            return true;
        }
        return false;
    }

    public static boolean intersect(MagnetViewModel viewModelA, MagnetViewModel viewModelB) {
        return viewModelA.mOuterRectF.intersect(viewModelB.mOuterRectF);
    }

    public static boolean contains(MagnetViewModel magnetViewModel, float x, float y) {
        return magnetViewModel.mOuterRectF.contains(x, y);
    }

    public void move(float newTouchPointX, float newTouchPointY) {
        float distanceX = mCenterPointF.x - newTouchPointX;
        float distanceY = mCenterPointF.y - newTouchPointY;
        moveDistance(distanceX, distanceY);
    }

    public void moveDistance(float distanceX, float distanceY) {
        mTopIconCenterPointF.x -= distanceX;
        mTopIconCenterPointF.y -= distanceY;
        mBottomIconCenterPointF.x -= distanceX;
        mBottomIconCenterPointF.y -= distanceY;
        mCenterPointF.x -= distanceX;
        mCenterPointF.y -= distanceY;
        mOuterRectF.left -= distanceX;
        mOuterRectF.right -= distanceX;
        mOuterRectF.top -= distanceY;
        mOuterRectF.bottom -= distanceY;
    }
}
