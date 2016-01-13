package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * TODO: document your custom view class.
 */
public class MindmapView extends View {

    public MindmapView(Context context) {
        super(context);
        init(null);
    }

    public MindmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MindmapView);

            final int N = a.getIndexCount();
            for (int i = 0; i < N; ++i) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.MindmapView_magnetTopCircleRadius:
                        break;
                    case R.styleable.MindmapView_addSignDrawable:
                        String myText = a.getString(attr);
                        //...do something with myText...
                        break;
                    case R.styleable.MindmapView_magnetBottomCircleRadius:
                        boolean fancyColors = a.getBoolean(attr, false);
                        //...do something with fancyColors...
                        break;
                    case R.styleable.MindmapView_magnetFrameBorderThickness:
                        String onAction = a.getString(attr);
                        //...we'll setup the callback in a bit...
                        break;
                    case R.styleable.MindmapView_magnetFrameHeight:
                        break;

                    case R.styleable.MindmapView_magnetSelectionFrameBorderThickness:
                        break;


                }
            }
            a.recycle();
        }

        // Model
        mindmap = null;

        // Renderer
        paint = new Paint();
        magnetGroupCanvas = new MagnetGroupCanvas();

        // Canvas
        canvasScaleFactor = 1.0f;
        canvasFocusPoint = new Point(0,0);
        canvasClipBounds = new Rect();
    }

    // Model
    private Mindmap mindmap;

    // Renderer
    private Paint paint;
    private MagnetGroupCanvas magnetGroupCanvas;

    // Canvas
    private float canvasScaleFactor;
    private Point canvasFocusPoint;
    private Rect canvasClipBounds;

    public void setMindmap(Mindmap mindmap) {
        this.mindmap = mindmap;

        this.getLayoutParams().width = 1920;
        this.getLayoutParams().height = 1080;

        canvasFocusPoint.x = 1920 / 2;
        canvasFocusPoint.y = 1080 / 2;

        canvasScaleFactor = 2.0f;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, 24.0f * canvasScaleFactor, paint);

        canvas.translate(-(canvasFocusPoint.x - this.getWidth() / 2), -(canvasFocusPoint.y - this.getHeight() / 2));
        canvas.scale(canvasScaleFactor, canvasScaleFactor, canvasFocusPoint.x, canvasFocusPoint.y);
        canvas.getClipBounds(canvasClipBounds);

        paint.setColor(Color.RED);
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2 , 24.0f * canvasScaleFactor, paint);

        /*
        if (mindmap != null) {
            canvas.drawColor(Color.LTGRAY);
            MagnetGroup group1;
            MagnetGroup group2;

            // Draw lines
            paint.setColor(Color.BLACK);
            for (Line line : mindmap.getLines()) {
                group1 = line.getMagnetGroup1();
                group2 = line.getMagnetGroup2();
                canvas.drawLine(group1.getX(), group1.getY(), group2.getX(), group2.getY(), paint);
            }

            // Draw groups
            for (MagnetGroup group : mindmap.getMagnetGroups()) {
                magnetGroupCanvas.setMagnetGroup(group);
                magnetGroupCanvas.draw(canvas, paint);
            }
        }*/
    }

}
