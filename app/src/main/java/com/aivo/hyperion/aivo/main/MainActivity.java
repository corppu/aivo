package com.aivo.hyperion.aivo.main;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.aivo.hyperion.aivo.views.LineFloatingActionBarFragment;
import com.aivo.hyperion.aivo.views.MagnetFloatingActionBarFragment;
import com.aivo.hyperion.aivo.views.MagnetGroupFloatingActionBarFragment;
import com.aivo.hyperion.aivo.views.MainMenuFragment;
import com.aivo.hyperion.aivo.views.mindmap.LineViewModel;
import com.aivo.hyperion.aivo.views.mindmap.MagnetGroupViewModel;
import com.aivo.hyperion.aivo.views.mindmap.MagnetViewModel;
import com.aivo.hyperion.aivo.views.mindmap.MindmapFragment;
import com.aivo.hyperion.aivo.views.NoteFragment;
import com.aivo.hyperion.aivo.views.SearchFragment;
import com.aivo.hyperion.aivo.views.SideNoteFragment;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements ModelListener, MindmapFragment.OnMindmapFragmentInteractionListener, NoteFragment.OnNoteFragmentInteractionListener {


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

    private static Button sEditBtn;
    private static Button sUndoBtn;
    private static Button sRedoBtn;
    private static Button sSideBtn;
    private static Button sMainMenuBtn;
    private static Button sSearchBtn;
    private static Button sSettingsBtn;


    private static final String TAG = "MainActivity";
    private static final String DEFAULT_MINDMAP_TITLE = "Default";
    private static String mMindmapTitle;
    private static final String MAGNETS_NEW_POINT_F = "magnets_new_point_f";
    private static PointF mMagnetsNewPointF;


    private static MindmapFragment sMindmapFragment;
    private static Fragment sVisibleFloatingActionBar;
    private static MagnetGroupFloatingActionBarFragment sMagnetGroupFloatingActionBarFragment;
    private static MagnetFloatingActionBarFragment sMagnetFloatingActionBarFragment;
    private static LineFloatingActionBarFragment sLineFloatingActionBarFragment;
    private static SearchFragment sSearchFragment;
    private static SideNoteFragment sSideNoteFragment;
    private static MainMenuFragment sMainMenuFragment;

    public static void setNoteFragment(NoteFragment noteFragment) {
        sNoteFragment = noteFragment;
    }
    private static NoteFragment sNoteFragment = null;


    private void openNoteFragment(int id, boolean isMagnet, String title, String content) {
        if (sNoteFragment != null) return;
        sNoteFragment = NoteFragment.newInstance(id, isMagnet, title, content);
        sNoteFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog);
        sNoteFragment.show(getSupportFragmentManager(), "noteFragment");
    }

    private void openMindmapFragment(String title) {
        sMindmapFragment = MindmapFragment.newInstance(title);
        sMagnetGroupFloatingActionBarFragment = new MagnetGroupFloatingActionBarFragment();
        sMagnetFloatingActionBarFragment = new MagnetFloatingActionBarFragment();
        sLineFloatingActionBarFragment = new LineFloatingActionBarFragment();
        sSearchFragment = new SearchFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentArea, sMindmapFragment);
        ft.add(R.id.contentAreaParent, sMagnetGroupFloatingActionBarFragment);
        ft.add(R.id.contentAreaParent, sMagnetFloatingActionBarFragment);
        ft.add(R.id.contentAreaParent, sLineFloatingActionBarFragment);
        ft.add(R.id.contentAreaParent, sSearchFragment);
        ft.hide(sMagnetGroupFloatingActionBarFragment);
        ft.hide(sMagnetFloatingActionBarFragment);
        ft.hide(sLineFloatingActionBarFragment);
        ft.hide(sSearchFragment);
        ft.commit();
    }

    private void initMenus() {
        sSideNoteFragment = new SideNoteFragment();
        sMainMenuFragment = new MainMenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.contentAreaParent, sMainMenuFragment);
        ft.add(R.id.contentAreaParent, sSideNoteFragment);
        ft.commit();

        hideMainMenuFragment();
        hideSideNoteMenuFragment();
    }

    private void removeMenus() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(sMainMenuFragment);
        ft.remove(sSideNoteFragment);
        ft.commit();

        sMainMenuFragment = null;
        sSideNoteFragment = null;
    }

    private void hideSearchFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(sSearchFragment);
        ft.commit();
    }

    private void showSearchFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(sSearchFragment);
        ft.commit();
    }

    private void hideFloatingActionBar() {
        if (sVisibleFloatingActionBar == null) return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(sVisibleFloatingActionBar);
        ft.commit();
        sVisibleFloatingActionBar = null;
    }

    private void showFloatingActionBar(MagnetViewModel magnetViewModel) {
        sVisibleFloatingActionBar = sMagnetFloatingActionBarFragment;
        sMagnetFloatingActionBarFragment.openMagnetActions(magnetViewModel);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(sMagnetFloatingActionBarFragment);
        ft.commit();
    }

    private void showFloatingActionBar(MagnetGroupViewModel magnetGroupViewModel) {
        sVisibleFloatingActionBar = sMagnetGroupFloatingActionBarFragment;
        sMagnetGroupFloatingActionBarFragment.openMagnetGroupActions(magnetGroupViewModel);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(sMagnetGroupFloatingActionBarFragment);
        ft.commit();
    }

    private void showFloatingActionBar(LineViewModel lineViewModel) {
        sVisibleFloatingActionBar = sLineFloatingActionBarFragment;
        sLineFloatingActionBarFragment.openLineActions(lineViewModel);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(sLineFloatingActionBarFragment);
        ft.commit();
    }

    private void hideMainMenuFragment() {
        LinearLayout mainMenuPanel = (LinearLayout) findViewById(R.id.main_menu);
        if (mainMenuPanel != null) mainMenuPanel.animate().translationX(-mainMenuPanel.getWidth());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(sMainMenuFragment);
        ft.commit();
    }

    private void showMainMenuFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(sMainMenuFragment);
        ft.commit();

        LinearLayout mainMenuPanel = (LinearLayout) findViewById(R.id.main_menu);
        if (mainMenuPanel != null) mainMenuPanel.animate().translationX(0);
    }

    private void hideSideNoteMenuFragment() {
        LinearLayout sidePanel = (LinearLayout) findViewById(R.id.side_note_fragment);
        if (sidePanel != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)sidePanel.getLayoutParams();
            if (params.rightMargin > 0) params.rightMargin *= -1;
            Animation animation = new TranslateAnimation(-sidePanel.getWidth(), 0, 0, 0);
            animation.setDuration(300);
            animation.setFillAfter(true);
            sidePanel.startAnimation(animation);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(sSideNoteFragment);
        ft.commit();
    }

    private void showSideNoteMenuFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(sSideNoteFragment);
        ft.commit();

        LinearLayout sidePanel = (LinearLayout) findViewById(R.id.side_note_fragment);
        if (sidePanel != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)sidePanel.getLayoutParams();
            if (params.rightMargin > 0) params.rightMargin *= -1;
            Animation animation = new TranslateAnimation(0, -sidePanel.getWidth(), 0, 0);
            animation.setDuration(300);
            animation.setFillAfter(true);
            sidePanel.startAnimation(animation);
        }
    }

    private void removeMindmapFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(sMindmapFragment);
        ft.remove(sSearchFragment);
        ft.remove(sMagnetGroupFloatingActionBarFragment);
        ft.remove(sMagnetFloatingActionBarFragment);
        ft.remove(sLineFloatingActionBarFragment);
        ft.commit();

        sMindmapFragment = null;
        sSearchFragment = null;
        sMagnetGroupFloatingActionBarFragment = null;
        sMagnetFloatingActionBarFragment = null;
        sLineFloatingActionBarFragment = null;
        sVisibleFloatingActionBar = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButtons();

        sTheContext = MainActivity.this;
        sModelMediator = new ModelMediator();
        sModelMediator.registerListener(this);
        sModelMediator.createUser();

        if (savedInstanceState != null) {
            mMagnetsNewPointF = savedInstanceState.getParcelable(MAGNETS_NEW_POINT_F);
            mMindmapTitle = savedInstanceState.getString(DEFAULT_MINDMAP_TITLE);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
        if (!sModelMediator.openMindmap("Default")) sModelMediator.createMindmap("Default");
    }


    @Override
    protected void onResume() {
        super.onResume();
        sSearchFragment.setListener((SearchFragment.OnSearchFragmentInteractionListener) sMindmapFragment.getView());

        Log.d(TAG, "onResume " + Boolean.toString(sModelMediator.startAllThreads()));
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause " + Boolean.toString(sModelMediator.saveEverythingAndJoinAllThreads()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("TAG", "onSaveInstanceState " + Boolean.toString(sModelMediator.closeMindmap()));

        super.onSaveInstanceState(outState);
        outState.putParcelable(MAGNETS_NEW_POINT_F, mMagnetsNewPointF);
        outState.putString(DEFAULT_MINDMAP_TITLE, mMindmapTitle);
    }



    private void initButtons() {
        sEditBtn = (Button) findViewById(R.id.edit_button);
        sUndoBtn = (Button) findViewById(R.id.undo_button);
        sUndoBtn.setEnabled(false);
        sRedoBtn = (Button) findViewById(R.id.redo_button);
        sRedoBtn.setEnabled(false);
        sSideBtn = (Button) findViewById(R.id.side_note_button);
        sMainMenuBtn = (Button) findViewById(R.id.main_menu_button);
        sSearchBtn = (Button) findViewById(R.id.search_imagebutton);
        sSettingsBtn = (Button) findViewById(R.id.settings_imagebutton);

        sUndoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mindmap mindmap = sModelMediator.getMindmap();
                if (mindmap != null) {
                    ActionHandler actionHandler = sModelMediator.getMindmap().getActionHandler();
                    if (actionHandler != null && actionHandler.canUndo()) {
                        actionHandler.undo();
                        sRedoBtn.setEnabled(true);
                        sUndoBtn.setEnabled(actionHandler.canUndo());
                    }
                }
            }
        });

        sRedoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mindmap mindmap = sModelMediator.getMindmap();
                if (mindmap != null) {
                    ActionHandler actionHandler = sModelMediator.getMindmap().getActionHandler();
                    if (actionHandler != null && actionHandler.canRedo()) {
                        actionHandler.redo();
                        sUndoBtn.setEnabled(true);
                        sRedoBtn.setEnabled(actionHandler.canRedo());
                    }
                }
            }
        });

        sSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mindmap mindmap = sModelMediator.getMindmap();
                if (mindmap != null) {
                    ActionHandler actionHandler = sModelMediator.getMindmap().getActionHandler();
                    if (actionHandler != null && actionHandler.canRedo()) {
                        actionHandler.redo();
                        sSettingsBtn.setEnabled(true);
                        sSettingsBtn.setEnabled(actionHandler.canRedo());
                    }
                }
            }
        });

        sSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mindmap mindmap = sModelMediator.getMindmap();
                if (mindmap != null) {
                    if (sSearchFragment.isHidden()) {
                        showSearchFragment();
                    } else {
                        hideSearchFragment();
                    }
                }
            }
        });

        sMainMenuBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (sMainMenuFragment.isVisible()) {
                    hideMainMenuFragment();
                } else {
                    showMainMenuFragment();
                }
            }
        });

        sSideBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (sSideNoteFragment.isVisible()) {
                    hideSideNoteMenuFragment();
                } else {
                    showSideNoteMenuFragment();
                }
            }
        });

        sEditBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (sModelMediator.getUser() != null) {
                    openNoteFragment(0, false, "", "");
                }
            }
        });
    }

    /** ModelListener interface implementation **/

    @Override
    public void onUserOpen(User user) {
        sUser = user;
    }

    @Override
    public void onMindmapOpen(Mindmap mindmap) {
        mMindmapTitle = mindmap.getTitle();
        openMindmapFragment(mMindmapTitle);
        initMenus();
    }

    @Override
    public void onUserChange(User user) {
        sUser = user;
    }

    @Override
    public void onMindmapTitleChange(Mindmap mindmap) {
        mMindmapTitle = mindmap.getTitle();
    }

    @Override
    public void onUserClosed() {
        sUser = null;
    }

    @Override
    public void onMindmapClosed() {
        removeMindmapFragment();
        removeMenus();
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
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onMagnetCreate(Magnet magnet) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onMagnetChange(Magnet magnet) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onMagnetDelete(Magnet magnet) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onLineCreate(Line line) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onLineChange(Line line) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onLineDelete(Line line) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {
        sUndoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canUndo());
        sRedoBtn.setEnabled(sModelMediator.getMindmap().getActionHandler().canRedo());
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


    /** OnMindmapFragmentListener implementation **/

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
    public void onSelectMagnet(MagnetViewModel magnetViewModel) {
        showFloatingActionBar(magnetViewModel);
    }

    @Override
    public void onSelectMagnetGroup(MagnetGroupViewModel magnetGroupViewModel) {
        showFloatingActionBar(magnetGroupViewModel);
    }

    @Override
    public void onSelectLine(LineViewModel lineViewModel) {
        showFloatingActionBar(lineViewModel);
    }

    @Override
    public void onRemoveSelection() {
        hideFloatingActionBar();
        hideSearchFragment();
    }

    /** OnNoteFragmentListener implementation **/
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
        else {
            // FIXME: 22.3.2016 @lootabox!
            Note note = sModelMediator.createNote();
            note.setData(newTitle, newContent,
                    Color.argb(255, sRandom.nextInt(255), sRandom.nextInt(255), sRandom.nextInt(255)));
        }
    }
}
