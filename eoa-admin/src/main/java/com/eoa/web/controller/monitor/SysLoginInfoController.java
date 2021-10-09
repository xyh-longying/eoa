package com.eoa.web.controller.monitor;

import com.eoa.common.annotation.Log;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.framework.shiro.service.SysPasswordService;
import com.eoa.system.domain.SysLogininfor;
import com.eoa.system.service.ISysLogininforService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysLoginInfoController
 * @Description TODO
 * @Date 2021/10/9 0009 12:12
 * @Version 1.0.0
 */
@RequestMapping("/monitor/logininfor")
@Controller
public class SysLoginInfoController extends BaseController {
    
    private String prefix = "monitor/logininfor";
    
    @Autowired
    private ISysLogininforService logininforService;
    @Autowired
    private SysPasswordService passwordService;
    
    @RequiresPermissions("monitor:logininfor:view")
    @GetMapping()
    public String logininfo(){
        return prefix + "/logininfor";
    }
    
    @RequiresPermissions("monitor:logininfor:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysLogininfor logininfor){
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }
    
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:logininfor:exprot")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysLogininfor logininfor){
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<>(SysLogininfor.class);
        return util.exportExcel(list, "登录日志");
    }
    
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("monitor:logininfor:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        return toAjax(logininforService.deleteLogininforByIds(ids));
    }
    
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:logininfor:remove")
    @PostMapping(value = "/clean")
    @ResponseBody
    public AjaxResult clean(){
        logininforService.cleanLogininfor();
        return success();
    }
    
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @RequiresPermissions("monitor:logininfor:unlock")
    @PostMapping(value = "unlock")
    @ResponseBody
    public AjaxResult unlock(String loginName){
        passwordService.clearLoginRecordCache(loginName);        
        return success();
    }
}
