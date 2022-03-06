package com.novedu.nov.statistics.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.statistics.entity.StatisticsDaily;
import com.novedu.nov.statistics.entity.vo.EchartsVO;
import com.novedu.nov.statistics.mapper.StatisticsDailyMapper;
import com.novedu.nov.statistics.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-09
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    StatisticsDailyMapper statisticsDailyMapper;

    @Override
    public BaseResult statisticsAWeekUserRegisterAndLoginCount() {
        List<StatisticsDaily> statisticsDailyList = statisticsDailyMapper.statisticsAWeekUserRegisterAndLoginCount();
        EchartsVO barCharts = new EchartsVO();
        barCharts.setType("bar");
        Map title = new HashMap();
        title.put("text", "最近一周用户注册和登录次数");
        barCharts.setTitle(title);
        barCharts.setXRorate(25);
        List<String> days = new ArrayList<>();
        List<Integer> registerNum = new ArrayList<>();
        List<Integer> loginNum = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        for (StatisticsDaily statisticsDaily : statisticsDailyList) {
            registerNum.add(statisticsDaily.getRegisterNum());
            loginNum.add(statisticsDaily.getLoginNum());
            days.add(formatter.format(statisticsDaily.getCreateTime()));
        }
        barCharts.setLabels(days);
        Map registerMap = new HashMap();
        Map accessMap = new HashMap();
        accessMap.put("label", "登录人数");
        accessMap.put("data", loginNum);
        registerMap.put("label", "注册人数");
        registerMap.put("data", registerNum);
        List<Map> mapArrayList = new ArrayList<>();
        mapArrayList.add(accessMap);
        mapArrayList.add(registerMap);
        barCharts.setDatasets(mapArrayList);
        EchartsVO lineCharts = new EchartsVO();
        BeanUtils.copyProperties(barCharts, lineCharts);
        lineCharts.setType("line");
        return BaseResult.success().mapSet("logAndRegBC",barCharts).mapSet("logAndRegLC",lineCharts);
    }
}
