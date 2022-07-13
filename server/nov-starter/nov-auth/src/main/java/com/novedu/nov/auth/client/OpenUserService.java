package com.novedu.nov.auth.client;


import com.novedu.nov.auth.domain.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = "service-ucenter")
public interface OpenUserService {

    @PostMapping("/ucenter/member/load-username")
    UserDTO loadUserByUsername(@RequestParam String username);

}
