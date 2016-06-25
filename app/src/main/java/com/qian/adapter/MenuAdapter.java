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


public class MenuAdapter extends BaseAdapter {

    private Context mContext;
    private int i = 0;
    public MenuAdapter(Context context) {
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

        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_menu,null);
            holder.dishImage = (ImageView) view.findViewById(R.id.menu_image);
            holder.dishName = (TextView) view.findViewById(R.id.menu_name);
           // holder.dishDesc = (TextView) view.findViewById(R.id.menu_desc);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

       // holder.dishName.setText("菜名" + (i ++));


        return view;
    }

    public final class ViewHolder {
        public ImageView dishImage;
        public TextView dishName;
        public TextView dishDesc;
    }
}
