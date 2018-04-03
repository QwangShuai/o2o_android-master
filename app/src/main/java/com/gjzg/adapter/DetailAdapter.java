package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.DetailBean;
import com.gjzg.utils.DataUtils;

public class DetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<DetailBean.DataBeanX.DataBean> mList;

    public DetailAdapter(Context mContext, List<DetailBean.DataBeanX.DataBean> mList) {
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
        ViewHoler holer;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_detail, null);
            holer = new ViewHoler(convertView);
            convertView.setTag(holer);
        } else {
            holer = (ViewHoler) convertView.getTag();
        }
        DetailBean.DataBeanX.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            String type = dataBean.getType();
            if (!TextUtils.isEmpty(type)) {
                if (!TextUtils.isEmpty(dataBean.getAmount()))
                    holer.desTv.setText(dataBean.getAmount());
                if (type.equals("recharge")) {
                    holer.titleTv.setText("充值");
                } else if (type.equals("withdraw")) {
                    holer.titleTv.setText("提现");
                } else if (type.equals("income")) {
                    holer.titleTv.setText("收入");
                } else if (type.equals("pay")) {
                    holer.titleTv.setText("支出");
                }
                if (!TextUtils.isEmpty(dataBean.getTime()))
                    holer.timeTv.setText(DataUtils.msgTimes(dataBean.getTime()));
            }
        }
        return convertView;
    }

    private class ViewHoler {

        private TextView titleTv, timeTv, desTv;

        public ViewHoler(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_detail_title);
            timeTv = (TextView) itemView.findViewById(R.id.tv_item_detail_time);
            desTv = (TextView) itemView.findViewById(R.id.tv_item_detail_des);
        }
    }
}
