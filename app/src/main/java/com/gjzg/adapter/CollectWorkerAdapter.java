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
import com.gjzg.bean.CollectWorker;
import com.gjzg.config.GlideCircleTransform;

import java.util.List;

import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.custom.CImageView;

/**
 * 收藏的工人适配器
 */
public class CollectWorkerAdapter extends BaseAdapter {

    private Context mContext;
    private List<CollectWorker.DataBeanX.DataBean> mList;
    private IdPosClickHelp idPosClickHelp;

    public CollectWorkerAdapter(Context mContext, List<CollectWorker.DataBeanX.DataBean> mList, IdPosClickHelp idPosClickHelp) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_worker, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectWorker.DataBeanX.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getU_img()))
                Glide.with(mContext).load(dataBean.getU_img()).placeholder(R.mipmap.person_face_default).transform(new GlideCircleTransform(mContext)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imageIv);
            String status = dataBean.getU_task_status();
            if (!TextUtils.isEmpty(status)) {
                if (status.equals("0")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_leisure);
                } else if (status.equals("1")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_mid);
                }
            }
            holder.collectIv.setImageResource(R.mipmap.collect_yellow);
            if (!TextUtils.isEmpty(dataBean.getU_name()))
                holder.nameTv.setText(dataBean.getU_name());
            if (!TextUtils.isEmpty(dataBean.getUei_info()))
                holder.infoTv.setText(dataBean.getUei_info());
            final int p = position;
            final int llId = holder.ll.getId();
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(llId, p);
                }
            });
            final int cancelCollectId = holder.collectIv.getId();
            holder.collectIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(cancelCollectId, p);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private LinearLayout ll;
        private CImageView imageIv;
        private ImageView statusIv, collectIv;
        private TextView nameTv, infoTv;

        public ViewHolder(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_worker);
            imageIv = (CImageView) itemView.findViewById(R.id.iv_item_worker_icon);
            statusIv = (ImageView) itemView.findViewById(R.id.iv_item_worker_status);
            collectIv = (ImageView) itemView.findViewById(R.id.iv_item_worker_collect);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_worker_title);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_worker_info);
        }
    }
}
