package com.gjzg.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import editinfo.listener.AddSkillClickHelp;
import com.gjzg.bean.SkillsBean;

public class AddSkillAdapter extends BaseAdapter {

    private Context context;
    private List<SkillsBean> list;
    private AddSkillClickHelp addSkillClickHelp;

    public AddSkillAdapter(Context context, List<SkillsBean> list, AddSkillClickHelp addSkillClickHelp) {
        this.context = context;
        this.list = list;
        this.addSkillClickHelp = addSkillClickHelp;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_skill, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SkillsBean skillsBean = list.get(position);
        if (skillsBean != null) {
            String icon = skillsBean.getImg();
            if (icon == null || icon.equals("null") || TextUtils.isEmpty(icon)) {
            } else {
                Picasso.with(context).load(icon).into(holder.iconIv);
            }
            holder.nameTv.setText(skillsBean.getS_name());
            holder.checkCb.setChecked(skillsBean.isCheck());
        }
        final int checkId = holder.checkCb.getId();
        final int p = position;
        holder.checkCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addSkillClickHelp.onClick(checkId, p, isChecked);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private ImageView iconIv;
        private TextView nameTv;
        private CheckBox checkCb;

        public ViewHolder(View itemView) {
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_add_skill_icon);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_add_skill_name);
            checkCb = (CheckBox) itemView.findViewById(R.id.cb_item_add_kind_checked);
        }
    }
}
