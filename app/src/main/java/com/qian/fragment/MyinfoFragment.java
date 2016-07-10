package com.qian.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.qian.R;
import com.qian.ui.PagerSlidingTabStrip;

import java.lang.reflect.Field;

/**
 * Created by QHF on 2016/6/23.
 */
public class MyinfoFragment extends Fragment {
    private static final String TAG = "MyinfoFragment";



    private NewOrderFragment mNewOrderFragment;

    private HistoryOrderFragment mHistoryOrderFragment;


    private PagerSlidingTabStrip tabs;

    private DisplayMetrics dm;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_order, null);

        setOverflowShowingAlways();
        dm = getResources().getDisplayMetrics();
        ViewPager pager = (ViewPager) view.findViewById(R.id.order_pager);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.order_tabs);
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();
        Log.e(TAG,"onCreateView");
        return view;
    }
    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#11cd6e"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#11cd6e"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = { "新订单", "历史订单"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (mNewOrderFragment == null) {
                        mNewOrderFragment = new NewOrderFragment();
                    }
                    return mNewOrderFragment;
                case 1:
                    if (mHistoryOrderFragment == null) {
                        mHistoryOrderFragment = new HistoryOrderFragment();
                    }
                    return mHistoryOrderFragment;

                default:

                    Log.e(TAG,"getItem");
                    return null;
            }
        }

    }


    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());//get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
