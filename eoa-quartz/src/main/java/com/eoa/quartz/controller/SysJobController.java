package com.eoa.quartz.controller;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.Constants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.exception.job.TaskException;
import com.eoa.common.utils.StringUtils;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.quartz.domain.SysJob;
import com.eoa.quartz.service.ISysJobService;
import com.eoa.quartz.util.CronUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysJobController
 * @Description TODO
 * @Date 2021/10/9 0009 10:49
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/monitor/job")
public class SysJobController extends BaseController {
    
    private String prefix = "monitor/job";
            
    @Autowired
    private ISysJobService jobService;
    
    @RequiresPermissions("monitor:job:view")
    @GetMapping()
    public String job(){
        return prefix + "/job";
    }
    
    @RequiresPermissions("monitor:job:list")
    @PostMapping(value = "list")
    @ResponseBody
    public TableDataInfo list(SysJob job){
        startPage();
        List<SysJob> list = jobService.selectJobList(job);
        return getDataTable(list);
    }
    
    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:job:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysJob job){
        List<SysJob> list = jobService.selectJobList(job);
        ExcelUtil<SysJob> util = new ExcelUtil<>(SysJob.class);
        return util.exportExcel(list, "定时任务");
    }
    
    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @RequiresPermissions("monitor:job:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) throws SchedulerException {
        jobService.deleteJobByIds(ids);
        return success();
    }
    
    @RequiresPermissions("monitor:job:detail")
    @GetMapping(value = "/detail/{jobId}")
    public String detail(@PathVariable("jobId") Long jobId, ModelMap mmap){
        mmap.put("name", "job");
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/detail";
    }
    
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("monitor:job:changeStatus")
    @PostMapping(value = "/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysJob job) throws SchedulerException {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }
    
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("monitor:job:changeStatus")
    @PostMapping(value = "/run")
    @ResponseBody
    public AjaxResult run(SysJob job) throws SchedulerException {
        jobService.run(job);
        return success();
    }
    
    @GetMapping(value = "/add")
    public String add(){
        return prefix + "/add";
    }
    
    @Log(title = "定任务", businessType = BusinessType.INSERT)
    @RequiresPermissions("monitor:job:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysJob job) throws SchedulerException, TaskException {
        if(!CronUtils.isValid(job.getCronExpression())){
            return error("新增定时任务'" + job.getJobName()+ "'失败，cron表达式不正确");
        }
        if(StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)){
            return error("新增定时任务'" + job.getJobName()+ "'失败，目标字符串不允许'rmi://'调用");
        }
        if(StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_LDAP)){
            return error("新增定时任务'" + job.getJobName()+ "'失败，目标字符串不允许'ldap://'调用");
        }
        if(StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})){
            return error("新增定时任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)://'调用");
        }
        return toAjax(jobService.insertJob(job));
    }
    
    @GetMapping(value = "/edit/{jobId}")
    public String edit(@PathVariable("jobId") Long jobId, ModelMap mmap){
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/edit";
    }
    
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("monitor:job:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysJob job) throws SchedulerException, TaskException {
        if(!CronUtils.isValid(job.getCronExpression())){
            return error("修改定时任务'" + job.getJobName()+ "'失败，cron表达式不正确");
        }
        if(StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)){
            return error("修改定时任务'" + job.getJobName()+ "'失败，目标字符串不允许'rmi://'调用");
        }
        if(StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_LDAP)){
            return error("修改定时任务'" + job.getJobName()+ "'失败，目标字符串不允许'ldap://'调用");
        }
        if(StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})){
            return error("修改定时任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)://'调用");
        }
        return toAjax(jobService.updateJob(job));
    }
    
    @PostMapping(value = "checkCronExpressionIsValid")
    @ResponseBody
    public boolean checkCronExpressionIsValid(SysJob job){
        return jobService.checkCronExpressionIsValid(job.getCronExpression());
    }
    
    @GetMapping(value = "/cron")
    public String cron(){
        return prefix + "/cron";
    }
    
    @GetMapping(value = "/queryCronExpression")
    @ResponseBody
    public AjaxResult queryCronExpression(@RequestParam(value = "cronExpression", required = false) String cronExpression){
        if(jobService.checkCronExpressionIsValid(cronExpression)){
            List<String> dateLsit = CronUtils.getRecentTriggerTime(cronExpression);
            return success(dateLsit);
        }else {
            return error("表达式无效");
        }
    }
}
