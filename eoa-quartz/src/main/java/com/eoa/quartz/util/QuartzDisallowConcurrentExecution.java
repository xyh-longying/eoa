package com.eoa.quartz.util;

import com.eoa.quartz.domain.SysJob;
import org.quartz.JobExecutionContext;

/**
 * @author ChengLong
 * @ClassName: QuartzDisallowConcurrentExecution
 * @Description 定时任务处理（禁止并发执行）
 * @Date 2021/10/9 0009 11:01
 * @Version 1.0.0
 */
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
