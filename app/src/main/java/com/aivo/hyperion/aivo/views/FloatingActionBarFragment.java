package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FloatingActionBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FloatingActionBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FloatingActionBarFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton starBtn;
    private ImageButton trashBtn;
    private ImageButton netBtn;
    //private ImageView starView;

    private Boolean isFavourite=false;

    private OnFragmentInteractionListener mListener;

    public FloatingActionBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FloatingActionBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FloatingActionBarFragment newInstance(String param1, String param2) {
        FloatingActionBarFragment fragment = new FloatingActionBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View floatbarView = inflater.inflate(R.layout.fragment_floating_action_bar_fragment, container, false);

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
        if (starBtn == null) {
            starBtn = (ImageButton) floatbarView.findViewById(R.id.floatbar_btn_star);
            starBtn.setOnClickListener(this);
        }

        if (netBtn == null) {
            netBtn = (ImageButton) floatbarView.findViewById(R.id.floatbar_btn_net);
            netBtn.setOnClickListener(this);
        }
    }




    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.floatbar_btn_trash:
                mMagnet.actionDelete();
                break;
            case R.id.floatbar_btn_star:

                if (isFavourite){
                    starBtn.setBackgroundResource(R.drawable.is_star_2x);
                    isFavourite=false;
                    mMagnet.actionChangeFavourite(false);
                }else{
                    starBtn.setBackgroundResource(R.drawable.ic_star_gold_2x);
                    isFavourite=true;
                    mMagnet.actionChangeFavourite(true);
                }


                break;
            case R.id.floatbar_btn_net:
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


    private Magnet mMagnet;
    private MagnetGroup mMagnetGroup;
    private Line mLine;

    public void openMagnetActions(Magnet magnet) {
        mMagnetGroup = null;
        mLine = null;
        mMagnet = magnet;

        isFavourite = mMagnet.getIsFavourite();
        if (isFavourite)
            starBtn.setBackgroundResource(R.drawable.ic_star_gold_2x);
        else
            starBtn.setBackgroundResource(R.drawable.is_star_2x);
    }

    public void openMagnetGroupActions(MagnetGroup magnetGroup) {
        // TODO: do layout for these.
        mMagnetGroup = magnetGroup;
        mLine = null;
        mMagnet = null;
    }

    public void openLineActions(Line line) {
        // TODO: do layout for these.
        mMagnetGroup = null;
        mLine = line;
        mMagnet = null;
    }
}
