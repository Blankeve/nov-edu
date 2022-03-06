package com.novedu.nov.statistics.mapper;

import com.novedu.nov.statistics.entity.StatisticsDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 网站统计日数据 Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-02-09
 */
@Repository
public interface StatisticsDailyMapper extends BaseMapper<StatisticsDaily> {

    List<StatisticsDaily> statisticsAWeekUserRegisterAndLoginCount();
}
