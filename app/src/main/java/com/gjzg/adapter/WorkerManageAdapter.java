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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.gjzg.listener.IdPosClickHelp;

import com.gjzg.bean.WorkerManageBean;

public class WorkerManageAdapter extends BaseAdapter {

    private Context context;
    private List<WorkerManageBean> list;
    private IdPosClickHelp idPosClickHelp;

    public WorkerManageAdapter(Context context, List<WorkerManageBean> list, IdPosClickHelp idPosClickHelp) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_worker_manage, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WorkerManageBean workerManageBean = list.get(position);
        Picasso.with(context).load(workerManageBean.getIcon()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(holder.iconIv);
        String o_status = workerManageBean.getO_status();
        if (o_status.equals("0")) {
            if (workerManageBean.getO_confirm().equals("0") || workerManageBean.getO_confirm().equals("2")) {
                holder.statusIv.setImageResource(R.mipmap.worker_talk);
            } else if (workerManageBean.getO_confirm().equals("1")) {
                holder.statusIv.setImageResource(R.mipmap.worker_mid);
            }
            holder.deleteTv.setVisibility(View.INVISIBLE);
        } else if (o_status.equals("1")) {
            holder.statusIv.setImageResource(R.mipmap.worker_over);
            holder.deleteTv.setVisibility(View.VISIBLE);
        }
        holder.titleTv.setText(workerManageBean.getTitle());
        holder.infoTv.setText(workerManageBean.getInfo());
        final int p = position;
        final int llId = holder.ll.getId();
        final int delId = holder.deleteTv.getId();
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPosClickHelp.onClick(llId, p);
            }
        });
        holder.deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPosClickHelp.onClick(delId, p);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private LinearLayout ll;
        private ImageView iconIv, statusIv;
        private TextView titleTv, infoTv, deleteTv;

        public ViewHolder(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_worker_manage);
            iconIv = (ImageView) itemView.findViewById(R.id.iv_item_worker_manage_icon);
            statusIv = (ImageView) itemView.findViewById(R.id.iv_item_worker_manage_status);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_worker_manage_title);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_worker_manage_info);
            deleteTv = (TextView) itemView.findViewById(R.id.tv_item_worker_manage_delete);
        }
    }
}
