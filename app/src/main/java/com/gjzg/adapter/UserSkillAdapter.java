package com.gjzg.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.SkillsBean;

public class UserSkillAdapter extends BaseAdapter {

    private Context context;
    private List<SkillsBean> list;

    public UserSkillAdapter(Context context, List<SkillsBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_skill, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SkillsBean skillsBean = list.get(position);
        holder.tv.setText(skillsBean.getS_name());
        return convertView;
    }

    private class ViewHolder {

        private TextView tv;

        public ViewHolder(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.tv_item_user_skill);
        }
    }
}
