package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.Skill;

public class SkillAdapter extends BaseAdapter {

    private Context mContext;
    private List<Skill.DataBean> mList;

    public SkillAdapter(Context mContext, List<Skill.DataBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_skills, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Skill.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getS_name()))
                holder.nameTv.setText(dataBean.getS_name());
            if (!TextUtils.isEmpty(dataBean.getImg()))
                Glide.with(mContext).load(dataBean.getImg()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.iconIv);
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView iconIv;
        private TextView nameTv;

        public ViewHolder(View itemView) {
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_skills_icon);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_skills_name);
        }
    }
}
