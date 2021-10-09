package com.eoa.framework.shiro.web.filter.sync;

import com.eoa.common.constant.ShiroConstants;
import com.eoa.framework.shiro.session.OnlineSession;
import com.eoa.framework.shiro.session.OnlineSessionDAO;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author ChengLong
 * @ClassName: SyncOnlineSessionFilter
 * @Description 同步Session数据到Db
 * @Date 2021/9/9 0009 18:20
 * @Version 1.0.0
 */
public class SyncOnlineSessionFilter extends PathMatchingFilter {
    private OnlineSessionDAO onlineSessionDAO;

    /**
     * 同步会话数据到DB 一次请求最多同步一次 防止过多处理 需要放到Shiro过滤器之前
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception
    {
        OnlineSession session = (OnlineSession) request.getAttribute(ShiroConstants.ONLINE_SESSION);
        // 如果session stop了 也不同步
        // session停止时间，如果stopTimestamp不为null，则代表已停止
        if (session != null && session.getUserId() != null && session.getStopTimestamp() == null)
        {
            onlineSessionDAO.syncToDb(session);
        }
        return true;
    }

    public void setOnlineSessionDAO(OnlineSessionDAO onlineSessionDAO)
    {
        this.onlineSessionDAO = onlineSessionDAO;
    }
}
