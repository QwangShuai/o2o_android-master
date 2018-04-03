package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gjzg.R;
import com.gjzg.bean.PhotoBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends BaseAdapter {

    private Context context;
    private List<PhotoBean> list;

    public PhotoAdapter(Context context, List<PhotoBean> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_photo, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhotoBean photoBean = list.get(position);
        if (photoBean != null) {
            String path = photoBean.getPath();
            if (TextUtils.isEmpty(path)) {
            } else {
                Picasso.with(context).load("file://" + path).into(holder.imgIv);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView imgIv;

        public ViewHolder(View itemView) {
            imgIv = (ImageView) itemView.findViewById(R.id.iv_item_photo_img);
        }
    }
}
