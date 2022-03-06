package com.novedu.nov.ucenter.client;

import com.novedu.nov.common.api.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author ：juam
 * @date ：2022/2/8 10:07
 * @description：
 * @modified By：
 * @version:
 */
@Component
@FeignClient(name = "service-edu")
public interface EduClient {

    @PostMapping("/edu/edu-teacher/clear-bind/{uid}")
     BaseResult clearBind(@PathVariable String uid);
}
