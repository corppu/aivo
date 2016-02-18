package com.aivo.hyperion.aivo.main;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.User;
import com.aivo.hyperion.aivo.views.MainMenuFragment;
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

    FragmentManager fragmentManager;
    Button sideBtn;
    Button mainMenuButton;
    Boolean isSideNoteVisible = false;
    Boolean isMainMenuVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.contentArea, new NoteFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.contentAreaParent, new MainMenuFragment()).commit();

        sideBtn =(Button)findViewById(R.id.side_note_button);
        mainMenuButton = (Button)findViewById(R.id.main_menu_button);
        setButtonsOnClickListeners();

        sTheContext = MainActivity.this;
        sModelMediator = new ModelMediator();
        sModelMediator.registerListener(this);
        sModelMediator.createUser();
    }

    public void setButtonsOnClickListeners(){

        sideBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                LinearLayout sidePanel = (LinearLayout) findViewById(R.id.side_note_panel);
                // animate the side bar
                if (isSideNoteVisible){
                    // Start the animation
//                    sidePanel.animate().translationXBy(sidePanel.getWidth());
                    sidePanel.animate().translationX(sidePanel.getWidth());
                    sidePanel.setVisibility(View.INVISIBLE);
                    isSideNoteVisible = false;
                } else {
//                    sidePanel.animate().translationXBy(-120);
                    // Start the animation
                    sidePanel.animate().translationX(0);
                    sidePanel.setVisibility(View.VISIBLE);
                    isSideNoteVisible = true;
                }
            }

        });

        mainMenuButton.setOnClickListener(new Button.OnClickListener(){
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
    public void onUserOpened(User user) {
        sUser = user;
        sModelMediator.createMindmap("Default");
    }

    private MindmapFragment mMindmapFragment;
    @Override
    public void onMindmapOpened(Mindmap mindmap) {
        mMindmapFragment = new MindmapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentArea, mMindmapFragment).commit();
        mMindmapFragment.onMindmapOpened(mindmap);
    }

    @Override
    public void onUserChanged(User user) {
        sUser = user;
    }

    @Override
    public void onMindmapChanged(Mindmap mindmap) {
        mMindmapFragment.onMindmapChanged(mindmap);
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
}
