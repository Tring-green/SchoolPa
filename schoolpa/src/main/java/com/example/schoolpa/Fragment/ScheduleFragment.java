package com.example.schoolpa.Fragment;

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

import com.example.schoolpa.Adapter.ScheduleAdapter;
import com.example.schoolpa.Fragment.Data.ScheduleData;
import com.example.schoolpa.R;
import com.example.schoolpa.Utils.HttpUtils;
import com.example.schoolpa.Utils.RegexUtils;
import com.example.schoolpa.Utils.StringUtils;
import com.example.schoolpa.Utils.TextUtils;
import com.example.schoolpa.Utils.ThreadUtils;
import com.example.schoolpa.Utils.ToastUtils;
import com.example.schoolpa.Utils.UrlUtils;
import com.example.schoolpa.View.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ScheduleFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int OBTAINSUCCESSED = 0;

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private Activity mActivity;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OBTAINSUCCESSED:
                    pb_center.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    handleData();
                    adapter.setValues(scheduleItems);
                    adapter.notifyDataSetChanged();
            }
        }
    };

    private ScheduleAdapter adapter;
    private View mView;
    private Button mBt_bind;
    private RelativeLayout mRl_input;

    /**
     * 处理数据，将姓名和课程抽离出来
     */
    private void handleData() {
        List<String> couNames = HttpUtils.couNames;
        String stuNum = HttpUtils.stuNum;
        for (String couName : couNames) {
            int middle = couName.indexOf(":");
            if (middle == -1) {
                continue;
            }
            String trueName = couName.substring(0, middle);
            String couClass = couName.substring(middle + 1, couName.length());
            String[] couClasses = couClass.split(";");
            handleClass(trueName, couClasses);
        }

    }

    public static List<ScheduleData.ScheduleItem> scheduleItems;


    /**
     * @param couName
     * @param couClasses 为课程设置所在的位置
     */
    private void handleClass(String couName, String[] couClasses) {
        for (String couClass : couClasses) {
            String week = RegexUtils.RegexGroup(couClass, "周(.{1})", 1);
            week = StringUtils.transWeek(week);
            String date = RegexUtils.RegexGroup(couClass, "周(.{1})(.+?),", 2);
            String classpos = RegexUtils.RegexGroup(couClass, "\\((.+?)\\)", 1);
            int pos = getPos(week, date);
            ScheduleData.ScheduleItem scheduleItem = new ScheduleData.ScheduleItem(couName, week,
                    date, classpos, pos);
            scheduleItems.add(scheduleItem);
        }
    }

    private int getPos(String week, String date) {
        return (ScheduleData.COLUMN_COUNT * (Integer.parseInt(date) / 2 + 1) + Integer.parseInt(week));
    }

    private RecyclerView recyclerView;
    private ProgressBar pb_center;

    @SuppressWarnings("unused")
    public static ScheduleFragment newInstance(int columnCount) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduleItems = new ArrayList<>();
        mActivity = getActivity();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_schedule_item_list, container, false);
        initView();


        return mView;
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.list_schedule);
        mBt_bind = (Button) mView.findViewById(R.id.bt_bind_schedule);
        pb_center = (ProgressBar) mView.findViewById(R.id.pb_center_schedule);
        mRl_input = (RelativeLayout) mView.findViewById(R.id.rl_input_schedule);

        mBt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_input = (EditText) mView.findViewById(R.id.et_input_schedule);
                final String stuNum = et_input.getText().toString();
                if (TextUtils.isEffective(stuNum)) {
                    if (stuNum.length() != 10) {
                        ToastUtils.showTestShort(mActivity, "学号错误，请重新输入");
                        return;
                    }

                    String stuyear = stuNum.substring(0, 4);
                    mRl_input.setVisibility(View.INVISIBLE);
                    pb_center.setVisibility(View.VISIBLE);
                    final String mapPath = UrlUtils.BASEDIRPATHMAP + "numtocouby" + stuyear + "" +
                            ".txt";
                    HttpUtils.setHandler(mHandler);
                    ThreadUtils.execute(new ThreadUtils.onStartThreadListener() {
                        @Override
                        public void onStartThread() {
                            HttpUtils.findStuNum(stuNum, mapPath);

                        }
                    });
                } else {
                    ToastUtils.showTestShort(mActivity, "学号为空，请重新输入！");
                }
            }
        });
        Context context = mView.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            recyclerView.addItemDecoration(new DividerGridItemDecoration(context));
        }

        adapter = new ScheduleAdapter(ScheduleData.ITEMS, mListener);
        recyclerView.setAdapter(adapter);
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ScheduleData.ScheduleItem item);
    }


}
