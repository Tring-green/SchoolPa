package com.example.schoolpa.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.schoolpa.R;

/**
 * Created by admin on 2016/2/20.
 */
public class RefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;

    private View mListViewFooter;

    private RecyclerView mRecyclerView;

    private int mYDown;

    private int mLastY;

    private boolean isLoading = false;


    public RefreshLayout(Context context) {
        this(context, null);
    }


    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mListViewFooter = LayoutInflater.from(context).inflate(R.layout
                        .fragment_official_document_item_loading_more, null,
                false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mRecyclerView == null) {
            getRecyclerView();
        }
    }

    public void getRecyclerView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) childView;
                mRecyclerView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean loadData() {
        return false;
    }

    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    private boolean isBottom() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {

        }
        return false;
    }

    private boolean isPullUp() {
        return false;
    }


}
