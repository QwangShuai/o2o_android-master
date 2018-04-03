package com.gjzg.adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.PublishWorkerBean;
import publishjob.listener.PublishJobClickHelp;

public class PublishKindAdapter extends BaseAdapter {

    private Context context;
    private List<PublishWorkerBean> list;
    private PublishJobClickHelp publishJobClickHelp;

    public PublishKindAdapter(Context context, List<PublishWorkerBean> list, PublishJobClickHelp publishJobClickHelp) {
        this.context = context;
        this.list = list;
        this.publishJobClickHelp = publishJobClickHelp;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_publish_job, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PublishWorkerBean publishWorkerBean = list.get(position);
        if (TextUtils.isEmpty(publishWorkerBean.getKind())) {
            holder.kindTv.setText("选择招聘工种");
            holder.hintTv.setText("(点击此处选择招聘工种)");
        } else {
            holder.kindTv.setText(publishWorkerBean.getKind());
            holder.hintTv.setText("(点击此处更改招聘工种)");
        }
        if (TextUtils.isEmpty(publishWorkerBean.getStartTime())) {
            holder.startTimeTv.setText("开始时间");
        } else {
            holder.startTimeTv.setText(publishWorkerBean.getStartTime());
        }
        if (TextUtils.isEmpty(publishWorkerBean.getEndTime())) {
            holder.endTimeTv.setText("结束时间");
        } else {
            holder.endTimeTv.setText(publishWorkerBean.getEndTime());
        }
        if (position == 0) {
            holder.deleteRl.setVisibility(View.GONE);
        } else {
            holder.deleteRl.setVisibility(View.VISIBLE);
        }
        final int viewPosition = position;
        final int kindId = holder.kindTv.getId();
        final int startTimeId = holder.startTimeTv.getId();
        final int endTimeId = holder.endTimeTv.getId();
        final int deleteId = holder.deleteRl.getId();
        holder.kindRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishJobClickHelp.onClick(kindId, viewPosition);
            }
        });
        holder.startTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishJobClickHelp.onClick(startTimeId, viewPosition);
            }
        });
        holder.endTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishJobClickHelp.onClick(endTimeId, viewPosition);
            }
        });
        holder.deleteRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishJobClickHelp.onClick(deleteId, viewPosition);
            }
        });
        if (holder.amountEt.getTag() instanceof TextWatcher) {
            holder.amountEt.removeTextChangedListener((TextWatcher) holder.amountEt.getTag());
        }
        holder.amountEt.setText(publishWorkerBean.getAmount());
        TextWatcher amountTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                publishWorkerBean.setAmount(s.toString());
            }
        };
        holder.amountEt.addTextChangedListener(amountTw);
        holder.amountEt.setTag(amountTw);
        if (holder.salaryEt.getTag() instanceof TextWatcher) {
            holder.salaryEt.removeTextChangedListener((TextWatcher) holder.salaryEt.getTag());
        }
        holder.salaryEt.setText(publishWorkerBean.getSalary());
        TextWatcher salaryTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                publishWorkerBean.setSalary(s.toString());
            }
        };
        holder.salaryEt.addTextChangedListener(salaryTw);
        holder.salaryEt.setTag(salaryTw);
        return convertView;
    }

    private class ViewHolder {

        private TextView kindTv,startTimeTv, endTimeTv,hintTv;
        private EditText amountEt, salaryEt;
        private RelativeLayout deleteRl,kindRl;

        public ViewHolder(View itemView) {
            hintTv = (TextView) itemView.findViewById(R.id.tv_item_publish_job_kind_hint);
            kindTv = (TextView) itemView.findViewById(R.id.tv_item_publish_job_kind);
            startTimeTv = (TextView) itemView.findViewById(R.id.tv_item_publish_job_start_time);
            endTimeTv = (TextView) itemView.findViewById(R.id.tv_item_publish_job_end_time);
            amountEt = (EditText) itemView.findViewById(R.id.et_item_publish_job_amount);
            salaryEt = (EditText) itemView.findViewById(R.id.et_item_publish_job_salary);
            deleteRl = (RelativeLayout) itemView.findViewById(R.id.rl_item_publish_job_delete);
            kindRl = (RelativeLayout) itemView.findViewById(R.id.rl_item_publish_job_kind);
        }
    }
}
