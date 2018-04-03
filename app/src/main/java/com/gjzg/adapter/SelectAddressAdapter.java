package com.gjzg.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.SelectAddressBean;

public class SelectAddressAdapter extends BaseAdapter {

    private Context context;
    private List<SelectAddressBean> list;

    public SelectAddressAdapter(Context context, List<SelectAddressBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_address, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SelectAddressBean selectAddressBean = list.get(position);
        if (selectAddressBean != null) {
            holder.nameTv.setText(selectAddressBean.getName());
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView nameTv;

        public ViewHolder(View itemView) {
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_select_address_name);
        }
    }
}
