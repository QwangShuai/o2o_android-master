package com.gjzg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.EmployerToTalkBean;
import com.gjzg.custom.CImageView;
import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.utils.DataUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployerToTalkAdapter extends BaseAdapter {

    private Context context;
    private List<EmployerToTalkBean> list;
    private IdPosClickHelp idPosClickHelp;

    public EmployerToTalkAdapter(Context context, List<EmployerToTalkBean> list, IdPosClickHelp idPosClickHelp) {
        this.context = context;
        this.list = list;
        this.idPosClickHelp = idPosClickHelp;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        ViewHolder0 holder0;
        ViewHolder1 holder1;
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_employer_to_talk_type_0, null);
                    holder0 = new ViewHolder0(convertView);
                    convertView.setTag(holder0);
                } else {
                    holder0 = (ViewHolder0) convertView.getTag();
                }
                EmployerToTalkBean employerToTalkBean0 = list.get(position);
                holder0.skillNameTv.setText(employerToTalkBean0.getSkillName());
                holder0.timeTv.setText(DataUtils.times(employerToTalkBean0.getStartTime()) + "-" + DataUtils.times(employerToTalkBean0.getEndTime()));
                break;
            case 1:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_employer_to_talk_type_1, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                EmployerToTalkBean employerToTalkBean1 = list.get(position);
                Picasso.with(context).load(employerToTalkBean1.getWorkerIcon()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(holder1.workerIconIv);
                String sex = employerToTalkBean1.getWorkerSex();
                if (sex.equals("0")) {
                    holder1.workerSexIv.setImageResource(R.mipmap.female);
                } else if (sex.equals("1")) {
                    holder1.workerSexIv.setImageResource(R.mipmap.male);
                }
                String status = employerToTalkBean1.getWorkerStatus();
                if (status.equals("0")) {
                    holder1.workerStatusIv.setImageResource(R.mipmap.worker_leisure);
                } else if (status.equals("1")) {
                    holder1.workerStatusIv.setImageResource(R.mipmap.worker_mid);
                }
                holder1.workerNameTv.setText(employerToTalkBean1.getWorkerName());
                holder1.skillNameTv.setText(employerToTalkBean1.getSkillName());
                final int p = position;
                final int llId = holder1.ll.getId();
                final int phoneId = holder1.workerPhoneIv.getId();
                holder1.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idPosClickHelp.onClick(llId, p);
                    }
                });
                holder1.workerPhoneIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idPosClickHelp.onClick(phoneId, p);
                    }
                });
                break;
        }
        return convertView;
    }

    private class ViewHolder0 {

        private TextView skillNameTv, timeTv;

        public ViewHolder0(View itemView) {
            skillNameTv = (TextView) itemView.findViewById(R.id.tv_item_employer_to_talk_type_0_skill_name);
            timeTv = (TextView) itemView.findViewById(R.id.tv_item_employer_to_talk_type_0_time);
        }
    }

    private class ViewHolder1 {

        private LinearLayout ll;
        private CImageView workerIconIv, workerPhoneIv;
        private ImageView workerSexIv, workerStatusIv;
        private TextView workerNameTv, skillNameTv;

        public ViewHolder1(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_employer_to_talk_type_1);
            workerIconIv = (CImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_icon);
            workerPhoneIv = (CImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_phone);
            workerSexIv = (ImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_sex);
            workerStatusIv = (ImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_status);
            workerNameTv = (TextView) itemView.findViewById(R.id.tv_item_employer_to_talk_type_1_worker_name);
            skillNameTv = (TextView) itemView.findViewById(R.id.tv_item_employer_to_talk_type_1_worker_skill_name);
        }
    }
}
