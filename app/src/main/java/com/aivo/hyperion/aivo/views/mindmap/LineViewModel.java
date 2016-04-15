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

    /**
     * Gives the square of the distance between the point and the line segment.
     *
     * @param x1
     *            the x coordinate of the starting point of the line segment.
     * @param y1
     *            the y coordinate of the starting point of the line segment.
     * @param x2
     *            the x coordinate of the end point of the line segment.
     * @param y2
     *            the y coordinate of the end point of the line segment.
     * @param px
     *            the x coordinate of the test point.
     * @param py
     *            the y coordinate of the test point.
     * @return the the square of the distance between the point and the line
     *         segment.
     */
    public static double ptSegDistSq(double x1, double y1, double x2, double y2, double px,
                                     double py) {
        /*
         * A = (x2 - x1, y2 - y1) P = (px - x1, py - y1)
         */
        x2 -= x1; // A = (x2, y2)
        y2 -= y1;
        px -= x1; // P = (px, py)
        py -= y1;
        double dist;
        if (px * x2 + py * y2 <= 0.0) { // P*A
            dist = px * px + py * py;
        } else {
            px = x2 - px; // P = A - P = (x2 - px, y2 - py)
            py = y2 - py;
            if (px * x2 + py * y2 <= 0.0) { // P*A
                dist = px * px + py * py;
            } else {
                dist = px * y2 - py * x2;
                dist = dist * dist / (x2 * x2 + y2 * y2); // pxA/|A|
            }
        }
        if (dist < 0) {
            dist = 0;
        }
        return dist;
    }
    /**
     * Gives the distance between the point and the line segment.
     *
     * @param x1
     *            the x coordinate of the starting point of the line segment.
     * @param y1
     *            the y coordinate of the starting point of the line segment.
     * @param x2
     *            the x coordinate of the end point of the line segment.
     * @param y2
     *            the y coordinate of the end point of the line segment.
     * @param px
     *            the x coordinate of the test point.
     * @param py
     *            the y coordinate of the test point.
     * @return the the distance between the point and the line segment.
     */
    public static double ptSegDist(double x1, double y1, double x2, double y2, double px, double py) {
        return Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
    }


    public boolean contains(float x, float y) {
        if (middlePointF == null) {
            return ptSegDist(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), x, y) <= MagnetViewModel.HIGHLIGHT_BORDER_SIZE;
        }
        return ptSegDist(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), middlePointF.x, middlePointF.y, x, y) <= MagnetViewModel.HIGHLIGHT_BORDER_SIZE
                || ptSegDist(mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), middlePointF.x, middlePointF.y, x, y) <= MagnetViewModel.HIGHLIGHT_BORDER_SIZE;
    }

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

    public Line getModel() {
        return mLine;
    }

    @Override
    public float getLeft() {
        if (middlePointF == null) return Float.MAX_VALUE;
        return middlePointF.x;
    }

    @Override
    public float getTop() {
        if (middlePointF == null) return Float.MAX_VALUE;
        return middlePointF.y;
    }

    @Override
    public float getRight() {
        if (middlePointF == null) return Float.MAX_VALUE;
        return middlePointF.x;
    }

    @Override
    public float getBottom() {
        if (middlePointF == null) return Float.MAX_VALUE;
        return middlePointF.y;
    }

    @Override
    public void getOuterRectF(RectF outerRectF) {
        if (middlePointF == null) outerRectF.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        else outerRectF.set(middlePointF.x, middlePointF.y, middlePointF.x, middlePointF.y);
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

        mColor = MainActivity.getUser().getTheme().getColorLine();
    }


//    Float parentSideX = 0f;
//    Float parentSideY = 0f;
//    Float childSideX = 0f;
//    Float childSideY = 0f;

    public PointF createMiddlePointF() {
//        //getLineStartAndEnd();
//        return new PointF(
//                (parentSideX + childSideX) / 2.0f,
//                (parentSideY + childSideY) / 2.0f
//        );

        return new PointF(
                (mParentMagnetGroupViewModel.getCenterX() + mChildMagnetGroupViewModel.getCenterX()) / 2f,
                (mParentMagnetGroupViewModel.getCenterY() + mChildMagnetGroupViewModel.getCenterY()) / 2f
        );
    }
//
//    private void getLineStartAndEnd() {
//        RectF rectFParent = new RectF();
//        RectF rectFChild = new RectF();
//
//        mParentMagnetGroupViewModel.getOuterRectF(rectFParent);
//        mChildMagnetGroupViewModel.getOuterRectF(rectFChild);
//
//        // The child is in the right side  of parent
//        if (mParentMagnetGroupViewModel.getCenterX() < mChildMagnetGroupViewModel.getCenterX()) {
//            parentSideX = rectFParent.right;
//            childSideX = rectFChild.left;
//        }
//        // The child is in the left side of parent
//        else {
//            parentSideX = rectFParent.left;
//            childSideX = rectFChild.right;
//        }
//
//        // The child is in the bottom side of parent
//        if (mParentMagnetGroupViewModel.getCenterY() < mChildMagnetGroupViewModel.getCenterY()) {
//            parentSideY = rectFParent.bottom;
//            childSideY = rectFChild.top;
//        }
//
//        // The child is in the top side of parent
//        else {
//            parentSideY = rectFParent.top;
//            childSideY = rectFChild.bottom;
//        }
//    }

    public LineViewModel(Line line, Map<MagnetGroup, MagnetGroupViewModel> magnetMagnetGroupViewModelMap) {
        mParentMagnetGroupViewModel = magnetMagnetGroupViewModelMap.get(line.getMagnetGroup1());
        mChildMagnetGroupViewModel = magnetMagnetGroupViewModelMap.get(line.getMagnetGroup2());
        mLine = line;
        mIsGhost = false;
        mIsSelected = false;

//        if (line.getPoints().isEmpty()) {
//            createMiddlePointF();
//        }
//        else
        refresh();

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
}



