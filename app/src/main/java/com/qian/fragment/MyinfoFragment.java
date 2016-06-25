package com.qian.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by QHF on 2016/6/23.
 */
public class MyinfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());
        textView.setText("这是我的信息");

        return textView;
    }
}
