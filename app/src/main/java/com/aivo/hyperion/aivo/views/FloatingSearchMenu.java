package com.aivo.hyperion.aivo.views;


import android.app.Service;
import android.app.usage.UsageEvents;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.content.Intent;
import android.util.EventLog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

/**
 * Created by Matus Mucha on 27-Feb-16.
 */
public class FloatingSearchMenu extends Service {

    private WindowManager wm;
    private LinearLayout ll;
    private Button stop;
    private SearchView SearchBar;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        stop = new Button(this);
        SearchBar = new SearchView(this);

        ViewGroup.LayoutParams btnParameters= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       // btnParameters.
        stop.setText("Stop");


        stop.setLayoutParams(btnParameters);

        LinearLayout.LayoutParams llParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setBackgroundColor(Color.rgb(51, 153, 255));
        ll.setLayoutParams(llParameters);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(600, 400, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        parameters.x = 0;
        parameters.y = 0;
        parameters.gravity = Gravity.CENTER | Gravity.CENTER;

        ll.addView(SearchBar);

        SearchBar.setIconifiedByDefault(false); // so Searchbar is expanded on click

        ll.addView(stop);
        wm.addView(ll, parameters);



        ll.setOnTouchListener(new View.OnTouchListener() {

            private WindowManager.LayoutParams updatedParameters = parameters;
            int x, y;
            float touchedX, touchedY;

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        touchedX = event.getRawX();
                        touchedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        updatedParameters.x = (int) (x + (event.getRawX() - touchedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - touchedY));

                        wm.updateViewLayout(ll, updatedParameters);

                    default:
                        break;
                }
                return false;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                wm.removeView(ll);
                stopSelf();
            }
        });
    }
}
