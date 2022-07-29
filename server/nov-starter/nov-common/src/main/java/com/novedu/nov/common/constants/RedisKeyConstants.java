package com.novedu.nov.common.constants;

public class RedisKeyConstants {

    /* -------------------单条缓存-------------------------------- */
    //    首页访问量
    public static final String ACCESS_NUM = "access_num";
    //    前台公告
    public static final String FRONT_NOTICE = "front_notice";
    //    课程详情
    public static final String COURSE_DETAIL = "course_detail_";
    //    文章点击量
    public static final String INFO_CLICK_COUNT = "info_click_count_";
    //    历史观看记录
    public static final String HISTORY_WATCH = "history_watch_";
    public static final String USER_ORDER = "order_course_";

    /* -------------------多条缓存-------------------------------- */
    //    前台首页热门课程
    public static final String CLIENT_COURSE_LIST1 = "client_course_list1";
    //    前台首页最多人学习课程
    public static final String CLIENT_COURSE_LIST2 = "client_course_list2";
    //    前台首页名师大咖
    public static final String CLIENT_TEACHER_LIST = "client_teacher_list";
    //    所有视频播放量统计
    public static final String VIDEO_PLAY_COUNT = "video_play_count";
    //    所有用户缓存
    public static final String USERS_CACHE = "users_cache";


}
