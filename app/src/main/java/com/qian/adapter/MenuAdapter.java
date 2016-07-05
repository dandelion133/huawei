package com.qian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qian.R;
import com.qian.entity.Dish;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/6/23.
 */


public class MenuAdapter extends BaseAdapter {

    private Context mContext;
    private int i = 0;
    private ArrayList<Dish> mDishs = new ArrayList<>();
    public MenuAdapter(Context context) {
        mContext = context;

    }
    public MenuAdapter(Context context,ArrayList<Dish> dishs) {
        this.mContext = context;
        this.mDishs = dishs;
    }
    @Override
    public int getCount() {
        return mDishs.size();
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
            holder.dec = (TextView) view.findViewById(R.id.dec);
            holder.add = (TextView) view.findViewById(R.id.add);
            holder.delete = (ImageView) view.findViewById(R.id.delete);
            holder.dishNum = (TextView) view.findViewById(R.id.dish_num);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.dishName.setText(mDishs.get(position).getName());
        holder.dishImage.setImageResource(mDishs.get(position).getImage());

        //final int num =Integer.valueOf(holder.dishNum.getText().toString()) ;
        final ViewHolder finalHolder = holder;
        holder.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num =Integer.valueOf(finalHolder.dishNum.getText().toString()) ;
                if(num > 1) {
                    int num1 = num - 1;
                    finalHolder.dishNum.setText(num1 + "");
                }
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num =Integer.valueOf(finalHolder.dishNum.getText().toString()) ;
                int num1 = num + 1;
                finalHolder.dishNum.setText(num1 + "");
            }
        });
        //holder.delete.set
        return view;
    }

    public final class ViewHolder {
        public ImageView dishImage;
        public TextView dishName;
        public TextView dishDesc;
        public TextView dec;
        public TextView add;
        public ImageView delete;
        public TextView dishNum;
    }
}
