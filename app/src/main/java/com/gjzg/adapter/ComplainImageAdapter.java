package com.gjzg.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gjzg.R;

import java.util.List;

public class ComplainImageAdapter extends CommonAdapter<Bitmap> {

    public ComplainImageAdapter(Context context, List<Bitmap> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_complain_image, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageIv.setImageBitmap(list.get(position));
        return convertView;
    }

    private class ViewHolder {

        private ImageView imageIv;

        public ViewHolder(View itemView) {
            imageIv = (ImageView) itemView.findViewById(R.id.iv_item_complain_image);
        }
    }
}
