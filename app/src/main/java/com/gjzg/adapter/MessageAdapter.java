package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.OfferBean;

import com.gjzg.utils.DataUtils;

public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private List<OfferBean.DataBeanX.DataBean> mList;

    public MessageAdapter(Context mContext, List<OfferBean.DataBeanX.DataBean> mList) {
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_msg, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OfferBean.DataBeanX.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getUm_status())) {
                if (dataBean.getUm_status().equals("0")) {
                    holder.pointTipIv.setVisibility(View.VISIBLE);
                } else {
                    holder.pointTipIv.setVisibility(View.INVISIBLE);
                }
            }
            if (!TextUtils.isEmpty(dataBean.getWm_title())) {
                holder.titleTv.setText(dataBean.getWm_title());
            }
            if (!TextUtils.isEmpty(dataBean.getUm_in_time())) {
                holder.dateTv.setText(DataUtils.times(dataBean.getUm_in_time()));
            }
            if (!TextUtils.isEmpty(dataBean.getWm_desc())) {
                holder.contentTv.setText(dataBean.getWm_desc());
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView pointTipIv;
        private TextView titleTv, dateTv, contentTv;

        public ViewHolder(View itemView) {
            pointTipIv = (ImageView) itemView.findViewById(R.id.iv_item_msg_point_tip);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_msg_title);
            dateTv = (TextView) itemView.findViewById(R.id.tv_item_msg_date);
            contentTv = (TextView) itemView.findViewById(R.id.tv_item_msg_content);
        }
    }
}
