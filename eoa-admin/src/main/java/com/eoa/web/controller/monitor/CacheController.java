package com.eoa.web.controller.monitor;

import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.framework.web.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ChengLong
 * @ClassName: CacheController
 * @Description TODO
 * @Date 2021/10/9 0009 13:11
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/monitor/cache")
public class CacheController extends BaseController {
    
    private String prefix = "monitor/cache";
    
    @Autowired
    private CacheService cacheService;
    
    @GetMapping
    public String cache(ModelMap mmap){
        mmap.put("cacheNames", cacheService.getCacheNames());
        return prefix + "/cache";
    }
    
    @PostMapping(value = "/getNames")
    public String getCacheNames(String fragment, ModelMap mmap){
        mmap.put("cacheNames", cacheService.getCacheNames());
        return prefix + "/cache::" + fragment;
    }
    
    @PostMapping(value = "/getKeys")
    public String getCacheKeys(String fragment, String cacheName, ModelMap mmap){
        mmap.put("cacheName", cacheName);
        mmap.put("cacheKyes", cacheService.getCacheKeys(cacheName));
        return prefix + "/cache::" + fragment;
    }
    
    @PostMapping(value = "/getValue")
    public String getValue(String fragment, String cacheName, String cacheKey, ModelMap mmap){
        mmap.put("cacheName", cacheName);
        mmap.put("cacheKey", cacheKey);
        mmap.put("cacheValue", cacheService.getCacheValue(cacheName, cacheKey));
        return prefix + "/cache::" + fragment;
    }
    
    @PostMapping(value = "/clearCacheName")
    @ResponseBody
    public AjaxResult clearCacheName(String cacheName){
        cacheService.clearCacheName(cacheName);
        return success();
    }
    
    @PostMapping(value = "/clearCacheKey")
    @ResponseBody
    public AjaxResult clearCacheKey(String cacheName, String cacheKey){
        cacheService.clearCacheKey(cacheName, cacheKey);
        return success();
    }
    
    @GetMapping(value = "/clearAll")
    @ResponseBody
    public AjaxResult clearAll(){
        cacheService.clearAll();
        return success();
    }
}

