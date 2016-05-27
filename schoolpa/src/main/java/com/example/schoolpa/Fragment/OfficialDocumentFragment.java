package com.example.schoolpa.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolpa.Activity.SplashActivity;
import com.example.schoolpa.Adapter.OfficialDocumentAdapter;
import com.example.schoolpa.Fragment.Data.OfficialDocumentData;
import com.example.schoolpa.R;
import com.example.schoolpa.Utils.HttpUtils;
import com.example.schoolpa.Utils.ThreadUtils;

public class OfficialDocumentFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    private SwipeRefreshLayout srl;
    private RecyclerView rv_list;
    private int lastVisibleItemPosition;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mAdapter.refresh();
                    mAdapter.notifyDataSetChanged();
                    srl.setRefreshing(false);
                case 1:

            }
        }
    };
    private OfficialDocumentAdapter mAdapter;
    private View mView;


    @SuppressWarnings("unused")
    public static OfficialDocumentFragment newInstance(int columnCount) {
        OfficialDocumentFragment fragment = new OfficialDocumentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_official_document_item_list, container,
                false);
        initView();
        return mView;
    }

    private void initView() {
        srl = (SwipeRefreshLayout) mView.findViewById(R.id.srl);
        rv_list = (RecyclerView) mView.findViewById(R.id.rv_list);
        mAdapter = new OfficialDocumentAdapter(getActivity(),
                OfficialDocumentData.ITEMS, mListener, mView);
        rv_list.setAdapter(mAdapter);

        //设置下拉刷新颜色
        srl.setColorSchemeColors(new int[]{Color.GRAY});
        //下拉刷新逻辑
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ThreadUtils.execute(new ThreadUtils.onStartThreadListener() {
                    @Override
                    public void onStartThread() {
                        HttpUtils.downloadDetailInfo(getActivity(), SplashActivity.getDownloadUrl
                                ());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.refresh();
                                mAdapter.notifyDataSetChanged();
                                srl.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });

        final LinearLayoutManager layoutManager = (LinearLayoutManager) rv_list.getLayoutManager();

        //设置上划加载更多逻辑
        rv_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) &&
                        (lastVisibleItemPosition + 1)
                                == mAdapter.getItemCount()) {
                    ThreadUtils.execute(new ThreadUtils.onStartThreadListener() {
                        @Override
                        public void onStartThread() {
                            mAdapter.addMore();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
    }




    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(OfficialDocumentData.OfficialDocumentItem item);
    }

}
