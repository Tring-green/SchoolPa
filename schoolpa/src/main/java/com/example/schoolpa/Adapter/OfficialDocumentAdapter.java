package com.example.schoolpa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.schoolpa.activity.DetailDocumentActivity;
import com.example.schoolpa.activity.SplashActivity;
import com.example.schoolpa.base.BaseViewHolder;
import com.example.schoolpa.base.DefaultAdapter;
import com.example.schoolpa.domain.WebBean;
import com.example.schoolpa.fragment.Data.OfficialDocumentData;
import com.example.schoolpa.fragment.Data.OfficialDocumentData.OfficialDocumentItem;
import com.example.schoolpa.fragment.OfficialDocumentFragment.OnListFragmentInteractionListener;
import com.example.schoolpa.R;
import com.example.schoolpa.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OfficialDocumentItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OfficialDocumentAdapter extends DefaultAdapter {


    private CoordinatorLayout coordinatorLayout;
    private static final int COMMONVIEW = 0;
    private static final int MOREVIEW = 1;
    private final List<OfficialDocumentItem> mValues;
    private final List<String> mUrls;
    private List<WebBean> webDetails = HttpUtils.getWebDetails();
    private final OnListFragmentInteractionListener mListener;
    private final Activity mActivity;
    private View view;

    private boolean noValues = false;


    public OfficialDocumentAdapter(Context context, List<OfficialDocumentItem> items,
                                   OnListFragmentInteractionListener
                                           listener, View view) {
        hasMore = false;
        mActivity = (Activity) context;
        mValues = items;
        mUrls = SplashActivity.getUrllist();
        mListener = listener;
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.container);
    }

    public void addMore() {
        List<OfficialDocumentItem> newValues = new ArrayList<OfficialDocumentItem>();
        if (!noValues) {
            for (int i = 0; i < 5; i++) {
                System.out.println("-----------------");
                System.out.println("i:" + i + "----i+mValues.size():" + (i + mValues.size()) + "----" + "webDetails" +
                        ".size():" +
                        webDetails.size());
                if (i + mValues.size() < webDetails.size()) {
                    OfficialDocumentItem officialDocumentItem = OfficialDocumentData.createDummyItem(i + mValues.size
                            ());
                    if (officialDocumentItem != null)
                        newValues.add(officialDocumentItem);
                    //                        mValues.add(officialDocumentItem);
                } else {
                    noValues = true;
                    break;
                }
            }
        }
        mValues.addAll(newValues);
    }

    public void refresh() {
        System.out.println(mValues.size() + "--------" + webDetails.size());
        List<OfficialDocumentItem> refresh = new ArrayList<>();
        for (int i = 1; i + mValues.size() < webDetails.size(); i++) {
            OfficialDocumentItem officialDocumentItem = OfficialDocumentData.createDummyItem(i +
                    mValues.size());
            if (officialDocumentItem != null)
                mValues.add(officialDocumentItem);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == mValues.size())
            return MOREVIEW;
        else
            return COMMONVIEW;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        switch (viewType) {
            case COMMONVIEW:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_official_document_item_common, parent, false);
                holder = new CommonViewHolder(view);
                break;
            case MOREVIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .fragment_official_document_item_loading_more, parent, false);
                holder = new MoreViewHolder(view);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (holder instanceof CommonViewHolder) {
            final CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
            commonViewHolder.mItem = mValues.get(position);
            commonViewHolder.mUnitView.setText(mValues.get(position).unit);
            commonViewHolder.mTitleView.setText(mValues.get(position).title);
            commonViewHolder.mContentView.setText(mValues.get(position).content);

            commonViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DetailDocumentActivity.class);
                    //                intent.putExtra("currentId", mUrls.get(position));
                    intent.putExtra("currentPosition", position);
                    mActivity.startActivity(intent);
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(commonViewHolder.mItem);
                    }
                }
            });
        } else if (holder instanceof MoreViewHolder) {
            final MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            if (!noValues) {
                moreViewHolder.mLoadError.setVisibility(View.INVISIBLE);
                moreViewHolder.mLoadMore.setVisibility(View.VISIBLE);
                moreViewHolder.mLoadNoValue.setVisibility(View.INVISIBLE);
            } else {
                moreViewHolder.mLoadMore.setVisibility(View.INVISIBLE);
                moreViewHolder.mLoadNoValue.setVisibility(View.VISIBLE);

                Snackbar.make(coordinatorLayout, "没有更多数据了", Snackbar.LENGTH_SHORT);
            }

        }
    }


    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    @Override
    public void loadMore() {

    }

    public class CommonViewHolder extends BaseViewHolder {
        public final View mView;
        public final TextView mUnitView;
        public final TextView mTitleView;
        public final TextView mContentView;
        public OfficialDocumentItem mItem;

        public CommonViewHolder(View view) {
            super(view);
            mView = view;
            mUnitView = (TextView) view.findViewById(R.id.unit);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.content);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public class MoreViewHolder extends BaseViewHolder {
        public final View mView;
        public final RelativeLayout mLoadMore;
        public final RelativeLayout mLoadError;
        public final RelativeLayout mLoadNoValue;

        public MoreViewHolder(View view) {
            super(view);
            mView = view;
            mLoadMore = (RelativeLayout) view.findViewById(R.id.rl_load_more);
            mLoadError = (RelativeLayout) view.findViewById(R.id.rl_load_error);
            mLoadNoValue = (RelativeLayout) view.findViewById(R.id.rl_load_no_value);

        }


    }

    OnLoadMoreListener loadMoreListener;

    public interface OnLoadMoreListener {
        void LoadMore(int currentPosition);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        loadMoreListener = listener;
    }
}
