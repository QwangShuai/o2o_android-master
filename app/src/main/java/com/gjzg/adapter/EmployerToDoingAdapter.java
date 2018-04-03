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
import com.gjzg.bean.EmployerToDoingBean;
import com.gjzg.custom.CImageView;
import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.utils.DataUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployerToDoingAdapter extends BaseAdapter {

    private Context context;
    private List<EmployerToDoingBean> list;
    private IdPosClickHelp idPosClickHelp;

    public EmployerToDoingAdapter(Context context, List<EmployerToDoingBean> list, IdPosClickHelp idPosClickHelp) {
        this.context = context;
        this.list = list;
        this.idPosClickHelp = idPosClickHelp;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
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
        ViewHolder0 holder0;
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_employer_to_talk_type_0, null);
                    holder0 = new ViewHolder0(convertView);
                    convertView.setTag(holder0);
                } else {
                    holder0 = (ViewHolder0) convertView.getTag();
                }
                holder0.skillNameTv.setText(list.get(position).getSkillName());
                holder0.timeTv.setText(DataUtils.times(list.get(position).getStartTime()) + "-" + DataUtils.times(list.get(position).getEndTime()));
                break;
            case 1:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_employer_to_talk_type_1, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                Picasso.with(context).load(list.get(position).getIcon()).into(holder1.iconIv);
                if (list.get(position).getSex().equals("0")) {
                    holder1.sexIv.setImageResource(R.mipmap.female);
                } else if (list.get(position).getSex().equals("1")) {
                    holder1.sexIv.setImageResource(R.mipmap.male);
                }
                if (list.get(position).getStatus().equals("0")) {
                    holder1.statusIv.setImageResource(R.mipmap.worker_leisure);
                } else if (list.get(position).getStatus().equals("1")) {
                    holder1.statusIv.setImageResource(R.mipmap.worker_mid);
                }
                holder1.nameTv.setText(list.get(position).getName());
                holder1.skillNameTv.setText(list.get(position).getSkillName());
                final int p = position;
                final int llId = holder1.ll.getId();
                holder1.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idPosClickHelp.onClick(llId, p);
                    }
                });
                break;
            case 2:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_employer_to_doing, null);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }
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
        private CImageView iconIv, phoneIv;
        private ImageView sexIv, statusIv;
        private TextView nameTv, skillNameTv;

        public ViewHolder1(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_employer_to_talk_type_1);
            iconIv = (CImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_icon);
            phoneIv = (CImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_phone);
            sexIv = (ImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_sex);
            statusIv = (ImageView) itemView.findViewById(R.id.iv_item_employer_to_talk_type_1_worker_status);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_employer_to_talk_type_1_worker_name);
            skillNameTv = (TextView) itemView.findViewById(R.id.tv_item_employer_to_talk_type_1_worker_skill_name);
        }
    }

    private class ViewHolder2 {


        public ViewHolder2(View itemView) {
        }
    }
}
