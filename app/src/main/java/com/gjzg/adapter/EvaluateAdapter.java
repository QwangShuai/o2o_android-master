package com.gjzg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.gjzg.bean.EvaluateBean;
import com.gjzg.utils.DataUtils;
import com.gjzg.custom.CImageView;

public class EvaluateAdapter extends BaseAdapter {

    private Context context;
    private List<EvaluateBean> list;

    public EvaluateAdapter(Context context, List<EvaluateBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_evaluate, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EvaluateBean evaluateBean = list.get(position);
        Picasso.with(context).load(evaluateBean.getIcon()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(holder.iconIv);
        holder.infoTv.setText(evaluateBean.getInfo());
        holder.timeTv.setText(DataUtils.times(evaluateBean.getTime()));
        return convertView;
    }

    private class ViewHolder {

        private CImageView iconIv;
        private TextView infoTv, timeTv;

        public ViewHolder(View itemView) {
            iconIv = (CImageView) itemView.findViewById(R.id.iv_item_evaluate_icon);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_evaluate_info);
            timeTv = (TextView) itemView.findViewById(R.id.tv_item_evaluate_time);
        }
    }
}
