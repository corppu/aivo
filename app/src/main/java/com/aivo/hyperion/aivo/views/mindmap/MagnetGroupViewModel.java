package com.aivo.hyperion.aivo.views.mindmap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by corppu on 23.2.2016.
 */
public class MagnetGroupViewModel implements ViewModel {
    private float mLastTouchX;
    private float mLastTouchY;

    @Override
    public void setLastTouchPoint(float lastTouchX, float lastTouchY) {
        mLastTouchX = lastTouchX;
        mLastTouchY = lastTouchY;
    }

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
        mOuterRectF.left -= distanceX;
        mOuterRectF.right -= distanceX;
        mOuterRectF.top -= distanceY;
        mOuterRectF.bottom -= distanceY;

        for (ArrayList<MagnetViewModel> magnetViewModels : this.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                magnetViewModel.moveBy(distanceX, distanceY);
            }
        }
    }

    private boolean mHasMoved = false;
    public boolean getHasMoved()
    {
        return mHasMoved;
    }
    public void setHasMoved(boolean hasMoved)
    {
        mHasMoved = hasMoved;
        Log.d("mHasMoved", Boolean.toString(mHasMoved));
        if (mSize == 1) {
            mMagnetViewModelRows.get(0).get(0).setHasMoved(hasMoved);
        }
    }

    private boolean mIsHighLighted;
    public boolean getIsHighLighted()
    {
        return mIsHighLighted;
    }
    public void setIsHighLighted(boolean isHighLighted)
    {
        mIsHighLighted = isHighLighted;
        Log.d("mIsHighLighted", Boolean.toString(mIsHighLighted));

        if (mSize == 1) {
            mMagnetViewModelRows.get(0).get(0).setIsHighLighted(isHighLighted);
        }
    }

    static public int[] getRowCol(MagnetGroupViewModel magnetGroupViewModel, MagnetViewModel magnetViewModelA) {
      Log.d("ASD", "ASD");
        int[] rVal = new int[2];

        rVal[0] = 0;
        rVal[1] = 0;

        float dY = 2 * (MagnetViewModel.HALF_HEIGHT + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE);
        float separatorY = magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.INDICATOR_ICON_SIZE + dY; // the y value of the separator

        RectF rectFa = new RectF();
        magnetViewModelA.getOuterRectF(rectFa);
        RectF rectFb = new RectF();


        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.mMagnetViewModelRows) {
            if (rectFa.bottom < separatorY) {
                for (MagnetViewModel magnetViewModelB : magnetViewModels) {
                    if (magnetViewModelA != magnetViewModelB) {
                        magnetViewModelB.getOuterRectF(rectFb);
                        if (rectFa.right < rectFb.right) {
                            Log.d("Return", "row " + Integer.toString(rVal[0]) + " col " + Integer.toString(rVal[1]));
                            return rVal;
                        }
                        rVal[1]++;
                    }
                }
                Log.d("Return", "row " + Integer.toString(rVal[0]) + " col " + Integer.toString(rVal[1]));
                return rVal;
            }

            rVal[0]++;
            separatorY += dY;
        }
        return rVal;
    }

    // Retrieve view constants from dimensions.xml
    private final Map<Magnet, MagnetViewModel> mMagnetMagnetViewModelMap;

    public MagnetGroup getModel() {
        return mMagnetGroup;
    }
    public int getSize() {
        return mSize;
    }

    private int mSize;
    // From model
    private MagnetGroup mMagnetGroup;
    private String mTitle = "";

    // View model state stuff
    private boolean mIsGhost = false;
    private boolean mIsSelected = false;

    public boolean getIsGhost() { return mIsGhost; }
    public void setIsGhost(boolean isGhost) {
        mIsGhost = isGhost;
        if (mSize == 1) {
            mMagnetViewModelRows.get(0).get(0).setIsGhost(isGhost);
        }
    }
    public boolean getIsSelected() {
        return  mIsSelected;
    }
    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }


    public MagnetViewModel getMagnetViewModel(int row, int column) {
        if (mMagnetViewModelRows.size() > row && mMagnetViewModelRows.get(row).size() > column) {
            return mMagnetViewModelRows.get(row).get(column);
        }
        return null;
    }

    public void getOuterRectF(RectF outerRectF) {
        outerRectF.set(new RectF(mOuterRectF));
    }

    private void resize() {
        mSize = getModel().getMagnetCount();

        mOuterRectF = new RectF();
        if (mSize == 1) {
            MagnetViewModel magnetViewModel = mMagnetMagnetViewModelMap.get(mMagnetGroup.getMagnets().get(0).get(0));
            magnetViewModel.setTopLeftPointF(mMagnetGroup.getPoint());
            magnetViewModel.getOuterRectF(mOuterRectF);
            magnetViewModel.setMagnetGroupViewModel(this);
            ArrayList<MagnetViewModel> magnetViewModelRow = new ArrayList<>();
            magnetViewModelRow.add(magnetViewModel);
            mMagnetViewModelRows.add(magnetViewModelRow);
            return;
        }

        float left = getModel().getPoint().x + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE;
        float top = getModel().getPoint().y + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE;
        float right = left;
        float bottom = top;
        for (List<Magnet> magnetRow : mMagnetGroup.getMagnets()) {
            ArrayList<MagnetViewModel> magnetViewModelRow = new ArrayList<>();
            for (Magnet magnet : magnetRow) {
                MagnetViewModel magnetViewModel = mMagnetMagnetViewModelMap.get(magnet);
                magnetViewModelRow.add(magnetViewModel);
                magnetViewModel.setTopLeftPointF(new PointF(left, top));
                magnetViewModel.setMagnetGroupViewModel(this);
                left += 2 * MagnetViewModel.INDICATOR_ICON_SIZE + 2 * (MagnetViewModel.HALF_WIDTH + MagnetViewModel.HIGHLIGHT_BORDER_SIZE);
                if (left > right) right = left;
            }
            mMagnetViewModelRows.add(magnetViewModelRow);
            left = getModel().getPoint().x + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE;
            top += 2 * MagnetViewModel.INDICATOR_ICON_SIZE + 2 * (MagnetViewModel.HALF_HEIGHT + MagnetViewModel.HIGHLIGHT_BORDER_SIZE);
            if (top > bottom) bottom = top;
        }
        mOuterRectF = new RectF(getModel().getPoint().x, getModel().getPoint().y, right, bottom);
    }


    public void refresh() {
        mTitle = mMagnetGroup.getTitle();
        mMagnetViewModelRows = new ArrayList<>();
        resize();
    }

    private RectF mOuterRectF;
    private ArrayList<ArrayList<MagnetViewModel>> mMagnetViewModelRows;

    public float halfWidth() {
        return mOuterRectF.centerX() - mOuterRectF.left;
    }

    public float halfHeight() {
        return mOuterRectF.bottom - mOuterRectF.centerY();
    }

    public MagnetGroupViewModel(float x, float y) {
        mMagnetMagnetViewModelMap = null;

        this.mMagnetViewModelRows = new ArrayList<>();
        ArrayList<MagnetViewModel> magnetViewModels = new ArrayList<>();
        MagnetViewModel magnetViewModel = new MagnetViewModel();
        magnetViewModel.setTopLeftPointF(new PointF(x, y));
        magnetViewModels.add(magnetViewModel);
        this.mMagnetViewModelRows.add(magnetViewModels);
        mOuterRectF = new RectF();
        magnetViewModel.getOuterRectF(mOuterRectF);
        mTitle = MagnetViewModel.TITLE;
        mSize = 1;
    }

    // Build from model:
    public MagnetGroupViewModel(MagnetGroup magnetGroup, final Map<Magnet, MagnetViewModel> magnetMagnetViewModelMap) {
        mMagnetGroup = magnetGroup;
        mMagnetMagnetViewModelMap = magnetMagnetViewModelMap;
        refresh();
    }

    public void remove(MagnetViewModel magnetViewModelA) {
        for (ArrayList<MagnetViewModel> magnetViewModels : this.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModelB : magnetViewModels) {
                if (magnetViewModelA == magnetViewModelB) {
                    magnetViewModels.remove(magnetViewModelA);
                    --mSize;
                    return;
                }
            }
        }
    }

    public float getCenterX() {
        return mOuterRectF.centerX();
    }

    public float getCenterY() {
        return mOuterRectF.centerY();
    }


    public void getCenterPointF(PointF pointF) {
        pointF.set(mOuterRectF.centerX(), mOuterRectF.centerY());
    }

    static public void draw(MagnetGroupViewModel magnetGroupViewModel, Canvas canvas, Paint paint) {

        if (magnetGroupViewModel.mSize > 1) {
            if (magnetGroupViewModel.mIsHighLighted) {
                paint.setColor(MagnetViewModel.HIGHLIGHT_BORDER_COLOR);
                if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);
                canvas.drawRoundRect(magnetGroupViewModel.mOuterRectF, MagnetViewModel.ROUND_RADIUS, MagnetViewModel.ROUND_RADIUS, paint);
            }
            paint.setColor(MagnetViewModel.BORDER_COLOR);
            if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

            canvas.drawRoundRect(new RectF(magnetGroupViewModel.mOuterRectF.left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.HIGHLIGHT_BORDER_SIZE,
                magnetGroupViewModel.mOuterRectF.right - MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.bottom - MagnetViewModel.HIGHLIGHT_BORDER_SIZE),
                    MagnetViewModel.ROUND_RADIUS, MagnetViewModel.ROUND_RADIUS, paint);

            paint.setColor(MagnetViewModel.CONTENT_COLOR);
            if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

            canvas.drawRoundRect(new RectF(magnetGroupViewModel.mOuterRectF.left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE, magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE,
                            magnetGroupViewModel.mOuterRectF.right - MagnetViewModel.HIGHLIGHT_BORDER_SIZE - MagnetViewModel.BORDER_SIZE, magnetGroupViewModel.mOuterRectF.bottom - MagnetViewModel.HIGHLIGHT_BORDER_SIZE - MagnetViewModel.BORDER_SIZE),
                    MagnetViewModel.ROUND_RADIUS, MagnetViewModel.ROUND_RADIUS, paint);

            //magnetGroupViewModel.mTitle = "Preview";
            paint.setColor(MagnetViewModel.TEXT_COLOR);
            paint.setTextSize(MagnetViewModel.TEXT_SIZE);
            if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);
            canvas.drawText(magnetGroupViewModel.mTitle, magnetGroupViewModel.getCenterX(), magnetGroupViewModel.mOuterRectF.bottom + MagnetViewModel.HIGHLIGHT_BORDER_SIZE, paint);
        }


        float dY = 2 * (MagnetViewModel.HALF_HEIGHT + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE);
        float yVal = magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.INDICATOR_ICON_SIZE + dY;
        paint.setColor(MagnetViewModel.BORDER_COLOR);
        paint.setStrokeWidth(MagnetViewModel.BORDER_SIZE);
        if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                MagnetViewModel.draw(magnetViewModel, canvas, paint);
            }
            if (magnetGroupViewModel.getSize() > 1 && yVal < magnetGroupViewModel.mOuterRectF.bottom) {
                canvas.drawLine(magnetGroupViewModel.mOuterRectF.left + 2*MagnetViewModel.HIGHLIGHT_BORDER_SIZE, yVal, magnetGroupViewModel.mOuterRectF.right - 2*MagnetViewModel.HIGHLIGHT_BORDER_SIZE, yVal, paint);
            }
            yVal += dY;
        }
    }

    static boolean contains(MagnetGroupViewModel magnetGroupViewModel, float x, float y) {
        return magnetGroupViewModel.mOuterRectF.contains(x,y);
    }

    static MagnetViewModel getMagnetViewModel(MagnetGroupViewModel magnetGroupViewModel, float x, float y) {
        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                if(magnetViewModel.contains(magnetViewModel, x, y)) return magnetViewModel;
            }
        }
        return null;
    }
}
