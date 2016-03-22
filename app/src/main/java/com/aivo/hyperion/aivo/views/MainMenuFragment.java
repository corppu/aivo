package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aivo.hyperion.aivo.R;

import static android.support.v4.content.ContextCompat.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mindmapButton;
    private Button notesButton;
    private Button favoriteButton;

    private OnFragmentInteractionListener mListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
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
        // Inflate the layout for this fragment
        View mainMenuView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        mindmapButton = (Button) mainMenuView.findViewById(R.id.mindmapButton);
        mindmapButton.setOnClickListener(this);

        notesButton = (Button) mainMenuView.findViewById(R.id.notesButton);
        notesButton.setOnClickListener(this);

        favoriteButton = (Button) mainMenuView.findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(this);

        return mainMenuView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mindmapButton:
                // TODO: Mindmap button clicked
                mindmapButton.setBackgroundColor(getColor(getContext(), R.color.colorBlueMainMenuButton));
                notesButton.setBackgroundColor(Color.TRANSPARENT);
                favoriteButton.setBackgroundColor(Color.TRANSPARENT);
                break;
            case R.id.notesButton:
                // TODO: Notes button clicked
                notesButton.setBackgroundColor(getColor(getContext(), R.color.colorBlueMainMenuButton));
                mindmapButton.setBackgroundColor(Color.TRANSPARENT);
                favoriteButton.setBackgroundColor(Color.TRANSPARENT);
                break;
            case R.id.favoriteButton:
                // TODO: Favorite button clicked
                favoriteButton.setBackgroundColor(getColor(getContext(), R.color.colorBlueMainMenuButton));
                notesButton.setBackgroundColor(Color.TRANSPARENT);
                mindmapButton.setBackgroundColor(Color.TRANSPARENT);
                break;
            default:
                mindmapButton.setBackgroundColor(Color.TRANSPARENT);
                notesButton.setBackgroundColor(Color.TRANSPARENT);
                favoriteButton.setBackgroundColor(Color.TRANSPARENT);
                break;
        }
    }
}
