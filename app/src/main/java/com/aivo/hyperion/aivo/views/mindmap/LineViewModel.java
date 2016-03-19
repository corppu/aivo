package com.aivo.hyperion.aivo.views.mindmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;


import com.aivo.hyperion.aivo.main.MainActivity;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.MagnetGroup;

import java.util.Map;

/**
 * Created by corpp on 23.2.2016.
 */
public class LineViewModel {

    public PointF getMiddlePointF() {
        return middlePointF;
    }

    private Line mLine = null;
    private MagnetGroupViewModel mParentMagnetGroupViewModel = null;
    private MagnetGroupViewModel mChildMagnetGroupViewModel = null;
    private boolean mIsGhost = true;
    private boolean mIsSelected = true;

    public boolean getIsGhost() { return mIsGhost; }
    public void setIsGhost(boolean isGhost) { mIsGhost = isGhost; }
    public boolean getIsSelected() { return  mIsSelected; }
    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }

    private int mColor = 0;
    private PointF middlePointF = new PointF(0,0);

    public void refresh() {
        middlePointF.set(mLine.getPoints().get(0));
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

        middlePointF = new PointF();
        middlePointF.set(
                (mParentMagnetGroupViewModel.getCenterX() + mChildMagnetGroupViewModel.getCenterX()) / 2.0f,
                (mParentMagnetGroupViewModel.getCenterY() + mChildMagnetGroupViewModel.getCenterY()) / 2.0f
        );

        int r = MainActivity.getRandom().nextInt(255);
        int g = MainActivity.getRandom().nextInt(255);
        int b = MainActivity.getRandom().nextInt(255);
        mColor = Color.argb(255, r, g, b);
    }

    public LineViewModel(Line line, Map<MagnetGroup, MagnetGroupViewModel> magnetMagnetGroupViewModelMap) {
        mParentMagnetGroupViewModel = magnetMagnetGroupViewModelMap.get(line.getMagnetGroup1());
        mChildMagnetGroupViewModel = magnetMagnetGroupViewModelMap.get(line.getMagnetGroup2());
        mLine = line;
        mIsGhost = false;
        mIsSelected = false;

        middlePointF = new PointF();
        if (line.getPoints().isEmpty()) {
            middlePointF.set(
                    (mParentMagnetGroupViewModel.getCenterX() + mChildMagnetGroupViewModel.getCenterX()) / 2.0f,
                    (mParentMagnetGroupViewModel.getCenterY() + mChildMagnetGroupViewModel.getCenterY()) / 2.0f
            );
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

    public void moveMiddlePoint(float newPointX, float newPointY) {
        middlePointF.set(newPointX, newPointY);
    }

    public void draw(Canvas canvas, Paint paint) {
        if (mIsSelected || mIsGhost) {
            paint.setColor(MagnetViewModel.HIGHLIGHT_BORDER_COLOR);
            paint.setStrokeWidth(MagnetViewModel.HIGHLIGHT_BORDER_SIZE);
            if (mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

            canvas.drawLine(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), middlePointF.x, middlePointF.y, paint);
            canvas.drawLine(middlePointF.x, middlePointF.y, mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), paint);
            canvas.drawCircle(middlePointF.x, middlePointF.y, MagnetViewModel.CIRLCE_RADIUS + MagnetViewModel.HIGHLIGHT_BORDER_SIZE, paint);
        }

        paint.setColor(mColor);
        paint.setStrokeWidth(MagnetViewModel.BORDER_SIZE);
        if (mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

        canvas.drawLine(mParentMagnetGroupViewModel.getCenterX(), mParentMagnetGroupViewModel.getCenterY(), middlePointF.x, middlePointF.y, paint);
        canvas.drawLine(middlePointF.x, middlePointF.y, mChildMagnetGroupViewModel.getCenterX(), mChildMagnetGroupViewModel.getCenterY(), paint);
        canvas.drawCircle(middlePointF.x, middlePointF.y, MagnetViewModel.CIRLCE_RADIUS, paint);
    }

    boolean contains(float x, float y) {
        return x > middlePointF.x - MagnetViewModel.CIRLCE_RADIUS && x < middlePointF.x + MagnetViewModel.CIRLCE_RADIUS && y > middlePointF.y - MagnetViewModel.CIRLCE_RADIUS && y < middlePointF.y + MagnetViewModel.CIRLCE_RADIUS;
    }
}



