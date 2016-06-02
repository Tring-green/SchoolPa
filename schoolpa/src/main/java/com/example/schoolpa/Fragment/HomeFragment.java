package com.example.schoolpa.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.schoolpa.domain.Account;
import com.example.schoolpa.ChatApplication;
import com.example.schoolpa.fragment.Chat.ChatFragment;
import com.example.schoolpa.fragment.Chat.ContactFragment;
import com.example.schoolpa.fragment.Chat.DiscoverFragment;
import com.example.schoolpa.fragment.Chat.MeFragment;
import com.example.schoolpa.R;
import com.example.schoolpa.receiver.PushReceiver;
import com.example.schoolpa.view.TabIndicatorView;
import com.example.schoolpa.db.MessageDao;

public class HomeFragment extends Fragment implements TabHost.OnTabChangeListener {


    private FragmentTabHost mTabHost;

    private View mView;

    private TabIndicatorView mChatIndicator;
    private TabIndicatorView mContactIndicator;
    private TabIndicatorView mDiscoverIndicator;
    private TabIndicatorView mMeIndicator;

    private static final String TAB_CHAT = "chat";
    private static final String TAB_CONTACT = "contact";
    private static final String TAB_DISCOVER = "discover";
    private static final String TAB_ME = "me";
    private Activity mActivity;

    public HomeFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    private PushReceiver pushReceiver = new PushReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadTabData();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        mActivity.registerReceiver(pushReceiver, filter);

        initTabHost();
        initIndicator();
        initTab();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTabData();
    }

    private void loadTabData() {
        MessageDao dao = new MessageDao(mActivity);
        Account currentAccount = ((ChatApplication) mActivity.getApplication())
                .getCurrentAccount();
        if (currentAccount != null)
            mChatIndicator.setUnread(dao.getAllUnread(currentAccount.getUserId()));
        else
            mChatIndicator.setUnread(0);
    }

    private void initTabHost() {
        mTabHost = (FragmentTabHost) mView.findViewById(android.R.id.tabhost);
        mTabHost.setup(mActivity, getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.setOnTabChangedListener(this);
    }


    private void initIndicator() {
        mChatIndicator = new TabIndicatorView(mActivity);
        mChatIndicator.setTabIcon(R.drawable.tab_icon_chat_normal,
                R.drawable.tab_icon_chat_focus);
        mChatIndicator.setTabHint(R.string.home_tab_chat);

        mContactIndicator = new TabIndicatorView(mActivity);
        mContactIndicator.setTabIcon(R.drawable.tab_icon_contact_normal,
                R.drawable.tab_icon_contact_focus);
        mContactIndicator.setTabHint(R.string.home_tab_contact);

        mDiscoverIndicator = new TabIndicatorView(mActivity);
        mDiscoverIndicator.setTabIcon(R.drawable.tab_icon_discover_normal,
                R.drawable.tab_icon_discover_focus);
        mDiscoverIndicator.setTabHint(R.string.home_tab_discover);

        mMeIndicator = new TabIndicatorView(mActivity);
        mMeIndicator.setTabIcon(R.drawable.tab_icon_me_normal,
                R.drawable.tab_icon_me_focus);
        mMeIndicator.setTabHint(R.string.home_tab_me);
    }

    private void initTab() {
        TabHost.TabSpec chatSpec = mTabHost.newTabSpec(TAB_CHAT);
        chatSpec.setIndicator(mChatIndicator);
        mTabHost.addTab(chatSpec, ChatFragment.class, null);

        TabHost.TabSpec contactSpec = mTabHost.newTabSpec(TAB_CONTACT);
        contactSpec.setIndicator(mContactIndicator);
        mTabHost.addTab(contactSpec, ContactFragment.class, null);

        TabHost.TabSpec discoverSpec = mTabHost.newTabSpec(TAB_DISCOVER);
        discoverSpec.setIndicator(mDiscoverIndicator);
        mTabHost.addTab(discoverSpec, DiscoverFragment.class, null);

        TabHost.TabSpec meSpec = mTabHost.newTabSpec(TAB_ME);
        meSpec.setIndicator(mMeIndicator);
        mTabHost.addTab(meSpec, MeFragment.class, null);

        setCurrentTabByTag(TAB_CHAT);
    }

    private void setCurrentTabByTag(String tag) {
        mChatIndicator.setCurrentFocus(false);
        mContactIndicator.setCurrentFocus(false);
        mDiscoverIndicator.setCurrentFocus(false);
        mMeIndicator.setCurrentFocus(false);

        mTabHost.setCurrentTabByTag(tag);
        if (TAB_CHAT.equals(tag)) {
            mChatIndicator.setCurrentFocus(true);
        } else if (TAB_CONTACT.equals(tag)) {
            mContactIndicator.setCurrentFocus(true);
        } else if (TAB_DISCOVER.equals(tag)) {
            mDiscoverIndicator.setCurrentFocus(true);
        } else if (TAB_ME.equals(tag)) {
            mMeIndicator.setCurrentFocus(true);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        System.out.println(tabId);
        setCurrentTabByTag(tabId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(pushReceiver);
    }
}
