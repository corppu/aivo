package com.aivo.hyperion.aivo.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.aivo.hyperion.aivo.views.mindmap.MagnetViewModel;
import com.aivo.hyperion.aivo.views.mindmap.MindmapFragment;
import com.aivo.hyperion.aivo.views.NoteFragment;
import com.aivo.hyperion.aivo.views.SideNoteFragment;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements ModelListener, MindmapFragment.OnMindmapFragmentInteractionListener, NoteFragment.OnNoteFragmentInteractionListener {
    private static final String TAG = "MainActivity";

    private static final String DEFAULT_USERNAME = "User 0";
    private String mUserName;

    private void openUser() {
        //getModelMediator().getUser();
        getModelMediator().closeMindmap();
        getModelMediator().closeUser();
        getModelMediator().createUser();
    }

    private static final String DEFAULT_MINDMAP_TITLE = "Example 0";
    private String mMindmapTitle;
    private void openMindmap() {
        SharedPreferences sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        sharedPreferences.getString(mMindmapTitle, DEFAULT_MINDMAP_TITLE);
        if (mMindmapTitle == DEFAULT_MINDMAP_TITLE) {
            int counter = 0;
            while (sModelMediator.isMindmapTitleUsed(mMindmapTitle)) {
                mMindmapTitle = mMindmapTitle.substring(0, mMindmapTitle.indexOf(Integer.toString(counter)));
                ++counter;
                mMindmapTitle += Integer.toString(counter);
            }
            sModelMediator.createMindmap(mMindmapTitle);
        } else {
            // TODO: open saved mindmap
            //sModelMediator.open...
        }
    }

    private NoteFragment mNoteFragment;
    private void openNoteFragment(boolean isNew, boolean isMagnet, String title, String content) {
        mNoteFragment = NoteFragment.newInstance(isNew, isMagnet, title, content);
        mNoteFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog);
        mNoteFragment.show(getFragmentManager(), "noteDialogFragment");
    }

    private MindmapFragment mMindmapFragment;
    private void openMindmapFragment(String title) {
        mMindmapFragment = MindmapFragment.newInstance(title);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentArea, mMindmapFragment).commit();
    }





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
        if (params != null) {
            params.rightMargin = params.rightMargin * -1;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(sideNoteFragment);
        fragmentTransaction.remove(mainMenuFragment);
//        fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.commit();
        mNoteFragment.dismiss();
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


    @Override
    public void onMindmapOpen(Mindmap mindmap) {
        openMindmapFragment(mindmap.getTitle());
    }

    @Override
    public void onUserChange(User user) {
        sUser = user;
    }

    @Override
    public void onMindmapTitleChange(Mindmap mindmap) {

    }

    @Override
    public void onUserClosed() {
        sUser = null;
    }

    @Override
    public void onMindmapClosed() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.contentArea, new NoteFragment());
        this.getSupportFragmentManager().popBackStack("asd", 0);
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
        mCreatedMagnet = magnet;
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
        mCreatedMagnetGroup = magnetGroup;
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


    /** OnMindmapFragmentListener **/

    @Override
    public void onCreateMagnet(PointF pointF) {
        mNewSavePointF = pointF;
        openNoteFragment(true, true, "", "");
    }

    @Override
    public void onCreateMagnet(MagnetGroup parent, PointF newPointF) {
        mMagnetGroupParent = parent;
        mNewSavePointF = newPointF;
        openNoteFragment(true, true, "", "");
    }

    /** OnNoteFragmentListener **/
    @Override
    public void onSave(boolean isMagnet, String oldTitle, String newTitle, String newContent) {
        if (isMagnet) {
            //mCreatedMagnet.actionChangeData();
        }
    }

    MagnetGroup mMagnetGroupParent = null;
    PointF mNewSavePointF = null;

    Note mCreatedNote = null;
    Magnet mCreatedMagnet = null;
    MagnetGroup mCreatedMagnetGroup = null;

    @Override
    public void onSaveNew(boolean isMagnet, String newTitle, String newContent) {
        if (isMagnet) {
            if (mMagnetGroupParent != null) {
                sModelMediator.getMindmap().actionCreateMagnetChild(mMagnetGroupParent, mNewSavePointF);
            } else {
                sModelMediator.getMindmap().actionCreateMagnet(mNewSavePointF);
            }

            mCreatedMagnet.setData(newTitle, newContent, Color.argb(255, sRandom.nextInt(255), sRandom.nextInt(255), sRandom.nextInt(255)));
        } else { // !isMagnet
            sModelMediator.createNote();
            mCreatedNote.setData(newTitle, newContent, Color.argb(255, sRandom.nextInt(255), sRandom.nextInt(255), sRandom.nextInt(255)));
        }
    }

    @Override
    public void onCancel(boolean isMagnet) {

    }



}
