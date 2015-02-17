package com.farbox.wml.withy.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farbox.wml.withy.R;
import com.farbox.wml.withy.bean.Thing;

import java.util.List;

/**
 * 所有事情的 adapter
 * Created by wml on 14/11/27 15:38.
 */
public class ThingsLvAdapter extends BaseAdapter {

    private Context context;
    public List<Thing> thingList;

    public ThingsLvAdapter(Context context, List<Thing> thingList) {
        this.context = context;
        this.thingList = thingList;
    }

    @Override
    public int getCount() {
        return thingList == null ? 0 : thingList.size();
    }

    @Override
    public Thing getItem(int position) {
        if (thingList != null && thingList.size() != 0) {
            return thingList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (getItem(position) != null) {
            return getItem(position).getId();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_things_item, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.thingName = (TextView) convertView.findViewById(android.R.id.text1);


        /*if (position % 2 == 0) { // listview 交叉显示不同的两种颜色
            convertView.setBackgroundResource(R.drawable.listitembgcolor1);//这个很关键，不能用contentView.setBackgroundColor(color)
        } else if (position % 2 == 1) {
            convertView.setBackgroundResource(R.drawable.listitembgcolor2);
        }*/

        Thing thing = getItem(position);
        String strNum = "";
        if (thing.getId() < 10) {
            strNum = "0" + (thing.getId());
        } else {
            strNum = "" + (thing.getId());
        }
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/american_typewriter.ttf");//读取字体
        holder.thingName.setTypeface(tf);//设置字体
        holder.thingName.setText(strNum + " " + thing.getName());
        return convertView;

    }

    private static class ViewHolder {
        TextView thingName;
    }


}
