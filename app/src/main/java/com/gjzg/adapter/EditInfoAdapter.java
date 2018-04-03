package com.gjzg.adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gjzg.R;

import java.util.List;

import com.gjzg.bean.SkillsBean;

import editinfo.listener.EditInfoClickHelp;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.utils.Utils;

public class EditInfoAdapter extends BaseAdapter {

    private Context context;
    private UserInfoBean userInfoBean;
    private List<SkillsBean> skillsBeanList;
    private EditInfoClickHelp editInfoClickHelp;

    public EditInfoAdapter(Context context, UserInfoBean userInfoBean, List<SkillsBean> skillsBeanList, EditInfoClickHelp editInfoClickHelp) {
        this.context = context;
        this.userInfoBean = userInfoBean;
        this.skillsBeanList = skillsBeanList;
        this.editInfoClickHelp = editInfoClickHelp;
    }

    @Override
    public int getCount() {
        return userInfoBean == null ? 0 : 1;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_editinfo, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.nameEt.getTag() instanceof TextWatcher) {
            holder.nameEt.removeTextChangedListener((TextWatcher) holder.nameEt.getTag());
        }
        holder.nameEt.setText(userInfoBean.getU_true_name());
        TextWatcher nameTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userInfoBean.setU_true_name(s.toString());
            }
        };
        holder.nameEt.addTextChangedListener(nameTw);
        holder.nameEt.setTag(nameTw);
        if (userInfoBean.getU_sex().equals("-1")) {

        } else if (userInfoBean.getU_sex().equals("0")) {
            ((RadioButton) holder.sexRg.getChildAt(1)).setChecked(true);
        } else if (userInfoBean.getU_sex().equals("1")) {
            ((RadioButton) holder.sexRg.getChildAt(0)).setChecked(true);
        }
        if (holder.idCardEt.getTag() instanceof TextWatcher) {
            holder.idCardEt.removeTextChangedListener((TextWatcher) holder.idCardEt.getTag());
        }
        String idCard = userInfoBean.getU_idcard();
//                        if (!TextUtils.isEmpty(idCard)) {
//                            idCard = idCard.substring(0, 3) + "***********" + idCard.substring(13, idCard.length());
//                        }
        holder.idCardEt.setText(idCard);
        TextWatcher idCardTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userInfoBean.setU_idcard(s.toString());
            }
        };
        holder.idCardEt.addTextChangedListener(idCardTw);
        holder.idCardEt.setTag(idCardTw);
        holder.areaTv.setText(userInfoBean.getUser_area_name());
        final int areaId = holder.areaTv.getId();
        holder.areaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfoClickHelp.onClick(areaId, 0, 0);
            }
        });
        if (holder.addressEt.getTag() instanceof TextWatcher) {
            holder.addressEt.removeTextChangedListener((TextWatcher) holder.addressEt.getTag());
        }
        holder.addressEt.setText(userInfoBean.getUei_address());
        TextWatcher addressTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userInfoBean.setUei_address(s.toString());
            }
        };
        holder.addressEt.addTextChangedListener(addressTw);
        holder.addressEt.setTag(addressTw);
        if (holder.infoEt.getTag() instanceof TextWatcher) {
            holder.infoEt.removeTextChangedListener((TextWatcher) holder.infoEt.getTag());
        }
        holder.infoEt.setText(userInfoBean.getU_info());
        TextWatcher infoTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userInfoBean.setU_info(s.toString());
            }
        };
        holder.infoEt.addTextChangedListener(infoTw);
        holder.infoEt.setTag(infoTw);
        String mobile = userInfoBean.getU_mobile();
        if (!TextUtils.isEmpty(mobile)) {
            mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        }
        holder.mobileTv.setText(mobile);
        if (userInfoBean.getU_skills().equals("0")) {
            ((RadioButton) holder.roleRg.getChildAt(0)).setChecked(true);
            holder.workerLl.setVisibility(View.GONE);
        } else {
            ((RadioButton) holder.roleRg.getChildAt(1)).setChecked(true);
            holder.workerLl.setVisibility(View.VISIBLE);
            holder.gridView.setAdapter(new EditSkillAdapter(context, skillsBeanList));
            Utils.setGridViewHeight(holder.gridView, 4);
        }
        final int sexId = holder.sexRg.getId();
        holder.sexRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editInfoClickHelp.onClick(sexId, 0, checkedId);
            }
        });
        final int roleId = holder.roleRg.getId();
        holder.roleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editInfoClickHelp.onClick(roleId, 0, checkedId);
            }
        });
        final int gridId = holder.gridView.getId();
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editInfoClickHelp.onClick(gridId, position, 0);
            }
        });
        final int addId = holder.addIv.getId();
        holder.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfoClickHelp.onClick(addId, 0, 0);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        private EditText nameEt, idCardEt, addressEt, infoEt;
        private TextView areaTv, mobileTv;
        private RadioGroup sexRg, roleRg;
        private LinearLayout workerLl;
        private GridView gridView;
        private ImageView addIv;

        public ViewHolder(View itemView) {
            nameEt = (EditText) itemView.findViewById(R.id.et_item_editinfo_name);
            idCardEt = (EditText) itemView.findViewById(R.id.et_item_editinfo_idcard);
            addressEt = (EditText) itemView.findViewById(R.id.et_item_editinfo_address);
            infoEt = (EditText) itemView.findViewById(R.id.et_item_editinfo_info);
            areaTv = (TextView) itemView.findViewById(R.id.tv_item_editinfo_area);
            mobileTv = (TextView) itemView.findViewById(R.id.tv_item_editinfo_mobile);
            sexRg = (RadioGroup) itemView.findViewById(R.id.rg_item_editinfo_sex);
            roleRg = (RadioGroup) itemView.findViewById(R.id.rg_item_editinfo_role);
            workerLl = (LinearLayout) itemView.findViewById(R.id.ll_item_editinfo_worker);
            gridView = (GridView) itemView.findViewById(R.id.gv_item_editinfo);
            addIv = (ImageView) itemView.findViewById(R.id.iv_item_editinfo_addskill);
        }
    }
}
