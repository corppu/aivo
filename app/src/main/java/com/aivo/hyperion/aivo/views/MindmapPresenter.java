package com.aivo.hyperion.aivo.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.Theme;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by corpp on 8.1.2016.
 */
public class MindmapPresenter implements Runnable, View.OnTouchListener{
    private final String TAG = "MindmapPresenter";

    // Thread
    private volatile boolean mRunning;
    private Thread mThread;
    private Lock mLock;

    // View
    private SurfaceView mSurfaceView;
    private MindmapView mMindmapView;

    private Paint mPaint = new Paint();

    // Model

    public MindmapPresenter() {
        mRunning = false;
        mThread = null;
        mLock = new ReentrantLock();

        mSurfaceView = null;
        mMindmapView = null;
    }

    public void setViews(SurfaceView surfaceView, MindmapView mindmapView) {
        this.mSurfaceView = surfaceView;
        this.mMindmapView = mindmapView;

        surfaceView.setOnTouchListener(this);
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
        while (mRunning) {
            if (mSurfaceView.getHolder().getSurface().isValid()) {

                Canvas canvas = mSurfaceView.getHolder().lockCanvas();
                // Draw here...

                canvas.drawColor(Color.BLACK);

                mMindmapView.draw(canvas);
                //mPaint.setTextAlign(Paint.Align.CENTER);
                //mPaint.setColor(Color.BLUE);
                //canvas.drawText(mContent, canvas.getWidth() / 2, canvas.getHeight() / 2, mPaint);

                drawPointers(canvas, mPaint);
                // ... end drawing
                mSurfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    SparseArray<PointF> pointerSparseArray = new SparseArray<>();


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
            pointF = pointerSparseArray.valueAt(i);
            canvas.drawCircle(pointF.x, pointF.y, 32, paint);
        }
        mLock.unlock();
    }

    private void putPointer(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        PointF pointF = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
        mLock.lock();
        pointerSparseArray.put(pointerIndex, pointF);
        mLock.unlock();
    }

    private void movePointer(MotionEvent event) {
        int pointerIndex;
        PointF pointF;

        mLock.lock();
        for (pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++) {
            pointF = pointerSparseArray.get(event.getPointerId(pointerIndex));
            if (pointF != null) {
                pointF.set(event.getX(pointerIndex), event.getY(pointerIndex));
            } else {
                //pointerSparseArray.removeAt(pointerIndex);
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

        return true;
    }
}
