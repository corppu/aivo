package com.aivo.hyperion.aivo.views.mindmap;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;

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
    private final int BORDER_COLOR = Color.WHITE;
    private RectF mOuterRectF = new RectF(0, 0, 0, 0);
    private PointF mTopLeft = new PointF(0, 0);
    private ViewModel mSelectedViewModel;
    private ViewModel mLeftMostViewModel;
    private ViewModel mRightMostViewModel;
    private ViewModel mTopMostViewModel;
    private ViewModel mBottomMostViewModel;
    private boolean mScrollInProgress;
    private Rect mClipBounds = new Rect();
    private GestureDetector mGestureDetector;
    boolean mSearchActive;
    private ScaleGestureDetector mScaleGestureDetector;
    private Paint mPaint = new Paint();
    private float mScaleFactor = 1.0f;
    private HashMap<MagnetGroup, MagnetGroupViewModel> mMagnetGroupMagnetViewModelHashMap = new HashMap<>();
    private SparseArray<MagnetGroupViewModel> mActionDownMagnetGroupViewModels = new SparseArray<>();
    private HashMap<Line, LineViewModel> mLineViewModelHashMap = new HashMap<>();
    private SparseArray<LineViewModel> mActionDownLineViewModels = new SparseArray<>();
    private HashMap<Magnet, MagnetViewModel> mMagnetMagnetViewModelHashMap = new HashMap<>();
    private SparseArray<MagnetViewModel> mActionDownMagnetViewModels = new SparseArray<>();





    float getCanvasX(float touchX) {
        return touchX / mScaleFactor + mClipBounds.left;
    }

    float getCanvasY(float touchY) {
        return touchY / mScaleFactor + mClipBounds.top;
    }

    float getTouchX(float canvasX) {
        return (canvasX - mClipBounds.left) * mScaleFactor;
    }

    float getTouchY(float canvasY) {
        return (canvasY - mClipBounds.top) * mScaleFactor;
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
        findOuterRectF();

        for (MagnetGroup magnetGroup : MainActivity.getModelMediator().getMindmap().getMagnetGroups()) {
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
    }

    private void findOuterRectF() {
        mOuterRectF.set(0, 0, 0, 0);

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            findOuterRectF(magnetGroupViewModel);
        }

        for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            findOuterRectF(lineViewModel);
        }

        if (mOuterRectF.left > 0) mOuterRectF.left = 0;
        if (mOuterRectF.top > 0) mOuterRectF.top = 0;
        if (mOuterRectF.right < getWidth()) mOuterRectF.right = getWidth();
        if (mOuterRectF.bottom < getHeight()) mOuterRectF.bottom = getHeight();
    }

    private void findOuterRectF(ViewModel viewModelA) {
        RectF outerRectFa = new RectF();

        viewModelA.getOuterRectF(outerRectFa);
        if (outerRectFa.left == Float.MIN_VALUE
                || outerRectFa.top == Float.MIN_VALUE
                || outerRectFa.right == Float.MIN_VALUE
                || outerRectFa.bottom == Float.MIN_VALUE) {
            return;
        }

        RectF outerRectFb = new RectF();
        if (mLeftMostViewModel == null) {
            mLeftMostViewModel = viewModelA;
            mOuterRectF.left = outerRectFa.left;
        } else {
            mLeftMostViewModel.getOuterRectF(outerRectFb);
            if (outerRectFa.left <= outerRectFb.left) {
                mLeftMostViewModel = viewModelA;
                mOuterRectF.left = outerRectFa.left;
            }
        }

        if (mTopMostViewModel == null) {
            mTopMostViewModel = viewModelA;
            mOuterRectF.top = outerRectFa.top;
        } else {
            mTopMostViewModel.getOuterRectF(outerRectFb);
            if (outerRectFa.top <= outerRectFb.top) {
                mTopMostViewModel = viewModelA;
                mOuterRectF.top = outerRectFa.top;
            }
        }
        if (mRightMostViewModel == null) {
            mRightMostViewModel = viewModelA;
            mOuterRectF.right = outerRectFa.right;
        } else {
            mRightMostViewModel.getOuterRectF(outerRectFb);
            if (outerRectFa.right >= outerRectFb.right) {
                mRightMostViewModel = viewModelA;
                mOuterRectF.right = outerRectFa.right;
            }
        }

        if (mBottomMostViewModel == null) {
            mBottomMostViewModel = viewModelA;
            mOuterRectF.bottom = outerRectFa.bottom;
        } else {
            mBottomMostViewModel.getOuterRectF(outerRectFb);
            if (outerRectFa.bottom >= outerRectFb.bottom) {
                mBottomMostViewModel = viewModelA;
                mOuterRectF.bottom = outerRectFa.bottom;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawColor(BORDER_COLOR);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.translate(-mTopLeft.x, -mTopLeft.y);
        canvas.getClipBounds(mClipBounds);

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
    public boolean onTouchEvent(MotionEvent e) {
        mScrollInProgress = false;
        mScaleGestureDetector.onTouchEvent(e);
        mGestureDetector.onTouchEvent(e);

        if (mScaleGestureDetector.isInProgress() || mScrollInProgress) return true;
        if (e.getActionMasked() == MotionEvent.ACTION_MOVE) onActionMove(e);
        if (e.getActionMasked() == MotionEvent.ACTION_UP) onActionUpEvent(e);
        return true;
    }


    private boolean onActionDownEvent(MotionEvent e) {
        final int pointerIndex = e.getActionIndex();
        final int pointerId = e.getPointerId(pointerIndex);
        final float canvasX = getCanvasX(e.getX(pointerIndex));
        final float canvasY = getCanvasY(e.getY(pointerIndex));

        for (MagnetViewModel magnetViewModel : mMagnetMagnetViewModelHashMap.values()) {
            if (!magnetViewModel.getIsGhost() && MagnetViewModel.contains(magnetViewModel, canvasX, canvasY) && magnetViewModel.getModel().getMagnetGroup().getMagnetCount() != 1) {
                pressViewModel(magnetViewModel, canvasX, canvasY);
                mActionDownMagnetViewModels.append(pointerId, magnetViewModel);
                invalidate();
                return true;
            }
        }

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            if (!magnetGroupViewModel.getIsGhost() && MagnetGroupViewModel.contains(magnetGroupViewModel, canvasX, canvasY)) {
                pressViewModel(magnetGroupViewModel, canvasX, canvasY);
                mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModel);
                invalidate();
                return true;
            }
        }

        for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            if (!lineViewModel.getIsGhost() && lineViewModel.contains(canvasX, canvasY)) {
                pressViewModel(lineViewModel, canvasX, canvasY);
                mActionDownLineViewModels.append(pointerId, lineViewModel);
                invalidate();
                return true;
            }
        }

        return true;
    }

    private boolean onActionUpEvent(MotionEvent e) {
        int pointerIndex = e.getActionIndex();
        int pointerId = e.getPointerId(pointerIndex);
        RectF viewModelOuterRectF = new RectF();
        MagnetViewModel magnetViewModel = mActionDownMagnetViewModels.get(pointerId);

        if (magnetViewModel != null) {
            mActionDownMagnetViewModels.remove(pointerId);

            if (magnetViewModel.getHasMoved()) {
                MagnetGroupViewModel magnetGroupViewModel = magnetViewModel.getMagnetGroupViewModel();
                if (magnetGroupViewModel.getSize() == 1) {
                    magnetGroupViewModel = getMagnetGroupViewModel(magnetViewModel, magnetGroupViewModel);
                } else {
                    magnetGroupViewModel = getMagnetGroupViewModel(magnetViewModel, null);
                }

                if (magnetGroupViewModel != null && !magnetGroupViewModel.getIsGhost()) {
                    int [] rowCol = MagnetGroupViewModel.getRowCol(magnetGroupViewModel, magnetViewModel);
                    magnetViewModel.getModel().actionMoveTo(magnetGroupViewModel.getModel(), rowCol[0], rowCol[1], false);
                }

                // Move to new point
                else {
                    magnetViewModel.getOuterRectF(viewModelOuterRectF);
                    magnetViewModel.getModel().actionMoveTo(new PointF(viewModelOuterRectF.left, viewModelOuterRectF.top));
                }
            }

            unPressViewModel(magnetViewModel);
            invalidate();
            return true;
        }

        MagnetGroupViewModel magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);
        LineViewModel lineViewModel = mActionDownLineViewModels.get(pointerId);
        if (magnetGroupViewModel != null && lineViewModel != null) {
            mActionDownMagnetGroupViewModels.remove(pointerId);
            mActionDownLineViewModels.remove(pointerId);
            unPressViewModel(lineViewModel);
            unPressViewModel(magnetGroupViewModel);

            magnetGroupViewModel.getOuterRectF(viewModelOuterRectF);

            MagnetGroupViewModel magnetGroupViewModelB = getMagnetGroupViewModel(magnetGroupViewModel.getMagnetViewModel(0,0), magnetGroupViewModel);
            if (magnetGroupViewModelB != null && magnetGroupViewModelB.getModel() != null) {
                MainActivity.getModelMediator().getMindmap().actionCreateLine(lineViewModel.getParent().getModel(), magnetGroupViewModelB.getModel());
            } else {
                ((MainActivity) getContext())
                        .onCreateMagnet(
                                lineViewModel.getParent().getModel(),
                                new PointF(viewModelOuterRectF.left, viewModelOuterRectF.top));
            }
        }

        else if (magnetGroupViewModel != null) {
            mActionDownMagnetGroupViewModels.remove(pointerId);

            if (magnetGroupViewModel.getHasMoved()) {
                MagnetGroupViewModel magnetGroupViewModelB = null;
                if (magnetGroupViewModel.getSize() == 1) magnetGroupViewModelB = getMagnetGroupViewModel(magnetGroupViewModel.getMagnetViewModel(0, 0), magnetGroupViewModel);
                if (magnetGroupViewModelB != null && !magnetGroupViewModelB.getIsGhost()) {
                    int [] rowCol = MagnetGroupViewModel.getRowCol(magnetGroupViewModelB, magnetGroupViewModel.getMagnetViewModel(0,0));
                    magnetGroupViewModel.getMagnetViewModel(0,0).getModel().actionMoveTo(magnetGroupViewModelB.getModel(), rowCol[0], rowCol[1], false);
                } else {
                    magnetGroupViewModel.getOuterRectF(viewModelOuterRectF);
                    magnetGroupViewModel.getModel().actionMoveTo(new PointF(viewModelOuterRectF.left, viewModelOuterRectF.top));
                }
            }

            unPressViewModel(magnetGroupViewModel);
        }

        else if (lineViewModel != null) {
            mActionDownLineViewModels.remove(pointerId);

            if (lineViewModel.getHasMoved()) {
                lineViewModel.getModel().actionMovePoint(lineViewModel.getModel().getPoints().get(0), lineViewModel.getMiddlePointF());
            }

            unPressViewModel(lineViewModel);
        }

        invalidate();
        return true;
    }

    private void onActionMove(MotionEvent e) {
        int pointerId;
        MagnetViewModel magnetViewModel;
        MagnetGroupViewModel magnetGroupViewModel;
        LineViewModel lineViewModel;
        float canvasX;
        float canvasY;
        for (int pointerIndex = 0; pointerIndex < e.getPointerCount(); pointerIndex++) {
            pointerId = e.getPointerId(pointerIndex);
            canvasX = getCanvasX(e.getX(pointerIndex));
            canvasY = getCanvasY(e.getY(pointerIndex));

            lineViewModel = mActionDownLineViewModels.get(pointerId);
            magnetViewModel = mActionDownMagnetViewModels.get(pointerId);
            magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);

            if (magnetGroupViewModel != null) {
                moveViewModel(magnetGroupViewModel, canvasX, canvasY);
                continue;
            }

            if (magnetViewModel != null) {
                moveViewModel(magnetViewModel, canvasX, canvasY);
                continue;
            }

            if (lineViewModel != null && lineViewModel.getMiddlePointF() != null) {
                moveViewModel(lineViewModel, canvasX, canvasY);
            }
        }
        invalidate();
    }





    private boolean trySelect(float canvasX, float canvasY) {
        ((MainActivity) this.getContext()).onRemoveSelection();

        if (mSelectedViewModel != null) {
            mSelectedViewModel.setIsSelected(false);
            if (!mSelectedViewModel.getIsGhost()) mSelectedViewModel.setIsHighLighted(false);
            mSelectedViewModel = null;
        }

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            if (MagnetGroupViewModel.contains(magnetGroupViewModel, canvasX, canvasY)) {
                MagnetViewModel magnetViewModel = MagnetGroupViewModel.getMagnetViewModel(magnetGroupViewModel, canvasX, canvasY);
                if (magnetViewModel != null) {
                    magnetViewModel.setIsSelected(true);
                    magnetViewModel.setIsHighLighted(true);
                    mSelectedViewModel = magnetViewModel;
                    ((MainActivity) this.getContext()).onSelectMagnet(magnetViewModel);

                    invalidate();
                    return true;
                }

                magnetGroupViewModel.setIsSelected(true);
                magnetGroupViewModel.setIsHighLighted(true);
                mSelectedViewModel = magnetGroupViewModel;
                ((MainActivity) this.getContext()).onSelectMagnetGroup(magnetGroupViewModel);
                invalidate();
                return true;
            }
        }

        for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            if (lineViewModel.contains(canvasX, canvasY)) {
                lineViewModel.setIsSelected(true);
                lineViewModel.setIsHighLighted(true);
                mSelectedViewModel = lineViewModel;
                ((MainActivity) this.getContext()).onSelectLine(lineViewModel);
                invalidate();
                return true;
            }
        }

        invalidate();
        return false;
    }


    public boolean tryOpenSelected(float canvasX, float canvasY) {

        if (mSelectedViewModel != null && mSelectedViewModel instanceof MagnetViewModel) {
            MagnetViewModel magnetViewModel = (MagnetViewModel) mSelectedViewModel;
            if (MagnetViewModel.contains(magnetViewModel, canvasX, canvasY)) {
                ((MainActivity) this.getContext()).onEditMagnet(magnetViewModel.getModel());
                invalidate();
                return true;
            }
        }
        return false;
    }


    private void pressViewModel(ViewModel viewModel, float canvasTouchX, float canvasTouchY) {
        viewModel.setHasMoved(false);
        viewModel.setIsGhost(true);
        viewModel.setIsHighLighted(true);
        viewModel.setLastTouchPoint(canvasTouchX, canvasTouchY);
    }

    private void moveViewModel(ViewModel viewModel, float canvasTouchX, float canvasTouchY) {
        if (mSearchActive) {
            clearStates();
            mSearchActive = false;
        }

        viewModel.setHasMoved(true);
        float distanceX = viewModel.getLastTouchX() - canvasTouchX;
        float distanceY = viewModel.getLastTouchY() - canvasTouchY;
        viewModel.setLastTouchPoint(canvasTouchX, canvasTouchY);
        viewModel.moveBy(distanceX, distanceY);
    }

    private void unPressViewModel(ViewModel viewModel) {
        viewModel.setHasMoved(false);
        viewModel.setIsGhost(false);
        if (!viewModel.getIsSelected()) {
            if (viewModel instanceof MagnetGroupViewModel && ((MagnetGroupViewModel) viewModel).getSize() == 1 && ((MagnetGroupViewModel) viewModel).getMagnetViewModel(0, 0).getIsSelected()) {
                return;
            }
        viewModel.setIsHighLighted(false);
        }
    }


    MagnetGroupViewModel getMagnetGroupViewModel(MagnetViewModel magnetViewModel, @Nullable MagnetGroupViewModel ignore) {

        RectF magnetRectF = new RectF();
        magnetViewModel.getOuterRectF(magnetRectF);
        RectF groupRectF = new RectF();

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            magnetGroupViewModel.getOuterRectF(groupRectF);
            if (magnetGroupViewModel != ignore && groupRectF.intersect(magnetRectF)) {
                return magnetGroupViewModel;
            }
        }
        return null;
    }


    private void createChildAction(int pointerId, float canvasX, float canvasY, MagnetGroupViewModel magnetGroupViewModelParent) {
        mActionDownMagnetGroupViewModels.remove(pointerId);
        MagnetGroupViewModel magnetGroupViewModelChild = new MagnetGroupViewModel(canvasX, canvasY);
        magnetGroupViewModelChild.setLastTouchPoint(magnetGroupViewModelChild.getCenterX(), magnetGroupViewModelChild.getCenterY());
        mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModelChild);
        LineViewModel lineViewModel = new LineViewModel(magnetGroupViewModelParent, magnetGroupViewModelChild);
        mActionDownLineViewModels.append(pointerId, lineViewModel);

        unPressViewModel(magnetGroupViewModelParent);
        //unPressViewModel(magnetGroupViewModelParent.getMagnetViewModel(0,0));
    }




    private void clearState(ViewModel viewModel) {
        viewModel.setIsGhost(false);
        viewModel.setIsHighLighted(false);
        viewModel.setHasMoved(false);
        if (viewModel.getIsSelected()) {
            viewModel.setIsSelected(false);
            ((MainActivity) this.getContext()).onRemoveSelection();
            mSelectedViewModel = null;
        }
    }

    public void clearStates() {

        for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            if (mActionDownLineViewModels.indexOfValue(lineViewModel) < 0) {
                clearState(lineViewModel);
            }
        }

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            if (mActionDownMagnetGroupViewModels.indexOfValue(magnetGroupViewModel) < 0) {
                clearState(magnetGroupViewModel);
            }
        }

        for (MagnetViewModel magnetViewModel : mMagnetMagnetViewModelHashMap.values()) {
            if (mActionDownMagnetViewModels.indexOfValue(magnetViewModel) < 0) {
                clearState(magnetViewModel);
            }
        }
    }



    /** For the MindmapFragment **/
    public void setTopLeft(PointF topLeft) {
        this.mTopLeft = topLeft;
    }

    public PointF getTopLeft() {
        return mTopLeft;
    }



    /**
     *  ModelListener
     *  implementation
     */
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

        findOuterRectF(magnetGroupViewModel);
        invalidate();
    }

    @Override
    public void onMagnetGroupChange(MagnetGroup magnetGroup) {
        MagnetGroupViewModel magnetGroupViewModel = mMagnetGroupMagnetViewModelHashMap.get(magnetGroup);
        magnetGroupViewModel.refresh();
        findOuterRectF(magnetGroupViewModel);

        if(mSelectedViewModel == magnetGroupViewModel) {
            ((MainActivity)getContext()).onSelectMagnetGroup(magnetGroupViewModel);
        }
        invalidate();
    }

    private void onDeleteViewModel(ViewModel viewModel) {
        if (mSelectedViewModel == viewModel) {
            mSelectedViewModel = null;
            ((MainActivity) this.getContext()).onRemoveSelection();
        }

        if (viewModel == mLeftMostViewModel) {
            mLeftMostViewModel = null;
        }
        if (viewModel == mTopMostViewModel) {
            mTopMostViewModel = null;
        }
        if (viewModel == mRightMostViewModel) {
            mRightMostViewModel = null;
        }
        if (viewModel == mBottomMostViewModel) {
            mBottomMostViewModel = null;
        }

        findOuterRectF();
    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {
        onDeleteViewModel(mMagnetGroupMagnetViewModelHashMap.get(magnetGroup));
        mMagnetGroupMagnetViewModelHashMap.remove(magnetGroup);
        invalidate();
    }

    @Override
    public void onMagnetCreate(Magnet magnet) {
        mMagnetMagnetViewModelHashMap.put(magnet, new MagnetViewModel(magnet));
        invalidate();
    }

    @Override
    public void onMagnetChange(Magnet magnet) {
        MagnetViewModel magnetViewModel = mMagnetMagnetViewModelHashMap.get(magnet);
        magnetViewModel.refresh();
        if(mSelectedViewModel == magnetViewModel) {
            ((MainActivity)getContext()).onSelectMagnet(magnetViewModel);
        }
        invalidate();
    }

    @Override
    public void onMagnetDelete(Magnet magnet) {

        MagnetViewModel magnetViewModel = mMagnetMagnetViewModelHashMap.get(magnet);
        mMagnetMagnetViewModelHashMap.remove(magnet);

        if (mSelectedViewModel == magnetViewModel) {
            mSelectedViewModel = null;
            ((MainActivity) this.getContext()).onRemoveSelection();
        }

//        findOuterRectF(magnetViewModel.getMagnetGroupViewModel());
        invalidate();
    }

    @Override
    public void onLineCreate(Line line) {
        LineViewModel lineViewModel = new LineViewModel(line, mMagnetGroupMagnetViewModelHashMap);
        mLineViewModelHashMap.put(line, lineViewModel);

        findOuterRectF(lineViewModel);
        invalidate();
    }

    @Override
    public void onLineChange(Line line) {
        LineViewModel lineViewModel = mLineViewModelHashMap.get(line);
        if (lineViewModel == null) Log.d("MindmapView says", "lineViewModel is null");
        else {
            lineViewModel.refresh();
            findOuterRectF(lineViewModel);
        }

        if(mSelectedViewModel == lineViewModel) {
            ((MainActivity)getContext()).onSelectLine(lineViewModel);
        }

        invalidate();
    }

    @Override
    public void onLineDelete(Line line) {
        onDeleteViewModel(mLineViewModelHashMap.get(line));
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
    }


    /**
     *  GestureDetector.OnGestureListener,
     *  GestureDetector.OnDoubleTapListener and
     *  ScaleGestureDetector.OnScaleGestureListener
     *  implementations
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (mSearchActive) {
            clearStates();
            mSearchActive = false;
        }
        final float canvasX = getCanvasX(e.getX());
        final float canvasY = getCanvasY(e.getY());
        if (!tryOpenSelected(canvasX, canvasY)) trySelect(canvasX, canvasY);
        invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mSearchActive) {
            clearStates();
            mSearchActive = false;
        }

        final float canvasX = getCanvasX(e.getX());
        final float canvasY = getCanvasY(e.getY());

        if (!tryOpenSelected(canvasX, canvasY) && trySelect(canvasX, canvasY) && mSelectedViewModel != null && mSelectedViewModel instanceof MagnetViewModel) {

            MagnetViewModel magnetViewModel = (MagnetViewModel) mSelectedViewModel;
            ((MainActivity) this.getContext()).onEditMagnet(magnetViewModel.getModel());

        } else if (mSelectedViewModel == null) {
            ((MainActivity) this.getContext()).onCreateMagnet(new PointF(canvasX, canvasY));
        }

        invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        onActionDownEvent(e);
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }



    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mActionDownMagnetViewModels.size() == 0 && mActionDownMagnetGroupViewModels.size() == 0 && mActionDownLineViewModels.size() == 0) {
            mScrollInProgress = true;

            float minLeft = mLeftMostViewModel.getLeft() - ((float)getMeasuredWidth()) / 2f;
            float minTop = mTopMostViewModel.getTop() - ((float)getMeasuredHeight()) / 2f;
            float maxLeft = mRightMostViewModel.getRight() - (float)getMeasuredWidth() / 2f;
            float maxTop = mBottomMostViewModel.getBottom() - (float)getMeasuredHeight() / 2f;

            mTopLeft.x += distanceX / mScaleFactor;
            mTopLeft.y += distanceY / mScaleFactor;

            if (mTopLeft.x < minLeft) mTopLeft.x = minLeft;
            else if (mTopLeft.x > maxLeft) mTopLeft.x = maxLeft;

            if (mTopLeft.y < minTop) mTopLeft.y = minTop;
            else if (mTopLeft.y > maxTop) mTopLeft.y = maxTop;

//            mTopLeft.x = Math.max(Math.min(maxLeft, mTopLeft.x + distanceX / mScaleFactor), minLeft);
//            mTopLeft.y = Math.max(Math.min(maxTop, mTopLeft.y + distanceY / mScaleFactor), minTop);
            invalidate();
        }

        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {
        int pointerId = e.getPointerId(e.getActionIndex());
        MagnetGroupViewModel magnetGroupViewModelParent = mActionDownMagnetGroupViewModels.get(pointerId);
        if (magnetGroupViewModelParent != null) {
            MagnetGroup magnetGroup = magnetGroupViewModelParent.getModel();
            if (magnetGroup != null) {
                createChildAction(pointerId, getCanvasX(e.getX()), getCanvasY(e.getY()), magnetGroupViewModelParent);
                invalidate();
            }
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleFactor = Math.min(Math.max(0.25f,mScaleFactor * detector.getScaleFactor()), 2.0f);
        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return mActionDownMagnetGroupViewModels.size() == 0 && mActionDownLineViewModels.size() == 0 && mActionDownMagnetViewModels.size() == 0;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }


    /**
     *  SearchFragmentListener
     *  implementation
     */
    @Override
    public void search(String text, boolean image, boolean video, boolean file) {
        if (mActionDownMagnetGroupViewModels.size() == 0 && mActionDownMagnetViewModels.size() == 0 && mActionDownLineViewModels.size() == 0) {
            mSearchActive = true;
            ((MainActivity)this.getContext()).onRemoveSelection();
            mSelectedViewModel = null;

            for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
                lineViewModel.setIsGhost(true);
                lineViewModel.setIsHighLighted(false);
                lineViewModel.setIsSelected(false);
            }

            for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
                magnetGroupViewModel.setIsHighLighted(false);
                magnetGroupViewModel.setIsGhost(true);
                magnetGroupViewModel.setIsSelected(false);
            }

            for (MagnetViewModel magnetViewModel : mMagnetMagnetViewModelHashMap.values()) {
                magnetViewModel.setIsSelected(false);

                if ((image == magnetViewModel.getModel().hasImage() && (video == magnetViewModel.getModel().hasVideo()) && magnetViewModel.getModel().getTitle().contains(text))) {
                    magnetViewModel.setIsGhost(false);
                    magnetViewModel.setIsHighLighted(true);
                    continue;
                }

                magnetViewModel.setIsGhost(true);
                magnetViewModel.setIsHighLighted(false);
            }

            invalidate();
        }
    }
}
