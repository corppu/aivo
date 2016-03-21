package com.aivo.hyperion.aivo.views.mindmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
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
 * {@link MindmapFragment.OnMindmapFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MindmapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MindmapFragment extends Fragment {
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

    @Override
    public void onStart() {
        super.onStart();

        try {
            mListener = (OnMindmapFragmentInteractionListener) this.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.getActivity().toString()
                    + " must implement OnMindmapFragmentInteractionListener");
        }
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
        void onCreateMagnet(PointF pointF);
        void onCreateMagnet(MagnetGroup parent, PointF newPointF);
        void onEditMagnet(Magnet magnet);
        void onSelectMagnet(Magnet magnet);
    }

}
