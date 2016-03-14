package com.aivo.hyperion.aivo.main;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.Note;
import com.aivo.hyperion.aivo.models.User;
import com.aivo.hyperion.aivo.views.MainMenuFragment;
import com.aivo.hyperion.aivo.views.mindmap.MindmapFragment;
import com.aivo.hyperion.aivo.views.NoteFragment;
import com.aivo.hyperion.aivo.views.SideNoteFragment;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements ModelListener {
    NoteFragment noteFragment;
    SideNoteFragment sideNoteFragment;
    MainMenuFragment mainMenuFragment;
    Button sideBtn;
    Button mainMenuButton;
    Boolean isSideNoteVisible = false;
    Boolean isMainMenuVisible = true;
    FrameLayout sidePanel;

    static private Random sRandom = new Random();
    public static Random getRandom() { return sRandom; }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sideNoteFragment = new SideNoteFragment();
        mainMenuFragment = new MainMenuFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contentAreaParent, mainMenuFragment);
        fragmentTransaction.add(R.id.contentAreaParent, sideNoteFragment);
        fragmentTransaction.commit();

        noteFragment = new NoteFragment();
        noteFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog);
        noteFragment.show(getFragmentManager(), "noteDialogFragment");

        sideBtn = (Button)findViewById(R.id.side_note_button);
        mainMenuButton = (Button)findViewById(R.id.main_menu_button);
        setButtonsOnClickListeners();

        sTheContext = MainActivity.this;
        sModelMediator = new ModelMediator();
        sModelMediator.registerListener(this);
        sModelMediator.createUser();
    }

    @Override
    public void onStart(){
        super.onStart();
        sidePanel = (FrameLayout) findViewById(R.id.side_note_fragment);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sidePanel.getLayoutParams();
        // make the right margin negative so the view is moved to the right of the screen
        params.rightMargin = params.rightMargin * -1;
    }

    @Override
    public void onPause(){
        super.onPause();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(sideNoteFragment);
        fragmentTransaction.remove(mainMenuFragment);
//        fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.commit();
        noteFragment.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  fragment transaction cant be here cause it will crash
    }

    public void setButtonsOnClickListeners(){

        sideBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // animate the side bar
                if (isSideNoteVisible) {
                    // Start the animation
                    // We have to translate to 0 because the view's default starting position is moved out of the screen bounds in onStart() method
                    sidePanel.animate().translationX(0);
                    isSideNoteVisible = false;
                } else {
                    // Start the animation
                    sidePanel.animate().translationX(-sidePanel.getWidth());
                    isSideNoteVisible = true;
                }
            }

        });

        mainMenuButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                LinearLayout mainMenuPanel = (LinearLayout) findViewById(R.id.main_menu);
                // animate the main menu panel
                if (isMainMenuVisible) {
                    mainMenuPanel.animate().translationX(-mainMenuPanel.getWidth());
                    isMainMenuVisible = false;
                } else {
                    mainMenuPanel.animate().translationX(0);
                    isMainMenuVisible = true;
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
//        getSupportFragmentManager().beginTransaction().replace(R.id.contentArea, new NoteFragment());
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

    @Override
    public void onNoteCreate(Note note) {

    }

    @Override
    public void onNoteChange(Note note) {

    }

    @Override
    public void onNoteDelete(Note note) {

    }
}
