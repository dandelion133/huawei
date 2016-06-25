package com.qian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qian.R;

/**
 * Created by QHF on 2016/6/23.
 */


public class DishAdapter extends BaseAdapter {

    private Context mContext;
    private int i = 0;
    public DishAdapter(Context context) {
        mContext = context;

    }


    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        RecommendViewHolder holder = null;
        if(convertView == null) {
            holder = new RecommendViewHolder();
            view = View.inflate(mContext, R.layout.item_grid,null);
            holder.dishImage = (ImageView) view.findViewById(R.id.item_grid_image);
            holder.dishName = (TextView) view.findViewById(R.id.tv_explain);


            view.setTag(holder);
        } else {
            view = convertView;
            holder = (RecommendViewHolder) view.getTag();
        }

        return view;
    }

    public final class RecommendViewHolder {
        public ImageView dishImage;
        public TextView dishName;
    }
}
