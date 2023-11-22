package com.digcoin.snapx.server.admin.system.runner;

import com.digcoin.snapx.server.admin.system.service.AdminUserAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitRunner implements ApplicationRunner {

    private final AdminUserAppService adminUserAppService;

    @Override
    public void run(ApplicationArguments args) {
        adminUserAppService.initAdmin();
    }

}
