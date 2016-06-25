package com.qian.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qian.R;
import com.qian.adapter.DishAdapter;

/**
 * Created by QHF on 2016/6/23.
 */
public class OtherFoodFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.other_fragment,null);
        ListView listView = (ListView) view.findViewById(R.id.other_lv);
        DishAdapter adapter = new DishAdapter(getActivity());
        listView.setAdapter(adapter);



        return view;
    }
}
