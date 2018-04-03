package com.gjzg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.TInfoTaskBean;
import com.gjzg.bean.TInfoWorkerBean;
import com.gjzg.config.ColorConfig;
import com.gjzg.listener.TInfoClickHelp;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.Utils;

public class TInfoTaskAdapter extends BaseAdapter implements TInfoClickHelp {

    private Context context;
    private TInfoTaskBean tInfoTaskBean;
    private TInfoClickHelp tInfoClickHelp;

    public TInfoTaskAdapter(Context context, TInfoTaskBean tInfoTaskBean, TInfoClickHelp tInfoClickHelp) {
        this.context = context;
        this.tInfoTaskBean = tInfoTaskBean;
        this.tInfoClickHelp = tInfoClickHelp;
    }

    @Override
    public int getCount() {
        return tInfoTaskBean.gettInfoWorkerBeanList() == null ? 0 : tInfoTaskBean.gettInfoWorkerBeanList().size();
    }

    @Override
    public Object getItem(int position) {
        return tInfoTaskBean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tinfotask, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TInfoWorkerBean tInfoWorkerBean = tInfoTaskBean.gettInfoWorkerBeanList().get(position);
        if (tInfoWorkerBean != null) {
            holder.skillTv.setText(tInfoWorkerBean.getSkill());
            holder.timeTv.setText(DataUtils.times(tInfoWorkerBean.getTew_start_time()) + "——" + DataUtils.times(tInfoWorkerBean.getTew_end_time()));
            boolean pay = true;
            for (int i = 0; i < tInfoWorkerBean.gettInfoOrderBeanList().size(); i++) {
                if (tInfoWorkerBean.gettInfoOrderBeanList().get(i).getO_pay().equals("0")) {
                    pay = false;
                }
            }
            if (pay) {
                holder.buttonTv.setText("已完工");
                holder.buttonTv.setEnabled(false);
                holder.buttonTv.setBackgroundColor(ColorConfig.gray_c4ced3);
            } else {
                holder.buttonTv.setText("确认完工");
                holder.buttonTv.setEnabled(true);
                holder.buttonTv.setBackgroundColor(ColorConfig.blue_2681fc);
            }
            holder.innerListView.setAdapter(new TInfoTaskInnerAdapter(context, tInfoWorkerBean.gettInfoOrderBeanList(), this));
            Utils.setListViewHeight(holder.innerListView);
            final int outerPos = position;
            final int buttonId = holder.buttonTv.getId();
            holder.buttonTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tInfoClickHelp.onClick(buttonId, outerPos, 0, "");
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView skillTv, timeTv, buttonTv;
        private ListView innerListView;

        public ViewHolder(View itemView) {
            skillTv = (TextView) itemView.findViewById(R.id.tv_item_tinfotask_skill);
            timeTv = (TextView) itemView.findViewById(R.id.tv_item_tinfotask_time);
            buttonTv = (TextView) itemView.findViewById(R.id.tv_item_tinfotask_button);
            innerListView = (ListView) itemView.findViewById(R.id.lv_item_tinfotask);
        }
    }

    @Override
    public void onClick(int id, int outerPos, int innerPos, String orderId) {
        switch (id) {
            case R.id.ll_item_tinfotask_inner:
                tInfoClickHelp.onClick(id, outerPos, innerPos, orderId);
                break;
            case R.id.iv_item_tinfotask_inner_mobile:
                tInfoClickHelp.onClick(id, outerPos, innerPos, orderId);
                break;
            case R.id.tv_item_tinfotask_inner_evaluate:
                tInfoClickHelp.onClick(id, outerPos, innerPos, orderId);
                break;
        }
    }
}
