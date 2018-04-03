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
import com.gjzg.bean.EmployerManageBean;
import com.gjzg.listener.IdPosClickHelp;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployerManageAdapter extends BaseAdapter {

    private Context context;
    private List<EmployerManageBean> list;
    private IdPosClickHelp idPosClickHelp;

    public EmployerManageAdapter(Context context, List<EmployerManageBean> list, IdPosClickHelp idPosClickHelp) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_employer_manage, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EmployerManageBean employerManageBean = list.get(position);
        if (employerManageBean != null) {
            Picasso.with(context).load(employerManageBean.getIcon()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(holder.iconIv);
            if (employerManageBean.getStatus().equals("0")) {
//                holder.statusTv.setImageResource(R.mipmap.worker_wait);
                holder.waitLl.setVisibility(View.VISIBLE);
                holder.talkLl.setVisibility(View.INVISIBLE);
                holder.doneLl.setVisibility(View.INVISIBLE);
                holder.statusTv.setText("发布中");
                holder.statusTv.setBackgroundResource(R.drawable.sp_yellow);
            } else if (employerManageBean.getStatus().equals("1")) {
//                holder.statusTv.setImageResource(R.mipmap.worker_talk);
                holder.waitLl.setVisibility(View.INVISIBLE);
                holder.talkLl.setVisibility(View.VISIBLE);
                holder.doneLl.setVisibility(View.INVISIBLE);
                holder.statusTv.setText("发布中");
                holder.statusTv.setBackgroundResource(R.drawable.sp_yellow);
            } else if (employerManageBean.getStatus().equals("-3") || employerManageBean.getStatus().equals("2") || employerManageBean.getStatus().equals("5")) {
//                holder.statusTv.setImageResource(R.mipmap.worker_mid);
                holder.waitLl.setVisibility(View.INVISIBLE);
                holder.talkLl.setVisibility(View.INVISIBLE);
                holder.doneLl.setVisibility(View.INVISIBLE);
                holder.statusTv.setText("发布中");
                holder.statusTv.setBackgroundResource(R.drawable.sp_yellow);
            } else if (employerManageBean.getStatus().equals("3") || employerManageBean.getStatus().equals("4")) {
//                holder.statusTv.setImageResource(R.mipmap.worker_over);
                holder.waitLl.setVisibility(View.INVISIBLE);
                holder.talkLl.setVisibility(View.INVISIBLE);
                holder.doneLl.setVisibility(View.VISIBLE);
                holder.statusTv.setText("已完成");
                holder.statusTv.setBackgroundResource(R.drawable.sp_green);
            }
            holder.titleTv.setText(employerManageBean.getTitle());
            holder.infoTv.setText(employerManageBean.getInfo());
            holder.areaTv.setText(employerManageBean.getArea());
            final int p = position;
            final int llId = holder.ll.getId();
            final int waitCancelId = holder.waitCancelTv.getId();
            final int talkCancelId = holder.talkCancelTv.getId();
            final int doneDelId = holder.doneDelTv.getId();
            final int endId = holder.endTv.getId();
            final int detailsId = holder.detailsTv.getId();
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(llId, p);
                }
            });
            holder.waitCancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(waitCancelId, p);
                }
            });
            holder.talkCancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(talkCancelId, p);
                }
            });
            holder.doneDelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(doneDelId, p);
                }
            });
            holder.endTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(endId, p);
                }
            });
            holder.detailsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(detailsId, p);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView iconIv;
        private TextView titleTv, infoTv, statusTv,endTv,areaTv,detailsTv;
        private LinearLayout waitLl, talkLl, doneLl;
        private LinearLayout ll;
        private TextView waitCancelTv, talkCancelTv, doneDelTv;

        public ViewHolder(View itemView) {
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_employer_manage_icon);
            statusTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_status);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_title);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_info);
            endTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_end) ;
            detailsTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_details);
            waitLl = (LinearLayout) itemView.findViewById(R.id.ll_item_employer_manage_wait);
            talkLl = (LinearLayout) itemView.findViewById(R.id.ll_item_employer_manage_talk);
            areaTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_area);
            doneLl = (LinearLayout) itemView.findViewById(R.id.ll_item_employer_manage_done);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_employer_manage);
            waitCancelTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_wait_cancel);
            talkCancelTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_talk_cancel);
            doneDelTv = (TextView) itemView.findViewById(R.id.tv_item_employer_manage_done_del);
        }
    }
}
