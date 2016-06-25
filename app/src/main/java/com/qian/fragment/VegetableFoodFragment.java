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
public class VegetableFoodFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.vegetable_fragment,null);
        GridView gridView = (GridView) view.findViewById(R.id.vegetable_lv);
        DishAdapter adapter = new DishAdapter(getActivity());
        gridView.setAdapter(adapter);



        return view;
    }
}
