package com.novedu.nov.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.statistics.entity.StatisticsDaily;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-09
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    BaseResult statisticsAWeekUserRegisterAndLoginCount();
}
