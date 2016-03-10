package com.aivo.hyperion.aivo.views.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by corppu on 23.2.2016.
 */
public class MagnetGroupViewModel {
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
    private PointF mCenterPointF;


    // For view-model:
    static private final int maxRowSize = 5;
    private int currentRowSize;
    static private final int maxRowCount = 4;
    private int currentRowCount;

    private RectF mOuterRectF;
    private ArrayList<ArrayList<MagnetViewModel>> magnetViewModels;

    public MagnetGroupViewModel(float x, float y) {
        mMagnetMagnetViewModelMap = null;

        this.magnetViewModels = new ArrayList<>();
        ArrayList<MagnetViewModel> magnetViewModels = new ArrayList<>();
        MagnetViewModel magnetViewModel = new MagnetViewModel();
        this.mCenterPointF = new PointF(x,y);
        magnetViewModel.setCenterPointF(mCenterPointF);
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
        mTitle = magnetGroup.getTitle();
        mCenterPointF = magnetGroup.getPoint();
        mMagnetMagnetViewModelMap = magnetMagnetViewModelMap;
        magnetViewModels = new ArrayList<>();
        mOuterRectF = new RectF();

        mSize = 0;
/*
        float left = mCenterPointF.x - MagnetViewModel.OUTER_HALF_WIDTH_WITH_INDICATOR_SIZE * maxRowSize;
        float top = mCenterPointF.y - MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE * maxRowCount;
        PointF pointF = new PointF(
                left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE + MagnetViewModel.HALF_WIDTH,
                top + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE + MagnetViewModel.HALF_HEIGHT);
        for (List<Magnet> magnets : magnetGroup.getMagnets()) {
            ArrayList<MagnetViewModel> magnetViewModelList = new ArrayList<>();
            for (Magnet magnet : magnets) {
                MagnetViewModel magnetViewModel = magnetMagnetViewModelMap.get(magnet);
                magnetViewModel.setCenterPointF(pointF);
                magnetViewModelList.add(magnetViewModel);
                pointF.x += MagnetViewModel.OUTER_HALF_WIDTH_WITH_INDICATOR_SIZE;
                ++mSize;
            }
            magnetViewModels.add(magnetViewModelList);
            pointF.y += MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE;
            pointF.x = left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE + MagnetViewModel.HALF_WIDTH;
        }*/

        Magnet magnet = magnetGroup.getMagnets().get(0).get(0);
        MagnetViewModel magnetViewModel = magnetMagnetViewModelMap.get(magnet);
        magnetViewModels.add(new ArrayList<MagnetViewModel>());
        magnetViewModels.get(0).add(magnetViewModel);
        magnetViewModel.setCenterPointF(mCenterPointF);
        magnetViewModel.getOuterRectF(mOuterRectF);
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

//    public MagnetGroupViewModel(MagnetViewModel magnetViewModel, Map<Magnet, MagnetViewModel> mMagnetMagnetViewModelMap) {
//        this.mMagnetMagnetViewModelMap = mMagnetMagnetViewModelMap;
//        magnetViewModel.getOuterRectF(mOuterRectF);
//        magnetViewModel.getCenterPointF(mCenterPointF);
//
//        mOuterRectF.top -= MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE;
//        mOuterRectF.left -= MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE;
//        mOuterRectF.bottom += MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE;
//        mOuterRectF.right += MagnetViewModel.HIGHLIGHT_BORDER_SIZE + MagnetViewModel.BORDER_SIZE;
//        magnetViewModels.add(new ArrayList<MagnetViewModel>());
//        magnetViewModels.get(0).add(magnetViewModel);
//    }

    public float getCenterX() {
        return mCenterPointF.x;
    }

    public float getCenterY() {
        return mCenterPointF.y;
    }


    public void getCenterPointF(PointF pointF) {
        pointF.set(mCenterPointF);
    }

//    public void pushMagnetViewModel(MagnetViewModel magnetViewModel) {
//        if (magnetViewModel.getCenterY() < mOuterRectF.bottom - MagnetViewModel.HIGHLIGHT_BORDER_SIZE - MagnetViewModel.BORDER_SIZE) {
//            ArrayList<MagnetViewModel> magnetViewModels = new ArrayList<>();
//            magnetViewModels.add(magnetViewModel);
//            this.magnetViewModels.add(magnetViewModels);
//            mCenterPointF.y = mOuterRectF.bottom;
//            mOuterRectF.bottom += MagnetViewModel.HALF_HEIGHT * 2;
//        }
//
//        else {
//            this.magnetViewModels.get(this.magnetViewModels.size() - 1).add(magnetViewModel);
//            magnetViewModel.move(mCenterPointF.x, mCenterPointF.y);
//            magnetViewModel.move(mOuterRectF.right, mCenterPointF.y);
//            mCenterPointF.x = mOuterRectF.right;
//            mOuterRectF.right += MagnetViewModel.HALF_WIDTH * 2;
//        }
//    }

    static public void draw(MagnetGroupViewModel magnetGroupViewModel, Canvas canvas, Paint paint) {

        if (magnetGroupViewModel.magnetViewModels.size() == 1 && magnetGroupViewModel.magnetViewModels.get(0).size() == 1) {
            paint.setColor(MagnetViewModel.HIGHLIGHT_BORDER_COLOR);
            canvas.drawRect(magnetGroupViewModel.mOuterRectF, paint);

            paint.setColor(MagnetViewModel.BORDER_COLOR);
            canvas.drawRect(magnetGroupViewModel.mOuterRectF.left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.HIGHLIGHT_BORDER_SIZE,
                    magnetGroupViewModel.mOuterRectF.right - MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.bottom - MagnetViewModel.HIGHLIGHT_BORDER_SIZE, paint);
        }

        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.magnetViewModels) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                MagnetViewModel.draw(magnetViewModel,canvas, paint);
            }
        }
    }

    public void move(float newTouchPointX, float newTouchPointY) {
        float distanceX = mCenterPointF.x - newTouchPointX;
        float distanceY = mCenterPointF.y - newTouchPointY;

        mCenterPointF.x -= distanceX;
        mCenterPointF.y -= distanceY;
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
