package com.qian.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.qian.R;
import com.qian.adapter.DishAdapter;

/**
 * Created by QHF on 2016/6/23.
 */
public class RecommendFoodFragment extends Fragment {


    private static final String TAG = "RecommendFoodFragment";
    private DishAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.recommend_fragment, null);
        GridView listView = (GridView) view.findViewById(R.id.recommend_lv);
        mAdapter = new DishAdapter(getActivity());
        listView.setAdapter(mAdapter);

      //  Log.e(TAG,"onCreateView");
        return view;
    }

}
