package com.eoa.web.controller.monitor;

import com.eoa.common.annotation.Log;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.core.text.Convert;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.enums.OnlineStatus;
import com.eoa.common.utils.ShiroUtils;
import com.eoa.framework.shiro.session.OnlineSession;
import com.eoa.framework.shiro.session.OnlineSessionDAO;
import com.eoa.system.domain.SysUserOnline;
import com.eoa.system.service.ISysUserOnlineService;
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
 * @ClassName: SysUserOnlineController
 * @Description TODO
 * @Date 2021/10/9 0009 10:28
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/monitor/online")
public class SysUserOnlineController extends BaseController {
    
    private String prefix = "/monitor/online";
    
    @Autowired
    private ISysUserOnlineService userOnlineService;
    @Autowired
    private OnlineSessionDAO onlineSessionDAO;
    
    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online(){
        return prefix + "/online";
    } 
    
    @RequiresPermissions("monitor:online:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysUserOnline userOnline){
        startPage();
        List<SysUserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        return getDataTable(list);
    }
    
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @RequiresPermissions(value = {"monitor:online:batchForceLogout", "monitor:online:forceLogout"})
    @PostMapping(value = "batchForceLogout")
    @ResponseBody
    public AjaxResult batchForceLogout(String ids){
        for(String sessionId : Convert.toStrArray(ids)){
            SysUserOnline online = userOnlineService.selectOnlineById(sessionId); 
            if(online == null){
                return error("用户已下线");
            }
            OnlineSession session = (OnlineSession) onlineSessionDAO.readSession(sessionId);
            if(session == null){
                return error("用户已下线");
            }
            if(sessionId.equals(ShiroUtils.getSessionId())){
                return error("当前登录用户无法强退");
            }
            onlineSessionDAO.delete(session);
            online.setStatus(OnlineStatus.off_line);
            userOnlineService.saveOnline(online);
            userOnlineService.removeUserCache(online.getLoginName(), sessionId);
        }
        return success();
    }
    
}
