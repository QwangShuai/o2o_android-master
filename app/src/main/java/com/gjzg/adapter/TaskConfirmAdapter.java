package com.gjzg.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;

import com.gjzg.bean.PublishBean;
import com.gjzg.bean.PublishWorkerBean;

public class TaskConfirmAdapter extends BaseAdapter {

    private Context context;
    private PublishBean publishBean;

    public TaskConfirmAdapter(Context context, PublishBean publishBean) {
        this.context = context;
        this.publishBean = publishBean;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return 1 + publishBean.getPublishWorkerBeanList().size();
    }

    @Override
    public Object getItem(int position) {
        return publishBean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder0 holder0;
        ViewHolder1 holder1;
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_task_confirm_title, null);
                    holder0 = new ViewHolder0(convertView);
                    convertView.setTag(holder0);
                } else {
                    holder0 = (ViewHolder0) convertView.getTag();
                }
                holder0.titleTv.setText(publishBean.getTitle());
                holder0.descriptionTv.setText(publishBean.getInfo());
                holder0.typeTv.setText(publishBean.getType());
                holder0.areaTv.setText(publishBean.getArea());
                holder0.addressTv.setText(publishBean.getAddress());
                break;
            case 1:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_task_confirm_kind, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                PublishWorkerBean publishWorkerBean = publishBean.getPublishWorkerBeanList().get(position - 1);
                holder1.kindTv.setText(publishWorkerBean.getKind());
                holder1.amountTv.setText(publishWorkerBean.getAmount() + "人");
                holder1.salaryTv.setText(publishWorkerBean.getSalary() + "元/人/天");
                holder1.durationTv.setText(publishWorkerBean.getStartTime() + "-" + publishWorkerBean.getEndTime());
                break;
        }
        return convertView;
    }

    private class ViewHolder0 {

        private TextView titleTv, descriptionTv, typeTv, areaTv, addressTv;

        public ViewHolder0(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_title_title);
            descriptionTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_title_description);
            typeTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_title_type);
            areaTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_title_area);
            addressTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_title_address);
        }
    }

    private class ViewHolder1 {

        private TextView kindTv, amountTv, salaryTv, durationTv;

        public ViewHolder1(View itemView) {
            kindTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_kind_kind);
            amountTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_kind_amount);
            salaryTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_kind_salary);
            durationTv = (TextView) itemView.findViewById(R.id.tv_item_task_confirm_kind_duration);
        }
    }
}
