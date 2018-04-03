package com.gjzg.utils;

import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.gjzg.bean.ArticleBean;
import com.gjzg.bean.CollectWorkerBean;
import com.gjzg.bean.ComplainIssueBean;
import com.gjzg.bean.EmployerManageBean;
import com.gjzg.bean.EmployerToDoingBean;
import com.gjzg.bean.EmployerToTalkBean;
import com.gjzg.bean.EvaluateBean;
import com.gjzg.bean.LonLatBean;
import com.gjzg.bean.MessageBean;
import com.gjzg.bean.PayWayBean;
import com.gjzg.bean.PublishBean;
import com.gjzg.bean.PublishWorkerBean;
import com.gjzg.bean.RedPacketBean;
import com.gjzg.bean.SkillsBean;
import com.gjzg.bean.TInfoOrderBean;
import com.gjzg.bean.TInfoTaskBean;
import com.gjzg.bean.TInfoWorkerBean;
import com.gjzg.bean.TalkEmployerBean;
import com.gjzg.bean.TalkEmployerWorkerBean;
import com.gjzg.bean.TaskBean;
import com.gjzg.bean.TaskWorkerBean;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.bean.VoucherBean;
import com.gjzg.bean.WorkerBean;
import com.gjzg.bean.WorkerManageBean;
import com.gjzg.bean.WxDataBean;
import com.gjzg.config.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataUtils {

    //站内信
    public static List<MessageBean> getMessageBeanList(String json) {
        List<MessageBean> messageBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 1) {
                JSONObject dataObj = beanObj.optJSONObject("data");
                if (dataObj != null) {
                    JSONArray dataArr = dataObj.optJSONArray("data");
                    if (dataArr != null) {
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject o = dataArr.optJSONObject(i);
                            if (o != null) {
                                MessageBean messageBean = new MessageBean();
                                messageBean.setWm_title(o.optString("wm_title"));
                                messageBean.setUm_in_time(o.optString("um_in_time"));
                                messageBean.setWm_type(o.optString("wm_type"));
                                messageBean.setWm_id(o.optString("wm_id"));
                                messageBean.setUm_id(o.optString("um_id"));
                                messageBean.setWm_desc(o.optString("wm_desc"));
                                messageBean.setUm_status(o.optString("um_status"));
                                messageBeanList.add(messageBean);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBeanList;
    }

    //距离
    public static String getDistance(LonLatBean lonLatBean1, LonLatBean lonLatBean2) {
        if (lonLatBean1 != null && lonLatBean2 != null) {
            String lon1 = lonLatBean1.getLon();
            String lat1 = lonLatBean1.getLat();
            String lon2 = lonLatBean2.getLon();
            String lat2 = lonLatBean2.getLat();
            if (lon1.equals("0.00000000") || lat1.equals("0.00000000") || lon2.equals("0.00000000") || lat2.equals("0.00000000")) {
            } else {
                LatLng latLng1 = new LatLng(Double.parseDouble(lat1), Double.parseDouble(lon1));
                LatLng latLng2 = new LatLng(Double.parseDouble(lat2), Double.parseDouble(lon2));
                Double d = DistanceUtil.getDistance(latLng1, latLng2);
                String distance = null;
                if (d < 1) {
                    distance = "离我1米";
                } else if (d >= 1 && d < 1000) {
                    String temp = d + "";
                    if (temp.contains(".")) {
                        int point = temp.indexOf(".");
                        distance = "离我" + temp.substring(0, point) + "米";
                    }
                } else {
                    distance = "离我" + String.format("%.2f", d / 1000) + "公里";
                }
                return distance;
            }
        }
        return null;
    }

    //技能
    public static List<SkillsBean> getSkillBeanList(String json) {
        List<SkillsBean> resultList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 200) {
                JSONArray dataArr = beanObj.optJSONArray("data");
                if (dataArr != null) {
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject o = dataArr.optJSONObject(i);
                        if (o != null) {
                            SkillsBean skillsBean = new SkillsBean();
                            skillsBean.setS_id(o.optString("s_id"));
                            skillsBean.setS_name(o.optString("s_name"));
                            skillsBean.setS_info(o.optString("s_info"));
                            skillsBean.setS_desc(o.optString("s_desc"));
                            skillsBean.setS_status(o.optString("s_status"));
                            skillsBean.setImg(o.optString("img"));
                            resultList.add(skillsBean);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    //技能名称
    public static List<String> getSkillNameList(String json, List<String> skillIdList) {
        List<String> skillNameList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 200) {
                JSONArray dataArr = beanObj.optJSONArray("data");
                if (dataArr != null) {
                    for (int i = 0; i < skillIdList.size(); i++) {
                        for (int j = 0; j < dataArr.length(); j++) {
                            JSONObject o = dataArr.optJSONObject(j);
                            if (o != null) {
                                if (o.optString("s_id").equals(skillIdList.get(i))) {
                                    skillNameList.add(o.optString("s_name"));
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return skillNameList;
    }

    public static String getSkillName(String json, String skillId) {
        String skillName = null;
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 200) {
                JSONArray dataArr = beanObj.optJSONArray("data");
                if (dataArr != null) {
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject o = dataArr.optJSONObject(i);
                        if (o != null) {
                            if (o.optString("s_id").equals(skillId)) {
                                skillName = o.optString("s_name");
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return skillName;
    }

    //工人
    public static List<WorkerBean> getWorkerBeanList(String workerJson) {
        List<WorkerBean> workerBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(workerJson);
            int code = beanObj.optInt("code");
            switch (code) {
                case 1:
                    JSONObject dataObj = beanObj.optJSONObject("data");
                    if (dataObj != null) {
                        JSONArray dataArr = dataObj.optJSONArray("data");
                        if (dataArr != null) {
                            for (int i = 0; i < dataArr.length(); i++) {
                                JSONObject o = dataArr.optJSONObject(i);
                                if (o != null) {
                                    WorkerBean workerBean = new WorkerBean();
                                    workerBean.setU_id(o.optString("u_id"));
                                    workerBean.setU_mobile(o.optString("u_mobile"));
                                    workerBean.setU_idcard(o.optString("u_idcard"));
                                    workerBean.setU_sex(o.optString("u_sex"));
                                    workerBean.setU_name(o.optString("u_name"));
                                    workerBean.setU_skills(o.optString("u_skills"));
                                    workerBean.setUei_info(o.optString("uei_info"));
                                    workerBean.setU_task_status(o.optString("u_task_status"));
                                    workerBean.setU_true_name(o.optString("u_true_name"));
                                    workerBean.setUcp_posit_x(o.optString("ucp_posit_x"));
                                    workerBean.setUcp_posit_y(o.optString("ucp_posit_y"));
                                    workerBean.setUei_address(o.optString("uei_address"));
                                    workerBean.setU_in_time(o.optString("u_in_time"));
                                    workerBean.setU_last_edit_time(o.optString("u_last_edit_time"));
                                    workerBean.setU_online(o.optString("u_online"));
                                    workerBean.setU_start(o.optString("u_start"));
                                    workerBean.setU_credit(o.optString("u_credit"));
                                    workerBean.setU_top(o.optString("u_top"));
                                    workerBean.setU_recommend(o.optString("u_recommend"));
                                    workerBean.setU_jobs_num(o.optString("u_jobs_num"));
                                    workerBean.setU_worked_num(o.optString("u_worked_num"));
                                    workerBean.setU_high_opinions(o.optString("u_high_opinions"));
                                    workerBean.setU_low_opinions(o.optString("u_low_opinions"));
                                    workerBean.setU_middile_opinions(o.optString("u_middle_opinions"));
                                    workerBean.setU_dissensions(o.optString("u_dissensions"));
                                    workerBean.setU_img(o.optString("u_img"));
                                    workerBean.setIs_fav(o.optInt("is_fav"));
                                    workerBean.setRelation(o.optInt("relation"));
                                    workerBean.setRelation_type(o.optInt("relation_type"));
                                    workerBean.setO_confirm(o.optString("o_confirm"));
                                    workerBeanList.add(workerBean);
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workerBeanList;
    }

    //收藏的工人
    public static List<CollectWorkerBean> getCollectWorkerBeanList(String json) {
        List<CollectWorkerBean> collectWorkerBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 1) {
                JSONObject dataObj = beanObj.optJSONObject("data");
                if (dataObj != null) {
                    JSONArray dataArr = dataObj.optJSONArray("data");
                    if (dataArr != null) {
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject o = dataArr.optJSONObject(i);
                            if (o != null) {
                                CollectWorkerBean collectWorkerBean = new CollectWorkerBean();
                                collectWorkerBean.setU_id(o.optString("u_id"));
                                collectWorkerBean.setU_img(o.optString("u_img"));
                                collectWorkerBean.setU_name(o.optString("u_name"));
                                collectWorkerBean.setU_task_status(o.optString("u_task_status"));
                                collectWorkerBean.setU_sex(o.optString("u_sex"));
                                collectWorkerBean.setUei_info(o.optString("uei_info"));
                                collectWorkerBean.setUcp_posit_x(o.optString("ucp_posit_x"));
                                collectWorkerBean.setUcp_posit_y(o.optString("ucp_posit_y"));
                                collectWorkerBean.setF_id(o.optString("f_id"));
                                collectWorkerBeanList.add(collectWorkerBean);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return collectWorkerBeanList;
    }

    //任务
    public static List<TaskBean> getTaskBeanList(String taskJson) {
        List<TaskBean> taskBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(taskJson);
            int code = beanObj.optInt("code");
            switch (code) {
                case 200:
                    JSONArray dataArr = beanObj.optJSONArray("data");
                    if (dataArr != null) {
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject obj = dataArr.optJSONObject(i);
                            if (obj != null) {
                                TaskBean taskBean = new TaskBean();
                                List<TaskWorkerBean> taskWorkerBeanList = new ArrayList<>();
                                taskBean.setTaskId(obj.optString("t_id"));
                                taskBean.setIcon(obj.optString("u_img"));
                                taskBean.setTitle(obj.optString("t_title"));
                                taskBean.setInfo(obj.optString("t_info"));
                                taskBean.setStatus(obj.optString("t_status"));
                                taskBean.setFavorite(obj.optInt("favorate"));
                                taskBean.setAuthorId(obj.optString("t_author"));
                                taskBean.setTewId(obj.optString("tew_id"));
                                taskBean.setPosX(obj.optString("t_posit_x"));
                                taskBean.setPosY(obj.optString("t_posit_y"));
                                JSONArray workerArr = obj.optJSONArray("workers");
                                if (workerArr != null) {
                                    for (int j = 0; j < workerArr.length(); j++) {
                                        JSONObject o = workerArr.optJSONObject(j);
                                        if (o != null) {
                                            TaskWorkerBean taskWorkerBean = new TaskWorkerBean();
                                            taskWorkerBean.setTewId(o.optString("tew_id"));
                                            taskWorkerBean.setTewSkill(o.optString("tew_skills"));
                                            taskWorkerBeanList.add(taskWorkerBean);
                                        }
                                    }
                                }
                                taskBean.setTaskWorkerBeanList(taskWorkerBeanList);
                                taskBeanList.add(taskBean);
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taskBeanList;
    }

    //收藏的任务
    public static List<TaskBean> getCollectTaskList(String json) {
        List<TaskBean> collectTaskList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 1:
                    JSONObject dataObj = beanObj.optJSONObject("data");
                    if (dataObj != null) {
                        JSONArray dataArr = dataObj.optJSONArray("data");
                        if (dataArr != null) {
                            for (int i = 0; i < dataArr.length(); i++) {
                                JSONObject o = dataArr.optJSONObject(i);
                                if (o != null) {
                                    TaskBean taskBean = new TaskBean();
                                    taskBean.setIcon(o.optString("u_img"));
                                    taskBean.setTitle(o.optString("t_title"));
                                    taskBean.setStatus(o.optString("t_status"));
                                    taskBean.setCollectId(o.optString("f_id"));
                                    collectTaskList.add(taskBean);
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return collectTaskList;
    }

    //收到的评价
    public static List<EvaluateBean> getEvaluateBeanList(String json) {
        List<EvaluateBean> evaluateBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 1:
                    JSONObject dataObj = beanObj.optJSONObject("data");
                    if (dataObj != null) {
                        JSONArray dataArr = dataObj.optJSONArray("data");
                        if (dataArr != null) {
                            for (int i = 0; i < dataArr.length(); i++) {
                                JSONObject o = dataArr.optJSONObject(i);
                                if (o != null) {
                                    EvaluateBean evaluateBean = new EvaluateBean();
                                    evaluateBean.setIcon(o.optString("u_img"));
                                    evaluateBean.setInfo(o.optString("tce_desc"));
                                    evaluateBean.setTime(o.optString("tc_in_time"));
                                    evaluateBeanList.add(evaluateBean);
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return evaluateBeanList;
    }

    //红包
    public static List<RedPacketBean> getRedPacketBeanList(String json) {
        List<RedPacketBean> redPacketBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 200:
                    JSONArray dataArr = beanObj.optJSONArray("data");
                    if (dataArr != null) {
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject o = dataArr.optJSONObject(i);
                            if (o != null) {
                                RedPacketBean redPacketBean = new RedPacketBean();
                                redPacketBean.setB_id(o.optString("b_id"));
                                redPacketBean.setBt_id(o.optString("bt_id"));
                                redPacketBean.setB_start_time(o.optString("b_start_time"));
                                redPacketBean.setB_end_time(o.optString("b_end_time"));
                                redPacketBean.setB_total(o.optString("b_total"));
                                redPacketBean.setB_offset(o.optString("b_offset"));
                                redPacketBean.setB_status(o.optString("b_status"));
                                redPacketBean.setB_type(o.optString("b_type"));
                                redPacketBean.setB_author(o.optString("b_author"));
                                redPacketBean.setB_in_time(o.optString("b_in_time"));
                                redPacketBean.setB_last_editor(o.optString("b_last_editor"));
                                redPacketBean.setB_last_edit_time(o.optString("b_last_edit_time"));
                                redPacketBean.setB_info(o.optString("b_info"));
                                redPacketBean.setB_amount(o.optString("b_amount"));
                                redPacketBean.setB_use_amount(o.optString("b_use_amount"));
                                redPacketBean.setBd_id(o.optString("bd_id"));
                                redPacketBean.setBd_serial(o.optString("bd_serial"));
                                redPacketBean.setBd_author(o.optString("bd_author"));
                                redPacketBean.setBd_use_time(o.optString("bd_use_time"));
                                redPacketBean.setBt_name(o.optString("bt_name"));
                                redPacketBean.setBt_in_time(o.optString("bt_in_time"));
                                redPacketBean.setBt_author(o.optString("bt_author"));
                                redPacketBean.setBt_last_editor(o.optString("bt_last_editor"));
                                redPacketBean.setBt_last_edit_time(o.optString("bt_last_edit_time"));
                                redPacketBean.setBt_info(o.optString("bt_info"));
                                redPacketBean.setBt_withdraw(o.optString("bt_withdraw"));
                                redPacketBeanList.add(redPacketBean);
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return redPacketBeanList;
    }

    //代金券
    public static List<VoucherBean> getVoucherBeanList(String json) {
        List<VoucherBean> voucherBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 200:
                    JSONArray dataArr = beanObj.optJSONArray("data");
                    if (dataArr != null) {
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject o = dataArr.optJSONObject(i);
                            if (o != null) {
                                VoucherBean voucherBean = new VoucherBean();
                                voucherBean.setAmount(o.optString("b_amount"));
                                voucherBean.setStartTime(o.optString("b_start_time"));
                                voucherBean.setEndTime(o.optString("b_end_time"));
                                voucherBean.setInfo(o.optString("b_info"));
                                voucherBeanList.add(voucherBean);
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return voucherBeanList;
    }

    //雇主管理点击洽谈
    public static List<EmployerToTalkBean> getEmployerToTalkBeanList(String json) {
        List<EmployerToTalkBean> employerToTalkBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 200:
                    JSONObject dataObj = beanObj.optJSONObject("data");
                    if (dataObj != null) {
                        JSONArray workerArr = dataObj.optJSONArray("t_workers");
                        if (workerArr != null) {
                            for (int i = 0; i < workerArr.length(); i++) {
                                JSONObject workerObj = workerArr.optJSONObject(i);
                                if (workerObj != null) {
                                    EmployerToTalkBean workerEttb = new EmployerToTalkBean();
                                    workerEttb.setType(0);
                                    workerEttb.setSkillId(workerObj.optString("tew_skills"));
                                    workerEttb.setAmount(workerObj.optString("tew_worker_num"));
                                    workerEttb.setPrice(workerObj.optString("tew_price"));
                                    workerEttb.setStartTime(workerObj.optString("tew_start_time"));
                                    workerEttb.setEndTime(workerObj.optString("tew_end_time"));
                                    JSONArray orderArr = workerObj.optJSONArray("orders");
                                    if (orderArr != null) {
                                        if (orderArr.length() != 0) {
                                            employerToTalkBeanList.add(workerEttb);
                                        }
                                        for (int j = 0; j < orderArr.length(); j++) {
                                            JSONObject orderObj = orderArr.optJSONObject(j);
                                            if (orderObj != null) {
                                                EmployerToTalkBean orderEttb = new EmployerToTalkBean();
                                                orderEttb.setType(1);
                                                orderEttb.setSkillId(workerEttb.getSkillId());
                                                orderEttb.setWorkerIcon(orderObj.optString("u_img"));
                                                orderEttb.setWorkerName(orderObj.optString("u_true_name"));
                                                orderEttb.setWorkerSex(orderObj.optString("u_sex"));
                                                orderEttb.setWorkerStatus(orderObj.optString("u_task_status"));
                                                orderEttb.setMobile(orderObj.optString("u_mobile"));
                                                orderEttb.setWorkerId(orderObj.optString("o_worker"));
                                                orderEttb.setOrderId(orderObj.optString("o_id"));
                                                orderEttb.setTewId(orderObj.optString("tew_id"));
                                                orderEttb.setTaskId(orderObj.optString("t_id"));
                                                orderEttb.setO_status(orderObj.optString("o_status"));
                                                orderEttb.setO_confirm(orderObj.optString("o_confirm"));
                                                orderEttb.setAmount(workerEttb.getAmount());
                                                orderEttb.setPrice(workerEttb.getPrice());
                                                orderEttb.setStartTime(workerEttb.getStartTime());
                                                orderEttb.setEndTime(workerEttb.getEndTime());
                                                employerToTalkBeanList.add(orderEttb);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return employerToTalkBeanList;
    }

    //雇主管理点击进行
    public static List<EmployerToDoingBean> getEmployerToDoingBeanList(String json) {
        List<EmployerToDoingBean> employerToDoingBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 200:
                    JSONObject dataObj = beanObj.optJSONObject("data");
                    if (dataObj != null) {
                        JSONArray workerArr = dataObj.optJSONArray("t_workers");
                        if (workerArr != null) {
                            for (int i = 0; i < workerArr.length(); i++) {
                                JSONObject obj = workerArr.optJSONObject(i);
                                if (obj != null) {
                                    JSONArray arr = obj.optJSONArray("orders");
                                    if (arr != null) {
                                        if (arr.length() != 0) {
                                            EmployerToDoingBean e0 = new EmployerToDoingBean();
                                            e0.setType(0);
                                            e0.setSkillId(obj.optString("tew_skills"));
                                            e0.setStartTime(obj.optString("tew_start_time"));
                                            e0.setEndTime(obj.optString("tew_end_time"));
                                            employerToDoingBeanList.add(e0);
                                            for (int j = 0; j < arr.length(); j++) {
                                                JSONObject o = arr.optJSONObject(j);
                                                if (o != null) {
                                                    EmployerToDoingBean e1 = new EmployerToDoingBean();
                                                    e1.setType(1);
                                                    e1.setIcon(o.optString("u_img"));
                                                    e1.setName(o.optString("u_true_name"));
                                                    e1.setSkillId(e0.getSkillId());
                                                    e1.setStatus(o.optString("u_task_status"));
                                                    e1.setSex(o.optString("u_sex"));
                                                    e1.setWorkerId(o.optString("o_worker"));
                                                    e1.setOrderId(o.optString("o_id"));
                                                    e1.setTaskId(o.optString("t_id"));
                                                    e1.setTewId(o.optString("tew_id"));
                                                    e1.setO_status(o.optString("o_status"));
                                                    e1.setO_confirm(o.optString("o_confirm"));
                                                    employerToDoingBeanList.add(e1);
                                                }
                                            }
                                            EmployerToDoingBean e2 = new EmployerToDoingBean();
                                            e2.setType(2);
                                            employerToDoingBeanList.add(e2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return employerToDoingBeanList;
    }

    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String msgTimes(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static List<WorkerManageBean> getWorkerManageBeanList(String json) {
        List<WorkerManageBean> resultList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 200) {
                JSONArray dataArr = beanObj.optJSONArray("data");
                if (dataArr != null) {
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject obj = dataArr.optJSONObject(i);
                        if (obj != null) {
                            String status = obj.optString("o_status");
                            WorkerManageBean workerManageBean = new WorkerManageBean();
                            workerManageBean.setIcon(obj.optString("u_img"));
                            workerManageBean.setTitle(obj.optString("t_title"));
                            workerManageBean.setInfo(obj.optString("t_info"));
                            workerManageBean.setO_status(status);
                            workerManageBean.setO_confirm(obj.optString("o_confirm"));
                            workerManageBean.setTaskId(obj.optString("t_id"));
                            workerManageBean.setOrderId(obj.optString("o_id"));
                            resultList.add(workerManageBean);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    //任务管理
    public static List<EmployerManageBean> getEmployerManageBeanList(String json) {
        List<EmployerManageBean> resultList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 200) {
                JSONArray dataArr = beanObj.optJSONArray("data");
                if (dataArr != null) {
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject obj = dataArr.optJSONObject(i);
                        if (obj != null) {
                            EmployerManageBean employerManageBean = new EmployerManageBean();
                            employerManageBean.setTaskId(obj.optString("t_id"));
                            employerManageBean.setIcon(obj.optString("u_img"));
                            employerManageBean.setTitle(obj.optString("t_title"));
                            employerManageBean.setInfo(obj.optString("t_info"));
                            employerManageBean.setStatus(obj.optString("t_status"));
                            JSONArray jsonArray = obj.optJSONArray("workers");
                            JSONObject jsObj = jsonArray.optJSONObject(0);
                            employerManageBean.setArea(jsObj.optString("tew_address"));
                            resultList.add(employerManageBean);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    //用户详细信息
    public static UserInfoBean getUserInfoBean(String json) {
        UserInfoBean userInfoBean = null;
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 1) {
                JSONObject objData = objBean.optJSONObject("data");
                if (objData != null) {
                    JSONObject obj = objData.optJSONObject("data");
                    if (obj != null) {
                        userInfoBean = new UserInfoBean();
                        userInfoBean.setU_id(obj.optString("u_id"));
                        userInfoBean.setU_name(obj.optString("u_name"));
                        userInfoBean.setU_mobile(obj.optString("u_mobile"));
                        userInfoBean.setU_phone(obj.optString("u_phone"));
                        userInfoBean.setU_fax(obj.optString("u_fax"));
                        userInfoBean.setU_sex(obj.optString("u_sex"));
                        userInfoBean.setU_in_time(obj.optString("u_in_time"));
                        userInfoBean.setU_online(obj.optString("u_online"));
                        userInfoBean.setU_status(obj.optString("u_status"));
                        userInfoBean.setU_type(obj.optString("u_type"));
                        userInfoBean.setU_task_status(obj.optString("u_task_status"));
                        userInfoBean.setU_skills(obj.optString("u_skills"));
                        userInfoBean.setU_start(obj.optString("u_start"));
                        userInfoBean.setU_credit(obj.optString("u_credit"));
                        userInfoBean.setU_top(obj.optString("u_top"));
                        userInfoBean.setU_recommend(obj.optString("u_recommend"));
                        userInfoBean.setU_jobs_num(obj.optString("u_jobs_num"));
                        userInfoBean.setU_worked_num(obj.optString("u_worked_num"));
                        userInfoBean.setU_high_opinions(obj.optString("u_high_opinions"));
                        userInfoBean.setU_low_opinions(obj.optString("u_low_opinions"));
                        userInfoBean.setU_middle_opinions(obj.optString("u_middle_opinions"));
                        userInfoBean.setU_dissensions(obj.optString("u_dissensions"));
                        userInfoBean.setU_true_name(obj.optString("u_true_name"));
                        userInfoBean.setU_idcard(obj.optString("u_idcard"));
                        userInfoBean.setU_info(obj.optString("u_info"));
                        JSONObject objArea = obj.optJSONObject("area");
                        if (objArea != null) {
                            userInfoBean.setUei_province(objArea.optString("uei_province"));
                            userInfoBean.setUei_city(objArea.optString("uei_city"));
                            userInfoBean.setUei_area(objArea.optString("uei_area"));
                            userInfoBean.setUei_address(objArea.optString("uei_address"));
                            userInfoBean.setUser_area_name(objArea.optString("user_area_name"));
                        }
                        userInfoBean.setU_img(obj.optString("u_img"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfoBean;
    }

    //投诉问题
    public static List<ComplainIssueBean> getComplainIssueBeanList(String json) {
        List<ComplainIssueBean> complainIssueBeanList = new ArrayList<>();
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 1:
                    JSONArray dataArr = beanObj.optJSONArray("data");
                    if (dataArr != null) {
                        JSONObject dataObj = dataArr.optJSONObject(0);
                        if (dataObj != null) {
                            JSONArray arr = dataObj.optJSONArray("data");
                            if (arr != null) {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject ob = arr.optJSONObject(i);
                                    if (ob != null) {
                                        ComplainIssueBean cib = new ComplainIssueBean();
                                        cib.setId(ob.optString("ct_id"));
                                        cib.setName(ob.optString("ct_name"));
                                        complainIssueBeanList.add(cib);
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return complainIssueBeanList;
    }

    public static TalkEmployerBean getTalkEmployerBean(String json) {
        TalkEmployerBean talkEmployerBean = new TalkEmployerBean();
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 200) {
                JSONObject dataObj = beanObj.optJSONObject("data");
                talkEmployerBean.setIcon(dataObj.optString("u_img"));
                talkEmployerBean.setMobile(dataObj.optString("t_phone"));
                talkEmployerBean.setName(dataObj.optString("u_true_name"));
                talkEmployerBean.setSex(dataObj.optString("u_sex"));
                talkEmployerBean.setAddress(dataObj.optString("tew_address"));
                talkEmployerBean.setDesc(dataObj.optString("t_desc"));
                talkEmployerBean.setPosX(dataObj.optString("t_posit_x"));
                talkEmployerBean.setPosY(dataObj.optString("t_posit_y"));
                talkEmployerBean.setAuthorId(dataObj.optString("t_author"));
                talkEmployerBean.setRelation(dataObj.optInt("relation"));
                talkEmployerBean.setRelationType(dataObj.optInt("relation_type"));
                talkEmployerBean.setT_status(dataObj.optString("t_status"));
                JSONArray workerArr = dataObj.optJSONArray("t_workers");
                if (workerArr != null) {
                    List<TalkEmployerWorkerBean> talkEmployerWorkerBeanList = new ArrayList<>();
                    for (int i = 0; i < workerArr.length(); i++) {
                        JSONObject obj = workerArr.optJSONObject(i);
                        if (obj != null) {
                            if (obj.optInt("remaining") != 0) {
                                TalkEmployerWorkerBean t = new TalkEmployerWorkerBean();
                                t.setId(obj.optString("tew_skills"));
                                t.setAmount(obj.optString("tew_worker_num"));
                                t.setPrice(obj.optString("tew_price"));
                                t.setStartTime(obj.optString("tew_start_time"));
                                t.setEndTime(obj.optString("tew_end_time"));
                                t.setTewId(obj.optString("tew_id"));
                                talkEmployerWorkerBeanList.add(t);
                            }
                        }
                    }
                    talkEmployerBean.setTalkEmployerWorkerBeanList(talkEmployerWorkerBeanList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return talkEmployerBean;
    }

    public static String getPublishJson(PublishBean publishBean, boolean havePass) {
        JSONObject object = new JSONObject();
        try {
            if (havePass) {
                object.put("u_pass", publishBean.getPass());
            }
            object.put("t_title", publishBean.getTitle());
            object.put("t_info", publishBean.getInfo());
            object.put("t_posit_x", publishBean.getPositionX());
            object.put("t_posit_y", publishBean.getPositionY());
            object.put("t_author", publishBean.getAuthorId());
            object.put("t_type", publishBean.getTypeId());
            object.put("province", publishBean.getProvinceId());
            object.put("city", publishBean.getCityId());
            object.put("area", publishBean.getAreaId());
            object.put("address", publishBean.getAddress());
            object.put("t_storage", "0");
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < publishBean.getPublishWorkerBeanList().size(); i++) {
                PublishWorkerBean publishWorkerBean = publishBean.getPublishWorkerBeanList().get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("skill", publishWorkerBean.getId());
                jsonObject.put("personNum", publishWorkerBean.getAmount());
                jsonObject.put("money", publishWorkerBean.getSalary());
                jsonObject.put("startTime", publishWorkerBean.getStartTime());
                jsonObject.put("endTime", publishWorkerBean.getEndTime());
                jsonArray.put(jsonObject);
            }
            object.put("worker", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    //任务详情
    public static TInfoTaskBean getTInfoTaskBean(String json) {
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 200) {
                JSONObject dataObj = beanObj.optJSONObject("data");
                if (dataObj != null) {
                    TInfoTaskBean tInfoTaskBean = new TInfoTaskBean();
                    tInfoTaskBean.setT_id(dataObj.optString("t_id"));
                    tInfoTaskBean.setT_title(dataObj.optString("t_title"));
                    tInfoTaskBean.setT_info(dataObj.optString("t_info"));
                    tInfoTaskBean.setT_amount(dataObj.optString("t_amount"));
                    tInfoTaskBean.setT_duration(dataObj.optString("t_duration"));
                    tInfoTaskBean.setT_edit_amount(dataObj.optString("t_edit_amount"));
                    tInfoTaskBean.setT_amount_edit_times(dataObj.optString("t_amount_edit_times"));
                    tInfoTaskBean.setT_posit_x(dataObj.optString("t_posit_x"));
                    tInfoTaskBean.setT_posit_y(dataObj.optString("t_posit_y"));
                    tInfoTaskBean.setT_author(dataObj.optString("t_author"));
                    tInfoTaskBean.setT_in_time(dataObj.optString("t_in_time"));
                    tInfoTaskBean.setT_last_edit_time(dataObj.optString("t_last_edit_time"));
                    tInfoTaskBean.setT_last_editor(dataObj.optString("t_last_editor"));
                    tInfoTaskBean.setT_status(dataObj.optString("t_status"));
                    tInfoTaskBean.setT_phone(dataObj.optString("t_phone"));
                    tInfoTaskBean.setT_phone_status(dataObj.optString("t_phone_status"));
                    tInfoTaskBean.setT_type(dataObj.optString("t_type"));
                    tInfoTaskBean.setT_storage(dataObj.optString("t_storage"));
                    tInfoTaskBean.setBd_id(dataObj.optString("bd_id"));
                    tInfoTaskBean.setU_id(dataObj.optString("u_id"));
                    tInfoTaskBean.setU_name(dataObj.optString("u_name"));
                    tInfoTaskBean.setU_mobile(dataObj.optString("u_mobile"));
                    tInfoTaskBean.setU_sex(dataObj.optString("u_sex"));
                    tInfoTaskBean.setU_online(dataObj.optString("u_online"));
                    tInfoTaskBean.setU_status(dataObj.optString("u_status"));
                    tInfoTaskBean.setU_task_status(dataObj.optString("u_task_status"));
                    tInfoTaskBean.setU_start(dataObj.optString("u_start"));
                    tInfoTaskBean.setU_credit(dataObj.optString("u_credit"));
                    tInfoTaskBean.setU_jobs_num(dataObj.optString("u_jobs_num"));
                    tInfoTaskBean.setU_recommend(dataObj.optString("u_recommend"));
                    tInfoTaskBean.setU_worked_num(dataObj.optString("u_worked_num"));
                    tInfoTaskBean.setU_high_opinions(dataObj.optString("u_high_opinions"));
                    tInfoTaskBean.setU_low_opinions(dataObj.optString("u_low_opinions"));
                    tInfoTaskBean.setU_middle_opinions(dataObj.optString("u_middle_opinions"));
                    tInfoTaskBean.setU_dissensions(dataObj.optString("u_dissensions"));
                    tInfoTaskBean.setU_true_name(dataObj.optString("u_true_name"));
                    tInfoTaskBean.setU_img(dataObj.optString("u_img"));
                    tInfoTaskBean.setR_province(dataObj.optString("r_province"));
                    tInfoTaskBean.setR_city(dataObj.optString("r_city"));
                    tInfoTaskBean.setR_area(dataObj.optString("r_area"));
                    tInfoTaskBean.setTew_address(dataObj.optString("tew_address"));
                    tInfoTaskBean.setT_desc(dataObj.optString("t_desc"));
                    JSONArray workerArr = dataObj.optJSONArray("t_workers");
                    List<TInfoWorkerBean> tInfoWorkerBeanList = new ArrayList<>();
                    if (workerArr != null) {
                        for (int i = 0; i < workerArr.length(); i++) {
                            JSONObject workerObj = workerArr.optJSONObject(i);
                            if (workerObj != null) {
                                TInfoWorkerBean tInfoWorkerBean = new TInfoWorkerBean();
                                tInfoWorkerBean.setTew_id(workerObj.optString("tew_id"));
                                tInfoWorkerBean.setT_id(workerObj.optString("t_id"));
                                tInfoWorkerBean.setTew_skills(workerObj.optString("tew_skills"));
                                tInfoWorkerBean.setTew_worker_num(workerObj.optString("tew_worker_num"));
                                tInfoWorkerBean.setTew_price(workerObj.optString("tew_price"));
                                tInfoWorkerBean.setTew_start_time(workerObj.optString("tew_start_time"));
                                tInfoWorkerBean.setTew_end_time(workerObj.optString("tew_end_time"));
                                tInfoWorkerBean.setR_province(workerObj.optString("r_province"));
                                tInfoWorkerBean.setR_city(workerObj.optString("r_city"));
                                tInfoWorkerBean.setR_area(workerObj.optString("r_area"));
                                tInfoWorkerBean.setTew_address(workerObj.optString("tew_address"));
                                tInfoWorkerBean.setTew_lock(workerObj.optString("tew_lock"));
                                tInfoWorkerBean.setTew_status(workerObj.optString("tew_status"));
                                tInfoWorkerBean.setTew_type(workerObj.optString("tew_type"));
                                tInfoWorkerBean.setRemaining(workerObj.optInt("remaining"));
                                JSONArray orderArr = workerObj.optJSONArray("orders");
                                List<TInfoOrderBean> tInfoOrderBeanList = new ArrayList<>();
                                if (orderArr != null) {
                                    for (int j = 0; j < orderArr.length(); j++) {
                                        JSONObject orderObj = orderArr.optJSONObject(j);
                                        if (orderObj != null) {
                                            TInfoOrderBean tInfoOrderBean = new TInfoOrderBean();
                                            tInfoOrderBean.setO_id(orderObj.optString("o_id"));
                                            tInfoOrderBean.setT_id(orderObj.optString("t_id"));
                                            tInfoOrderBean.setU_id(orderObj.optString("u_id"));
                                            tInfoOrderBean.setO_worker(orderObj.optString("o_worker"));
                                            tInfoOrderBean.setO_amount(orderObj.optString("o_amount"));
                                            tInfoOrderBean.setO_in_time(orderObj.optString("o_in_time"));
                                            tInfoOrderBean.setO_last_edit_time(orderObj.optString("o_last_edit_time"));
                                            tInfoOrderBean.setO_status(orderObj.optString("o_status"));
                                            tInfoOrderBean.setTew_id(orderObj.optString("tew_id"));
                                            tInfoOrderBean.setS_id(orderObj.optString("s_id"));
                                            tInfoOrderBean.setO_confirm(orderObj.optString("o_confirm"));
                                            tInfoOrderBean.setUnbind_time(orderObj.optString("unbind_time"));
                                            tInfoOrderBean.setO_pay(orderObj.optString("o_pay"));
                                            tInfoOrderBean.setO_pay_time(orderObj.optString("o_pay_time"));
                                            tInfoOrderBean.setO_sponsor(orderObj.optString("o_sponsor"));
                                            tInfoOrderBean.setO_dispute_time(orderObj.optString("o_dispute_time"));
                                            tInfoOrderBean.setO_start_time(orderObj.optString("o_start_time"));
                                            tInfoOrderBean.setO_end_time(orderObj.optString("o_end_time"));
                                            tInfoOrderBean.setU_name(orderObj.optString("u_name"));
                                            tInfoOrderBean.setU_mobile(orderObj.optString("u_mobile"));
                                            tInfoOrderBean.setU_sex(orderObj.optString("u_sex"));
                                            tInfoOrderBean.setU_online(orderObj.optString("u_online"));
                                            tInfoOrderBean.setU_status(orderObj.optString("u_status"));
                                            tInfoOrderBean.setU_task_status(orderObj.optString("u_task_status"));
                                            tInfoOrderBean.setU_start(orderObj.optString("u_start"));
                                            tInfoOrderBean.setU_credit(orderObj.optString("u_credit"));
                                            tInfoOrderBean.setU_jobs_num(orderObj.optString("u_jobs_num"));
                                            tInfoOrderBean.setU_recommend(orderObj.optString("u_recommend"));
                                            tInfoOrderBean.setU_worked_num(orderObj.optString("u_worked_num"));
                                            tInfoOrderBean.setU_high_opinions(orderObj.optString("u_high_opinions"));
                                            tInfoOrderBean.setU_low_opinions(orderObj.optString("u_low_opinions"));
                                            tInfoOrderBean.setU_middle_opinions(orderObj.optString("u_middle_opinions"));
                                            tInfoOrderBean.setU_dissensions(orderObj.optString("u_dissensions"));
                                            tInfoOrderBean.setU_true_name(orderObj.optString("u_true_name"));
                                            tInfoOrderBean.setU_img(orderObj.optString("u_img"));
                                            tInfoOrderBeanList.add(tInfoOrderBean);
                                        }
                                    }
                                }
                                tInfoWorkerBean.settInfoOrderBeanList(tInfoOrderBeanList);
                                tInfoWorkerBeanList.add(tInfoWorkerBean);
                            }
                        }
                    }
                    tInfoTaskBean.settInfoWorkerBeanList(tInfoWorkerBeanList);
                    return tInfoTaskBean;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //支付方式
    public static List<PayWayBean> getPayWayBeanList(String json) {
        if (json == null || json.equals("null") || TextUtils.isEmpty(json)) {
        } else {
            List<PayWayBean> payWayBeanList = new ArrayList<>();
            try {
                JSONObject beanObj = new JSONObject(json);
                if (beanObj.optInt("code") == 200) {
                    JSONArray dataArr = beanObj.optJSONArray("data");
                    if (dataArr != null && dataArr.length() != 0) {
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject o = dataArr.optJSONObject(i);
                            if (o != null) {
                                PayWayBean payWayBean = new PayWayBean();
                                payWayBean.setP_id(o.optString("p_id"));
                                payWayBean.setP_type(o.optString("p_type"));
                                payWayBean.setP_name(o.optString("p_name"));
                                payWayBean.setP_info(o.optString("p_info"));
                                payWayBean.setP_status(o.optString("p_status"));
                                payWayBean.setP_author(o.optString("p_author"));
                                payWayBean.setP_last_editor(o.optString("p_last_editor"));
                                payWayBean.setP_last_edit_time(o.optString("p_last_edit_time"));
                                payWayBean.setP_default(o.optString("p_default"));
                                payWayBean.setImg(o.optString("img"));
                                payWayBeanList.add(payWayBean);
                            }
                        }
                        return payWayBeanList;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //微信支付
    public static WxDataBean getWxDataBean(String json) {
        if (json == null || json.equals("null") || TextUtils.isEmpty(json)) {
        } else {
            try {
                JSONObject beanObj = new JSONObject(json);
                if (beanObj.optInt("code") == 1) {
                    JSONObject dataObj = beanObj.optJSONObject("data");
                    if (dataObj != null) {
                        WxDataBean wxDataBean = new WxDataBean();
                        wxDataBean.setAppid(dataObj.optString("appId"));
                        wxDataBean.setNoncestr(dataObj.optString("nonceStr"));
                        wxDataBean.setPackageValue(dataObj.optString("package"));
                        wxDataBean.setPartnerid(dataObj.optString("partnerid"));
                        wxDataBean.setPrepayid(dataObj.optString("prepay_id"));
                        wxDataBean.setSignType(dataObj.optString("signType"));
                        wxDataBean.setTimestamp(dataObj.optString("timeStamp"));
                        wxDataBean.setPaySign(dataObj.optString("paySign"));
                        return wxDataBean;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //微信sign
    public static String signNum(WxDataBean wxDataBean) {
        if (wxDataBean != null) {
            String stringA = "appid=" + AppConfig.APP_ID +
                    "&noncestr=" + wxDataBean.getNoncestr() +
                    "&package=" + wxDataBean.getPackageValue() +
                    "&partnerid=" + wxDataBean.getPartnerid() +
                    "&prepayid=" + wxDataBean.getPrepayid() +
                    "&timestamp=" + wxDataBean.getTimestamp();
            String stringSignTemp = stringA + "&key=" + AppConfig.WX_KEY;
            String sign = MD5.getMessageDigest(stringSignTemp.getBytes()).toUpperCase();
            return sign;
        }
        return null;
    }

    //协议
    public static List<ArticleBean> getArticleBeanList(String json) {
        try {
            JSONObject beanObj = new JSONObject(json);
            if (beanObj.optInt("code") == 1) {
                JSONArray dataArr = beanObj.optJSONArray("data");
                if (dataArr != null && dataArr.length() != 0) {
                    List<ArticleBean> articleBeanList = new ArrayList<>();
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject o = dataArr.optJSONObject(i);
                        if (o != null) {
                            ArticleBean articleBean = new ArticleBean();
                            articleBean.setA_id(o.optString("a_id"));
                            articleBean.setA_title(o.optString("a_title"));
                            articleBean.setA_in_time(o.optString("a_in_time"));
                            articleBean.setA_link(o.optString("a_link"));
                            articleBean.setA_img(o.optString("a_img"));
                            articleBean.setA_top(o.optString("a_top"));
                            articleBean.setA_recommend(o.optString("a_recommend"));
                            articleBean.setA_desc(o.optString("a_desc"));
                            articleBeanList.add(articleBean);
                        }
                    }
                    return articleBeanList;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
