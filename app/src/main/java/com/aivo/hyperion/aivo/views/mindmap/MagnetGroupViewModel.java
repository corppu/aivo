package com.aivo.hyperion.aivo.views.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

    static public int[] getRowCol(MagnetGroupViewModel magnetGroupViewModel, MagnetViewModel magnetViewModel) {
        int[] rVal = new int[2];

        rVal[0] = 0;
        rVal[1] = 0;

        RectF rectFA = new RectF();
        magnetViewModel.getOuterRectF(rectFA);
        RectF rectFB = new RectF();
        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.magnetViewModels) {
            for (MagnetViewModel magnetViewModelB : magnetViewModels) {

                magnetViewModelB.getOuterRectF(rectFB);
                if (rectFA.right < rectFB.right + MagnetViewModel.INDICATOR_ICON_SIZE) break;
                ++rVal[1];
            }
            if (rectFA.bottom < rectFB.bottom + MagnetViewModel.INDICATOR_ICON_SIZE) break;
            if (magnetViewModels.size() != 0) ++rVal[0];
        }

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
    private String mTitle;

    // View model state stuff
    private boolean mIsGhost;
    private boolean mIsSelected;

    public boolean getIsGhost() { return mIsGhost; }
    public void setIsGhost(boolean isGhost) {
        mIsGhost = isGhost;
        for (ArrayList<MagnetViewModel> magnetViewModels : this.magnetViewModels) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                magnetViewModel.setIsGhost(isGhost);
            }
        }
    }
    public boolean getIsSelected() { return  mIsSelected; }
    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
//        if (mSize == 1) magnetViewModels.get(0).get(0).setIsSelected(mIsSelected);
//        for (ArrayList<MagnetViewModel> magnetViewModels : this.magnetViewModels) {
//            for (MagnetViewModel magnetViewModel : magnetViewModels) {
//                magnetViewModel.setIsSelected(isSelected);
//            }
//        }
    }


    public MagnetViewModel getMagnetViewModel(int row, int column) {
        if (magnetViewModels.size() > row && magnetViewModels.get(0).size() > column) {
            return magnetViewModels.get(row).get(column);
        }
        return null;
    }

    public void refresh() {
        mTitle = mMagnetGroup.getTitle();
        magnetViewModels = new ArrayList<>();
        mOuterRectF = new RectF(mMagnetGroup.getPoint().x, mMagnetGroup.getPoint().y, 0,0);
        mSize = 0;

        if (mMagnetGroup.getMagnets().size() == 1 && mMagnetGroup.getMagnets().get(0).size() == 1) {
            magnetViewModels.add(new ArrayList<MagnetViewModel>());
            magnetViewModels.get(0).add(new MagnetViewModel(mMagnetGroup.getMagnets().get(0).get(0)));
            magnetViewModels.get(0).get(0).setTopLeftPointF(mMagnetGroup.getPoint());
            magnetViewModels.get(0).get(0).getOuterRectF(mOuterRectF);
            mSize = 1;
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
                    if (mOuterRectF.right < pointF.x) mOuterRectF.right = pointF.x;
                    ++mSize;
                }
                if (magnetViewModelList.size() != 0) {
                    magnetViewModels.add(magnetViewModelList);
                    pointF.y += MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE * 2;
                }
                mOuterRectF.bottom = pointF.y;
                pointF.x = mOuterRectF.left + 2* MagnetViewModel.HIGHLIGHT_BORDER_SIZE;
            }
        }
    }

    // For view-model:
//    static private final int maxRowSize = 5;
//    private int currentRowSize;
//    static private final int maxRowCount = 4;
//    private int currentRowCount;

    private RectF mOuterRectF;
    private ArrayList<ArrayList<MagnetViewModel>> magnetViewModels;

    public float halfWidth() {
        return mOuterRectF.centerX() - mOuterRectF.left;
    }

    public float halfHeight() {
        return mOuterRectF.bottom - mOuterRectF.centerY();
    }

    public MagnetGroupViewModel(float x, float y) {
        mMagnetMagnetViewModelMap = null;

        this.magnetViewModels = new ArrayList<>();
        ArrayList<MagnetViewModel> magnetViewModels = new ArrayList<>();
        MagnetViewModel magnetViewModel = new MagnetViewModel();
        magnetViewModel.setTopLeftPointF(new PointF(x, y));
        magnetViewModels.add(magnetViewModel);
        this.magnetViewModels.add(magnetViewModels);
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
        for (ArrayList<MagnetViewModel> magnetViewModels : this.magnetViewModels) {
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
                canvas.drawRect(magnetGroupViewModel.mOuterRectF, paint);
            }
            paint.setColor(MagnetViewModel.BORDER_COLOR);
            if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

            canvas.drawRect(magnetGroupViewModel.mOuterRectF.left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.HIGHLIGHT_BORDER_SIZE,
                    magnetGroupViewModel.mOuterRectF.right - MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.bottom - MagnetViewModel.HIGHLIGHT_BORDER_SIZE, paint);

            paint.setColor(MagnetViewModel.CONTENT_COLOR);
            if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);

            canvas.drawRect(magnetGroupViewModel.mOuterRectF.left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE, magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE,
                    magnetGroupViewModel.mOuterRectF.right - MagnetViewModel.HIGHLIGHT_BORDER_SIZE - MagnetViewModel.BORDER_SIZE, magnetGroupViewModel.mOuterRectF.bottom - MagnetViewModel.HIGHLIGHT_BORDER_SIZE - MagnetViewModel.BORDER_SIZE, paint);

//            paint.setColor(Color.BLACK);
//            if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);
//            canvas.drawText(magnetGroupViewModel.mTitle, magnetGroupViewModel.getCenterX(), magnetGroupViewModel.getCenterY() + MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE, paint);
        }

        float dY = 2 * MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE;
        float yVal = magnetGroupViewModel.mOuterRectF.top +  2 * MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE + MagnetViewModel.INDICATOR_ICON_SIZE;;
        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.magnetViewModels) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                MagnetViewModel.draw(magnetViewModel, canvas, paint);
            }

            if (magnetGroupViewModel.getSize() > 1 && yVal < magnetGroupViewModel.mOuterRectF.bottom) {
                paint.setColor(MagnetViewModel.BORDER_COLOR);
                if (magnetGroupViewModel.mIsGhost) paint.setAlpha(MagnetViewModel.GHOST_ALPHA);
                canvas.drawLine(magnetGroupViewModel.mOuterRectF.left + 2*MagnetViewModel.HIGHLIGHT_BORDER_SIZE, yVal, magnetGroupViewModel.mOuterRectF.right - 2*MagnetViewModel.HIGHLIGHT_BORDER_SIZE, yVal, paint);
            }

            yVal += dY;
        }
     }

    public void move(float newTouchPointX, float newTouchPointY) {
        float distanceX = mOuterRectF.centerX() - newTouchPointX;
        float distanceY = mOuterRectF.centerY() - newTouchPointY;

        mOuterRectF.left -= distanceX;
        mOuterRectF.right -= distanceX;
        mOuterRectF.top -= distanceY;
        mOuterRectF.bottom -= distanceY;

        for (ArrayList<MagnetViewModel> magnetViewModels : this.magnetViewModels) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                magnetViewModel.moveDistance(distanceX, distanceY);
            }
        }
    }

    static boolean contains(MagnetGroupViewModel magnetGroupViewModel, float x, float y) {
        return magnetGroupViewModel.mOuterRectF.contains(x,y);
    }

    static MagnetViewModel getMagnetViewModel(MagnetGroupViewModel magnetGroupViewModel, float x, float y) {
        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.magnetViewModels) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                if(MagnetViewModel.contains(magnetViewModel, x, y)) return magnetViewModel;
            }
        }
        return null;
    }
}
