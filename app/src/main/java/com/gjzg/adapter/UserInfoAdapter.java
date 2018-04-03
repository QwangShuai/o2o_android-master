package com.gjzg.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.SkillsBean;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.utils.Utils;

public class UserInfoAdapter extends BaseAdapter {

    private Context context;
    private UserInfoBean userInfoBean;
    private List<SkillsBean> skillsBeanList;

    public UserInfoAdapter(Context context, UserInfoBean userInfoBean, List<SkillsBean> skillsBeanList) {
        this.context = context;
        this.userInfoBean = userInfoBean;
        this.skillsBeanList = skillsBeanList;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 8) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getCount() {
        return userInfoBean == null ? 0 : 9;
    }

    @Override
    public Object getItem(int position) {
        return userInfoBean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder0 holder0;
        ViewHolder1 holder1;
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_userinfo_0, null);
                    holder0 = new ViewHolder0(convertView);
                    convertView.setTag(holder0);
                } else {
                    holder0 = (ViewHolder0) convertView.getTag();
                }
                switch (position) {
                    case 0:
                        holder0.titleTv.setText("姓名");
                        holder0.contentTv.setText(userInfoBean.getU_true_name());
                        break;
                    case 1:
                        holder0.titleTv.setText("性别");
                        if (userInfoBean.getU_sex().equals("-1")) {
                            holder0.contentTv.setText("无");
                        } else if (userInfoBean.getU_sex().equals("0")) {
                            holder0.contentTv.setText("女");
                        } else if (userInfoBean.getU_sex().equals("1")) {
                            holder0.contentTv.setText("男");
                        }
                        break;
                    case 2:
                        holder0.titleTv.setText("身份证号");
                        String idCard = userInfoBean.getU_idcard();
                        if (!TextUtils.isEmpty(idCard)) {
                            idCard = idCard.substring(0, 6) + "********" + idCard.substring(14, idCard.length());
                        }
                        holder0.contentTv.setText(idCard);
                        break;
                    case 3:
                        holder0.titleTv.setText("现居地");
                        holder0.contentTv.setText(userInfoBean.getUser_area_name());
                        break;
                    case 4:
                        holder0.titleTv.setText("详细地址");
                        holder0.contentTv.setText(userInfoBean.getUei_address());
                        break;
                    case 5:
                        holder0.titleTv.setText("个人简介");
                        holder0.contentTv.setText(userInfoBean.getU_info());
                        break;
                    case 6:
                        if(userInfoBean.getU_mobile()==""||userInfoBean.getU_mobile().length()==0){
                            holder0.contentTv.setText("");
                            holder0.titleTv.setText("手机号码（未绑定）");
                        } else {
                            holder0.titleTv.setText("手机号码（已绑定）");
                            String mobile = userInfoBean.getU_mobile();
                            if (!TextUtils.isEmpty(mobile)) {
                                mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
                            }
                            holder0.contentTv.setText(mobile);
                        }
                        break;
                    case 7:
                        holder0.titleTv.setText("角色选择");
                        if (userInfoBean.getU_skills().equals("0")) {
                            holder0.contentTv.setText("我不是工人");
                        } else {
                            holder0.contentTv.setText("我是工人");
                        }
                        break;
                }
                break;
            case 1:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_userinfo_1, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                if (userInfoBean.getU_skills().equals("0")) {
                    holder1.gv.setVisibility(View.GONE);
                } else {
                    holder1.gv.setVisibility(View.VISIBLE);
                    holder1.gv.setAdapter(new UserSkillAdapter(context, skillsBeanList));
                    Utils.setGridViewHeight(holder1.gv, 4);
                }
                break;
        }
        return convertView;
    }

    private class ViewHolder0 {

        private TextView titleTv, contentTv;

        public ViewHolder0(View itemView) {
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_userinfo_0_title);
            contentTv = (TextView) itemView.findViewById(R.id.tv_item_userinfo_0_content);
        }
    }

    private class ViewHolder1 {

        private GridView gv;

        public ViewHolder1(View itemView) {
            gv = (GridView) itemView.findViewById(R.id.gv_item_userinfo_1);
        }
    }
}
