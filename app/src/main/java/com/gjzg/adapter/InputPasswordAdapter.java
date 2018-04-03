package com.gjzg.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.config.ColorConfig;
import com.gjzg.bean.InputPasswordBean;

public class InputPasswordAdapter extends BaseAdapter {

    private Context context;
    private List<InputPasswordBean> list;

    public InputPasswordAdapter(Context context, List<InputPasswordBean> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_input_password, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InputPasswordBean inputPasswordBean = list.get(position);
        if (inputPasswordBean != null) {
            switch (inputPasswordBean.getType()) {
                case 0:
                    holder.rl.setVisibility(View.VISIBLE);
                    holder.ll.setVisibility(View.VISIBLE);
                    holder.deleteIv.setVisibility(View.INVISIBLE);
                    if (inputPasswordBean.getNumber() == 0) {
                        holder.letterTv.setVisibility(View.GONE);
                    } else {
                        holder.letterTv.setVisibility(View.VISIBLE);
                    }
                    holder.numberTv.setText(inputPasswordBean.getNumber() + "");
                    holder.letterTv.setText(inputPasswordBean.getLetter());
                    break;
                case 1:
                    holder.rl.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.rl.setVisibility(View.VISIBLE);
                    holder.rl.setBackgroundColor(ColorConfig.gray_c4ced3);
                    holder.ll.setVisibility(View.INVISIBLE);
                    holder.deleteIv.setVisibility(View.VISIBLE);
                    break;
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView numberTv, letterTv;
        private RelativeLayout rl;
        private LinearLayout ll;
        private ImageView deleteIv;

        public ViewHolder(View itemView) {
            numberTv = (TextView) itemView.findViewById(R.id.tv_item_pop_input_password_number);
            letterTv = (TextView) itemView.findViewById(R.id.tv_item_pop_input_password_letter);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl_item_pop_input_password);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_pop_input_password);
            deleteIv = (ImageView) itemView.findViewById(R.id.iv_item_pop_input_password_delete);
        }
    }
}
