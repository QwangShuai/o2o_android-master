package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.ArticleBean;

import java.util.List;

public class ArticleAdapter extends BaseAdapter {

    private Context context;
    private List<ArticleBean> list;

    public ArticleAdapter(Context context, List<ArticleBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_article, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ArticleBean articleBean = list.get(position);
        if (articleBean != null) {
            String title = articleBean.getA_title();
            if (title == null || title.equals("null") || TextUtils.isEmpty(title)) {
            } else {
                holder.titleTv.setText(title);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView titleTv;

        public ViewHolder(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_article_title);
        }
    }
}
