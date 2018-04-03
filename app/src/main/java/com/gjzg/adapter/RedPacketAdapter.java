package com.gjzg.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.listener.IdPosClickHelp;

import com.gjzg.bean.RedPacketBean;

public class RedPacketAdapter extends BaseAdapter {

    private Context context;
    private List<RedPacketBean> list;
    private IdPosClickHelp idPosClickHelp;

    public RedPacketAdapter(Context context, List<RedPacketBean> list, IdPosClickHelp idPosClickHelp) {
        this.context = context;
        this.list = list;
        this.idPosClickHelp = idPosClickHelp;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_red_packet, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RedPacketBean redPacketBean = list.get(position);
        holder.amountTv.setText(redPacketBean.getB_amount() + "元");
        if (redPacketBean.getB_start_time().equals("0")) {
            holder.startTimeTv.setText("不限");
        } else {
            holder.startTimeTv.setText(redPacketBean.getB_start_time());
        }
        if (redPacketBean.getB_end_time().equals("0")) {
            holder.endTimeTv.setText("不限");
        } else {
            holder.endTimeTv.setText(redPacketBean.getB_end_time());
        }
        if (redPacketBean.getBd_use_time().equals("0")) {
            holder.statusTv.setText("立即领取");
            holder.statusTv.setEnabled(true);
        } else {
            holder.statusTv.setText("已领取");
            holder.statusTv.setEnabled(false);
        }
        final int pos = position;
        final int statusId = holder.statusTv.getId();
        holder.statusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPosClickHelp.onClick(statusId, pos);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private TextView amountTv, startTimeTv, endTimeTv, statusTv;

        public ViewHolder(View itemView) {
            amountTv = (TextView) itemView.findViewById(R.id.tv_item_red_packet_amount);
            startTimeTv = (TextView) itemView.findViewById(R.id.tv_item_red_packet_start_time);
            endTimeTv = (TextView) itemView.findViewById(R.id.tv_item_red_packet_end_time);
            statusTv = (TextView) itemView.findViewById(R.id.tv_item_red_packet_status);
        }
    }
}
