package com.aivo.hyperion.aivo.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
import com.aivo.hyperion.aivo.views.MindmapFragment;
import com.aivo.hyperion.aivo.views.NoteFragment;
import com.aivo.hyperion.aivo.views.SideNoteFragment;

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
    SideNoteFragment sideNoteFragment;
    Button sideBtn;
    Button mainMenuButton;
    Boolean isSideNoteVisible = false;
    Boolean isMainMenuVisible = true;
    FrameLayout sidePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sideNoteFragment=new SideNoteFragment();
        fragmentManager = getSupportFragmentManager();

        
//        fragmentManager.beginTransaction().replace(R.id.contentArea, new NoteFragment()).commit();
        fragmentManager.beginTransaction().replace(R.id.contentAreaParent, new MainMenuFragment()).commit();
        fragmentManager.beginTransaction().add(R.id.contentAreaParent, sideNoteFragment).commit();


        sideBtn =(Button)findViewById(R.id.side_note_button);
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
       // sidePanel.animate().translationX(sidePanel.getWidth());
        //sidePanel.setX(sidePanel.getWidth());
        sidePanel.setVisibility(View.GONE);

    }

    public void setButtonsOnClickListeners(){

        sideBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
               // FrameLayout sidePanel = (FrameLayout) findViewById(R.id.side_note_fragment);
                // animate the side bar
                if (isSideNoteVisible){
                    // Start the animation
//                    sidePanel.animate().translationXBy(sidePanel.getWidth());
                    sidePanel.animate().translationX(sidePanel.getWidth());
                    isSideNoteVisible = false;
                } else {
//                    sidePanel.animate().translationXBy(-120);
                    // Start the animation
                    sidePanel.setVisibility(View.VISIBLE);
                    sidePanel.animate().translationX(0);

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

    @Override
    public void onPause(){
        super.onPause();
        fragmentManager.beginTransaction().remove(sideNoteFragment).commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
///   fragment trasaction cant be here cause it will crash
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
