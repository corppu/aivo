package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aivo.hyperion.aivo.R;
import com.aivo.hyperion.aivo.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Floating_action_bar_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Floating_action_bar_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Floating_action_bar_fragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button starBtn;
    private Button trashBtn;
    private Button netBtn;
    private ImageView starView;

    private Boolean isFavourite=false;

    private OnFragmentInteractionListener mListener;

    public Floating_action_bar_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Floating_action_bar_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Floating_action_bar_fragment newInstance(String param1, String param2) {
        Floating_action_bar_fragment fragment = new Floating_action_bar_fragment();
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

        trashBtn = (Button) floatbarView.findViewById(R.id.floatbar_btn_trash);
        starBtn = (Button) floatbarView.findViewById(R.id.floatbar_btn_star);
        netBtn = (Button) floatbarView.findViewById(R.id.floatbar_btn_net);
        starView = (ImageView)floatbarView.findViewById(R.id.floatbar_img_star);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floating_action_bar_fragment, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatbar_btn_trash:

                break;
            case R.id.floatbar_btn_star:

                if (!isFavourite){
                    starView.setBackgroundResource(R.drawable.is_star_2x);

                }else{

                    starView.setBackgroundResource(R.drawable.ic_star_gold_2x);
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
        // TODO: Update argument type and name
      //  void onFragmentInteraction(Uri uri);
    }
}