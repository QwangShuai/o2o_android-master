package com.gjzg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.VoucherBean;

public class VoucherAdapter extends BaseAdapter {

    private Context context;
    private List<VoucherBean> list;

    public VoucherAdapter(Context context, List<VoucherBean> list) {
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_voucher, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VoucherBean voucherBean = list.get(position);
        holder.amountTv.setText(voucherBean.getAmount() + "元");
        holder.infoTv.setText(voucherBean.getInfo());
        holder.startTimeTv.setText(voucherBean.getStartTime());
        holder.endTimeTv.setText(voucherBean.getEndTime());
        return convertView;
    }

    private class ViewHolder {

        private TextView amountTv, infoTv, startTimeTv, endTimeTv, statusTv;

        public ViewHolder(View itemView) {
            amountTv = (TextView) itemView.findViewById(R.id.tv_item_voucher_amount);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_voucher_info);
            startTimeTv = (TextView) itemView.findViewById(R.id.tv_item_voucher_start_time);
            endTimeTv = (TextView) itemView.findViewById(R.id.tv_item_voucher_end_time);
            statusTv = (TextView) itemView.findViewById(R.id.tv_item_voucher_status);
        }
    }
}
