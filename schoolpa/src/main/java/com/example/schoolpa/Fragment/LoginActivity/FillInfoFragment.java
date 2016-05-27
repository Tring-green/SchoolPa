package com.example.schoolpa.Fragment.LoginActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.schoolpa.Activity.LoginActivity;
import com.example.schoolpa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FillInfoFragment extends Fragment {


    private View mView;
    private EditText mEt_name;

    public FillInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fill_info, container, false);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        ((LoginActivity)getActivity()).finish();
    }


    private void initView() {
        mEt_name = (EditText) mView.findViewById(R.id.name);
    }

}
