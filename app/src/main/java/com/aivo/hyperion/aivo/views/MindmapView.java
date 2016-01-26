package com.aivo.hyperion.aivo.views;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.Theme;

/**
 * TODO: document your custom view class.
 */
public class MindmapView {

    private final String TAG = "MindmapView";

    private Theme mTheme;
    private Mindmap mMindmap;
    private MagnetGroupCanvas mMagnetGroupCanvas;
    private Paint mPaint;

    // temporary
    private Canvas mCanvas;

    public MindmapView(Theme theme, Mindmap mindmap) {
        this.mTheme = theme;
        this.mMindmap = mindmap;
        this.mMagnetGroupCanvas = new MagnetGroupCanvas();
        this.mPaint = new Paint();

        // temporary
        this.mCanvas = null;
    }

    public void draw(Canvas canvas) {
        if (mMindmap != null) {
            mCanvas = canvas;

            canvas.drawColor(Color.LTGRAY);

            drawLines();

            drawMagnetGroups();

            mCanvas = null;
        }
    }

    // 1
    private void drawLines() {
        MagnetGroup group1;
        MagnetGroup group2;

        mPaint.setColor(Color.BLACK);
        for (Line line : mMindmap.getLines()) {
            group1 = line.getMagnetGroup1();
            group2 = line.getMagnetGroup2();
            mCanvas.drawLine(group1.getX(), group1.getY(), group2.getX(), group2.getY(), mPaint);
        }
    }

    // 2
    private void drawMagnetGroups() {
        // Draw groups
        for (MagnetGroup group : mMindmap.getMagnetGroups()) {
            mMagnetGroupCanvas.setMagnetGroup(group);
            mMagnetGroupCanvas.draw(mCanvas, mPaint);
        }
    }
}
