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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gjzg.R;
import com.gjzg.bean.Task;
import com.gjzg.config.GlideCircleTransform;

import java.util.List;

import com.gjzg.bean.LonLatBean;

import com.gjzg.listener.IdPosClickHelp;

import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.custom.CImageView;

public class TaskAdapter extends BaseAdapter {

    private Context mContext;
    private List<Task.DataBean> mList;
    private IdPosClickHelp idPosClickHelp;

    public TaskAdapter(Context mContext, List<Task.DataBean> mList, IdPosClickHelp idPosClickHelp) {
        this.mContext = mContext;
        this.mList = mList;
        this.idPosClickHelp = idPosClickHelp;
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
        Task.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getU_img()))
                Glide.with(mContext).load(dataBean.getU_img()).placeholder(R.mipmap.person_face_default).transform(new GlideCircleTransform(mContext)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.iconIv);
            if (!TextUtils.isEmpty(dataBean.getT_title()))
                holder.titleTv.setText(dataBean.getT_title());
            if (!TextUtils.isEmpty(dataBean.getT_info()))
                holder.infoTv.setText(dataBean.getT_info());
            String status = dataBean.getT_status();
            if (!TextUtils.isEmpty(status)) {
                if (status.equals("0")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_wait);
                } else if (status.equals("1")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_talk);
                } else if (status.equals("-3") || status.equals("2") || status.equals("5")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_mid);
                } else if (status.equals("3") || status.equals("4")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_over);
                }
            }
            LonLatBean lonLatBean1 = UserUtils.getLonLat(mContext);
            LonLatBean lonLatBean2 = new LonLatBean(dataBean.getT_posit_x(), dataBean.getT_posit_y());
            String distance = DataUtils.getDistance(lonLatBean1, lonLatBean2);
            if (!TextUtils.isEmpty(distance)) {
                holder.distanceTv.setText(distance);
            } else {
                holder.distanceTv.setText("无法获取距离");
            }
            switch (dataBean.getFavorate()) {
                case 0:
                    holder.collectIv.setImageResource(R.mipmap.collect_gray);
                    break;
                case 1:
                    holder.collectIv.setImageResource(R.mipmap.collect_yellow);
                    break;
            }
            final int pos = position;
            final int llId = holder.ll.getId();
            final int collectId = holder.collectIv.getId();
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(llId, pos);
                }
            });
            holder.collectIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(collectId, pos);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private LinearLayout ll;
        private TextView titleTv, infoTv, distanceTv;
        private CImageView iconIv;
        private ImageView statusIv, collectIv;

        public ViewHolder(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_task);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_task_name);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_task_info);
            iconIv = (CImageView) itemView.findViewById(R.id.iv_item_task_icon);
            statusIv = (ImageView) itemView.findViewById(R.id.iv_item_task_status);
            collectIv = (ImageView) itemView.findViewById(R.id.iv_item_task_collect);
            distanceTv = (TextView) itemView.findViewById(R.id.tv_item_task_distance);
        }
    }
}
