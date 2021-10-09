package com.eoa.web.controller.monitor;

import com.eoa.common.annotation.Log;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.system.domain.SysOperLog;
import com.eoa.system.service.ISysOperLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysOperLogController
 * @Description TODO
 * @Date 2021/10/9 0009 12:39
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/monitor/operlog")
public class SysOperLogController extends BaseController {
    
    private String prefix = "/monitor/operlog";
    
    @Autowired
    private ISysOperLogService operLogService; 
    
    @RequiresPermissions("monitor:operlog:view")
    @GetMapping()
    public String operlog(){
        return prefix + "/operlog";
    }
    
    @RequiresPermissions("monitor:operlog:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysOperLog operLog){
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }
    
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:operlog:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysOperLog operLog){
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }
    
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        return toAjax(operLogService.deleteOperLogByIds(ids));
    }
    
    @RequiresPermissions("monitor:operlog:detail")
    @GetMapping(value = "/detail/{operlogId}")
    public String detail(@PathVariable("operlogId") Long operLogId, ModelMap mmap){
        mmap.put("operLog", operLogService.selectOperLogById(operLogId));
        return prefix + "/detail";
    } 
    
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping(value = "/clean")
    @ResponseBody
    public AjaxResult clean(){
        operLogService.cleanOperLog();
        return success();
    }
}
