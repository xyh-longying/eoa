package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.system.domain.SysConfig;
import com.eoa.system.service.ISysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysConfigController
 * @Description 参数配置
 * @Date 2021/10/9 0009 8:31
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/config")
public class SysConfigController extends BaseController {
    
    private String prefix = "system/config";
    
    @Autowired
    private ISysConfigService configService;
    
    @RequiresPermissions("system:config:view")
    @GetMapping
    public String config(){
        return prefix + "/config";
    }
    
    @RequiresPermissions("system:config:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysConfig config){
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }
    
    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:config:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysConfig config){
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        return util.exportExcel(list, "参数数据");
    }
    
    @GetMapping(value = "/add")
    public String add(){
        return prefix + "/add";
    }
    
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:config:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysConfig config){
        if(UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))){
            return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getLoginName());
        return toAjax(configService.insertConfig(config));
    }
    
    @GetMapping(value = "/edit/{configId}")
    public String edit(@PathVariable("configId") Long configId, ModelMap mmap){
        mmap.put("config", configService.selectConfigById(configId));
        return prefix + "/edit";
    }
    
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:config:edit")
    @PostMapping(value = "edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysConfig config){
        if(UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))){
            return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getLoginName());
        return toAjax(configService.updateConfig(config));
    }
    
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:config:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        configService.deleteConfigByIds(ids);
        return success();
    }
    
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @RequiresPermissions("system:config:remove")
    @GetMapping(value = "/refreshCache")
    @ResponseBody
    public AjaxResult refreshCache(){
        configService.clearConfigCache();
        return success();
    }
    
    @PostMapping(value = "checkConfigKeyUnique")
    @ResponseBody
    public String checkConfigKeyUnique(SysConfig config){
        return configService.checkConfigKeyUnique(config);
    }
}
