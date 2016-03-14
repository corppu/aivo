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

    public MagnetViewModel getMagnetViewModel(int row, int column) {
        return magnetViewModels.get(row).get(column);
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
            PointF pointF = new PointF(mOuterRectF.left, mOuterRectF.top);
            for (List<Magnet> magnets : mMagnetGroup.getMagnets()) {
                ArrayList<MagnetViewModel> magnetViewModelList = new ArrayList<>();
                for (Magnet magnet : magnets) {
                    MagnetViewModel magnetViewModel = mMagnetMagnetViewModelMap.get(magnet);
                    magnetViewModel.setTopLeftPointF(pointF);
                    magnetViewModelList.add(magnetViewModel);
                    pointF.x += MagnetViewModel.OUTER_HALF_WIDTH_WITH_INDICATOR_SIZE * 2;
                    if (mOuterRectF.right < pointF.x) mOuterRectF.right = pointF.x;
                    ++mSize;
                    Log.d("MagnetGroupViewModel", "New magnet added to group" + pointF.toString());
                }
                magnetViewModels.add(magnetViewModelList);
                pointF.y += MagnetViewModel.OUTER_HALF_HEIGHT_WITH_INDICATOR_SIZE * 2;
                mOuterRectF.bottom = pointF.y;
                pointF.x = mOuterRectF.left;
            }
        }
    }

    // For view-model:
    static private final int maxRowSize = 5;
    private int currentRowSize;
    static private final int maxRowCount = 4;
    private int currentRowCount;

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
            paint.setColor(MagnetViewModel.HIGHLIGHT_BORDER_COLOR);
            canvas.drawRect(magnetGroupViewModel.mOuterRectF, paint);

            paint.setColor(MagnetViewModel.BORDER_COLOR);
            canvas.drawRect(magnetGroupViewModel.mOuterRectF.left + MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.top + MagnetViewModel.HIGHLIGHT_BORDER_SIZE,
                    magnetGroupViewModel.mOuterRectF.right - MagnetViewModel.HIGHLIGHT_BORDER_SIZE, magnetGroupViewModel.mOuterRectF.bottom - MagnetViewModel.HIGHLIGHT_BORDER_SIZE, paint);
        }

        for (ArrayList<MagnetViewModel> magnetViewModels : magnetGroupViewModel.magnetViewModels) {
            for (MagnetViewModel magnetViewModel : magnetViewModels) {
                MagnetViewModel.draw(magnetViewModel, canvas, paint);
            }
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