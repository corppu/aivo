package com.aivo.hyperion.aivo.views.mindmap;

import android.content.Context;
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
public class MagnetGroupViewModel {
    private boolean mHasMoved = false;
    public boolean getHasMoved()
    {
        return mHasMoved;
    }
    public void setHasMoved(boolean hasMoved)
    {
        mHasMoved = hasMoved;
        Log.d("mHasMoved", Boolean.toString(mHasMoved));
    }


    static public int[] getRowCol(MagnetGroupViewModel magnetGroupViewModel, MagnetViewModel magnetViewModel) {
        int[] rVal = new int[2];

        rVal[0] = 0;
        rVal[1] = 0;

        RectF rectFA = new RectF();
        magnetViewModel.getOuterRectF(rectFA);
        RectF rectFB = new RectF();
        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModelB : magnetViewModels) {
                if (magnetViewModel == magnetViewModelB) continue;
                magnetViewModelB.getOuterRectF(rectFB);
                if (rectFB.right + MagnetViewModel.INDICATOR_ICON_SIZE > rectFA.right && rectFA.bottom < rectFB.bottom + MagnetViewModel.INDICATOR_ICON_SIZE) {
                    return rVal;
                }

                ++rVal[1];
            }
            if (rectFB.right + MagnetViewModel.INDICATOR_ICON_SIZE < rectFA.right && rectFA.bottom < rectFB.bottom + MagnetViewModel.INDICATOR_ICON_SIZE) {
                return rVal;
            }

            rVal[1] = 0;
            if (magnetViewModels.size() == 1 && magnetViewModels.get(0) == magnetViewModel) continue;
            ++rVal[0];
        }

        Log.d("Return", "row " + Integer.toString(rVal[0]) + " col " + Integer.toString(rVal[1]));
        return rVal;
    }

    // Retrieve view constants from dimensions.xml
    private Context mContext;
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
        for (ArrayList<MagnetViewModel> magnetViewModels : this.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                magnetViewModel.setIsGhost(isGhost);
            }
        }
    }
    public boolean getIsSelected() { return  mIsSelected; }
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
                left += 2 * MagnetViewModel.INDICATOR_ICON_SIZE + 2 * (MagnetViewModel.HALF_WIDTH + MagnetViewModel.HIGHLIGHT_BORDER_SIZE);
                if (left > right) right = left;
            }
            mMagnetViewModelRows.add(magnetViewModelRow);
            left = getModel().getPoint().x + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE;
            top += 2 * MagnetViewModel.INDICATOR_ICON_SIZE + 2 * (MagnetViewModel.HALF_HEIGHT + MagnetViewModel.HIGHLIGHT_BORDER_SIZE);
            if (top > bottom) bottom = top;
        }

        mSize = getModel().getMagnetCount();
        mOuterRectF = new RectF(getModel().getPoint().x, getModel().getPoint().y, right, bottom);
        if (mSize == 1) getMagnetViewModel(0,0).getOuterRectF(mOuterRectF);
    }


    public void refresh() {
        mTitle = mMagnetGroup.getTitle();
        mMagnetViewModelRows = new ArrayList<>();
        mOuterRectF = new RectF(mMagnetGroup.getPoint().x, mMagnetGroup.getPoint().y, Float.MIN_VALUE + 1f, Float.MIN_VALUE + 1f);

        resize();

        /*
        if (mMagnetGroup.getMagnetCount() == 1) {

            for (List<Magnet> magnetRow : mMagnetGroup.getMagnets()) {
                for (Magnet magnet : magnetRow) {
                    mMagnetViewModelRows.add(new ArrayList<MagnetViewModel>());
                    mMagnetViewModelRows.get(0).add(mMagnetMagnetViewModelMap.get(magnet));
                    mMagnetViewModelRows.get(0).get(0).setTopLeftPointF(mMagnetGroup.getPoint());
                    mMagnetViewModelRows.get(0).get(0).getOuterRectF(mOuterRectF);
                    mSize = 1;
                    mMagnetViewModelRows.get(0).get(0).getOuterRectF(mOuterRectF);
                }
            }
        }
        else {
            PointF pointF = new PointF(mOuterRectF.left + 2 * MagnetViewModel.HIGHLIGHT_BORDER_SIZE, mOuterRectF.top + 2 * MagnetViewModel.HIGHLIGHT_BORDER_SIZE);
            for (List<Magnet> magnets : mMagnetGroup.getMagnets()) {
                ArrayList<MagnetViewModel> magnetViewModelList = new ArrayList<>();
                for (Magnet magnet : magnets) {
                    MagnetViewModel magnetViewModel = mMagnetMagnetViewModelMap.get(magnet);
                    magnetViewModel.setTopLeftPointF(pointF);
                    magnetViewModelList.add(magnetViewModel);
                    pointF.x += MagnetViewModel.OUTER_HALF_WIDTH_WITH_INDICATOR_SIZE * 2;

                    Log.d("GroupSize before:" , mOuterRectF.toString());
                    if (mOuterRectF.right < pointF.x) mOuterRectF.right = pointF.x;
                    Log.d("GroupSize after:" , mOuterRectF.toString());
                    ++mSize;
                }
                if (magnetViewModelList.size() != 0) {
                    mMagnetViewModelRows.add(magnetViewModelList);
                    pointF.y += MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE * 2;
                }
                mOuterRectF.bottom = pointF.y;
                pointF.x = mOuterRectF.left + 2 * MagnetViewModel.HIGHLIGHT_BORDER_SIZE;
            }
        }
        */
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
            if (magnetGroupViewModel.mIsSelected || magnetGroupViewModel.mIsGhost) {
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

//            paint.setColor(Color.BLACK);
//            if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);
//            canvas.drawText(magnetGroupViewModel.mTitle, magnetGroupViewModel.getCenterX(), magnetGroupViewModel.getCenterY() + MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE, paint);
        }


        float yVal = 0;
        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                MagnetViewModel.draw(magnetViewModel, canvas, paint);
                yVal = magnetViewModel.getCenterY() + MagnetViewModel.HALF_HEIGHT + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE;
            }

            if (magnetGroupViewModel.getSize() > 1 && yVal < magnetGroupViewModel.mOuterRectF.bottom) {
                paint.setColor(MagnetViewModel.BORDER_COLOR);
                if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);
                canvas.drawLine(magnetGroupViewModel.mOuterRectF.left + 2*MagnetViewModel.HIGHLIGHT_BORDER_SIZE, yVal, magnetGroupViewModel.mOuterRectF.right - 2*MagnetViewModel.HIGHLIGHT_BORDER_SIZE, yVal, paint);
            }
        }
     }

    public void moveDistance(float distanceX, float distanceY) {
        mOuterRectF.left -= distanceX;
        mOuterRectF.right -= distanceX;
        mOuterRectF.top -= distanceY;
        mOuterRectF.bottom -= distanceY;

        for (ArrayList<MagnetViewModel> magnetViewModels : this.mMagnetViewModelRows) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                magnetViewModel.moveDistance(distanceX, distanceY);
            }
        }
    }

    public void move(float newTouchPointX, float newTouchPointY) {
        float distanceX = mOuterRectF.centerX() - newTouchPointX;
        float distanceY = mOuterRectF.centerY() - newTouchPointY;

        moveDistance(distanceX, distanceY);
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
