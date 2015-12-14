package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.aivo.hyperion.aivo.R;

/**
 * TODO: document your custom view class.
 */
public class MindmapView extends View {

    public MindmapView(Context context) {
        super(context);
        init(null, 0);
    }

    public MindmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MindmapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MindmapView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
