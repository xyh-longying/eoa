package com.eoa.quartz.util;

import com.eoa.quartz.domain.SysJob;
import org.quartz.JobExecutionContext;

/**
 * @author ChengLong
 * @ClassName: QuartzJobExecution
 * @Description 定时任务处理（允许并发执行）
 * @Date 2021/10/9 0009 10:57
 * @Version 1.0.0
 */
public class QuartzJobExecution extends AbstractQuartzJob{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
