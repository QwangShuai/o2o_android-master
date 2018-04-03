package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.PayWayBean;

import java.util.List;


public class WithDrawAdapter extends BaseAdapter {

    private Context context;
    private List<PayWayBean> list;

    public WithDrawAdapter(Context context, List<PayWayBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_screen_dialog, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayWayBean payWayBean = list.get(position);
        if (payWayBean != null) {
            String name = payWayBean.getP_name();
            if (name == null || name.equals("null") || TextUtils.isEmpty(name)) {
            } else {
                holder.textView.setText(name);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView textView;

        public ViewHolder(View itemView) {
            textView = (TextView) itemView.findViewById(R.id.tv_item_screen_dialog);
        }
    }
}
