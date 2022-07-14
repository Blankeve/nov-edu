package com.novedu.nov.auth.client;


import com.novedu.nov.common.base.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "service-ucenter")
public interface OpenUserService {

    @PostMapping("/ucenter/member/load-username")
    UserDTO loadUserByUsername(@RequestParam String username);

}
