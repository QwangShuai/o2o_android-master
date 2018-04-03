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
import com.gjzg.bean.TInfoOrderBean;
import com.gjzg.config.ColorConfig;
import com.gjzg.custom.CImageView;
import com.gjzg.listener.TInfoClickHelp;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TInfoTaskInnerAdapter extends BaseAdapter {

    private Context context;
    private List<TInfoOrderBean> list;
    private TInfoClickHelp tInfoClickHelp;

    public TInfoTaskInnerAdapter(Context context, List<TInfoOrderBean> list, TInfoClickHelp tInfoClickHelp) {
        this.context = context;
        this.list = list;
        this.tInfoClickHelp = tInfoClickHelp;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tinfotask_inner, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TInfoOrderBean tInfoOrderBean = list.get(position);
        if (tInfoOrderBean != null) {
            Picasso.with(context).load(tInfoOrderBean.getU_img()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(holder.iconIv);
            String sex = tInfoOrderBean.getU_sex();
            if (sex.equals("0")) {
                holder.sexIv.setImageResource(R.mipmap.female);
            } else if (sex.equals("1")) {
                holder.sexIv.setImageResource(R.mipmap.male);
            }
            String o_pay = tInfoOrderBean.getO_pay();
            String o_status = tInfoOrderBean.getO_status();
            String t_status = tInfoOrderBean.getU_task_status();
            if (o_pay.equals("0")) {
                holder.mobileIv.setVisibility(View.VISIBLE);
                holder.mobileIv.setEnabled(true);
                holder.evaluateTv.setVisibility(View.GONE);
                if (t_status.equals("0")) {
                    holder.statusTv.setText("洽谈中");
                    holder.statusTv.setBackgroundColor(ColorConfig.yellow_ffc822);
                } else {
                    holder.statusTv.setText("工作中");
                    holder.statusTv.setBackgroundColor(ColorConfig.red_ff3e50);
                }
            } else {
                holder.mobileIv.setVisibility(View.GONE);
                holder.evaluateTv.setVisibility(View.VISIBLE);
            }
            if (o_status.equals("-1")) {
                holder.statusTv.setText("已辞职");
                holder.statusTv.setBackgroundColor(ColorConfig.gray_c4ced3);
                holder.mobileIv.setEnabled(false);
            } else if (o_status.equals("-2")) {
                holder.statusTv.setText("已解雇");
                holder.statusTv.setBackgroundColor(ColorConfig.gray_c4ced3);
                holder.mobileIv.setEnabled(false);
            }
            holder.nameTv.setText(tInfoOrderBean.getU_true_name());
            holder.skillTv.setText(tInfoOrderBean.getSkill());
        }
        final int innerPos = position;
        final int llId = holder.ll.getId();
        final int mobileId = holder.mobileIv.getId();
        final int evaluateId = holder.evaluateTv.getId();
        final String orderId = tInfoOrderBean.getO_id();
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tInfoClickHelp.onClick(llId, 0, innerPos, orderId);
            }
        });
        holder.mobileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tInfoClickHelp.onClick(mobileId, 0, innerPos, orderId);
            }
        });
        holder.evaluateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tInfoClickHelp.onClick(evaluateId, 0, innerPos, orderId);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private LinearLayout ll;
        private CImageView iconIv, mobileIv;
        private ImageView sexIv;
        private TextView nameTv, skillTv, statusTv, evaluateTv;

        public ViewHolder(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_tinfotask_inner);
            iconIv = (CImageView) itemView.findViewById(R.id.iv_item_tinfotask_inner_icon);
            mobileIv = (CImageView) itemView.findViewById(R.id.iv_item_tinfotask_inner_mobile);
            sexIv = (ImageView) itemView.findViewById(R.id.iv_item_tinfotask_inner_sex);
            nameTv = (TextView) itemView.findViewById(R.id.tv_item_tinfotask_inner_name);
            skillTv = (TextView) itemView.findViewById(R.id.tv_item_tinfotask_inner_skill);
            statusTv = (TextView) itemView.findViewById(R.id.tv_item_tinfotask_inner_status);
            evaluateTv = (TextView) itemView.findViewById(R.id.tv_item_tinfotask_inner_evaluate);
        }
    }
}
