package com.example.schoolpa.Fragment.LoginActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schoolpa.Lib.SPHttpClass;
import com.example.schoolpa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private View mView;
    private SPHttpClass mHttpClass;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        return mView;
    }

    private void initView() {


        Button register = (Button) mView.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                mView.setVisibility(View.GONE);
                transaction.replace(R.id.framelayout, new RegisterFragment()).commit();
            }
        });


    }

}
