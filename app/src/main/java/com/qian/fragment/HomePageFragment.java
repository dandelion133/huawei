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
public class HomePageFragment extends Fragment {

    private static final String TAG = "HomePageFragment";
    private RecommendFoodFragment mRecommendFoodFragment;

    private MeatFoodFragment mMeatFoodFragment;

    private VegetableFoodFragment mVegetableFoodFragment;
    private SoupFragment mSoupFragment;

    private ColdFoodFragment mColdFoodFragment;

    private WineFoodFragment mWineFoodFragment ;
    private OtherFoodFragment mOtherFoodFragment;



    private PagerSlidingTabStrip tabs;

    private DisplayMetrics dm;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.activity_home_page, null);

        setOverflowShowingAlways();
        dm = getResources().getDisplayMetrics();
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
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

        private final String[] titles = { "推荐", "荤菜", "素菜","汤菜","凉菜","酒水","其他" };

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
                    if (mRecommendFoodFragment == null) {
                        mRecommendFoodFragment = new RecommendFoodFragment();
                    }
                    return mRecommendFoodFragment;
                case 1:
                    if (mMeatFoodFragment == null) {
                        mMeatFoodFragment = new MeatFoodFragment();
                    }
                    return mMeatFoodFragment;
                case 2:
                    if (mVegetableFoodFragment == null) {
                        mVegetableFoodFragment = new VegetableFoodFragment();
                    }
                    return mVegetableFoodFragment;
                case 3:
                    if (mSoupFragment == null) {
                        mSoupFragment = new SoupFragment();
                    }
                    return mSoupFragment;
                case 4:
                    if (mColdFoodFragment == null) {
                        mColdFoodFragment = new ColdFoodFragment();
                    }
                    return mColdFoodFragment;
                case 5:
                    if (mWineFoodFragment == null) {
                        mWineFoodFragment = new WineFoodFragment();
                    }
                    return mWineFoodFragment;
                case 6:
                    if (mOtherFoodFragment == null) {
                        mOtherFoodFragment = new OtherFoodFragment();
                    }
                    return mOtherFoodFragment;
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
