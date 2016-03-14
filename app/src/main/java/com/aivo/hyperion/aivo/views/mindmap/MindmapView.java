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
    private final float MIN_X; //= -360.0f; //xxhdpi
    private final float MIN_Y; //= -380.0f; //xxhdpi
    private final float MAX_X;
    private final float MAX_Y;

    static private String TAG = "MindmapView";
    private Rect mClipBounds = new Rect();
    private Rect mDrawingRect = new Rect();
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private Paint mPaint = new Paint();
    private float mScaleFactor = 1.0f;
    private PointF topLeft;
    private Random random = new Random();


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

        Log.d(TAG, "Canvas clipbounds: " + mClipBounds.toString() + ". View DrawingRect:" + mDrawingRect.toString());

        mPaint.setColor(BACKGROUND_COLOR);
        canvas.drawRect(0, 0, MAX_X, MAX_Y, mPaint);

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

    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {
        MagnetGroupViewModel magnetGroupViewModel = new MagnetGroupViewModel(magnetGroup, mMagnetMagnetViewModelHashMap);
        mMagnetGroupMagnetViewModelHashMap.put(magnetGroup, magnetGroupViewModel);
        Log.d(TAG, "magnetGroup has been created!");
        invalidate();
    }

    @Override
    public void onMagnetGroupChange(MagnetGroup magnetGroup) {
        Log.d(TAG, "magnetGroup has changed!");

        mMagnetGroupMagnetViewModelHashMap.get(magnetGroup).refresh();
        invalidate();
    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {
        Log.d(TAG, "magnetGroup has been deleted");

        mMagnetGroupMagnetViewModelHashMap.remove(magnetGroup);
        invalidate();
    }

    @Override
    public void onMagnetCreate(Magnet magnet) {
        mMagnetMagnetViewModelHashMap.put(magnet, new MagnetViewModel(magnet));
        Log.d(TAG, "magnet has been deleted");
        invalidate();
    }

    @Override
    public void onMagnetChange(Magnet magnet) {
        Log.d(TAG, "magnet has changed");

        mMagnetMagnetViewModelHashMap.get(magnet).refresh();
    }

    @Override
    public void onMagnetDelete(Magnet magnet) {
        Log.d(TAG, "magnet has been deleted!");
        mMagnetMagnetViewModelHashMap.remove(magnet);
        invalidate();
    }

    @Override
    public void onLineCreate(Line line) {
        LineViewModel lineViewModel = new LineViewModel(line, mMagnetGroupMagnetViewModelHashMap);
        mLineViewModelHashMap.put(line, lineViewModel);
        invalidate();
    }

    @Override
    public void onLineChange(Line line) {
        mLineViewModelHashMap.get(line).refresh();
    }

    @Override
    public void onLineDelete(Line line) {
        mLineViewModelHashMap.remove(line);
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
        Log.d(TAG, e.toString());
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
                        magnetViewModel.setSelected(true);
                        mActionDownMagnetViewModels.put(pointerId, magnetViewModel);
                        return true;
                    }
                    else if (MagnetViewModel.bottomIconPressed(magnetViewModel, x, y)) {

                        magnetGroupViewModel = new MagnetGroupViewModel(x, y);
                        lineViewModel = new LineViewModel(magnetGroupViewModelParent, magnetGroupViewModel);
                        mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModel);
                        mActionDownLineViewModels.append(pointerId, lineViewModel);
                        return true;
                    }
                    else {
                        magnetViewModel.setSelected(true);
                    }
                }
                Log.d(TAG, "Pointer holds a group");
                mActionDownMagnetGroupViewModels.append(pointerId, magnetGroupViewModelParent);
                return true;
            }
         }


       for (LineViewModel lineViewModell : mLineViewModelHashMap.values()) {
            if (lineViewModell.contains(x, y)
                    && mActionDownLineViewModels.indexOfValue(lineViewModell) == -1) {
                mActionDownLineViewModels.append(pointerId, lineViewModell);
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


        if (magnetViewModel != null) {
            mActionDownMagnetViewModels.remove(pointerId);
            magnetViewModel.getCenterPointF(pointF);
            magnetViewModel.getModel().actionMoveTo(pointF);
            return true;
        }


        MagnetGroupViewModel magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);
        if (magnetGroupViewModel != null) {
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
                                magnetViewModel.getModel().actionMoveTo(magnetGroupViewModelz.getModel(), 0, 0);
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
                    MainActivity.getModelMediator().getMindmap().actionCreateMagnetChild(lineViewModel.getParent().getModel(), pointF);
                    mActionDownLineViewModels.remove(pointerId);
                } else {
                    MainActivity.getModelMediator().getMindmap().actionCreateMagnet(pointF);
                }
            }

            invalidate();
            return true;
        }

        LineViewModel lineViewModel = mActionDownLineViewModels.get(e.getPointerId(pointerIndex));
        if (lineViewModel != null) {
            mActionDownLineViewModels.remove(e.getPointerId(pointerIndex));
            invalidate();
        }
        return true;
    }

    boolean onActionMoveEvent(MotionEvent e) {
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
                    magnetGroupViewModel.move(x, y);
                }
                else {
                    lineViewModel = mActionDownLineViewModels.get(pointerId);
                    if (lineViewModel != null) {
                        lineViewModel.moveMiddlePoint(x, y);
                    }
                    else {
                        MagnetViewModel magnetViewModel = mActionDownMagnetViewModels.get(pointerId);
                        if (magnetViewModel != null) {
                            magnetViewModel.move(x, y);
                        }
                    }
                }
            }
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
        return true;
    }

    public MindmapView(Context context) {
        super(context);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MIN_X = -2*getPaddingLeft();
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        MIN_Y = -2*getPaddingTop();
        topLeft = new PointF(MIN_X, MIN_Y);
        init(context, null, 0, 0);
    }

    public MindmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MIN_X = -2*getPaddingLeft();
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        MIN_Y = -2*getPaddingTop();
        topLeft = new PointF(MIN_X, MIN_Y);
        init(context, attrs, defStyleAttr, 0);
    }

    public MindmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MIN_X = -2*getPaddingLeft();
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        MIN_Y = -2*getPaddingTop();
        topLeft = new PointF(MIN_X, MIN_Y);
        init(context, attrs, 0, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MindmapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        MAX_X = context.getResources().getDimension(R.dimen.canvas_width);
        MIN_X = -2*getPaddingLeft();
        MAX_Y = context.getResources().getDimension(R.dimen.canvas_height);
        MIN_Y = -2*getPaddingTop();
        topLeft = new PointF(MIN_X, MIN_Y);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


     private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, this);

         mGestureDetector.setIsLongpressEnabled(true);
         MainActivity.getModelMediator().registerListener(this);
         Log.d(TAG, "init(MAX_X,MAX_Y):" + Float.toString(MAX_X) + ", " + Float.toString(MAX_Y));
      }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "onSingleTapConfirmed: " + e.toString());
//        final float x = e.getX() / mScaleFactor + mClipBounds.left;
//        final float y = e.getY() / mScaleFactor + mClipBounds.top;
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: " + e.toString());
            final float x = e.getX() / mScaleFactor + mClipBounds.left;
            final float y = e.getY() / mScaleFactor + mClipBounds.top;
            MainActivity.getModelMediator().getMindmap().actionCreateMagnet(new PointF(x, y));
            invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(TAG, "onDoubleTapEvent: " + e.toString());
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown: " + e.toString());
        Log.d("TIEDOT", Float.toString(MAX_X) + Float.toString(MAX_Y));

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
       if (mActionDownMagnetViewModels.size() != 0 || mActionDownMagnetGroupViewModels.size() != 0 || mActionDownLineViewModels.size() != 0) return true;
        topLeft.x = Math.min(Math.max(MIN_X, topLeft.x + distanceX / mScaleFactor), MAX_X - mDrawingRect.right + getPaddingRight() + getPaddingLeft());
        topLeft.y = Math.min(Math.max(MIN_Y, topLeft.y + distanceY / mScaleFactor), MAX_Y - mDrawingRect.bottom + getPaddingBottom() + getPaddingTop());
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress: " + e.toString());

        int pointerId = e.getPointerId(e.getActionIndex());
        MagnetGroupViewModel magnetGroupViewModel = mActionDownMagnetGroupViewModels.get(pointerId);
        if (magnetGroupViewModel != null && magnetGroupViewModel.getSize() == 1) {
            MagnetGroup magnetGroup = magnetGroupViewModel.getModel();
            if (magnetGroup != null) {
                mActionDownMagnetGroupViewModels.remove(pointerId);

//                mMagnetGroupMagnetViewModelHashMap.remove(magnetGroup);
                Magnet magnet = magnetGroup.getMagnets().get(0).get(0);
//                mMagnetMagnetViewModelHashMap.remove(magnet);
                magnet.actionDelete();
                return;
            }
        }
        MagnetViewModel magnetViewModel = mActionDownMagnetViewModels.get(pointerId);
        if (magnetViewModel != null) {
            Magnet magnet = magnetViewModel.getModel();
            if (magnet != null) {
                mActionDownMagnetViewModels.remove(pointerId);
//                mMagnetMagnetViewModelHashMap.remove(magnet);
                magnet.actionDelete();
                return;
            }
        }
        LineViewModel lineViewModel = mActionDownLineViewModels.get(pointerId);
        if (lineViewModel != null) {
            Line line = lineViewModel.getLine();
            if (line != null) {
                mActionDownLineViewModels.remove(pointerId);
//                mLineViewModelHashMap.remove(line);
                line.actionDelete();
            }
        }

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling");
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
        return mActionDownMagnetGroupViewModels.size() == 0 && mActionDownLineViewModels.size() == 0 && mActionDownMagnetViewModels.size() == 0;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleEnd: " + detector.toString());
    }
}