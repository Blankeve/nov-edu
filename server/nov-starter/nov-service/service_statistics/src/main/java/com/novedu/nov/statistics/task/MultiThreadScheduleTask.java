package com.novedu.nov.statistics.task;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.statistics.client.EduClient;
import com.novedu.nov.statistics.client.UcenterClient;
import com.novedu.nov.statistics.entity.StatisticsDaily;
import com.novedu.nov.statistics.service.StatisticsDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    @Autowired
    UcenterClient ucenterClient;
    @Autowired
    StatisticsDailyService statisticsDailyService;

    @Async
    @Scheduled(cron = "0 0/10 * * * ?")  //间隔10分钟
    public void syncCourseViewCount() {
        log.info("---------------正在同步课程播放次数...");
        BaseResult baseResult = eduClient.statisticsCoursePlayCount();
        log.info("---------------课程播放次数同步" + (baseResult.getCode().equals(200) ? "完成" : "失败"));
    }

    @Async
    @Scheduled(cron = "45 59 23 * * ?")  //每天下午11点59分45秒同步
    public void syncRegisterLoginCount() {
        log.info("---------------正在同步每天用户新增注册和登录人数...");
        BaseResult baseResult = ucenterClient.syncRegisterLoginCount();
        if (BaseResult.success().getCode().equals(baseResult.getCode())) {
            StatisticsDaily statisticsDaily = new StatisticsDaily();
            Map map = (Map) baseResult.getData();
            statisticsDaily.setRegisterNum((Integer) map.get("registerCount"));
            statisticsDaily.setLoginNum((Integer) map.get("loginCount"));
            statisticsDailyService.save(statisticsDaily);
        }
        log.info("---------------同步每天用户新增注册和登录人数" + (baseResult.getCode().equals(200) ? "完成" : "失败"));
    }
}
