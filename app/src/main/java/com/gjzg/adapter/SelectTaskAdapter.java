package com.gjzg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.TaskBean;
import com.gjzg.custom.CImageView;
import com.gjzg.listener.IdPosClickHelp;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SelectTaskAdapter extends BaseAdapter {

    private Context context;
    private List<TaskBean> list;
    private IdPosClickHelp idPosClickHelp;

    public SelectTaskAdapter(Context context, List<TaskBean> list, IdPosClickHelp idPosClickHelp) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_task, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskBean taskBean = list.get(position);
        Picasso.with(context).load(taskBean.getIcon()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(holder.iconIv);
        String status = taskBean.getStatus();
        if (status.equals("0")) {
            holder.statusIv.setImageResource(R.mipmap.worker_leisure);
        } else if (status.equals("1")) {
            holder.statusIv.setImageResource(R.mipmap.worker_talk);
        } else if (status.equals("-3") || status.equals("2") || status.equals("5")) {
            holder.statusIv.setImageResource(R.mipmap.worker_mid);
        } else if (status.equals("3") || status.equals("4")) {
            holder.statusIv.setImageResource(R.mipmap.worker_over);
        }
        holder.titleTv.setText(taskBean.getTitle());
        holder.infoTv.setText(taskBean.getInfo());
        final int p = position;
        final int chooseId = holder.chooseTv.getId();
        holder.chooseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPosClickHelp.onClick(chooseId, p);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private CImageView iconIv;
        private ImageView statusIv;
        private TextView titleTv, infoTv, chooseTv;

        public ViewHolder(View itemView) {
            iconIv = (CImageView) itemView.findViewById(R.id.iv_item_select_task_icon);
            statusIv = (ImageView) itemView.findViewById(R.id.iv_item_select_task_state);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_select_task_title);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_select_task_info);
            chooseTv = (TextView) itemView.findViewById(R.id.tv_item_select_task_choose);
        }
    }
}
