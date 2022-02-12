package com.novedu.nov.statistics.task;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.statistics.client.EduClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ：juam
 * @date ：2022/2/9 13:37
 * @description：
 * @modified By：
 * @version:
 */
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
@Slf4j
public class MultiThreadScheduleTask {

    @Autowired
    EduClient eduClient;

    @Async
    @Scheduled(cron = "0/30 * * * * ?")  //间隔30秒
    public void asyncCourseViewCount() {
        log.info("---------------正在同步课程播放次数...");
        BaseResult baseResult = eduClient.statisticsCoursePlayCount();
        log.info("---------------课程播放次数同步" + (baseResult.getCode().equals(200) ? "完成" : "失败"));
    }



}
