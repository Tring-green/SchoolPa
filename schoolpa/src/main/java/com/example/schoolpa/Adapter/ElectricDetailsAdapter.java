package com.example.schoolpa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolpa.R;

import java.util.List;

/**
 * Created by admin on 2016/4/5.
 */
public class ElectricDetailsAdapter extends RecyclerView.Adapter {

    public static List<String> sList;
    private double mElectricWeekCost;
    private double mElectricDayCost;
    private double mRemain;
    private double mOnlyRemain;

    /**
     * @param list
     * 初始化电费界面
     */
    public ElectricDetailsAdapter(List<String> list) {
        sList = list;
        if (list.size() > 2) {
            mElectricWeekCost = 0;
            String end = list.get(list.size() - 1);
            String endSub = list.get(list.size() - 2);
            String first = list.get(0);

            String[] endSplit = end.split(" ");
            String[] endSubSplit = endSub.split(" ");
            String[] firstSplit = first.split(" ");
            mElectricWeekCost = Double.valueOf(endSplit[3]) - Double.valueOf(firstSplit[3]);
            mElectricDayCost = 0;
            mElectricDayCost = Double.valueOf(endSplit[3]) - Double.valueOf(endSubSplit[3]);

            mRemain = Double.valueOf(endSplit[4]) - Double.valueOf(endSplit[3]);

            mOnlyRemain = mRemain / mElectricDayCost;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .fragment_electric_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position == 0) {
            viewHolder.mNameView.setText("昨日使用了" + (int)mElectricDayCost + "度电");
        }
        if (position == 1) {
            viewHolder.mNameView.setText("7天使用" + (int)mElectricWeekCost + "度电");
        }
        if (position == 2) {
            viewHolder.mNameView.setText("还剩" + (int)mRemain + "度电思密达！！！");
        }
        if (position == 3) {
            viewHolder.mNameView.setText("按昨日耗电量还有" + (int)mOnlyRemain + "天没电");
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.tv_buildingname);
        }

    }
}
