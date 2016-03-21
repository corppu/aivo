package com.aivo.hyperion.aivo.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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
import com.aivo.hyperion.aivo.models.actions.ActionHandler;
import com.aivo.hyperion.aivo.views.Floating_action_bar_fragment;
import com.aivo.hyperion.aivo.views.MainMenuFragment;
import com.aivo.hyperion.aivo.views.mindmap.MindmapFragment;
import com.aivo.hyperion.aivo.views.NoteFragment;
import com.aivo.hyperion.aivo.views.SearchFragment;
import com.aivo.hyperion.aivo.views.SideNoteFragment;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements ModelListener, MindmapFragment.OnMindmapFragmentInteractionListener, NoteFragment.OnNoteFragmentInteractionListener {

    SideNoteFragment sideNoteFragment;
    MainMenuFragment mainMenuFragment;
    SearchFragment searchFragment;
    Button editBtn;
    Button undoBtn;
    Button redoBtn;
    Button sideBtn;
    Button mainMenuButton;
    Button searchButton;
    Boolean isSideNoteVisible = false;
    Boolean isMainMenuVisible = true;
    Boolean isSearchPanelVisible = false;
    Boolean isFloatingMenuHidden = true;
    RelativeLayout sidePanel;
    RelativeLayout searchPanel;
    LinearLayout mainMenuPanel;
    Floating_action_bar_fragment floatingMenu;
    FragmentTransaction fragmentTransaction;


    private static final String TAG = "MainActivity";

    private static final String DEFAULT_USERNAME = "User 1";
    private String mUserName;

    private static final String DEFAULT_PASSWORD = "123456";
    private String mPassword;

    private static final String DEFAULT_MINDMAP_TITLE = "Mindmap 1";
    private String mMindmapTitle;

    private static final String MAGNETS_NEW_POINT_F = "magnets_new_point_f";
    private PointF mMagnetsNewPointF;



    private NoteFragment mNoteFragment;





    private void openNoteFragment(int id, boolean isMagnet, String title, String content) {
        mNoteFragment = NoteFragment.newInstance(id, isMagnet, title, content);
        mNoteFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog);
        mNoteFragment.show(getFragmentManager(), "noteFragment");
    }

    private void openMindmapFragment(String title) {
        mMindmapFragment = MindmapFragment.newInstance(title);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentArea, mMindmapFragment).commit();
    }


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
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sideNoteFragment = new SideNoteFragment();
        mainMenuFragment = new MainMenuFragment();
        searchFragment = new SearchFragment();
        floatingMenu = new Floating_action_bar_fragment();


        editBtn = (Button)findViewById(R.id.edit_button);
        undoBtn = (Button)findViewById(R.id.undo_button);
        redoBtn = (Button)findViewById(R.id.redo_button);
        sideBtn = (Button)findViewById(R.id.side_note_button);
        mainMenuButton = (Button)findViewById(R.id.main_menu_button);
        searchButton = (Button)findViewById(R.id.search_imagebutton);
        setButtonsOnClickListeners();

        sTheContext = MainActivity.this;
        sModelMediator = new ModelMediator();
        sModelMediator.registerListener(this);
        sModelMediator.createUser();

        if (savedInstanceState == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.contentAreaParent, mainMenuFragment);
            fragmentTransaction.add(R.id.contentAreaParent, sideNoteFragment);
            fragmentTransaction.add(R.id.contentAreaParent, searchFragment);
            fragmentTransaction.add(R.id.contentAreaParent, floatingMenu);
            fragmentTransaction.hide(floatingMenu);
            fragmentTransaction.commit();
        } else {
            mMagnetsNewPointF = savedInstanceState.getParcelable(MAGNETS_NEW_POINT_F);
            mMindmapTitle = savedInstanceState.getString(DEFAULT_MINDMAP_TITLE);
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        // crashes because after going back to foreground  setContentView(R.layout.activity_main); doesnt run therefore null pointer exceprion
        // cant hadle fragment view if they are not yet there, move code to onCreateView

        Log.d(TAG, "onStart");

        if (mMindmapFragment == null) {
            mMindmapFragment = (MindmapFragment) getSupportFragmentManager().findFragmentById(R.id.mindmapFragment);
        }

        if (mMindmapFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mMindmapFragment);
            ft.commit();
        }

        if (!sModelMediator.openMindmap("Default")) sModelMediator.createMindmap("Default");


        if (floatingMenu == null) {
            floatingMenu = (Floating_action_bar_fragment) getSupportFragmentManager().findFragmentById(R.id.floatBar);
        }

        if (floatingMenu != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(floatingMenu);
            ft.commit();
        }

        if (sidePanel == null) {
            sidePanel = (RelativeLayout) findViewById(R.id.side_note_fragment);
        }
        if (searchPanel == null) {
            searchPanel = (RelativeLayout) findViewById(R.id.search_bar);
        }
        if (mainMenuPanel == null) {
            mainMenuPanel = (LinearLayout) findViewById(R.id.main_menu);
        }

        RelativeLayout.LayoutParams sidePanelParams = (RelativeLayout.LayoutParams) sidePanel.getLayoutParams();
        // make the right margin negative so the view is moved to the right of the screen
        if (sidePanelParams.rightMargin > -1) sidePanelParams.rightMargin = sidePanelParams.rightMargin * -1;

        RelativeLayout.LayoutParams searchParams = (RelativeLayout.LayoutParams) searchPanel.getLayoutParams();
        // make the top margin negative so the view is moved to the top of the screen
        if (searchParams.topMargin > -1) searchParams.topMargin = searchParams.topMargin * -1;
    }




    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume " + Boolean.toString(sModelMediator.startAllThreads()));
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause " + Boolean.toString(sModelMediator.saveEverythingAndJoinAllThreads()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelable(MAGNETS_NEW_POINT_F, mMagnetsNewPointF);
        outState.putString(DEFAULT_MINDMAP_TITLE, mMindmapTitle);
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop " + Boolean.toString(sModelMediator.closeMindmap()));
    }


    public void setButtonsOnClickListeners(){

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mindmap mindmap = sModelMediator.getMindmap();
                if (mindmap != null) {
                    ActionHandler actionHandler = sModelMediator.getMindmap().getActionHandler();
                    if (actionHandler != null && actionHandler.canUndo()) {
                        actionHandler.undo();
                        redoBtn.setEnabled(true);
                        undoBtn.setEnabled(actionHandler.canUndo());
                    }
                }
            }
        });

        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mindmap mindmap = sModelMediator.getMindmap();
                if (mindmap != null) {
                    ActionHandler actionHandler = sModelMediator.getMindmap().getActionHandler();
                    if (actionHandler != null && actionHandler.canRedo()) {
                        actionHandler.redo();
                        undoBtn.setEnabled(true);
                        redoBtn.setEnabled(actionHandler.canRedo());
                    }
                }
            }
        });


        sideBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // animate the side bar
                if (isSideNoteVisible) {
                    // Start the animation
                    Animation animation = new TranslateAnimation(-sidePanel.getWidth(), 0, 0, 0);
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    sidePanel.startAnimation(animation);

                    isSideNoteVisible = false;
                } else {
                    // Start the animation
                    Animation animation = new TranslateAnimation(0, -sidePanel.getWidth(), 0, 0);
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    sidePanel.startAnimation(animation);

                    isSideNoteVisible = true;
                }
            }

        });

        mainMenuButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
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

        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (isSearchPanelVisible) {
                    Animation animation = new TranslateAnimation(0, 0, searchPanel.getHeight(), 0);
                    animation.setDuration(250);
                    animation.setFillAfter(true);
                    searchPanel.startAnimation(animation);

                    isSearchPanelVisible = false;
                } else {
                    Animation animation = new TranslateAnimation(0, 0, 0, searchPanel.getHeight());
                    animation.setDuration(250);
                    animation.setFillAfter(true);
                    searchPanel.startAnimation(animation);

                    isSearchPanelVisible = true;
                }
            }
        });
    }


    @Override
    public void onUserOpen(User user) {
        sUser = user;
    }

    private MindmapFragment mMindmapFragment;
    //private SearchFragment mSearchFragment;

    @Override
    public void onMindmapOpen(Mindmap mindmap) {
        mMindmapTitle = mindmap.getTitle();
        openMindmapFragment(mMindmapTitle);
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
//        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//        transaction.remove(getSupportFragmentManager().findFragmentById(R.id.mindmapFragment));
//        transaction.commit();
    }

    @Override
    public void onException(Exception e) {
        try {
            throw e;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onMagnetGroupChange(MagnetGroup magnetGroup) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onMagnetCreate(Magnet magnet) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onMagnetChange(Magnet magnet) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onMagnetDelete(Magnet magnet) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onLineCreate(Line line) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onLineChange(Line line) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onLineDelete(Line line) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {
        undoBtn.setEnabled(true);
    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {
        undoBtn.setEnabled(true);
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
    public void onCreateMagnet(PointF newPointF) {
        mMagnetsNewPointF = newPointF;
        openNoteFragment(0, true, "", "");
    }

    @Override
    public void onCreateMagnet(MagnetGroup parent, PointF newPointF) {
        mMagnetsNewPointF = newPointF;
        openNoteFragment(-parent.getId(), true, "", "");
    }

    @Override
    public void onEditMagnet(Magnet magnet) {
        openNoteFragment(magnet.getId(), true, magnet.getTitle(), magnet.getContent());
    }

    @Override
    public void onSelectMagnet(Magnet magnet) {
        //show floating menu fragment
        if(isFloatingMenuHidden){
            floatingMenu.openMagnetActions(magnet);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.show(floatingMenu);
            ft.commit();
        }
    }

    @Override
    public void onSelectMagnetGroup(MagnetGroup magnetGroup) {
        // TODO: implement some bar for this
    }

    @Override
    public void onSelectLine(Line line) {
        // TODO: implement some bar for this
    }

    @Override
    public void onRemoveSelection() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(floatingMenu);
        ft.commit();
    }

    /** OnNoteFragmentListener **/
    @Override
    public void onSave(int id, boolean isMagnet, String newTitle, String newContent) {
        if (isMagnet) {
            if (id > 0) {
                sModelMediator.getMindmap().getMagnet(id).actionChangeData(newTitle, newContent);
            } else if (id < 0) {
                sModelMediator.getMindmap().actionCreateMagnetChild(sModelMediator.getMindmap().getMagnetGroup(-id), mMagnetsNewPointF, newTitle, newContent,
                        Color.argb(255, sRandom.nextInt(255), sRandom.nextInt(255), sRandom.nextInt(255)));
            } else {
                sModelMediator.getMindmap().actionCreateMagnet(mMagnetsNewPointF, newTitle, newContent,
                        Color.argb(255, sRandom.nextInt(255), sRandom.nextInt(255), sRandom.nextInt(255)));
            }
        }
    }




}
