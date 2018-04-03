package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjzg.R;
import com.gjzg.bean.CollectTaskBean;

import java.util.List;

import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.custom.CImageView;

public class CollectTaskAdapter extends BaseAdapter {

    private Context mContext;
    private List<CollectTaskBean.DataBeanX.DataBean> mList;
    private IdPosClickHelp mHelp;

    public CollectTaskAdapter(Context mContext, List<CollectTaskBean.DataBeanX.DataBean> mList, IdPosClickHelp mHelp) {
        this.mContext = mContext;
        this.mList = mList;
        this.mHelp = mHelp;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectTaskBean.DataBeanX.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getT_title()))
                holder.titleTv.setText(dataBean.getT_title());
            if (!TextUtils.isEmpty(dataBean.getT_desc()))
                holder.infoTv.setText(dataBean.getT_desc());
            if (!TextUtils.isEmpty(dataBean.getU_img()))
                Glide.with(mContext).load(dataBean.getU_img()).into(holder.iconIv);
            if (!TextUtils.isEmpty(dataBean.getT_status())) {
                String status = dataBean.getT_status();
                if (status.equals("0")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_wait);
                } else if (status.equals("1")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_talk);
                } else if (status.equals("2")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_mid);
                } else if (status.equals("3")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_over);
                }
            }
            holder.collectIv.setImageResource(R.mipmap.collect_yellow);
        }
        final int p = position;
        final int llId = holder.ll.getId();
        final int collectId = holder.collectIv.getId();
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelp.onClick(llId, p);
            }
        });
        holder.collectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelp.onClick(collectId, p);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private LinearLayout ll;
        private CImageView iconIv;
        private ImageView statusIv, collectIv;
        private TextView titleTv, infoTv;

        public ViewHolder(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_task);
            iconIv = (CImageView) itemView.findViewById(R.id.iv_item_task_icon);
            statusIv = (ImageView) itemView.findViewById(R.id.iv_item_task_status);
            collectIv = (ImageView) itemView.findViewById(R.id.iv_item_task_collect);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_task_name);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_task_info);
        }
    }
}
