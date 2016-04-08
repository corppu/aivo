package com.aivo.hyperion.aivo.views.mindmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;


import com.aivo.hyperion.aivo.main.MainActivity;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.MagnetGroup;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by corpp on 23.2.2016.
 */
public class LineViewModel implements ViewModel{

    public PointF getMiddlePointF() {
        return middlePointF;
    }

    private Line mLine = null;
    private MagnetGroupViewModel mParentMagnetGroupViewModel = null;
    private MagnetGroupViewModel mChildMagnetGroupViewModel = null;
    private boolean mIsGhost = true;
    private boolean mIsSelected = true;

    private boolean mIsHighLighted;
    public boolean getIsHighLighted()
    {
        return mIsHighLighted;
    }

    @Override
    public void setLastTouchPoint(float canvasTouchX, float canvasTouchY) {
        mLastTouchX = canvasTouchX;
        mLastTouchY = canvasTouchY;
    }

    private float mLastTouchX;
    private float mLastTouchY;
    @Override
    public float getLastTouchX() {
        return mLastTouchX;
    }

    @Override
    public float getLastTouchY() {
        return mLastTouchY;
    }

    @Override
    public void moveBy(float distanceX, float distanceY) {
        if(middlePointF != null) {
            middlePointF.x -= distanceX;
            middlePointF.y -= distanceY;
        }
    }

    public void setIsHighLighted(boolean isHighLighted)
    {
        mIsHighLighted = isHighLighted;
        Log.d("mIsHighLighted", Boolean.toString(mIsHighLighted));
    }
    public boolean getIsGhost() { return mIsGhost; }
    public void setIsGhost(boolean isGhost) { mIsGhost = isGhost; }
    public boolean getIsSelected() { return  mIsSelected; }

    private boolean mHasMoved;

    @Override
    public void setHasMoved(boolean hasMoved) {
        mHasMoved = hasMoved;
    }

    @Override
    public boolean getHasMoved() {
        return mHasMoved;
    }

    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }

    private int mColor = 0;
    private PointF middlePointF = new PointF(0,0);

    public void refresh() {
        if (mLine.getPoints().isEmpty()) {
            middlePointF = null;
        } else {
            middlePointF = new PointF();
            middlePointF.set(mLine.getPoints().get(0));
        }
    }

    public Line getLine() {
        return mLine;
    }
    public MagnetGroupViewModel getParent() {
        return mParentMagnetGroupViewModel;
    }
    public MagnetGroupViewModel getChild() {
        return mChildMagnetGroupViewModel;
    }

    public LineViewModel(MagnetGroupViewModel parent, MagnetGroupViewModel child) {
        mParentMagnetGroupViewModel = parent;
        mChildMagnetGroupViewModel = child;
        mLine = null;
        mIsGhost = true;
        mIsSelected = true;

        middlePointF = null;

        int r = MainActivity.getRandom().nextInt(255);
        int g = MainActivity.getRandom().nextInt(255);
        int b = MainActivity.getRandom().nextInt(255);
        mColor = Color.argb(255, r, g, b);
    }

    private void createMiddlePointF() {
        RectF rectFParent = new RectF();
        RectF rectFChild = new RectF();

        float parentSideX;
        float parentSideY;
        float childSideX;
        float childSideY;

        mParentMagnetGroupViewModel.getOuterRectF(rectFParent);
        mChildMagnetGroupViewModel.getOuterRectF(rectFChild);

        // The child is in the right side  of parent
        if (mParentMagnetGroupViewModel.getCenterX() < mChildMagnetGroupViewModel.getCenterX()) {
            parentSideX = rectFParent.right;
            childSideX = rectFChild.left;
        }
        // The child is in the left side of parent
        else {
            parentSideX = rectFParent.left;
            childSideX = rectFChild.right;
        }

        // The child is in the bottom side of parent
        if (mParentMagnetGroupViewModel.getCenterY() < mChildMagnetGroupViewModel.getCenterY()) {
            parentSideY = rectFParent.bottom;
            childSideY = rectFChild.top;
        }

        // The child is in the top side of parent
        else {
            parentSideY = rectFParent.top;
            childSideY = rectFChild.bottom;
        }

        middlePointF = new PointF(
                (parentSideX + childSideX) / 2.0f,
                (parentSideY + childSideY) / 2.0f
        );
    }

    public LineViewModel(Line line, Map<MagnetGroup, MagnetGroupViewModel> magnetMagnetGroupViewModelMap) {
        mParentMagnetGroupViewModel = magnetMagnetGroupViewModelMap.get(line.getMagnetGroup1());
        mChildMagnetGroupViewModel = magnetMagnetGroupViewModelMap.get(line.getMagnetGroup2());
        mLine = line;
        mIsGhost = false;
        mIsSelected = false;

        if (line.getPoints().isEmpty()) {
            createMiddlePointF();
        }
        else refresh();

//        int r = MainActivity.getRandom().nextInt(255);
//        int g = MainActivity.getRandom().nextInt(255);
//        int b = MainActivity.getRandom().nextInt(255);
//        mColor = Color.argb(255, r, g, b);

       mColor = MainActivity.getUser().getTheme().getColorLine();
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void setMiddlePointF(float x, float y) {
        middlePointF = new PointF(x, y);
    }

    public void draw(Canvas canvas, Paint paint) {
        if (mIsSelected || mIsGhost) {
            paint.setColor(MagnetViewModel.HIGHLIGHT_BORDER_COLOR);
            paint.setStrokeWidth(MagnetViewModel.HIGHLIGHT_BORDER_SIZE);
            if (mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

            if (middlePointF != null) {
                canvas.drawLine(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), middlePointF.x, middlePointF.y, paint);
                canvas.drawLine(middlePointF.x, middlePointF.y, mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), paint);
                canvas.drawCircle(middlePointF.x, middlePointF.y, MagnetViewModel.CIRLCE_RADIUS + MagnetViewModel.HIGHLIGHT_BORDER_SIZE, paint);
            } else {
                canvas.drawLine(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), paint);
            }
        }

        paint.setColor(mColor);
        paint.setStrokeWidth(MagnetViewModel.BORDER_SIZE);
        if (mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

        if (middlePointF != null) {
            canvas.drawLine(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), middlePointF.x, middlePointF.y, paint);
            canvas.drawLine(middlePointF.x, middlePointF.y, mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), paint);
            canvas.drawCircle(middlePointF.x, middlePointF.y, MagnetViewModel.CIRLCE_RADIUS, paint);
        } else {
            canvas.drawLine(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), paint);
        }
    }

    boolean contains(float x, float y) {
        if (middlePointF == null) return false;
        return x > middlePointF.x - MagnetViewModel.CIRLCE_RADIUS && x < middlePointF.x + MagnetViewModel.CIRLCE_RADIUS && y > middlePointF.y - MagnetViewModel.CIRLCE_RADIUS && y < middlePointF.y + MagnetViewModel.CIRLCE_RADIUS;
    }
}



