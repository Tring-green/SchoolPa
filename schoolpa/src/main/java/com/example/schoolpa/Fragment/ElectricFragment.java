package com.example.schoolpa.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.schoolpa.adapter.ElectricAdapter;
import com.example.schoolpa.adapter.ElectricDetailsAdapter;
import com.example.schoolpa.fragment.Data.ElectricData;
import com.example.schoolpa.fragment.Data.ElectricData.ElectricItem;
import com.example.schoolpa.R;
import com.example.schoolpa.utils.HttpUtils;
import com.example.schoolpa.utils.TextUtils;
import com.example.schoolpa.utils.ToastUtils;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ElectricFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    private Activity mActivity;

    final private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mPb_center.setVisibility(View.INVISIBLE);
                    RecyclerView rv_details = (RecyclerView) mView.findViewById(R.id
                            .list_electricdetails);
                    rv_details.setLayoutManager(new GridLayoutManager(mView.getContext(), 2));
                    rv_details.setAdapter(new ElectricDetailsAdapter(HttpUtils.electricList));
                    rv_details.setVisibility(View.VISIBLE);
            }
        }
    };
    private RecyclerView mRecyclerView;
    private RelativeLayout mRl_input_electric;
    private String mBuildingName;
    private View mView;
    private ProgressBar mPb_center;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ElectricFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ElectricFragment newInstance(int columnCount) {
        ElectricFragment fragment = new ElectricFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = getActivity();
        mView = inflater.inflate(R.layout.fragment_electric_item_list, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.list_electric);
        mRecyclerView.setVisibility(View.VISIBLE);
        Button bt_bind = (Button) mView.findViewById(R.id.bt_bind_electric);

        mPb_center = (ProgressBar) mView.findViewById(R.id.pb_center_electric);
        mRl_input_electric = (RelativeLayout) mView.findViewById(R.id.rl_input_electric);

        mRl_input_electric.setVisibility(View.INVISIBLE);


        bt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_input = (EditText) mView.findViewById(R.id.et_input_electric);
                final String roomName = et_input.getText().toString();
                ToastUtils.showTestShort(mActivity, roomName);
                if (TextUtils.isEffective(roomName)) {
                    ToastUtils.showTestShort(mActivity, roomName);
                    mPb_center.setVisibility(View.VISIBLE);
                    mRl_input_electric.setVisibility(View.INVISIBLE);
                    new Thread() {
                        @Override
                        public void run() {
                            HttpUtils.getElectric(mBuildingName, roomName);
                            mHandler.sendEmptyMessage(0);
                        }


                    }.start();
                } else {
                    ToastUtils.showTestShort(mActivity, "房间号为空，请重新输入！");
                }
            }
        });

        // Set the adapter

        Context context = mView.getContext();
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mRecyclerView.setAdapter(new ElectricAdapter(ElectricData.ITEMS, new
                OnListFragmentInteractionListener() {

                    @Override
                    public void onListFragmentInteraction(ElectricItem item) {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mRl_input_electric.setVisibility(View.VISIBLE);
                        mBuildingName = item.buildingName;
                    }
                }));


        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ElectricItem item);
    }
}
