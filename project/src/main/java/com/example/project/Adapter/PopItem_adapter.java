package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project.R;

import java.util.ArrayList;

public class PopItem_adapter extends BaseAdapter {
    private Context context;
    private ArrayList list;

    public PopItem_adapter(Context context,ArrayList list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_pop, null);
            vh.textview = (TextView) convertView.findViewById(R.id.popItem);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textview.setText(list.get(position) + "");
        return convertView;
    }
    static class ViewHolder {
        TextView textview;
    }
}
