package com.aivo.hyperion.aivo.views.mindmap;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.main.MainActivity;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.Note;
import com.aivo.hyperion.aivo.models.User;

import java.util.HashMap;
import java.util.Random;

public class MindmapView extends View
implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener, ModelListener {

    // touch and drawing
    private final int BACKGROUND_COLOR = Color.WHITE;
    private final int BORDER_COLOR = Color.BLACK;
    private final float MIN_X = 0f; //= -360.0f; //xxhdpi
    private final float MIN_Y = 0f; //= -380.0f; //xxhdpi
    private final float MAX_X;
    private final float MAX_Y;

    static private String TAG = "MindmapView";
    private Rect mClipBounds = new Rect();
    private Rect mDrawingRect = new Rect();
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private Paint mPaint = new Paint();
    private float mScaleFactor = 1.0f;
    private PointF topLeft = new PointF(MIN_X, MIN_Y);
    private Random random = new Random();


    // Models
    private HashMap<MagnetGroup, MagnetGroupViewModel> mMagnetGroupMagnetViewModelHashMap = new HashMap<>();
    private SparseArray<MagnetGroupViewModel> mActionDownMagnetGroupViewModels = new SparseArray<>();

    private HashMap<Line, LineViewModel> mLineViewModelHashMap = new HashMap<>();
    private SparseArray<LineViewModel> actionDownLineViewModels = new SparseArray<>();

    private HashMap<Magnet, MagnetViewModel> magnetMagnetViewModelHashMap = new HashMap<>();


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(BORDER_COLOR);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.translate(-topLeft.x, -topLeft.y);
        canvas.getClipBounds(mClipBounds);
        getDrawingRect(mDrawingRect);

        Log.d(TAG, "Canvas clipbounds: " + mClipBounds.toString() + ". View DrawingRect:" + mDrawingRect.toString());

        mPaint.setColor(BACKGROUND_COLOR);
        canvas.drawRect(0, 0, MAX_X, MAX_Y, mPaint);

        for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            lineViewModel.draw(canvas, mPaint);
        }

        for (MagnetGroupViewModel magnetGroupViewModel : mMagnetGroupMagnetViewModelHashMap.values()) {
            MagnetGroupViewModel.draw(magnetGroupViewModel, canvas, mPaint);
        }

        for (int i = 0; i < actionDownLineViewModels.size(); ++i) {
            actionDownLineViewModels.valueAt(i).draw(canvas, mPaint);
        }

        for (int i = 0; i < mActionDownMagnetGroupViewModels.size(); ++i) {
            MagnetGroupViewModel.draw(mActionDownMagnetGroupViewModels.valueAt(i), canvas, mPaint);
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

    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {
        MagnetGroupViewModel magnetGroupViewModel = new MagnetGroupViewModel(magnetGroup, magnetMagnetViewModelHashMap);
        mMagnetGroupMagnetViewModelHashMap.put(magnetGroup, magnetGroupViewModel);
    }

    @Override
    public void onMagnetGroupChange(MagnetGroup magnetGroup) {

    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {

    }

    @Override
    public void onMagnetCreate(Magnet magnet) {
        magnetMagnetViewModelHashMap.put(magnet, new MagnetViewModel(magnet));
    }

    @Override
    public void onMagnetChange(Magnet magnet) {

    }

    @Override
    public void onMagnetDelete(Magnet magnet) {

    }

    @Override
    public void onLineCreate(Line line) {
        LineViewModel lineViewModel = new LineViewModel(line, mMagnetGroupMagnetViewModelHashMap);
        mLineViewModelHashMap.put(line, lineViewModel);
    }

    @Override
    public void onLineChange(Line line) {

    }

    @Override
    public void onLineDelete(Line line) {

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

    class TouchData {
        float x = -1;
        float y = -1;
        MagnetViewModel magnetViewModel = null;
    }
    private TouchData latestUpTouchData = new TouchData();
    boolean mClickedClearArea = false;

    boolean onActionDownEvent(MotionEvent e) {
        MagnetGroupViewModel magnetGroupViewModel;
        MagnetViewModel magnetViewModel;
        int pointerIndex = e.getActionIndex();
        int pointerId = e.getPointerId(pointerIndex);
        float x = e.getX(pointerIndex) / mScaleFactor + mClipBounds.left;
        float y = e.getY(pointerIndex) / mScaleFactor + mClipBounds.top;

        PointF pointF = new PointF(x,y);

        for (MagnetGroupViewModel magnetGroupViewModelParent : mMagnetGroupMagnetViewModelHashMap.values()) {
            Log.d(TAG, "WTF");
            if (MagnetGroupViewModel.contains(magnetGroupViewModelParent, x, y)
                    && mActionDownMagnetGroupViewModels.indexOfValue(magnetGroupViewModelParent) == -1) {

                Log.d(TAG, "WTF222");
//                magnetViewModel = MagnetGroupViewModel.getMagnetViewModel(magnetGroupViewModelParent, x, y);
//                if (magnetViewModel != null) {
//                    if (magnetGroupViewModelParent.getSize() == 1 && MagnetViewModel.bottomIconPressed(magnetViewModel, x ,y)) {
//                        magnetGroupViewModel = new MagnetGroupViewModel(x, y);
//                        mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModel);
//
//                        Log.d(TAG, "Created new ghost child");
//                        return true;
//                    }
//
//                    // TODO: remove from magnetgroup
//                    //magnetGroupViewModel = new MagnetGroupViewModel()
//                    Log.d(TAG, "Created  new ghost child");
//                }

                Log.d(TAG, "Pointer holds a group");
                mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModelParent);
                //MagnetGroupViewModel.getMagnetViewModel(magnetGroupViewModelParent, magnetGroupViewModelParent.getCenterX(), magnetGroupViewModelParent.getCenterY()).setSelected(true);
                return true;
            }
         }


       for (LineViewModel lineViewModel : mLineViewModelHashMap.values()) {
            if (lineViewModel.contains(x, y)
                    && actionDownLineViewModels.indexOfValue(lineViewModel) == -1) {
                actionDownLineViewModels.append(pointerId, lineViewModel);
                break;
            }
        }

        return true;
    }


    boolean onActionUpEvent(MotionEvent e) {

        mClickedClearArea = true;
        latestUpTouchData.magnetViewModel = null;
        latestUpTouchData.x = e.getX(e.getActionIndex());
        latestUpTouchData.y = e.getY(e.getActionIndex());
        int pointerIndex = e.getActionIndex();

        MagnetGroupViewModel magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(e.getPointerId(pointerIndex));
        if (magnetGroupViewModel != null) {
            mActionDownMagnetGroupViewModels.remove(e.getPointerId(pointerIndex));
            mClickedClearArea = false;

            // TODO: commit action to model

            PointF pointF = new PointF();
            magnetGroupViewModel.getCenterPointF(pointF);
            MagnetGroup magnetGroup = magnetGroupViewModel.getModel();
            if (magnetGroup != null) {
                magnetGroup.actionMoveTo(pointF);
            } else {
                MainActivity.getModelMediator().getMindmap().actionCreateMagnet(pointF);
            }

            invalidate();
        }

        LineViewModel lineViewModel = actionDownLineViewModels.get(e.getPointerId(pointerIndex));
        if (lineViewModel != null) {
            actionDownLineViewModels.remove(e.getPointerId(pointerIndex));
            mClickedClearArea = false;

            // TODO: commit action to model


            invalidate();
        }
        return !mClickedClearArea;
    }

    boolean onActionMoveEvent(MotionEvent e) {
        if (mActionDownMagnetGroupViewModels.size() == 0 && actionDownLineViewModels.size() == 0) return false;
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
                    magnetGroupViewModel.move(x, y);
                }
                else {
                    lineViewModel = actionDownLineViewModels.get(pointerId);
                    if (lineViewModel != null) {
                        lineViewModel.moveMiddlePoint(x, y);
                    }
                    else {
                        //TODO: next hierarchy....
                    }
                }
            }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

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

                    if(onActionMoveEvent(e)) {

                        invalidate();
                        return true;
                    }
                    break;

                default:
                    Log.d(TAG, e.toString());
                    break;
            }
        }

        mScaleGestureDetector.onTouchEvent(e);

        if (mActionDownMagnetGroupViewModels.size() == 0) mGestureDetector.onTouchEvent(e);
        return true;
    }

    public MindmapView(Context context) {
        super(context);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        init(context, null, 0, 0);
    }

    public MindmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        init(context, attrs, defStyleAttr, 0);
    }

    public MindmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        init(context, attrs, 0, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MindmapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


     private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, this);
//
//         setVerticalScrollBarEnabled(true);
//         setHorizontalScrollBarEnabled(true);
//         setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);

         MainActivity.getModelMediator().registerListener(this);
         Log.d(TAG, "init(MAX_X,MAX_Y):" + Float.toString(MAX_X) + ", " + Float.toString(MAX_Y));
      }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "onSingleTapConfirmed: " + e.toString());
        final float x = e.getX() / mScaleFactor + mClipBounds.left;
        final float y = e.getY() / mScaleFactor + mClipBounds.top;

        if (mClickedClearArea) {

        } else if (latestUpTouchData.magnetViewModel != null) {
//            latestUpTouchData.magnetViewModel.toggleIsIntersecting();
            invalidate();
        }

        latestUpTouchData.magnetViewModel = null;

        latestUpTouchData.x = -1;
        latestUpTouchData.y = -1;
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: " + e.toString());

        if (mClickedClearArea) {
            final float x = e.getX() / mScaleFactor + mClipBounds.left;
            final float y = e.getY() / mScaleFactor + mClipBounds.top;

            MainActivity.getModelMediator().getMindmap().actionCreateMagnet(new PointF(x, y));
            invalidate();
        }
        else if (latestUpTouchData.magnetViewModel != null) {
//            latestUpTouchData.magnetViewModel.toggleIsIntersecting();
            invalidate();
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(TAG, "onDoubleTapEvent: " + e.toString());

//        if (mClickedClearArea) {
//            Log.d(TAG, "onDoubleTap: " + e.toString());
//            final float x = e.getX() / mScaleFactor + mClipBounds.left;
//            final float y = e.getY() / mScaleFactor + mClipBounds.top;
//
//            MagnetViewModel magnetViewModel = new MagnetViewModel(false);
//            magnetViewModel.setCenterPointF(new PointF(x, y));
//            mMagnetGroupViewModels.add(magnetViewModel);
//
//            invalidate();
//        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown: " + e.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress: " + e.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp: " + e.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
       if (mActionDownMagnetGroupViewModels.size() != 0 || actionDownLineViewModels.size() != 0) return false;
        topLeft.x = Math.min(Math.max(MIN_X, topLeft.x + distanceX / mScaleFactor), MAX_X - mDrawingRect.right + getPaddingRight() + getPaddingLeft());
        topLeft.y = Math.min(Math.max(MIN_Y, topLeft.y + distanceY / mScaleFactor), MAX_Y - mDrawingRect.bottom + getPaddingBottom() + getPaddingTop());
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        final float x = e.getX() / mScaleFactor + mClipBounds.left;
        final float y = e.getY() / mScaleFactor + mClipBounds.top;
        Log.d(TAG, "onLongPress: " + e.toString());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d(TAG, "onScale: " + detector.toString());
        mScaleFactor = Math.min(Math.max(0.25f,mScaleFactor * detector.getScaleFactor()), 1.0f);
        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleBegin: " + detector.toString());
        return mActionDownMagnetGroupViewModels.size() == 0 && actionDownLineViewModels.size() == 0;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleEnd: " + detector.toString());
    }
}
