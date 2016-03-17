package com.aivo.hyperion.aivo.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.main.MainActivity;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Note;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteFragment.OnNoteFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NoteFragment extends DialogFragment implements OnTouchListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IS_NEW = "arg_is_new";
    private static final String ARG_IS_MAGNET = "arg_is_magnet";
    private static final String ARG_OLD_NOTE_TITLE = "arg_old_note_title";
    private static final String ARG_OLD_NOTE_CONTENT = "arg_old_note_content";
    private static final String TAG = "NoteFragment";

    // TODO: Rename and change types of parameters
    private boolean mIsNew;
    private boolean mIsMagnet;
    private String mOldNoteTitle;
    private String mOldNoteContent;


    private View attachFile;
    private ArcLayout arcLayout;

    private OnNoteFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param noteTitle Parameter 1.
     * @param noteContent Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(boolean isNew, boolean isMagnet, String noteTitle, String noteContent) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();

        args.putBoolean(ARG_IS_NEW, isNew);
        args.putBoolean(ARG_IS_MAGNET, isMagnet);
        args.putString(ARG_OLD_NOTE_TITLE, noteTitle);
        args.putString(ARG_OLD_NOTE_CONTENT, noteContent);

        fragment.setArguments(args);
        return fragment;
    }
    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsNew = getArguments().getBoolean(ARG_IS_NEW);
            mIsMagnet = getArguments().getBoolean(ARG_IS_MAGNET);
            mOldNoteTitle = getArguments().getString(ARG_OLD_NOTE_TITLE);
            mOldNoteContent = getArguments().getString(ARG_OLD_NOTE_CONTENT);
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
            editor.putBoolean(ARG_IS_NEW, mIsNew);
            editor.putBoolean(ARG_IS_MAGNET, mIsMagnet);
            editor.putString(ARG_OLD_NOTE_TITLE, mOldNoteContent);
            editor.putString(ARG_OLD_NOTE_CONTENT, mOldNoteContent);
            editor.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View noteView = inflater.inflate(R.layout.fragment_note, container, false);

        // Set onClick listeners for the Arc layout items (attach file/video/image buttons)
        arcLayout = (ArcLayout) noteView.findViewById(R.id.arc_layout);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }

        attachFile = noteView.findViewById(R.id.attachFile);
        attachFile.setOnClickListener(this);

        ImageButton resizeButton = (ImageButton) noteView.findViewById(R.id.expandImageButton);
        resizeButton.setOnClickListener(this);

        ImageButton saveButton = (ImageButton) noteView.findViewById(R.id.saveImageButton);
        saveButton.setOnClickListener(this);

        ImageButton cancelButton = (ImageButton) noteView.findViewById(R.id.cancelImageButton);
        cancelButton.setOnClickListener(this);

        return noteView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteFragmentInteractionListener) {
            mListener = (OnNoteFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoteFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mIsNew = sharedPreferences.getBoolean(ARG_IS_NEW, mIsNew);
        mIsMagnet = sharedPreferences.getBoolean(ARG_IS_MAGNET, mIsMagnet);
        mOldNoteTitle = sharedPreferences.getString(ARG_OLD_NOTE_TITLE, mOldNoteTitle);
        mOldNoteContent = sharedPreferences.getString(ARG_OLD_NOTE_CONTENT, mOldNoteContent);

        int width = getResources().getDimensionPixelSize(R.dimen.note_width);
        int height = getResources().getDimensionPixelSize(R.dimen.note_height);

        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = dialogWindow.getAttributes();

        dialogWindow.setLayout(width, height);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // remove the dim of background around dialog
        windowParams.dimAmount = 0.00f;
        dialogWindow.setAttributes(windowParams);
    }

    @Override
    public void onPause() {
        super.onPause();
        // save stuff to sharedpreffs
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(ARG_IS_NEW, mIsNew);
        editor.putBoolean(ARG_IS_MAGNET, mIsMagnet);
        editor.putString(ARG_OLD_NOTE_TITLE, mOldNoteContent);
        editor.putString(ARG_OLD_NOTE_CONTENT, mOldNoteContent);
        editor.commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNoteFragmentInteractionListener {
        void onSave(boolean isMagnet, String oldTitle, String newTitle, String newContent);
        void onSaveNew(boolean isMagnet, String newTitle, String newContent);
        void onCancel(boolean isMagnet);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveImageButton:
                String newTitle = ((EditText)this.getDialog().findViewById(R.id.titleEditText)).getText().toString();
                String newContent = ((EditText)this.getDialog().findViewById(R.id.contentEditText)).getText().toString();

                if (mIsNew) mListener.onSaveNew(mIsMagnet, newTitle, newContent);
                else mListener.onSave(mIsMagnet, mOldNoteTitle, newTitle, newContent);

                mIsNew = false;
                mOldNoteTitle = newTitle;
                mOldNoteContent = newContent;
                break;

            case R.id.cancelImageButton:
                // TODO: Cancel editing
                mListener.onCancel(mIsMagnet);
                this.dismiss();
                break;
            case R.id.attachFile:
                onAttachClick(view);
                break;
            case R.id.fileButton:
                // TODO: Attach a file
                break;
            case R.id.videoButton:
                // TODO: Attach a video
                break;
            case R.id.imageButton:
                // TODO: Attach an image
                break;
            case R.id.expandImageButton:
                // TODO: Resize note
                break;
            default:
                break;
        }

    }

    private void onAttachClick(View view) {
        if (view.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        view.setSelected(!view.isSelected());
    }

    private void showMenu() {
        arcLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                arcLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();
    }

    private Animator createShowItemAnimator(View item) {

        float dx = attachFile.getX() - item.getX();
        float dy = attachFile.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 0f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = attachFile.getX() - item.getX();
        float dy = attachFile.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }
}