package com.gjzg.bean;


import java.util.List;

public class Task {

    /**
     * code : 200
     * data : [{"t_id":"120","t_title":"11","t_info":"11","t_amount":"1","t_duration":"1","t_edit_amount":"1","t_amount_edit_times":"0","t_posit_x":"126.64435200","t_posit_y":"45.77871900","t_author":"11","t_in_time":"1511586961","t_last_edit_time":"1511586961","t_last_editor":"11","t_status":"1","t_phone":"13895750215","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"130","t_id":"120","tew_skills":"2","tew_worker_num":"1","tew_price":"1","tew_start_time":"1514131200","tew_end_time":"1514131200","r_province":"3","r_city":"38","r_area":"419","tew_address":"，","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/11.jpg"},{"t_id":"119","t_title":"11","t_info":"11","t_amount":"1","t_duration":"1","t_edit_amount":"1","t_amount_edit_times":"0","t_posit_x":"126.64435200","t_posit_y":"45.77871900","t_author":"11","t_in_time":"1511586961","t_last_edit_time":"1511586961","t_last_editor":"11","t_status":"0","t_phone":"13895750215","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"129","t_id":"119","tew_skills":"2","tew_worker_num":"1","tew_price":"1","tew_start_time":"1514131200","tew_end_time":"1514131200","r_province":"3","r_city":"38","r_area":"419","tew_address":"，","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/11.jpg"},{"t_id":"118","t_title":"测试","t_info":"1","t_amount":"10","t_duration":"1","t_edit_amount":"10","t_amount_edit_times":"0","t_posit_x":"126.64431900","t_posit_y":"45.77874700","t_author":"10","t_in_time":"1511580007","t_last_edit_time":"1511580007","t_last_editor":"10","t_status":"3","t_phone":"18645034186","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"128","t_id":"118","tew_skills":"1","tew_worker_num":"1","tew_price":"10","tew_start_time":"1514131200","tew_end_time":"1514131200","r_province":"4","r_city":"55","r_area":"540","tew_address":"111","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/10.jpg"},{"t_id":"117","t_title":"电工","t_info":"电路改造","t_amount":"10","t_duration":"1","t_edit_amount":"10","t_amount_edit_times":"0","t_posit_x":"126.64436600","t_posit_y":"45.77874600","t_author":"10","t_in_time":"1511579653","t_last_edit_time":"1511579653","t_last_editor":"10","t_status":"3","t_phone":"18645034186","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"127","t_id":"117","tew_skills":"1","tew_worker_num":"1","tew_price":"10","tew_start_time":"1514131200","tew_end_time":"1514131200","r_province":"12","r_city":"167","r_area":"1416","tew_address":"西大桥西大直街110号","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/10.jpg"},{"t_id":"116","t_title":"水暖工","t_info":"管道维修","t_amount":"1","t_duration":"1","t_edit_amount":"1","t_amount_edit_times":"0","t_posit_x":"126.64445200","t_posit_y":"45.77873300","t_author":"11","t_in_time":"1511578233","t_last_edit_time":"1511578233","t_last_editor":"11","t_status":"3","t_phone":"13895750215","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"126","t_id":"116","tew_skills":"2","tew_worker_num":"1","tew_price":"1","tew_start_time":"1514131200","tew_end_time":"1514131200","r_province":"12","r_city":"167","r_area":"1416","tew_address":"西大直街120号","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/11.jpg"},{"t_id":"111","t_title":"61616161","t_info":"61616161","t_amount":"500","t_duration":"1","t_edit_amount":"500","t_amount_edit_times":"1","t_posit_x":"126.64432300","t_posit_y":"45.77880900","t_author":"12","t_in_time":"1511518146","t_last_edit_time":"1511518205","t_last_editor":"12","t_status":"3","t_phone":"18045159920","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"121","t_id":"111","tew_skills":"1","tew_worker_num":"1","tew_price":"500","tew_start_time":"1514044800","tew_end_time":"1514044800","r_province":"12","r_city":"167","r_area":"1421","tew_address":"616161","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/12.jpg"},{"t_id":"110","t_title":"101010","t_info":"10101010","t_amount":"500","t_duration":"1","t_edit_amount":"500","t_amount_edit_times":"0","t_posit_x":"126.64440300","t_posit_y":"45.77878300","t_author":"12","t_in_time":"1511517205","t_last_edit_time":"1511517205","t_last_editor":"12","t_status":"3","t_phone":"18045159920","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"120","t_id":"110","tew_skills":"1","tew_worker_num":"1","tew_price":"500","tew_start_time":"1514044800","tew_end_time":"1514044800","r_province":"12","r_city":"167","r_area":"1415","tew_address":"787878787","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/12.jpg"},{"t_id":"109","t_title":"再来一单","t_info":"唠唠嗑","t_amount":"1","t_duration":"1","t_edit_amount":"1","t_amount_edit_times":"0","t_posit_x":"126.63742000","t_posit_y":"45.77918000","t_author":"8","t_in_time":"1511161184","t_last_edit_time":"1511161184","t_last_editor":"8","t_status":"2","t_phone":"15204662949","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"119","t_id":"109","tew_skills":"4","tew_worker_num":"1","tew_price":"1","tew_start_time":"1511107200","tew_end_time":"1511107200","r_province":"7","r_city":"103","r_area":"913","tew_address":"哦哦哦","tew_lock":"0","tew_status":"0","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/8.jpg"},{"t_id":"108","t_title":"呢嗯啊","t_info":"看看","t_amount":"2","t_duration":"1","t_edit_amount":"2","t_amount_edit_times":"1","t_posit_x":"126.63742000","t_posit_y":"45.77918000","t_author":"8","t_in_time":"1511160824","t_last_edit_time":"1511160934","t_last_editor":"8","t_status":"3","t_phone":"15204662949","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"118","t_id":"108","tew_skills":"3","tew_worker_num":"1","tew_price":"2","tew_start_time":"1511107200","tew_end_time":"1511107200","r_province":"8","r_city":"117","r_area":"1026","tew_address":"擦","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/8.jpg"},{"t_id":"107","t_title":"来来来","t_info":"干啥呀","t_amount":"4","t_duration":"1","t_edit_amount":"4","t_amount_edit_times":"0","t_posit_x":"126.64410000","t_posit_y":"45.77868000","t_author":"1","t_in_time":"1511160587","t_last_edit_time":"1511160587","t_last_editor":"1","t_status":"2","t_phone":"15840344241","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"117","t_id":"107","tew_skills":"3","tew_worker_num":"1","tew_price":"2","tew_start_time":"1511193600","tew_end_time":"1511280000","r_province":"3","r_city":"37","r_area":"411","tew_address":"公婆MSN","tew_lock":"0","tew_status":"0","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"104","t_title":"咳咳咳","t_info":"八寸","t_amount":"50","t_duration":"1","t_edit_amount":"50","t_amount_edit_times":"0","t_posit_x":"126.63742000","t_posit_y":"45.77918000","t_author":"8","t_in_time":"1511147954","t_last_edit_time":"1511147954","t_last_editor":"8","t_status":"3","t_phone":"15204662949","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"114","t_id":"104","tew_skills":"1","tew_worker_num":"1","tew_price":"50","tew_start_time":"1511107200","tew_end_time":"1511107200","r_province":"7","r_city":"102","r_area":"908","tew_address":"唠唠嗑","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/8.jpg"},{"t_id":"103","t_title":"测试多人","t_info":"吧","t_amount":"9","t_duration":"1","t_edit_amount":"9","t_amount_edit_times":"0","t_posit_x":"126.64434800","t_posit_y":"45.77872600","t_author":"10","t_in_time":"1511146881","t_last_edit_time":"1511146881","t_last_editor":"10","t_status":"3","t_phone":"18645034186","t_phone_status":"1","t_type":"2","t_storage":"0","bd_id":"0","workers":[{"tew_id":"113","t_id":"103","tew_skills":"5","tew_worker_num":"2","tew_price":"1","tew_start_time":"1513699200","tew_end_time":"1513699200","r_province":"6","r_city":"80","r_area":"750","tew_address":"哦哦哦","tew_lock":"0","tew_status":"1","tew_type":"0"},{"tew_id":"112","t_id":"103","tew_skills":"3","tew_worker_num":"1","tew_price":"5","tew_start_time":"1513699200","tew_end_time":"1513699200","r_province":"6","r_city":"80","r_area":"750","tew_address":"哦哦哦","tew_lock":"0","tew_status":"1","tew_type":"1"},{"tew_id":"111","t_id":"103","tew_skills":"1","tew_worker_num":"1","tew_price":"2","tew_start_time":"1513699200","tew_end_time":"1513699200","r_province":"6","r_city":"80","r_area":"750","tew_address":"哦哦哦","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/10.jpg"},{"t_id":"100","t_title":"你好工作","t_info":"萝莉控","t_amount":"10","t_duration":"1","t_edit_amount":"10","t_amount_edit_times":"0","t_posit_x":"126.63742000","t_posit_y":"45.77918000","t_author":"8","t_in_time":"1511145964","t_last_edit_time":"1511145964","t_last_editor":"8","t_status":"3","t_phone":"15204662949","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"106","t_id":"100","tew_skills":"1","tew_worker_num":"1","tew_price":"10","tew_start_time":"1511107200","tew_end_time":"1511107200","r_province":"5","r_city":"65","r_area":"628","tew_address":"看看","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/8.jpg"},{"t_id":"99","t_title":"测试招聘北京工人","t_info":"啊啊啊啊啊啊啊","t_amount":"100","t_duration":"1","t_edit_amount":"100","t_amount_edit_times":"0","t_posit_x":"126.63742000","t_posit_y":"45.77918000","t_author":"8","t_in_time":"1511143829","t_last_edit_time":"1511143829","t_last_editor":"8","t_status":"3","t_phone":"15204662949","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"105","t_id":"99","tew_skills":"4","tew_worker_num":"1","tew_price":"100","tew_start_time":"1511193600","tew_end_time":"1511193600","r_province":"2","r_city":"52","r_area":"502","tew_address":"中科院","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/8.jpg"},{"t_id":"95","t_title":"刮大白","t_info":"来干吧","t_amount":"200","t_duration":"1","t_edit_amount":"200","t_amount_edit_times":"0","t_posit_x":"126.64449000","t_posit_y":"45.77857000","t_author":"16","t_in_time":"1510822280","t_last_edit_time":"1510822280","t_last_editor":"16","t_status":"3","t_phone":"18714505321","t_phone_status":"1","t_type":"2","t_storage":"0","bd_id":"0","workers":[{"tew_id":"101","t_id":"95","tew_skills":"6","tew_worker_num":"2","tew_price":"100","tew_start_time":"1510848000","tew_end_time":"1510848000","r_province":"12","r_city":"167","r_area":"1421","tew_address":"开原街","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/16.jpg"},{"t_id":"90","t_title":"贴砖 好活","t_info":"旅游部要求长城贴瓷砖，特地雇佣泥瓦匠","t_amount":"100","t_duration":"1","t_edit_amount":"100","t_amount_edit_times":"0","t_posit_x":"126.64425800","t_posit_y":"45.77871500","t_author":"15","t_in_time":"1510822077","t_last_edit_time":"1510822077","t_last_editor":"15","t_status":"3","t_phone":"13936562027","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"96","t_id":"90","tew_skills":"3","tew_worker_num":"1","tew_price":"100","tew_start_time":"1513440000","tew_end_time":"1513440000","r_province":"2","r_city":"52","r_area":"500","tew_address":"长城门口景区内","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/15.jpg"},{"t_id":"79","t_title":"郭健无敌版","t_info":"那是真的无敌","t_amount":"4","t_duration":"1","t_edit_amount":"4","t_amount_edit_times":"0","t_posit_x":"126.64414000","t_posit_y":"45.77860000","t_author":"1","t_in_time":"1510816667","t_last_edit_time":"1510816667","t_last_editor":"1","t_status":"0","t_phone":"15840344241","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"84","t_id":"79","tew_skills":"2","tew_worker_num":"1","tew_price":"2","tew_start_time":"1510761600","tew_end_time":"1510848000","r_province":"2","r_city":"52","r_area":"500","tew_address":"强的一匹","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"76","t_title":"电工","t_info":"。","t_amount":"10","t_duration":"1","t_edit_amount":"10","t_amount_edit_times":"1","t_posit_x":"126.64432900","t_posit_y":"45.77873700","t_author":"5","t_in_time":"1510736706","t_last_edit_time":"1510736763","t_last_editor":"5","t_status":"1","t_phone":"15104629758","t_phone_status":"1","t_type":"2","t_storage":"0","bd_id":"0","workers":[{"tew_id":"81","t_id":"76","tew_skills":"4","tew_worker_num":"1","tew_price":"10","tew_start_time":"1513267200","tew_end_time":"1513267200","r_province":"3","r_city":"37","r_area":"410","tew_address":"。","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/5.jpg"},{"t_id":"73","t_title":"水暖","t_info":"改暖气","t_amount":"4","t_duration":"1","t_edit_amount":"4","t_amount_edit_times":"1","t_posit_x":"126.64447900","t_posit_y":"45.77874300","t_author":"14","t_in_time":"1510734981","t_last_edit_time":"1510735410","t_last_editor":"14","t_status":"3","t_phone":"18946188834","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"79","t_id":"73","tew_skills":"2","tew_worker_num":"1","tew_price":"4","tew_start_time":"1513267200","tew_end_time":"1513267200","r_province":"12","r_city":"167","r_area":"1421","tew_address":"开原街2号","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/0.jpg"},{"t_id":"70","t_title":"1000元","t_info":"。","t_amount":"1000","t_duration":"1","t_edit_amount":"1000","t_amount_edit_times":"0","t_posit_x":"126.64435200","t_posit_y":"45.77874500","t_author":"5","t_in_time":"1510732357","t_last_edit_time":"1510732357","t_last_editor":"5","t_status":"3","t_phone":"15104629758","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"76","t_id":"70","tew_skills":"1","tew_worker_num":"1","tew_price":"1000","tew_start_time":"1513267200","tew_end_time":"1513267200","r_province":"6","r_city":"80","r_area":"750","tew_address":"。","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/5.jpg"},{"t_id":"66","t_title":"负值","t_info":"。","t_amount":"1","t_duration":"1","t_edit_amount":"1","t_amount_edit_times":"0","t_posit_x":"126.64451800","t_posit_y":"45.77870800","t_author":"5","t_in_time":"1510731523","t_last_edit_time":"1510731523","t_last_editor":"5","t_status":"3","t_phone":"15104629758","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"72","t_id":"66","tew_skills":"1","tew_worker_num":"1","tew_price":"1","tew_start_time":"1513267200","tew_end_time":"1513267200","r_province":"2","r_city":"52","r_area":"500","tew_address":"。","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/5.jpg"},{"t_id":"62","t_title":"100元 于","t_info":"（ \u2019 - \u2019 * )","t_amount":"100","t_duration":"1","t_edit_amount":"100","t_amount_edit_times":"0","t_posit_x":"126.64328300","t_posit_y":"45.77876100","t_author":"5","t_in_time":"1510728741","t_last_edit_time":"1510728741","t_last_editor":"5","t_status":"3","t_phone":"15104629758","t_phone_status":"1","t_type":"2","t_storage":"0","bd_id":"0","workers":[{"tew_id":"68","t_id":"62","tew_skills":"1","tew_worker_num":"1","tew_price":"100","tew_start_time":"1513267200","tew_end_time":"1513267200","r_province":"4","r_city":"55","r_area":"540","tew_address":"（ \u2019 - \u2019 * )","tew_lock":"0","tew_status":"1","tew_type":"1"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/5.jpg"},{"t_id":"47","t_title":"哼yiyizx","t_info":"明上午","t_amount":"4","t_duration":"1","t_edit_amount":"4","t_amount_edit_times":"0","t_posit_x":"126.64393000","t_posit_y":"45.77872000","t_author":"1","t_in_time":"1510646641","t_last_edit_time":"1510646641","t_last_editor":"1","t_status":"0","t_phone":"15840344241","t_phone_status":"1","t_type":"1","t_storage":"0","bd_id":"0","workers":[{"tew_id":"53","t_id":"47","tew_skills":"2","tew_worker_num":"1","tew_price":"2","tew_start_time":"1510588800","tew_end_time":"1510675200","r_province":"3","r_city":"37","r_area":"410","tew_address":"哦婆婆哦","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"46","t_title":"郭健","t_info":"而哦婆婆共鸣xxi","t_amount":"64","t_duration":"1","t_edit_amount":"64","t_amount_edit_times":"0","t_posit_x":"126.64407000","t_posit_y":"45.77870000","t_author":"1","t_in_time":"1510646519","t_last_edit_time":"1510646519","t_last_editor":"1","t_status":"1","t_phone":"15840344241","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"52","t_id":"46","tew_skills":"3","tew_worker_num":"2","tew_price":"16","tew_start_time":"1510675200","tew_end_time":"1510761600","r_province":"4","r_city":"56","r_area":"551","tew_address":"你民工怎么wpwwpw","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"45","t_title":"郭健","t_info":"而哦婆婆共鸣xxi","t_amount":"40","t_duration":"1","t_edit_amount":"40","t_amount_edit_times":"0","t_posit_x":"126.64407000","t_posit_y":"45.77870000","t_author":"1","t_in_time":"1510646502","t_last_edit_time":"1510646502","t_last_editor":"1","t_status":"1","t_phone":"15840344241","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"51","t_id":"45","tew_skills":"3","tew_worker_num":"20","tew_price":"1","tew_start_time":"1510675200","tew_end_time":"1510761600","r_province":"4","r_city":"56","r_area":"551","tew_address":"你民工怎么wpwwpw","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"44","t_title":"郭健","t_info":"而哦婆婆共鸣xxi","t_amount":"40","t_duration":"1","t_edit_amount":"40","t_amount_edit_times":"0","t_posit_x":"126.64407000","t_posit_y":"45.77870000","t_author":"1","t_in_time":"1510646465","t_last_edit_time":"1510646465","t_last_editor":"1","t_status":"0","t_phone":"15840344241","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"50","t_id":"44","tew_skills":"3","tew_worker_num":"20","tew_price":"1","tew_start_time":"1510675200","tew_end_time":"1510761600","r_province":"4","r_city":"56","r_area":"551","tew_address":"你民工怎么wpwwpw","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"43","t_title":"郭健","t_info":"而哦婆婆共鸣xxi","t_amount":"318","t_duration":"1","t_edit_amount":"318","t_amount_edit_times":"0","t_posit_x":"126.64407000","t_posit_y":"45.77870000","t_author":"1","t_in_time":"1510646449","t_last_edit_time":"1510646449","t_last_editor":"1","t_status":"1","t_phone":"15840344241","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"49","t_id":"43","tew_skills":"3","tew_worker_num":"1","tew_price":"159","tew_start_time":"1510675200","tew_end_time":"1510761600","r_province":"4","r_city":"56","r_area":"551","tew_address":"你民工怎么wpwwpw","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"42","t_title":"郭健","t_info":"而哦婆婆共鸣xxi","t_amount":"600","t_duration":"1","t_edit_amount":"600","t_amount_edit_times":"0","t_posit_x":"126.64407000","t_posit_y":"45.77870000","t_author":"1","t_in_time":"1510646430","t_last_edit_time":"1510646430","t_last_editor":"1","t_status":"1","t_phone":"15840344241","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"48","t_id":"42","tew_skills":"3","tew_worker_num":"1","tew_price":"300","tew_start_time":"1510675200","tew_end_time":"1510761600","r_province":"4","r_city":"56","r_area":"551","tew_address":"你民工怎么wpwwpw","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"},{"t_id":"39","t_title":"郭健","t_info":"而哦婆婆","t_amount":"40","t_duration":"1","t_edit_amount":"40","t_amount_edit_times":"0","t_posit_x":"126.64407000","t_posit_y":"45.77870000","t_author":"1","t_in_time":"1510646351","t_last_edit_time":"1510646351","t_last_editor":"1","t_status":"1","t_phone":"15840344241","t_phone_status":"1","t_type":"0","t_storage":"0","bd_id":"0","workers":[{"tew_id":"45","t_id":"39","tew_skills":"3","tew_worker_num":"1","tew_price":"20","tew_start_time":"1510675200","tew_end_time":"1510761600","r_province":"3","r_city":"37","r_area":"410","tew_address":"你民工","tew_lock":"0","tew_status":"0","tew_type":"0"}],"favorate":0,"u_img":"http://static-app.gangjianwang.com/static/head/1.jpg"}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * t_id : 120
         * t_title : 11
         * t_info : 11
         * t_amount : 1
         * t_duration : 1
         * t_edit_amount : 1
         * t_amount_edit_times : 0
         * t_posit_x : 126.64435200
         * t_posit_y : 45.77871900
         * t_author : 11
         * t_in_time : 1511586961
         * t_last_edit_time : 1511586961
         * t_last_editor : 11
         * t_status : 1
         * t_phone : 13895750215
         * t_phone_status : 1
         * t_type : 0
         * t_storage : 0
         * bd_id : 0
         * workers : [{"tew_id":"130","t_id":"120","tew_skills":"2","tew_worker_num":"1","tew_price":"1","tew_start_time":"1514131200","tew_end_time":"1514131200","r_province":"3","r_city":"38","r_area":"419","tew_address":"，","tew_lock":"0","tew_status":"0","tew_type":"0"}]
         * favorate : 0
         * u_img : http://static-app.gangjianwang.com/static/head/11.jpg
         */

        private String t_id;
        private String t_title;
        private String t_info;
        private String t_amount;
        private String t_duration;
        private String t_edit_amount;
        private String t_amount_edit_times;
        private String t_posit_x;
        private String t_posit_y;
        private String t_author;
        private String t_in_time;
        private String t_last_edit_time;
        private String t_last_editor;
        private String t_status;
        private String t_phone;
        private String t_phone_status;
        private String t_type;
        private String t_storage;
        private String bd_id;
        private int favorate;
        private String u_img;
        private List<WorkersBean> workers;

        public String getT_id() {
            return t_id;
        }

        public void setT_id(String t_id) {
            this.t_id = t_id;
        }

        public String getT_title() {
            return t_title;
        }

        public void setT_title(String t_title) {
            this.t_title = t_title;
        }

        public String getT_info() {
            return t_info;
        }

        public void setT_info(String t_info) {
            this.t_info = t_info;
        }

        public String getT_amount() {
            return t_amount;
        }

        public void setT_amount(String t_amount) {
            this.t_amount = t_amount;
        }

        public String getT_duration() {
            return t_duration;
        }

        public void setT_duration(String t_duration) {
            this.t_duration = t_duration;
        }

        public String getT_edit_amount() {
            return t_edit_amount;
        }

        public void setT_edit_amount(String t_edit_amount) {
            this.t_edit_amount = t_edit_amount;
        }

        public String getT_amount_edit_times() {
            return t_amount_edit_times;
        }

        public void setT_amount_edit_times(String t_amount_edit_times) {
            this.t_amount_edit_times = t_amount_edit_times;
        }

        public String getT_posit_x() {
            return t_posit_x;
        }

        public void setT_posit_x(String t_posit_x) {
            this.t_posit_x = t_posit_x;
        }

        public String getT_posit_y() {
            return t_posit_y;
        }

        public void setT_posit_y(String t_posit_y) {
            this.t_posit_y = t_posit_y;
        }

        public String getT_author() {
            return t_author;
        }

        public void setT_author(String t_author) {
            this.t_author = t_author;
        }

        public String getT_in_time() {
            return t_in_time;
        }

        public void setT_in_time(String t_in_time) {
            this.t_in_time = t_in_time;
        }

        public String getT_last_edit_time() {
            return t_last_edit_time;
        }

        public void setT_last_edit_time(String t_last_edit_time) {
            this.t_last_edit_time = t_last_edit_time;
        }

        public String getT_last_editor() {
            return t_last_editor;
        }

        public void setT_last_editor(String t_last_editor) {
            this.t_last_editor = t_last_editor;
        }

        public String getT_status() {
            return t_status;
        }

        public void setT_status(String t_status) {
            this.t_status = t_status;
        }

        public String getT_phone() {
            return t_phone;
        }

        public void setT_phone(String t_phone) {
            this.t_phone = t_phone;
        }

        public String getT_phone_status() {
            return t_phone_status;
        }

        public void setT_phone_status(String t_phone_status) {
            this.t_phone_status = t_phone_status;
        }

        public String getT_type() {
            return t_type;
        }

        public void setT_type(String t_type) {
            this.t_type = t_type;
        }

        public String getT_storage() {
            return t_storage;
        }

        public void setT_storage(String t_storage) {
            this.t_storage = t_storage;
        }

        public String getBd_id() {
            return bd_id;
        }

        public void setBd_id(String bd_id) {
            this.bd_id = bd_id;
        }

        public int getFavorate() {
            return favorate;
        }

        public void setFavorate(int favorate) {
            this.favorate = favorate;
        }

        public String getU_img() {
            return u_img;
        }

        public void setU_img(String u_img) {
            this.u_img = u_img;
        }

        public List<WorkersBean> getWorkers() {
            return workers;
        }

        public void setWorkers(List<WorkersBean> workers) {
            this.workers = workers;
        }

        public static class WorkersBean {
            /**
             * tew_id : 130
             * t_id : 120
             * tew_skills : 2
             * tew_worker_num : 1
             * tew_price : 1
             * tew_start_time : 1514131200
             * tew_end_time : 1514131200
             * r_province : 3
             * r_city : 38
             * r_area : 419
             * tew_address : ，
             * tew_lock : 0
             * tew_status : 0
             * tew_type : 0
             */

            private String tew_id;
            private String t_id;
            private String tew_skills;
            private String tew_worker_num;
            private String tew_price;
            private String tew_start_time;
            private String tew_end_time;
            private String r_province;
            private String r_city;
            private String r_area;
            private String tew_address;
            private String tew_lock;
            private String tew_status;
            private String tew_type;

            public String getTew_id() {
                return tew_id;
            }

            public void setTew_id(String tew_id) {
                this.tew_id = tew_id;
            }

            public String getT_id() {
                return t_id;
            }

            public void setT_id(String t_id) {
                this.t_id = t_id;
            }

            public String getTew_skills() {
                return tew_skills;
            }

            public void setTew_skills(String tew_skills) {
                this.tew_skills = tew_skills;
            }

            public String getTew_worker_num() {
                return tew_worker_num;
            }

            public void setTew_worker_num(String tew_worker_num) {
                this.tew_worker_num = tew_worker_num;
            }

            public String getTew_price() {
                return tew_price;
            }

            public void setTew_price(String tew_price) {
                this.tew_price = tew_price;
            }

            public String getTew_start_time() {
                return tew_start_time;
            }

            public void setTew_start_time(String tew_start_time) {
                this.tew_start_time = tew_start_time;
            }

            public String getTew_end_time() {
                return tew_end_time;
            }

            public void setTew_end_time(String tew_end_time) {
                this.tew_end_time = tew_end_time;
            }

            public String getR_province() {
                return r_province;
            }

            public void setR_province(String r_province) {
                this.r_province = r_province;
            }

            public String getR_city() {
                return r_city;
            }

            public void setR_city(String r_city) {
                this.r_city = r_city;
            }

            public String getR_area() {
                return r_area;
            }

            public void setR_area(String r_area) {
                this.r_area = r_area;
            }

            public String getTew_address() {
                return tew_address;
            }

            public void setTew_address(String tew_address) {
                this.tew_address = tew_address;
            }

            public String getTew_lock() {
                return tew_lock;
            }

            public void setTew_lock(String tew_lock) {
                this.tew_lock = tew_lock;
            }

            public String getTew_status() {
                return tew_status;
            }

            public void setTew_status(String tew_status) {
                this.tew_status = tew_status;
            }

            public String getTew_type() {
                return tew_type;
            }

            public void setTew_type(String tew_type) {
                this.tew_type = tew_type;
            }
        }
    }
}
