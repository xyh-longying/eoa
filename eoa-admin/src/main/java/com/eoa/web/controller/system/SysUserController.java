package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.entity.SysRole;
import com.eoa.common.core.domain.entity.SysUser;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.core.text.Convert;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.ShiroUtils;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.framework.shiro.service.SysPasswordService;
import com.eoa.system.service.ISysDeptService;
import com.eoa.system.service.ISysPostService;
import com.eoa.system.service.ISysRoleService;
import com.eoa.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChengLong
 * @ClassName: SysUserController
 * @Description TODO
 * @Date 2021/10/3 0003 9:36
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/user")
public class SysUserController extends BaseController {
    
    private String prefix = "system/user";
    
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysPostService postService;
    @Autowired
    private SysPasswordService passwordService;
    
    @PostMapping(value = "/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user){
        return userService.checkEmailUnique(user);
    }
    
    @PostMapping(value = "/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user){
        return userService.checkPhoneUnique(user);
    }
    
    @PostMapping(value = "/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user){
        return userService.checkLoginNameUnique(user.getLoginName());
    }
    
    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user(){
        return prefix + "/user";
    } 
    
    @RequiresPermissions("system:user:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysUser user){
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    } 
    
    @GetMapping(value = "/add")
    public String add(ModelMap mmap){
        mmap.put("roles", roleService.selectRoleAll().stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }
    
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:user:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysUser user){
        if(UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))){
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
        if(StringUtils.isNotEmpty(user.getPhonenumber()) 
                && UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))){
            return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        }
        if(StringUtils.isNotEmpty(user.getEmail()) 
                && UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))){
            return error("新增用户'" + user.getLoginName() + "'失败，邮箱已存在");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(getLoginName());
        return toAjax(userService.insertUser(user));
    }
    
    @GetMapping(value = "/edit/{userId}")
    public String edit(@PathVariable("userId")Long userId, ModelMap mmap){
        userService.checkUserDataScope(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        mmap.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        mmap.put("posts", postService.selectPostsByUserId(userId));
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/edit";
    }
    
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysUser user){
        userService.checkUserAllowed(user);
        if(StringUtils.isNotEmpty(user.getPhonenumber()) 
                && UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))){
            return error("修改用户'" + user.getLoginName()+ "'失败，手机号码已存在");
        }
        if(StringUtils.isNotEmpty(user.getEmail()) 
                && UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))){
            return error("修改用户'" + user.getLoginName() + "'失败，邮箱已存在");
        }
        user.setUpdateBy(getLoginName());
        return toAjax(userService.updateUser(user));
    }
    
    
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:user:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        if(ArrayUtils.contains(Convert.toLongArray(ids), getUserId())){
            return error("当前用户不能删除"); 
        }
        return toAjax(userService.deleteUserByIds(ids));
    }
    
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysUser user){
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }
    
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping(value = "/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user){
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }
    
    @RequiresPermissions("system:user:resetPwd")
    @GetMapping(value = "/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap){
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }
    
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:resetPwd")
    @PostMapping(value = "/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(SysUser user){
        userService.checkUserAllowed(user);
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        if(userService.updateUser(user) > 0){
            if(ShiroUtils.getUserId().longValue() == user.getUserId().longValue()){
                setSysUser(userService.selectUserById(user.getUserId()));
            }
            return success();
        }
        return error();
    }
    
    @GetMapping(value = "/authRole/{userId}")
    public String authRole(@PathVariable("userId") Long userId, ModelMap mmap){
        SysUser user = userService.selectUserById(userId);
        mmap.put("user", user);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        mmap.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(role -> !role.isAdmin()).collect(Collectors.toList()));
        return prefix + "/authRole";
    }
    
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @RequiresPermissions("system:user:eidt")
    @PostMapping(value = "/authRole/insertAuthRole")
    @ResponseBody
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds){
        userService.insertUserAuth(userId, roleIds);
        return success();
    }
    
    @RequiresPermissions("system:user:view")
    @GetMapping(value = "/importTemplate")    
    @ResponseBody
    public AjaxResult importTemplate(){
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }
    
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping(value = "/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String message = userService.importUser(userList, updateSupport, getLoginName()); 
        return AjaxResult.success(message);
    }
    
}
