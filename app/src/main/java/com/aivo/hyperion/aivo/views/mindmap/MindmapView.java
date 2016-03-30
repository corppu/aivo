package com.aivo.hyperion.aivo.views.mindmap;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.aivo.hyperion.aivo.main.MainActivity;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.Note;
import com.aivo.hyperion.aivo.models.User;
import com.aivo.hyperion.aivo.views.SearchFragment;

import java.util.HashMap;
import java.util.List;


public class MindmapView extends View
implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener, ModelListener, SearchFragment.OnSearchFragmentInteractionListener {

    // touch and drawing
    private final int BACKGROUND_COLOR = Color.WHITE;
    private final int BORDER_COLOR = Color.BLACK;

    private RectF mOuterRectF = new RectF(0,0,0,0);

    private PointF topLeft = new PointF(0,0);
    private MagnetGroupViewModel leftMostMagnetGroup;
    private MagnetGroupViewModel rightMostMagnetGroup;
    private MagnetGroupViewModel topMostMagnetGroup;
    private MagnetGroupViewModel bottomMostMagnetGroup;

    private void findOuterRectF() {
        mOuterRectF.set(0,0,0,0);
        RectF outerRectFa = new RectF();
        RectF outerRectFb = new RectF();
        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            magnetGroupViewModel.getOuterRectF(outerRectFa);

            if (leftMostMagnetGroup == null) {
                leftMostMagnetGroup = magnetGroupViewModel;
                mOuterRectF.left = outerRectFa.left;
            }
            else {
                leftMostMagnetGroup.getOuterRectF(outerRectFb);
                if (outerRectFa.left <= outerRectFb.left) {
                    leftMostMagnetGroup = magnetGroupViewModel;
                    mOuterRectF.left = outerRectFa.left;
                }
            }

            if (topMostMagnetGroup == null) {
                topMostMagnetGroup = magnetGroupViewModel;
                mOuterRectF.top = outerRectFa.top;
            }
            else {
                topMostMagnetGroup.getOuterRectF(outerRectFb);
                if (outerRectFa.top <= outerRectFb.top) {
                    topMostMagnetGroup = magnetGroupViewModel;
                    mOuterRectF.top = outerRectFa.top;
                }
            }
            if (rightMostMagnetGroup == null) {
                rightMostMagnetGroup = magnetGroupViewModel;
                mOuterRectF.right = outerRectFa.right;
            }
            else {
                rightMostMagnetGroup.getOuterRectF(outerRectFb);
                if (outerRectFa.right >= outerRectFb.right) {
                    rightMostMagnetGroup = magnetGroupViewModel;
                    mOuterRectF.right = outerRectFa.right;
                }
            }

            if (bottomMostMagnetGroup == null) {
                bottomMostMagnetGroup = magnetGroupViewModel;
                mOuterRectF.bottom = outerRectFa.bottom;
            }
            else {
                bottomMostMagnetGroup.getOuterRectF(outerRectFb);
                if (outerRectFa.bottom >= outerRectFb.bottom) {
                    bottomMostMagnetGroup = magnetGroupViewModel;
                    mOuterRectF.bottom = outerRectFa.bottom;
                }
            }
        }
    }




    static private String TAG = "MindmapView";
    private Rect mClipBounds = new Rect();
    private Rect mDrawingRect = new Rect();
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private Paint mPaint = new Paint();
    private float mScaleFactor = 1.0f;


    // Models
    private HashMap<MagnetGroup, MagnetGroupViewModel> mMagnetGroupMagnetViewModelHashMap = new HashMap<>();
    private SparseArray<MagnetGroupViewModel> mActionDownMagnetGroupViewModels = new SparseArray<>();

    private HashMap<Line, LineViewModel> mLineViewModelHashMap = new HashMap<>();
    private SparseArray<LineViewModel> mActionDownLineViewModels = new SparseArray<>();

    private HashMap<Magnet, MagnetViewModel> mMagnetMagnetViewModelHashMap = new HashMap<>();
    private SparseArray<MagnetViewModel> mActionDownMagnetViewModels = new SparseArray<>();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawColor(BORDER_COLOR);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.translate(-topLeft.x, -topLeft.y);
        canvas.getClipBounds(mClipBounds);
        getDrawingRect(mDrawingRect);

        mPaint.setColor(BACKGROUND_COLOR);

        canvas.drawRect(mOuterRectF, mPaint);

        for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            lineViewModel.draw(canvas, mPaint);
        }

        for (int i = 0; i < mActionDownLineViewModels.size(); ++i) {
            mActionDownLineViewModels.valueAt(i).draw(canvas, mPaint);
        }

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            MagnetGroupViewModel.draw(magnetGroupViewModel, canvas, mPaint);
        }

        for (int i = 0; i < mActionDownMagnetGroupViewModels.size(); ++i) {
            MagnetGroupViewModel.draw(mActionDownMagnetGroupViewModels.valueAt(i), canvas, mPaint);
        }

        for (int i = 0; i < mActionDownMagnetViewModels.size(); ++i) {
            MagnetViewModel.draw(mActionDownMagnetViewModels.valueAt(i), canvas, mPaint);
        }
    }

    @Override
    public void onUserOpen(User user) {

    }

    @Override
    public void onUserChange(User user) {

    }

    @Override
    public void onUserClosed() {

    }

    @Override
    public void onMindmapOpen(Mindmap mindmap) {

    }

    @Override
    public void onMindmapTitleChange(Mindmap mindmap) {

    }

    @Override
    public void onMindmapClosed() {
        MainActivity.getModelMediator().unregisterListener(this);
    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {
        MagnetGroupViewModel magnetGroupViewModel = new MagnetGroupViewModel(magnetGroup, mMagnetMagnetViewModelHashMap);
        mMagnetGroupMagnetViewModelHashMap.put(magnetGroup, magnetGroupViewModel);
        //Log.d(TAG, "magnetGroup has been created!");

        findOuterRectF();
        invalidate();
    }

    @Override
    public void onMagnetGroupChange(MagnetGroup magnetGroup) {
        //Log.d(TAG, "magnetGroup has changed!");

        MagnetGroupViewModel magnetGroupViewModel = mMagnetGroupMagnetViewModelHashMap.get(magnetGroup);
        magnetGroupViewModel.refresh();

        findOuterRectF();
        invalidate();
    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {
        //Log.d(TAG, "magnetGroup has been deleted");

        if (mSelectedMagnetGroupViewModel != null && mSelectedMagnetGroupViewModel.getModel() == magnetGroup) {
            mSelectedMagnetGroupViewModel = null;
            ((MainActivity)this.getContext()).onRemoveSelection();
        }

        mMagnetGroupMagnetViewModelHashMap.remove(magnetGroup);

        if (magnetGroup == leftMostMagnetGroup.getModel()) {
            leftMostMagnetGroup = null;
        }
        if (magnetGroup == topMostMagnetGroup.getModel()) {
            topMostMagnetGroup = null;
        }
        if (magnetGroup == rightMostMagnetGroup.getModel()) {
            rightMostMagnetGroup = null;
        }
        if (magnetGroup == bottomMostMagnetGroup.getModel()) {
            bottomMostMagnetGroup = null;
        }

        findOuterRectF();
        invalidate();
    }

    @Override
    public void onMagnetCreate(Magnet magnet) {
        mMagnetMagnetViewModelHashMap.put(magnet, new MagnetViewModel(magnet));
        //Log.d(TAG, "magnet has been deleted");

        findOuterRectF();
        invalidate();
    }

    @Override
    public void onMagnetChange(Magnet magnet) {
        //Log.d(TAG, "magnet has changed");

        MagnetViewModel magnetViewModel = mMagnetMagnetViewModelHashMap.get(magnet);
        magnetViewModel.refresh();
        if (magnetViewModel == mSelectedMagnetViewModel) {
            ((MainActivity) getContext()).onSelectMagnet(magnet);
        }

        findOuterRectF();
        invalidate();
    }

    @Override
    public void onMagnetDelete(Magnet magnet) {
        //Log.d(TAG, "magnet has been deleted!");

        if (mSelectedMagnetViewModel != null && mSelectedMagnetViewModel.getModel() == magnet) {
            mSelectedMagnetViewModel = null;
            ((MainActivity)this.getContext()).onRemoveSelection();
        }

        mMagnetMagnetViewModelHashMap.remove(magnet);

        findOuterRectF();
        invalidate();
    }

    @Override
    public void onLineCreate(Line line) {
        LineViewModel lineViewModel = new LineViewModel(line, mMagnetGroupMagnetViewModelHashMap);
        mLineViewModelHashMap.put(line, lineViewModel);
        if (line.getPoints().isEmpty()) {
            line.actionAddPoint(lineViewModel.getMiddlePointF(), 0);
        }

        invalidate();
    }

    @Override
    public void onLineChange(Line line) {
        mLineViewModelHashMap.get(line).refresh();
        invalidate();
    }

    @Override
    public void onLineDelete(Line line) {

        if (mSelectedLineViewModel != null && mSelectedLineViewModel.getLine() == line) {
            mSelectedLineViewModel = null;
            ((MainActivity)this.getContext()).onRemoveSelection();
        }

        mLineViewModelHashMap.remove(line);
        invalidate();
    }

    @Override
    public void onNoteCreate(Note note) {

    }

    @Override
    public void onNoteChange(Note note) {

    }

    @Override
    public void onNoteDelete(Note note) {

    }

    @Override
    public void onException(Exception e) {
        //Log.d(TAG, e.toString());
    }

    boolean onActionDownEvent(MotionEvent e) {
        MagnetGroupViewModel magnetGroupViewModel;
        MagnetViewModel magnetViewModel;
        LineViewModel lineViewModel;
        int pointerIndex = e.getActionIndex();
        int pointerId = e.getPointerId(pointerIndex);
        float x = e.getX(pointerIndex) / mScaleFactor + mClipBounds.left;
        float y = e.getY(pointerIndex) / mScaleFactor + mClipBounds.top;

        for (MagnetGroupViewModel magnetGroupViewModelParent : mMagnetGroupMagnetViewModelHashMap.values()) {
            if (MagnetGroupViewModel.contains(magnetGroupViewModelParent, x, y)
                    && mActionDownMagnetGroupViewModels.indexOfValue(magnetGroupViewModelParent) == -1) {
                magnetViewModel = MagnetGroupViewModel.getMagnetViewModel(magnetGroupViewModelParent, x, y);

                if (magnetViewModel != null) {

                    if (magnetGroupViewModelParent.getSize() > 1) {
                        magnetViewModel.setIsGhost(true);
                        mActionDownMagnetViewModels.put(pointerId, magnetViewModel);
                        return true;
                    }
                    else if (magnetViewModel.getIsSelected() && MagnetViewModel.bottomIconPressed(magnetViewModel, x, y)) {

                        magnetGroupViewModel = new MagnetGroupViewModel(x, y);
                        lineViewModel = new LineViewModel(magnetGroupViewModelParent, magnetGroupViewModel);

                        mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModel);
                        mActionDownLineViewModels.append(pointerId, lineViewModel);
                        lineViewModel.setIsGhost(true);
                        return true;
                    }
                    else {
                        magnetViewModel.setIsGhost(true);
                    }
                }


                magnetGroupViewModelParent.setIsGhost(true);
                mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModelParent);
                return true;
            }
         }


       for (LineViewModel lineViewModell : mLineViewModelHashMap.values()) {
            if (lineViewModell.contains(x, y)
                    && mActionDownLineViewModels.indexOfValue(lineViewModell) == -1) {
                mActionDownLineViewModels.append(pointerId, lineViewModell);
                lineViewModell.setIsGhost(true);
                break;
            }
        }

        return true;
    }


    boolean onActionUpEvent(MotionEvent e) {

        int pointerIndex = e.getActionIndex();
        int pointerId = e.getPointerId(pointerIndex);

        PointF pointF = new PointF();
        MagnetViewModel magnetViewModel = mActionDownMagnetViewModels.get(pointerId);

        MagnetGroupViewModel magnetGroupViewModel;
        if (magnetViewModel != null) {
            magnetViewModel.setIsGhost(false);
            mActionDownMagnetViewModels.remove(pointerId);


            magnetGroupViewModel = mMagnetGroupMagnetViewModelHashMap.get(magnetViewModel.getModel().getMagnetGroup());
            magnetGroupViewModel.setIsGhost(false);
            if (MagnetGroupViewModel.contains(magnetGroupViewModel, magnetViewModel.getCenterX(), magnetViewModel.getCenterY())) {
//                return true;

                int[] rowCol = MagnetGroupViewModel.getRowCol(magnetGroupViewModel, magnetViewModel);
                if (magnetGroupViewModel.getMagnetViewModel(rowCol[0], rowCol[1]) != magnetViewModel) {
                    magnetViewModel.getModel().actionMoveTo(magnetGroupViewModel.getModel(), rowCol[0], rowCol[1], false);
                } else {
                    magnetGroupViewModel.refresh();
                }
                return true;
            }

            for (MagnetGroupViewModel magnetGroupViewModelz : mMagnetGroupMagnetViewModelHashMap.values()) {
                if (MagnetGroupViewModel.contains(magnetGroupViewModelz, magnetViewModel.getCenterX(), magnetViewModel.getCenterY())) {
                    int[] rowCol = MagnetGroupViewModel.getRowCol(magnetGroupViewModelz, magnetViewModel);
                    magnetViewModel.getModel().actionMoveTo(magnetGroupViewModelz.getModel(), rowCol[0], rowCol[1], false);
                    return true;
                }
            }


            magnetViewModel.getCenterPointF(pointF);
            magnetViewModel.getModel().actionMoveTo(pointF);
            return true;
        }


        magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);
        if (magnetGroupViewModel != null) {
            magnetGroupViewModel.setIsGhost(false);
            mActionDownMagnetGroupViewModels.remove(pointerId);

            magnetGroupViewModel.getCenterPointF(pointF);
            MagnetGroup magnetGroup = magnetGroupViewModel.getModel();
            if (magnetGroup != null) {

                if (magnetGroupViewModel.getSize() == 1) {

                    magnetViewModel = magnetGroupViewModel.getMagnetViewModel(0,0);
                    for (MagnetGroupViewModel magnetGroupViewModelz : mMagnetGroupMagnetViewModelHashMap.values()) {
                        if (magnetGroupViewModelz.getModel() == magnetGroupViewModel.getModel()) continue;
                        if (MagnetGroupViewModel.contains(magnetGroupViewModelz, pointF.x, pointF.y)) {
                            if (!magnetViewModel.getModel().getMagnetGroup().equals(magnetGroupViewModelz.getModel())) {

                                int[] rowCol = MagnetGroupViewModel.getRowCol(magnetGroupViewModelz, magnetViewModel);
                                magnetViewModel.getModel().actionMoveTo(magnetGroupViewModelz.getModel(), rowCol[0], rowCol[1], false);
                            } else {
                                continue;
                            }

                            return true;
                        }
                    }
                }


                pointF.x -= magnetGroupViewModel.halfWidth();
                pointF.y -= magnetGroupViewModel.halfHeight();
                magnetGroup.actionMoveTo(pointF);
                invalidate();
            } else {
                LineViewModel lineViewModel = mActionDownLineViewModels.get(pointerId);

                if (lineViewModel != null) {
                    mActionDownLineViewModels.remove(pointerId);
                    lineViewModel.setIsGhost(false);

                    for (MagnetGroupViewModel magnetGroupViewModelz : mMagnetGroupMagnetViewModelHashMap.values()) {
                        if (MagnetGroupViewModel.contains(magnetGroupViewModelz, pointF.x, pointF.y) && lineViewModel.getParent().getModel() != magnetGroupViewModelz.getModel()) {
                            lineViewModel.getParent().getModel().actionCreateLine(magnetGroupViewModelz.getModel());
                            invalidate();
                            return true;
                        }
                    }

                    ((MainActivity)this.getContext()).onCreateMagnet(lineViewModel.getParent().getModel(), pointF);
                }
                else {
                    ((MainActivity)this.getContext()).onCreateMagnet(pointF);
                }
            }

            invalidate();
            return true;
        }

        LineViewModel lineViewModel = mActionDownLineViewModels.get(e.getPointerId(pointerIndex));
        if (lineViewModel != null) {
            mActionDownLineViewModels.remove(e.getPointerId(pointerIndex));
            lineViewModel.setIsGhost(false);
            lineViewModel.getLine().actionMovePoint(lineViewModel.getLine().getPoints().get(0), lineViewModel.getMiddlePointF());
            invalidate();
        }
        return true;
    }

    boolean onActionMoveEvent(MotionEvent e, float distanceX, float distanceY) {
        if (mActionDownMagnetViewModels.size() == 0 && mActionDownMagnetGroupViewModels.size() == 0 && mActionDownLineViewModels.size() == 0) return false;
        int pointerIndex;
        int pointerId;
        float x;
        float y;
        LineViewModel lineViewModel;
        MagnetGroupViewModel magnetGroupViewModel;
            for (pointerIndex = 0; pointerIndex < e.getPointerCount(); pointerIndex++) {
                x = e.getX(pointerIndex) / mScaleFactor + mClipBounds.left;
                y = e.getY(pointerIndex) / mScaleFactor + mClipBounds.top;
                pointerId = e.getPointerId(pointerIndex);

                magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);
                if (magnetGroupViewModel != null) {
                    magnetGroupViewModel.moveDistance(distanceX, distanceY);
                }
                else {
                    lineViewModel = mActionDownLineViewModels.get(pointerId);
                    if (lineViewModel != null) {
                        lineViewModel.moveMiddlePointByDistance(x, y);
                    }
                    else {
                        MagnetViewModel magnetViewModel = mActionDownMagnetViewModels.get(pointerId);
                        if (magnetViewModel != null) {
                            magnetViewModel.moveDistance(distanceX, distanceY);
                        }
                    }
                }
            }

        findOuterRectF();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mScaleGestureDetector.onTouchEvent(e);
        mGestureDetector.onTouchEvent(e);

        if (!mScaleGestureDetector.isInProgress()) {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:

                    onActionDownEvent(e);
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                        onActionUpEvent(e);
                        invalidate();
                    break;

                case MotionEvent.ACTION_MOVE:
//                    if(onActionMoveEvent(e)) {
//
//                        invalidate();
//                        return true;
//                    }
//                    break;

                default:
                    break;
            }
        }
        return true;
    }

    public MindmapView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MindmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public MindmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MindmapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

     private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
         mScaleGestureDetector = new ScaleGestureDetector(context, this);
         mGestureDetector = new GestureDetector(context, this);
         mGestureDetector.setIsLongpressEnabled(true);

         MainActivity.getModelMediator().registerListener(this);

         mMagnetMagnetViewModelHashMap.clear();
         mMagnetGroupMagnetViewModelHashMap.clear();
         mLineViewModelHashMap.clear();

         for(MagnetGroup magnetGroup : MainActivity.getModelMediator().getMindmap().getMagnetGroups()) {
             for (List<Magnet> magnets : magnetGroup.getMagnets()) {
                 for (Magnet magnet : magnets) {
                     onMagnetCreate(magnet);
                 }
             }
             onMagnetGroupCreate(magnetGroup);
         }

         for (Line line : MainActivity.getModelMediator().getMindmap().getLines()) {
             onLineCreate(line);
         }

         findOuterRectF();
     }

    private MagnetGroupViewModel mSelectedMagnetGroupViewModel;
    private MagnetViewModel mSelectedMagnetViewModel;
    private LineViewModel mSelectedLineViewModel;


    private boolean trySelect(float x, float y) {
        ((MainActivity)this.getContext()).onRemoveSelection();

        if (mSelectedMagnetViewModel != null) {
            if (mSelectedMagnetViewModel.contains(mSelectedMagnetViewModel, x, y)) {
                return true;
            }
            mSelectedMagnetViewModel.setIsSelected(false);
            mSelectedMagnetViewModel = null;
        }

        if (mSelectedLineViewModel != null) {
            if (mSelectedLineViewModel.contains(x, y)) {
                return true;
            }
            mSelectedLineViewModel.setIsSelected(false);
            mSelectedLineViewModel = null;
        }

        if (mSelectedMagnetGroupViewModel != null) {
            mSelectedMagnetGroupViewModel.setIsSelected(false);
            mSelectedMagnetGroupViewModel = null;
        }

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            if (MagnetGroupViewModel.contains(magnetGroupViewModel, x, y)) {
                MagnetViewModel magnetViewModel = MagnetGroupViewModel.getMagnetViewModel(magnetGroupViewModel, x, y);
                if (magnetViewModel != null) {
                    magnetViewModel.setIsSelected(true);
                    mSelectedMagnetViewModel = magnetViewModel;
                    ((MainActivity) this.getContext()).onSelectMagnet(mSelectedMagnetViewModel.getModel());

                    invalidate();
                    return true;
                }

                magnetGroupViewModel.setIsSelected(true);
                mSelectedMagnetGroupViewModel = magnetGroupViewModel;
                ((MainActivity)this.getContext()).onSelectMagnetGroup(mSelectedMagnetGroupViewModel.getModel());
                invalidate();
                return true;
            }
        }

        for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            if (lineViewModel.contains(x,y)) {
                lineViewModel.setIsSelected(true);
                mSelectedLineViewModel = lineViewModel;
                ((MainActivity)this.getContext()).onSelectLine(mSelectedLineViewModel.getLine());
                invalidate();
                return true;
            }
        }

        invalidate();
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        final float x = e.getX() / mScaleFactor + mClipBounds.left;
        final float y = e.getY() / mScaleFactor + mClipBounds.top;
        if(!tryOpenSelected(x, y)) trySelect(x, y);
        invalidate();
        return true;
    }

    public boolean tryOpenSelected(float x, float y) {
        if (mSelectedMagnetViewModel != null) {
            if (mSelectedMagnetViewModel.contains(mSelectedMagnetViewModel, x, y)) {
                ((MainActivity) this.getContext()).onEditMagnet(mSelectedMagnetViewModel.getModel());
                invalidate();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: " + e.toString());
        final float x = e.getX() / mScaleFactor + mClipBounds.left;
        final float y = e.getY() / mScaleFactor + mClipBounds.top;

        if (!tryOpenSelected(x, y) && trySelect(x, y) && mSelectedMagnetViewModel != null) {
            ((MainActivity) this.getContext()).onEditMagnet(mSelectedMagnetViewModel.getModel());
        } else if (mSelectedLineViewModel == null && mSelectedMagnetGroupViewModel == null && mSelectedMagnetViewModel == null) {
            ((MainActivity) this.getContext()).onCreateMagnet(new PointF(x, y));
        }

        invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //Log.d(TAG, "onDoubleTapEvent: " + e.toString());
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //Log.d(TAG, "onDown: " + e.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Log.d(TAG, "onShowPress: " + e.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //Log.d(TAG, "onSingleTapUp: " + e.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if(mScaleFactor != 0) {
            distanceX /= mScaleFactor;
            distanceY /= mScaleFactor;
        }

        Log.d(TAG, "distance " + Float.toString(distanceX) + "," +  Float.toString(distanceY));

        if (mActionDownMagnetViewModels.size() == 0 && mActionDownMagnetGroupViewModels.size() == 0 && mActionDownLineViewModels.size() == 0) {
            topLeft.x += distanceX; //Math.min(Math.max(mOuterRectF.left, topLeft.x + distanceX), mOuterRectF.right);
            topLeft.y += distanceY; //Math.min(Math.max(mOuterRectF.top, topLeft.y + distanceY), mOuterRectF.bottom);
        }
        else {
            int pointerIndex;
            int pointerId;

            LineViewModel lineViewModel;
            MagnetGroupViewModel magnetGroupViewModel;
            for (pointerIndex = 0; pointerIndex < e2.getPointerCount(); pointerIndex++) {

                pointerId = e2.getPointerId(pointerIndex);

                magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);
                if (magnetGroupViewModel != null) {
                    //magnetGroupViewModel.move(x, y);
                    magnetGroupViewModel.moveDistance(distanceX, distanceY);
                    findOuterRectF();
                } else {
                    lineViewModel = mActionDownLineViewModels.get(pointerId);
                    if (lineViewModel != null) {
                        lineViewModel.moveMiddlePointByDistance(distanceX, distanceY);
                    } else {
                        MagnetViewModel magnetViewModel = mActionDownMagnetViewModels.get(pointerId);
                        if (magnetViewModel != null) {
                            //magnetViewModel.move(x, y);
                            magnetViewModel.moveDistance(distanceX, distanceY);
                            findOuterRectF();
                        }
                    }
                }
            }
        }

        if (topLeft.x < mOuterRectF.left) topLeft.x = mOuterRectF.left;
        else if (topLeft.x > mOuterRectF.right) topLeft.x = mOuterRectF.right;
        if (topLeft.y < mOuterRectF.top) topLeft.y = mOuterRectF.top;
        else  if (topLeft.y > mOuterRectF.bottom) topLeft.y = mOuterRectF.bottom;


        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //Log.d(TAG, "onLongPress: " + e.toString());

        int pointerId = e.getPointerId(e.getActionIndex());
        MagnetGroupViewModel magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);
        if (magnetGroupViewModel != null && magnetGroupViewModel.getSize() == 1) {
            MagnetGroup magnetGroup = magnetGroupViewModel.getModel();
            if (magnetGroup != null) {
                mActionDownMagnetGroupViewModels.remove(pointerId);
                Magnet magnet = magnetGroup.getMagnets().get(0).get(0);
                magnet.actionDelete();
                return;
            }
        }

        MagnetViewModel magnetViewModel = mActionDownMagnetViewModels.get(pointerId);
        if (magnetViewModel != null) {
            Magnet magnet = magnetViewModel.getModel();
            if (magnet != null) {
                mActionDownMagnetViewModels.remove(pointerId);
                magnet.actionDelete();
                return;
            }
        }

        LineViewModel lineViewModel = mActionDownLineViewModels.get(pointerId);
        if (lineViewModel != null) {
            Line line = lineViewModel.getLine();
            if (line != null) {
                mActionDownLineViewModels.remove(pointerId);
                line.actionDelete();
            }
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Log.d(TAG, "onFling");

        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //Log.d(TAG, "onScale: " + detector.toString());

        mScaleFactor = Math.min(Math.max(0.25f,mScaleFactor * detector.getScaleFactor()), 2.0f);
        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //Log.d(TAG, "onScaleBegin: " + detector.toString());
        return mActionDownMagnetGroupViewModels.size() == 0 && mActionDownLineViewModels.size() == 0 && mActionDownMagnetViewModels.size() == 0;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        //Log.d(TAG, "onScaleEnd: " + detector.toString());
    }

    @Override
    public void search(String text, boolean image, boolean video, boolean file) {
        for (MagnetViewModel magnetViewModel : mMagnetMagnetViewModelHashMap.values()) {
            if ((image == magnetViewModel.getModel().hasImage() && (video == magnetViewModel.getModel().hasVideo()) && magnetViewModel.getModel().getTitle().contains(text))) {
                magnetViewModel.setIsGhost(false);
                continue;
            }

            magnetViewModel.setIsGhost(true);
        }

        invalidate();
    }
}
