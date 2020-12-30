package com.hn.rbac.server.service.runner;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hn.rbac.server.cache.exception.RedisConnectException;
import com.hn.rbac.server.service.CacheService;
import com.hn.rbac.server.service.UserQueryService;
import com.hn.rbac.server.service.manager.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 缓存初始化
 */
@Slf4j
@Component
public class CacheInitRunner implements ApplicationRunner {

    @Resource
    private UserQueryService userQueryService;
    @Resource
    private CacheService cacheService;
    @Resource
    private UserManager userManager;

    @Resource
    private ConfigurableApplicationContext context;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Redis连接中 ······");
            cacheService.testConnect();

            log.info("缓存初始化 ······");
            log.info("缓存用户数据 ······");

            // load users to cache
            List<String> usernameList = userQueryService.findAllUsername();
            if (CollectionUtils.isNotEmpty(usernameList)) {
                for (String username : usernameList) {
                    userManager.initUserRedisCache(username);
                }
            }

        } catch (Exception e) {
            log.error("缓存初始化失败，{}", e.getMessage());
            log.error(" ____   __    _   _ ");
            log.error("| |_   / /\\  | | | |");
            log.error("|_|   /_/--\\ |_| |_|__");
            log.error("                        ");
            log.error("Admin启动失败              ");
            if (e instanceof RedisConnectException)
                log.error("Redis连接异常，请检查Redis连接配置并确保Redis服务已启动");
            // 关闭
            context.close();
        }
    }
}
