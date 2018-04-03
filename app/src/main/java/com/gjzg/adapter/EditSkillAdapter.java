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

public class EditSkillAdapter extends BaseAdapter {

    private Context context;
    private List<SkillsBean> list;

    public EditSkillAdapter(Context context, List<SkillsBean> list) {
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
        ViewHoler holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_edit_skill, null);
            holder = new ViewHoler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHoler) convertView.getTag();
        }
        SkillsBean skillsBean = list.get(position);
        if (skillsBean != null) {
            holder.textView.setText(skillsBean.getS_name());
        }
        return convertView;
    }

    private class ViewHoler {

        private TextView textView;

        public ViewHoler(View itemView) {
            textView = (TextView) itemView.findViewById(R.id.tv_item_edit_skill);
        }
    }
}
