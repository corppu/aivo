package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by corpp on 8.1.2016.
 */
public class MindmapPresenter
        extends GestureDetector.SimpleOnGestureListener
        implements Runnable, View.OnTouchListener {

    private class MindmapPointer {
        public PointF selectedPointF = null;
        public MagnetGroup selectedMagnetGroup = null;
        public Magnet selectedMagnet = null;
    }

    private final String TAG = "MindmapPresenter";

    // Thread
    private volatile boolean mRunning;
    private Thread mThread;
    private Lock mLock;

    // View
    private SurfaceView mSurfaceView;
    private GestureDetectorCompat mGestureDetector;

    // Model
    private Mindmap mMindmap;
    private MagnetViewModel mMagnetViewModel;
    private MagnetGroupViewModel mMagnetGroupViewModel;
    private MagnetGroup mSelectedMagnetGroup;
    private SparseArray<MindmapPointer> pointerSparseArray = new SparseArray<>();
    private Paint mPaint = new Paint();

    // Model

    public MindmapPresenter() {
        mRunning = false;
        mThread = null;
        mLock = new ReentrantLock();
        mMindmap = null;
        mSurfaceView = null;
        mGestureDetector = null;
        mSelectedMagnetGroup = null;
    }

    public void setView(SurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
        mGestureDetector = new GestureDetectorCompat(surfaceView.getContext(), this);
        mGestureDetector.setOnDoubleTapListener(this);
        mGestureDetector.setIsLongpressEnabled(true);

        //mScaleGestureDetector = new ScaleGestureDetector(surfaceView.getContext(), null);
        surfaceView.setOnTouchListener(this);
    }

    public void setModel(Mindmap mindmap) {
        mLock.lock();
        this.mMindmap = mindmap;
        mindmap.actionCreateMagnet(new PointF(150, 150));
        mMagnetViewModel = new MagnetViewModel();
        mMagnetGroupViewModel = new MagnetGroupViewModel();
        mLock.unlock();
    }

    public void resume() {
        mRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    public void pause() {
        boolean retry = true;
        mRunning = false;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        PointF point1;
        PointF point2;
        while (mRunning) {
            if (mSurfaceView.getHolder().getSurface().isValid()) {

                Canvas canvas = mSurfaceView.getHolder().lockCanvas();

                // Draw background...
                canvas.drawColor(Color.WHITE);

                mLock.lock();
                if (mMindmap != null) {

                    mPaint.setColor(Color.BLACK);
                    for (Line line : mMindmap.getLines()) {
                        point1 = line.getMagnetGroup1().getPoint();
                        point2 = line.getMagnetGroup2().getPoint();
                        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, mPaint);
                    }
                    for (MagnetGroup group : mMindmap.getMagnetGroups()) {
                        mMagnetViewModel.pointF = group.getPoint();
                        mMagnetViewModel.bottomIconPressed = false;
                        mMagnetViewModel.hasImg = true;
                        mMagnetViewModel.hasVid = true;
                        mMagnetViewModel.isSelected = false;

                        for (int i = 0; i < pointerSparseArray.size(); i++) {
                            if (!(group != pointerSparseArray.valueAt(i).selectedMagnetGroup)) {
                                mMagnetViewModel.isSelected = true;
                                break;
                            }
                        }
                        if (mSelectedMagnetGroup == group) {
                            mMagnetViewModel.isSelected = true;
                        }

                        mMagnetViewModel.topIconPressed = false;

                        MagnetViewModel.draw(mMagnetViewModel, canvas, mPaint);
                    }
                }
                mLock.unlock();

                drawPointers(canvas, mPaint);

                // ... end drawing
                mSurfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawPointers(Canvas canvas, Paint paint) {
        PointF pointF;
        Random random = new Random();

        mLock.lock();
        for (int i = 0; i < pointerSparseArray.size(); i++) {
            random.setSeed(pointerSparseArray.keyAt(i));
            paint.setColor(Color.argb(255,
                    random.nextInt(255),
                    random.nextInt(255),
                    random.nextInt(255)));
            pointF = pointerSparseArray.valueAt(i).selectedPointF;
            canvas.drawCircle(pointF.x, pointF.y, 32, paint);
        }
        mLock.unlock();
    }

    private void putPointer(MotionEvent event) {
        int pointerIndex = event.getActionIndex();

        MindmapPointer mindmapPointer = new MindmapPointer();
        mindmapPointer.selectedPointF = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
        mindmapPointer.selectedMagnetGroup = null;
        mindmapPointer.selectedMagnet = null;

        mLock.lock();
        for (MagnetGroup magnetGroup : mMindmap.getMagnetGroups()) {
            if (MagnetViewModel.checkSelection(magnetGroup.getPoint(), mindmapPointer.selectedPointF)) {
                mindmapPointer.selectedMagnetGroup = magnetGroup;

                /*
                for (int i = 0; i < magnetGroup.getMagnets().size(); i++) {
                    for (Magnet magnet : magnetGroup.getMagnets().get(i)) {
                        if (MagnetViewModel.checkSelection(magnet.getPointF(), mindmapPointer.selectedPointF)) {
                            mindmapPointer.selectedMagnet = magnet;
                            break;
                        }
                    }
                }*/

                break;
            }
        }

        pointerSparseArray.put(pointerIndex, mindmapPointer);
        mLock.unlock();
    }

    private void movePointer(MotionEvent event) {
        int pointerIndex;

        mLock.lock();

        for (pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++) {
            MindmapPointer pointer = pointerSparseArray.get(event.getPointerId(pointerIndex));
            if (pointer != null) {
                pointer.selectedPointF.set(event.getX(pointerIndex), event.getY(pointerIndex));
                if (pointer.selectedMagnetGroup != null) pointer.selectedMagnetGroup.actionMoveTo(pointer.selectedPointF);
            }
        }

        mLock.unlock();
    }

    private void removePointer(MotionEvent event) {
        mLock.lock();
        pointerSparseArray.remove(event.getPointerId(event.getActionIndex()));
        mLock.unlock();
    }

    private void removePointers() {
        mLock.lock();
        pointerSparseArray.clear();
        mLock.unlock();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                putPointer(event);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                putPointer(event);
                break;

            case MotionEvent.ACTION_MOVE:
                movePointer(event);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                removePointer(event);
                break;
            default:
                removePointers();
                break;
        }

        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        PointF newPointF = new PointF(e.getX(), e.getY());
        mLock.lock();
        for (MagnetGroup magnetGroup: mMindmap.getMagnetGroups()) {
            if (MagnetViewModel.checkSelection(magnetGroup.getPoint(), newPointF)) {
                mSelectedMagnetGroup = magnetGroup;
                mLock.unlock();
                return true;
            }
        }

        mSelectedMagnetGroup = null;
        mLock.unlock();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {

        PointF newPointF = new PointF(e.getX(), e.getY());

        mLock.lock();
        for (MagnetGroup magnetGroup: mMindmap.getMagnetGroups()) {
            if (MagnetViewModel.checkSelection(magnetGroup.getPoint(), newPointF)) {

                if (mSelectedMagnetGroup != null && mSelectedMagnetGroup != magnetGroup) {

                    for (Line line1 : mSelectedMagnetGroup.getLines()) {
                        for (Line line2: magnetGroup.getLines()) {
                            if (line1 == line2) {
                                mLock.unlock();
                                return true;
                            }
                        }
                    }
                    mMindmap.actionCreateLine(magnetGroup, mSelectedMagnetGroup);

                }

                //TODO: open and return
                mLock.unlock();
                return true;
            }
        }

        mMindmap.actionCreateMagnet(newPointF);
        mLock.unlock();
        return  true;
    }
}
