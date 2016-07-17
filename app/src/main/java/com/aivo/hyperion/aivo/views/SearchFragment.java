package com.aivo.hyperion.aivo.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import com.aivo.hyperion.aivo.R;

/**
 * Created by Matus Mucha on 28-Feb-16.
 */
public class SearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CheckBox imageSearchChckBtn;
    private CheckBox videoSearchChckBtn;
    private CheckBox fileSearchChckBtn;
    private Button searchBtn;
    private EditText searchTextArea;

    private OnSearchFragmentInteractionListener mListener;

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

        View searchFragmentView = inflater.inflate(R.layout.fragment_search, container,false);

        imageSearchChckBtn = (CheckBox) searchFragmentView.findViewById(R.id.imageCheckBox);
        imageSearchChckBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: image search
            }
        });

        videoSearchChckBtn = (CheckBox) searchFragmentView.findViewById(R.id.videoCheckBox);
        videoSearchChckBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: video search
            }
        });

        fileSearchChckBtn = (CheckBox) searchFragmentView.findViewById(R.id.fileCheckBox);
        fileSearchChckBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: file search
            }
        });

        searchBtn = (Button) searchFragmentView.findViewById(R.id.searchButton2);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.search(searchTextArea.getText().toString(), imageSearchChckBtn.isChecked(), videoSearchChckBtn.isChecked(), fileSearchChckBtn.isChecked());
            }
        });

        searchTextArea = (EditText) searchFragmentView.findViewById(R.id.editText);
//        searchTextArea.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                //TODO:add desired functionality
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //TODO:add desired functionality
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //TODO:add desired functionality
//            }
//        });

        return searchFragmentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setListener(OnSearchFragmentInteractionListener listener) {
        mListener = listener;
    }

    public interface OnSearchFragmentInteractionListener {
        void search(String text, boolean image, boolean video, boolean file);
    }
}

