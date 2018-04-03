package com.gjzg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.TalkEmployerWorkerBean;
import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.utils.DataUtils;

import java.util.List;


public class TalkEmployerAdapter extends BaseAdapter {

    private Context context;
    private List<TalkEmployerWorkerBean> list;
    private IdPosClickHelp idPosClickHelp;

    public TalkEmployerAdapter(Context context, List<TalkEmployerWorkerBean> list, IdPosClickHelp idPosClickHelp) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_talk_employer, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TalkEmployerWorkerBean talkEmployerWorkerBean = list.get(position);
        holder.skillTv.setText("招" + talkEmployerWorkerBean.getSkill() + talkEmployerWorkerBean.getAmount() + "人");
        holder.priceTv.setText("工资" + talkEmployerWorkerBean.getPrice() + "元/天");
        holder.timeTv.setText(DataUtils.times(talkEmployerWorkerBean.getStartTime()) + "-" + DataUtils.times(talkEmployerWorkerBean.getEndTime()));
        holder.selectRb.setChecked(talkEmployerWorkerBean.isSelect());
        final int p = position;
        final int selectId = holder.selectRb.getId();
        holder.selectRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPosClickHelp.onClick(selectId, p);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private TextView skillTv, priceTv, timeTv;
        private RadioButton selectRb;

        public ViewHolder(View itemView) {
            skillTv = (TextView) itemView.findViewById(R.id.tv_item_talk_employer_skill);
            priceTv = (TextView) itemView.findViewById(R.id.tv_item_talk_employer_price);
            timeTv = (TextView) itemView.findViewById(R.id.tv_item_talk_employer_time);
            selectRb = (RadioButton) itemView.findViewById(R.id.rb_item_talk_employer);
        }
    }
}
