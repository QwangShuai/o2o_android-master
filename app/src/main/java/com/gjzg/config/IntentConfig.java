package com.gjzg.config;

public interface IntentConfig {

    String toWorker = "to_worker";
    String screenToWorker = "screen_to_worker";
    String toTalkWorker = "to_talk_worker";
    String toTalkEmployer = "to_talk_employer";
    String toJumpEmployer = "to_jump_employer";
    String workerToTalkSkill = "worker_to_talk_skill";
    String toSelectTask = "to_select_skill";
    String toEmployerToTalk = "to_employer_to_talk";
    String toEmployerToDoing = "to_employer_to_doing";
    String toChangePrice = "to_change_price";
    String toComplain = "to_complain";
    String toJumpWorker = "to_jump_worker";
    String toResign = "to_resign";
    String toFire = "to_fire";
    String toEvaluate = "to_Evaluate";

    String taskToTalk = "task_to_talk";
    String talkToDetail = "talk_to_detail";
    String intentName = "status";//意图名称
    int COLLECT = 1;//我的收藏意图
    int EVALUATE = 2;//我的评价意图
    int MESSAGE = 3;//我的消息意图
    String CITY = "CITY";//城市意图名称
    String LOCAL_CITY = "LOCAL_CITY";//定位城市意图名称
    int CITY_REQUEST = 4;//城市请求码
    int CITY_RESULT = 5;//城市结果码
    String PIC = "PIC";//选取图片意图
    int PIC_REQUEST = 6;//选取图片请求码
    int PIC_RESULT = 7;//选取图片结果码
}
