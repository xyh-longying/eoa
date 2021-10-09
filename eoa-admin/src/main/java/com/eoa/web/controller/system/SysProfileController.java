package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.config.EoaConfig;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.entity.SysUser;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.DateUtils;
import com.eoa.common.utils.ShiroUtils;
import com.eoa.common.utils.StringUtils;
import com.eoa.common.utils.file.FileUploadUtils;
import com.eoa.framework.shiro.service.SysPasswordService;
import com.eoa.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ChengLong
 * @ClassName: SysProfileController
 * @Description 个人信息 业务处理
 * @Date 2021/10/2 0002 10:29
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);
    
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPasswordService passwordService;
    
    private String prefix = "system/user/profile";

    /**
     * 个人信息
     * @param mmap
     * @return
     */
    @GetMapping()
    public String profile(ModelMap mmap){
        SysUser user = getSysUser();
        mmap.put("user", user);
        mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
        return prefix + "/profile";
    }
    
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/update")
    @ResponseBody
    public AjaxResult update(SysUser user){
        SysUser currentUser = getSysUser();
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setEmail(user.getEmail());
        currentUser.setUserName(user.getUserName());
        currentUser.setSex(user.getSex());
        
        if(StringUtils.isNotEmpty(user.getPhonenumber()) 
                && UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(currentUser))){
            return error("修改用户'" + currentUser.getLoginName() + "'失败，手机号码已存在");
        }
        if(StringUtils.isNotEmpty(user.getEmail()) 
                && UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(currentUser))){
            return error("修改用户'" + currentUser.getLoginName() + "'失败，邮箱账号已存在");
        }
        if(userService.updateUser(currentUser) > 0){
            setSysUser(userService.selectUserById(currentUser.getUserId()));
            return success();
        }
        return error();
    }
    
    @GetMapping(value = "/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password){
        SysUser user = getSysUser();
        if(passwordService.matches(user, password)){
            return true;
        }
        return false;
    }
    
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword){
        SysUser user = getSysUser();
        if(!passwordService.matches(user, oldPassword)){
            return error("修改密码失败，旧密码错误");
        }
        if(passwordService.matches(user, newPassword)){
            return error("新密码不能与旧密码相同");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
        user.setPwdUpdateDate(DateUtils.getNowDate());
        if(userService.resetUserPwd(user) > 0){
            setSysUser(userService.selectUserById(user.getUserId()));
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }
    
    @GetMapping(value = "/avatar")
    public String avatar(ModelMap mmap){
        SysUser user = getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/avatar";
    }
    
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file){
        SysUser currentUser = getSysUser();
        try {
            if(!file.isEmpty()){
                String avatar = FileUploadUtils.upload(EoaConfig.getAvatarPath(), file);
                currentUser.setAvatar(avatar);
                if(userService.updateUser(currentUser) > 0){
                    setSysUser(userService.selectUserById(currentUser.getUserId()));
                    return success();
                }
            }
            return error();
        } catch (Exception e){
            log.error("修改头像失败！", e);
            return error(e.getMessage());
        }
    }
    
    @GetMapping(value = "/resetPwd")
    public String resetPwd(ModelMap mmap){
        SysUser user = getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/resetpwd";
    }
}
