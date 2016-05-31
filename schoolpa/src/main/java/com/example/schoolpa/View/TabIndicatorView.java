package com.example.schoolpa.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.schoolpa.R;


/**
 * Created by admin on 2016/5/22.
 */
public class TabIndicatorView extends RelativeLayout {
    private ImageView ivTabIcon;
    private TextView tvTabHint;
    private TextView tvTabUnRead;
    private int focusId = -1, normalId = -1;
    private int unreadCount;

    public TabIndicatorView(Context context) {
        this(context, null);
    }

    public TabIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //将布局文件和代码进行绑定
        View.inflate(context, R.layout.tab_indicator, this);

        ivTabIcon = (ImageView) findViewById(R.id.tab_indicator_icon);
        tvTabHint = (TextView) findViewById(R.id.tab_indicator_hint);
        tvTabUnRead = (TextView) findViewById(R.id.tab_indicator_unread);

        setTabUnreadCount(10);
    }



    public void setTabHint(int hintId) {
        tvTabHint.setText(hintId);
    }

    //初始化图标
    public void setTabIcon(int normalIconId, int focusIconId) {
        this.normalId = normalIconId;
        this.focusId = focusIconId;

        ivTabIcon.setImageResource(normalIconId);
    }

    //设置未读数
    public void setTabUnreadCount(int unreadCount) {
        if (unreadCount <= 0) {
            tvTabUnRead.setVisibility(View.GONE);
        } else {
            if (unreadCount <= 99) {
                tvTabUnRead.setText(unreadCount + "");
            } else {
                tvTabUnRead.setText("99+");
            }
            tvTabUnRead.setVisibility(View.VISIBLE);
        }
    }

    public void setUnread(int unreadCount) {
        this.unreadCount = unreadCount;

        if (unreadCount <= 0) {
            tvTabUnRead.setVisibility(View.GONE);
        } else {
            if (unreadCount >= 100) {
                tvTabUnRead.setText("99+");
            } else {
                tvTabUnRead.setText("" + unreadCount);
            }
            tvTabUnRead.setVisibility(View.VISIBLE);
        }
    }
    public void setCurrentFocus(boolean current) {
        if (current) {
            if (focusId != -1) {
                ivTabIcon.setImageResource(focusId);
            }
        } else {
            if (normalId != -1) {
                ivTabIcon.setImageResource(normalId);
            }
        }
    }


}
