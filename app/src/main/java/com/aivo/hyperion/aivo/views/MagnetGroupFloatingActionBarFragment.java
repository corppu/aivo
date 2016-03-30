package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.MagnetGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MagnetGroupFloatingActionBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MagnetGroupFloatingActionBarFragment extends Fragment implements View.OnClickListener{

    private ImageButton trashBtn;
    private ImageButton changeTitleButton;

    private OnFragmentInteractionListener mListener;

    public MagnetGroupFloatingActionBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View floatbarView = inflater.inflate(R.layout.fragment_magnet_group_floating_action_bar_fragment, container, false);

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

        if (changeTitleButton == null) {
            changeTitleButton = (ImageButton) floatbarView.findViewById(R.id.floatbar_btn_change_title);
            changeTitleButton.setOnClickListener(this);
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatbar_btn_trash:

                break;
            case R.id.floatbar_btn_change_title:

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

    private MagnetGroup mMagnetGroup;

    public void openMagnetGroupActions(MagnetGroup magnetGroup) {
        mMagnetGroup = magnetGroup;
    }
}
