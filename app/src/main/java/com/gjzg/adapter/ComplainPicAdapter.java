package com.gjzg.adapter;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.Pic;

public class ComplainPicAdapter extends CommonAdapter<Pic> {

    private BitmapFactory.Options options;

    public ComplainPicAdapter(Context context, List<Pic> list) {
        super(context, list);
        options = new BitmapFactory.Options();
        options.inSampleSize = 10;
        options.inTempStorage = new byte[1024];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Pic pic = list.get(position);
        if (pic != null) {
            holder.imgIv.setImageBitmap(BitmapFactory.decodeFile(pic.getPath(), options));
            if (pic.isChoose()) {
                holder.chooseIv.setImageResource(R.mipmap.pay_choosed);
            } else {
                holder.chooseIv.setImageResource(R.mipmap.ic_launcher);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView imgIv, chooseIv;

        public ViewHolder(View itemView) {
            imgIv = (ImageView) itemView.findViewById(R.id.iv_item_pic_img);
            chooseIv = (ImageView) itemView.findViewById(R.id.iv_item_pic_choose);
        }
    }
}
