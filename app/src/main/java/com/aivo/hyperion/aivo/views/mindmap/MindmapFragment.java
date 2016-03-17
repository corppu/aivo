package com.aivo.hyperion.aivo.views.mindmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.main.MainActivity;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.Note;
import com.aivo.hyperion.aivo.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MindmapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MindmapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MindmapFragment extends Fragment implements ModelListener {

    private final String TAG = "MindmapFragment";

    private OnMindmapFragmentInteractionListener mListener;

    private String mMindmapTitle;

    private static final String ARG_MINDMAP_TITLE = "arg_mindmap_title";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mindmapTitle Parameter 1.
     * @return A new instance of fragment MindmapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MindmapFragment newInstance(String mindmapTitle) {
        MindmapFragment fragment = new MindmapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MINDMAP_TITLE, mindmapTitle);


        fragment.setArguments(args);
        return fragment;
    }


    public MindmapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMindmapTitle = getArguments().getString(ARG_MINDMAP_TITLE);
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
            editor.putString(ARG_MINDMAP_TITLE, mMindmapTitle);
            editor.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mindmap, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        /*try {
            mListener = (OnMindmapFragmentInteractionListener) this.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.getActivity().toString()
                    + " must implement OnMindmapFragmentInteractionListener");
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putString(ARG_MINDMAP_TITLE, mMindmapTitle);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMindmapTitle = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(ARG_MINDMAP_TITLE, mMindmapTitle);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onUserOpen(User user) {
    }

    @Override
    public void onMindmapOpen(Mindmap mindmap) {
        Log.d(TAG, "onMindmapOpen " + mindmap.toString());
    }

    @Override
    public void onUserChange(User user) {
    }

    @Override
    public void onMindmapTitleChange(Mindmap mindmap) {

    }

    @Override
    public void onUserClosed() {
        Log.d(TAG, "onUserClosed");
    }

    @Override
    public void onMindmapClosed() {
        Log.d(TAG, "onMindmapClosed");
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
    public void onNoteCreate(Note note) {

    }

    @Override
    public void onNoteChange(Note note) {

    }

    @Override
    public void onNoteDelete(Note note) {

    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {

    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {

    }

    @Override
    public void onException(Exception e) {
        Log.d(TAG, "onException" + e.getMessage());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMindmapFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
