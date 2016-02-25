package com.aivo.hyperion.aivo.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.User;
import com.aivo.hyperion.aivo.views.MindmapFragment;
import com.aivo.hyperion.aivo.views.NoteFragment;

public class MainActivity extends AppCompatActivity implements ModelListener {

    private static Context sTheContext;
    public static Context getContext() {
        return sTheContext;
    }

    private static User sUser;
    public static User getUser() {
        return sUser;
    }

    private static ModelMediator sModelMediator;
    public static ModelMediator getModelMediator() {
        return sModelMediator;
    }

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
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contentArea, new NoteFragment()).commit();
//        ft.replace(R.id.contentArea, new MindmapFragment());
//        ft.replace(R.id.contentArea, new NoteFragment());
//        ft.commit();

        sideBtn =(Button)findViewById(R.id.side_note_button);
        buttonClicks();

        sTheContext = MainActivity.this;
        sModelMediator = new ModelMediator();
        sModelMediator.registerListener(this);
        sModelMediator.createUser();
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

    @Override
    public void onUserOpen(User user) {
        sUser = user;
        sModelMediator.createMindmap("Default");
    }

    private MindmapFragment mMindmapFragment;
    @Override
    public void onMindmapOpen(Mindmap mindmap) {
        mMindmapFragment = new MindmapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentArea, mMindmapFragment).commit();
        mMindmapFragment.onMindmapOpen(mindmap);
    }

    @Override
    public void onUserChange(User user) {
        sUser = user;
    }

    @Override
    public void onMindmapTitleChange(Mindmap mindmap) {
        mMindmapFragment.onMindmapTitleChange(mindmap);
    }

    @Override
    public void onUserClosed() {
        sUser = null;
    }

    @Override
    public void onMindmapClosed() {
        mMindmapFragment.onMindmapClosed();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentArea, new NoteFragment());
    }

    @Override
    public void onException(Exception e) {
        Log.d("MainActivity", e.toString());
    }

    @Override
    public void onMagnetGroupChange(MagnetGroup magnetGroup) {

    }

    @Override
    public void onMagnetCreate(Magnet magnet) {

    }

    @Override
    public void onMagnetChange(Magnet magnet) {

    }

    @Override
    public void onMagnetDelete(Magnet magnet) {

    }

    @Override
    public void onLineCreate(Line line) {

    }

    @Override
    public void onLineChange(Line line) {

    }

    @Override
    public void onLineDelete(Line line) {

    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {

    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {

    }
}
