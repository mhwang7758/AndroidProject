package com.jinuo.mhwang.listviewcantoushdelete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/6/17
 * Version : V1.0
 */

public class ShowRecipesAdapter extends BaseAdapter {

    private List<String> mStrings;
    private LayoutInflater mLayoutInflater;
    public ShowRecipesAdapter(Context context,List<String> strings){
        mStrings = strings;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mStrings.size();
    }

    @Override
    public Object getItem(int position) {
        return mStrings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = mStrings.get(position);
        ViewHolder holder;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item_listview,null,false);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(name);
        return convertView;
    }

    private class ViewHolder{
        TextView mTextView;
    }
}
