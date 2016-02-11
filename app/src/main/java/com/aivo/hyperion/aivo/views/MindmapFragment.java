package com.aivo.hyperion.aivo.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MindmapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MindmapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MindmapFragment extends Fragment implements View.OnTouchListener, ModelListener {

    private final String TAG = "MindmapFragment";

    private OnMindmapFragmentInteractionListener mListener;

    private ModelMediator mediator;

    private MindmapPresenter presenter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MindmapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MindmapFragment newInstance(String param1, String param2) {
        MindmapFragment fragment = new MindmapFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mindmap, container, false);
        view.setWillNotDraw(false);
        view.setOnTouchListener(this);

        mediator = new ModelMediator();
        mediator.registerListener(this);
        mediator.createUser();

        presenter = new MindmapPresenter();

        return view;
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

        presenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.setViews(
                (SurfaceView) getActivity().findViewById(R.id.surfaceView),
                new MindmapView(new Theme(), new Mindmap()));
        presenter.resume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d(TAG, event.toString());
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                v.invalidate();

                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                v.invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:

                v.invalidate();
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onUserOpened(User user) {
        Log.d(TAG, "onUserOpened " + user.toString());
        mediator.createMindmap("New Mindmap");
    }

    @Override
    public void onMindmapOpened(Mindmap mindmap) {
        Log.d(TAG, "onMindmapOpened " + mindmap.toString());
        //((MindmapView) this.getView()).setMindmap(mindmap);
        //this.getView().invalidate();
    }

    @Override
    public void onUserChanged(User user) {
        Log.d(TAG, "onUserChanged " + user.toString());
        mediator.createMindmap("New Mindmap");
    }

    @Override
    public void onMindmapChanged(Mindmap mindmap) {
        Log.d(TAG, "onMindmapChanged " + mindmap.toString());
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
