package com.eoa.quartz.controller;

import com.eoa.common.annotation.Log;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.StringUtils;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.quartz.domain.SysJob;
import com.eoa.quartz.domain.SysJobLog;
import com.eoa.quartz.service.ISysJobLogService;
import com.eoa.quartz.service.ISysJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysJobLogController
 * @Description TODO
 * @Date 2021/10/9 0009 11:46
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/monitor/jobLog")
public class SysJobLogController extends BaseController {
    
    private String prefix = "monitor/job";
    
    @Autowired
    private ISysJobService jobService;
    @Autowired
    private ISysJobLogService jobLogService;
    
    @RequiresPermissions("monitor:job:view")
    @GetMapping()
    public String jobLog(@RequestParam(value = "jobId", required = false) Long jobId, ModelMap mmap){
        if(StringUtils.isNotNull(jobId)){
            SysJob job = jobService.selectJobById(jobId);
            mmap.put("job", job);
        }
        return prefix + "/jobLog";
    }
    
    @RequiresPermissions("monitor:job:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysJobLog jobLog){
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
        return getDataTable(list);
    }
    
    @Log(title = "调度日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:job:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysJobLog jobLog){
        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<>(SysJobLog.class);
        return util.exportExcel(list, "调度日志");
    } 
    
    @Log(title = "调度日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("monitor:job:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        return toAjax(jobLogService.deleteJobLogByIds(ids));
    }
    
    @RequiresPermissions("monitor:job:detail")
    @GetMapping(value = "/detail/{jobLogId}")
    public String detail(@PathVariable("jobLogId") Long jobLogId, ModelMap mmap){
        mmap.put("name", "jobLog");
        mmap.put("jobLog", jobLogService.selectJobLogById(jobLogId));
        return prefix + "/detail";
    }
    
    @Log(title = "调度日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("monitor:job:remove")
    @PostMapping(value = "/clean")
    @ResponseBody
    public AjaxResult clean(){
        jobLogService.cleanJobLog();
        return success();
    }
    
    
}
