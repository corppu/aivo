package com.aivo.hyperion.aivo.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.views.MindmapFragment;

public class MainActivity extends AppCompatActivity {


    Button sideBtn;
    Boolean isSideNoteVisible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sideBtn =(Button)findViewById(R.id.side_note_button);
        buttonClicks();
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contentArea, new MindmapFragment());
        ft.commit();

        sideBtn =(Button)findViewById(R.id.side_note_button);
        buttonClicks();
    }

    public void buttonClicks(){

        sideBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                LinearLayout sidePAnel =(LinearLayout)findViewById(R.id.side_note_panel);
                // animate the side bar
                if (isSideNoteVisible){

// Start the animation

                    //sidePAnel.animate().translationXBy(sidePAnel.getWidth());

                    sidePAnel.animate()
                            .translationX(sidePAnel.getWidth());
                    sidePAnel.setVisibility(View.INVISIBLE);
                    isSideNoteVisible=false;
                }else {

                   // sidePAnel.animate().translationXBy(-120);
// Start the animation
                    sidePAnel.animate()
                            .translationX(0);
                    sidePAnel.setVisibility(View.VISIBLE);
                    isSideNoteVisible=true;
                }



            }

        });

    }

}
