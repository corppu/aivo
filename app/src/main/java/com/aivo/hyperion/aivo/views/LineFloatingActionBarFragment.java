package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.views.mindmap.LineViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LineFloatingActionBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LineFloatingActionBarFragment extends Fragment implements View.OnClickListener{

    private ImageButton trashBtn;

    private OnFragmentInteractionListener mListener;

    public LineFloatingActionBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View floatbarView = inflater.inflate(R.layout.fragment_line_floating_action_bar_fragment, container, false);

        // Inflate the layout for this fragment
        return floatbarView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnFragmentInteractionListener");
//        }
    }


    @Override
    public void onStart() {
        super.onStart();

        View floatbarView = this.getView();
        if (trashBtn == null) {
            trashBtn = (ImageButton) floatbarView.findViewById(R.id.floatbar_btn_trash);
            trashBtn.setOnClickListener(this);
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatbar_btn_trash:
                mLineViewModel.getModel().actionDelete();
                break;

            default:

                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
    }

    private LineViewModel mLineViewModel;

    public void openLineActions(LineViewModel lineViewModel) {
        mLineViewModel = lineViewModel;
    }
}
