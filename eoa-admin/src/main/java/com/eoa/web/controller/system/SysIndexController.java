package com.eoa.web.controller.system;

import com.eoa.common.config.EoaConfig;
import com.eoa.common.constant.ShiroConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.entity.SysMenu;
import com.eoa.common.core.domain.entity.SysUser;
import com.eoa.common.core.text.Convert;
import com.eoa.common.utils.CookieUtils;
import com.eoa.common.utils.DateUtils;
import com.eoa.common.utils.ServletUtils;
import com.eoa.common.utils.StringUtils;
import com.eoa.framework.shiro.service.SysPasswordService;
import com.eoa.system.service.ISysConfigService;
import com.eoa.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysIndexController
 * @Description 首页 业务处理
 * @Date 2021/9/15 0015 18:29
 * @Version 1.0.0
 */
@Controller
public class SysIndexController extends BaseController {
    @Autowired
    private ISysMenuService menuService;
    
    @Autowired
    private ISysConfigService configService;
    
    @Autowired
    private SysPasswordService passwordService;
    
    
    //系统首页
    @GetMapping(value = "/index")
    public String index(ModelMap mmap){
        //获取用户身份信息
        SysUser user = getSysUser();
        //获取用户菜单权限
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("ignoreFooter", configService.selectConfigByKey("sys.index.ignoreFooter"));
        mmap.put("copyrightYear", EoaConfig.getCopyrightYear());
        mmap.put("demoEnabled", EoaConfig.isDemoEnabled());
        mmap.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
        mmap.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
        mmap.put("isMobile", ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")));

        // 菜单导航显示风格
        String menuStyle = configService.selectConfigByKey("sys.index.menuStyle");
        // 移动端，默认使左侧导航菜单，否则取默认配置
        String indexStyle = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")) ? "index" : menuStyle;
        // 优先Cookie配置导航菜单
        Cookie[] cookies = ServletUtils.getRequest().getCookies();
        for (Cookie cookie : cookies)
        {
            if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName()))
            {
                indexStyle = cookie.getValue();
                break;
            }
        }
        String webIndex = "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";
        return webIndex;
    }
    
    //锁定屏幕
    @GetMapping(value = "/lockscreen")
    public String lockscreen(ModelMap mmap){
        mmap.put("user", getSysUser());
        ServletUtils.getSession().setAttribute(ShiroConstants.LOCK_SCREEN, true);
        return "lock";
    }
    
    //解锁屏幕
    @PostMapping(value = "/unlockscreen")
    @ResponseBody
    public AjaxResult unlockscreen(String password){
        SysUser user = getSysUser();
        if(StringUtils.isNull(user)){
            return AjaxResult.error("服务器超时，请重新登录。");
        }
        if(passwordService.matches(user, password)){
            ServletUtils.getSession().removeAttribute(ShiroConstants.LOCK_SCREEN);
            return AjaxResult.success();
        }
        return AjaxResult.error("密码不正确，请重新输入。");
    }
    
    @GetMapping(value = "/system/switchSkin")
    public String switchSkin(){
        return "skin";
    }
    
    @GetMapping(value = "/system/menuStyle/{style}")
    public void menuStyle(@PathVariable String style, HttpServletResponse response){
        CookieUtils.setCookie(response, "nav-style", style);
    }

    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Date pwdUpdateDate)
    {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate)
    {
        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0)
        {
            if (StringUtils.isNull(pwdUpdateDate))
            {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }
}
