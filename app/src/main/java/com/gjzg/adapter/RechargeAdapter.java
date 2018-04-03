package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.PayWayBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RechargeAdapter extends CommonAdapter<PayWayBean> {

    public RechargeAdapter(Context context, List<PayWayBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pay_way, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayWayBean payWayBean = list.get(position);
        if (payWayBean != null) {
            String img = payWayBean.getImg();
            if (img == null || img.equals("null") || TextUtils.isEmpty(img)) {
            } else {
                Picasso.with(context).load(img).into(holder.iconIv);
            }
            String name = payWayBean.getP_name();
            if (name == null || name.equals("null") || TextUtils.isEmpty(name)) {
            } else {
                holder.nameTv.setText(name);
            }
            boolean check = payWayBean.isCheck();
            if (check) {
                holder.checkIv.setImageResource(R.mipmap.pay_choosed);
            } else {
                holder.checkIv.setImageResource(R.mipmap.point_gray);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView iconIv, checkIv;
        private TextView nameTv;

        public ViewHolder(View itemView) {
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_pay_way_icon);
            checkIv = (ImageView) itemView.findViewById(R.id.iv_item_pay_way_check);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_pay_way_name);
        }
    }
}
