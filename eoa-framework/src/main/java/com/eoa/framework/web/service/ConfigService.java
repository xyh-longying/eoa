package com.eoa.framework.web.service;

import com.eoa.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ChengLong
 * @ClassName: ConfigService
 * @Description html调用 thymeleaf 实现参数管理
 * @Date 2021/9/9 0009 18:37
 * @Version 1.0.0
 */
@Service("config")
public class ConfigService {
    @Autowired
    private ISysConfigService configService;

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String getKey(String configKey)
    {
        return configService.selectConfigByKey(configKey);
    }
}
