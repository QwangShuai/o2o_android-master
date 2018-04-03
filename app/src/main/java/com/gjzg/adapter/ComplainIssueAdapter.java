package com.gjzg.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.ComplainIssueBean;

public class ComplainIssueAdapter extends BaseAdapter {

    private Context context;
    private List<ComplainIssueBean> list;

    public ComplainIssueAdapter(Context context, List<ComplainIssueBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_cpl_is, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ComplainIssueBean cplIs = list.get(position);
        if (cplIs != null) {
            holder.tv.setText(cplIs.getName());
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView tv;

        public ViewHolder(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.tv_item_pop_cpl_is);
        }
    }
}
