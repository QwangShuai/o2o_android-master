package com.gjzg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.SkillsBean;

import java.util.List;


public class SelectSkillAdapter extends BaseAdapter {

    private Context context;
    private List<SkillsBean> list;

    public SelectSkillAdapter(Context context, List<SkillsBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_screen_dialog, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.skillTv.setText(list.get(position).getS_name());
        return convertView;
    }

    private class ViewHolder {

        private TextView skillTv;

        public ViewHolder(View itemView) {
            skillTv = (TextView) itemView.findViewById(R.id.tv_item_screen_dialog);
        }
    }
}
