package com.qian.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qian.R;
import com.qian.adapter.MenuAdapter;

/**
 * Created by QHF on 2016/6/23.
 */
public class MenuFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.menu_fragment,null);
        ListView listView = (ListView) view.findViewById(R.id.menu_lv);
        MenuAdapter adapter = new MenuAdapter(getActivity());
        listView.setAdapter(adapter);



        return view;
    }
}
