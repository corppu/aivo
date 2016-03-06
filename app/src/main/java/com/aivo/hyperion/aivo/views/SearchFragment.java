package com.aivo.hyperion.aivo.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.aivo.hyperion.aivo.R;

/**
 * Created by Matus Mucha on 28-Feb-16.
 */
public class SearchFragment extends android.support.v4.app.Fragment implements View.OnTouchListener, View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CheckBox imageSearchChckBtn;
    private CheckBox videoSearchChckBtn;
    private CheckBox fileSearchChckBtn;
    private Button searchBtn;
    private EditText searchTextArea;

    //private View search_bar;

    private OnFragmentInteractionListener mListener;

    public static SearchFragment newInstance(String param1 , String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View searchFragmentView = inflater.inflate(R.layout.fragment_main_menu, container,false);

        imageSearchChckBtn = (CheckBox) searchFragmentView.findViewById(R.id.imageCheckBox);
        imageSearchChckBtn.setOnClickListener(this);

        videoSearchChckBtn = (CheckBox) searchFragmentView.findViewById(R.id.videoCheckBox);
        videoSearchChckBtn.setOnClickListener(this);

        fileSearchChckBtn = (CheckBox) searchFragmentView.findViewById(R.id.fileCheckBox);
        fileSearchChckBtn.setOnClickListener(this);

        searchBtn = (Button) searchFragmentView.findViewById(R.id.searchButton2);
        searchBtn.setOnClickListener(this);

        searchTextArea = (EditText) searchFragmentView.findViewById(R.id.editText);
        searchTextArea.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:add desired functionality
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO:add desired functionality
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO:add desired functionality
            }
        });

        return searchFragmentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri){
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//
    }

    @Override
    public void onClick(View view) {

//                switch (view.getId()) {
//                    case R.id.searchButton2:
//                        FragmentManager fm = getFragmentManager();
//                        FragmentTransaction ft = fm.beginTransaction();
//                        ft.replace(R.id.search_bar, new SearchFragment(mListener), "fragment_screen");
//                        ft.commit();
//                        break;
//                }
//
//        };

    }

//        private void onSearchClick(View view) {
//        if(view.isSelected()) {
//            showSearchMenu();
//        }
//        view.setSelected(!view.isSelected());
//    }
//
//        private void showSearchMenu() {
//        search_bar.setVisibility(View.VISIBLE);
//    }


    }

