package com.example.schoolpa.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.schoolpa.R;
import com.example.schoolpa.View.TabIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements TabHost.OnTabChangeListener {

    private static final String TAB_CHAT = "chat";
    private static final String TAB_CONTACT = "contact";
    private static final String TAB_DISCOVER = "discover";
    private static final String TAB_ME = "me";

    private FragmentTabHost tabhost;

    private TabIndicatorView chatIndicator;
    private TabIndicatorView contactIndicator;
    private TabIndicatorView discoverIndicator;
    private TabIndicatorView meIndicator;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View mView;

    public ChatFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        mView = inflater.inflate(R.layout.fragment_chat, container, false);

        //1.初始化TabHost
        tabhost = (FragmentTabHost) mView.findViewById(android.R.id.tabhost);
        tabhost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        //2.新建TabSpec
        TabHost.TabSpec spec = tabhost.newTabSpec(TAB_CHAT);
        //        spec.setIndicator("消息");
        chatIndicator = new TabIndicatorView(getActivity());
        chatIndicator.setTabTitle("消息");
        chatIndicator.setTabIcon(R.drawable.tab_icon_chat_normal, R.drawable.tab_icon_chat_focus);

        spec.setIndicator(chatIndicator);

        //3.添加TabSpec
        tabhost.addTab(spec, BlankFragment.class, null);

        // 2. 新建TabSpec
        spec = tabhost.newTabSpec(TAB_CONTACT);
        contactIndicator = new TabIndicatorView(getActivity());
        contactIndicator.setTabIcon(R.drawable.tab_icon_contact_normal,
                R.drawable.tab_icon_contact_focus);
        contactIndicator.setTabTitle("通讯录");
        contactIndicator.setTabUnreadCount(10);
        spec.setIndicator(contactIndicator);
        // 3. 添加TabSpec
        tabhost.addTab(spec, BlankFragment.class, null);

        // 2. 新建TabSpec
        spec = tabhost.newTabSpec(TAB_DISCOVER);
        discoverIndicator = new TabIndicatorView(getActivity());
        discoverIndicator.setTabIcon(R.drawable.tab_icon_discover_normal,
                R.drawable.tab_icon_discover_focus);
        discoverIndicator.setTabTitle("发现");
        discoverIndicator.setTabUnreadCount(10);
        spec.setIndicator(discoverIndicator);
        // 3. 添加TabSpec
        tabhost.addTab(spec, BlankFragment.class, null);

        // 2. 新建TabSpec
        spec = tabhost.newTabSpec(TAB_ME);
        meIndicator = new TabIndicatorView(getActivity());
        meIndicator.setTabIcon(R.drawable.tab_icon_me_normal,
                R.drawable.tab_icon_me_focus);
        meIndicator.setTabTitle("我");
        meIndicator.setTabUnreadCount(10);
        spec.setIndicator(meIndicator);
        // 3. 添加TabSpec
        tabhost.addTab(spec, BlankFragment.class, null);

        //去掉分割线
        tabhost.getTabWidget().setDividerDrawable(android.R.color.white);

        tabhost.setCurrentTabByTag(TAB_CHAT);
        chatIndicator.setTabSelected(true);

        //监听tabhost的选中事件
        tabhost.setOnTabChangedListener(this);

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTabChanged(String tabId) {
        chatIndicator.setTabSelected(false);
        contactIndicator.setTabSelected(false);
        discoverIndicator.setTabSelected(false);
        meIndicator.setTabSelected(false);

        if (TAB_CHAT.equals(tabId)) {
            chatIndicator.setTabSelected(true);
        } else if (TAB_CONTACT.equals(tabId)) {
            contactIndicator.setTabSelected(true);
        } else if (TAB_DISCOVER.equals(tabId)) {
            discoverIndicator.setTabSelected(true);
        } else if (TAB_ME.equals(tabId)) {
            meIndicator.setTabSelected(true);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
