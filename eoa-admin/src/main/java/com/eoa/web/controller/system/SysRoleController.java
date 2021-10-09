package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.entity.SysRole;
import com.eoa.common.core.domain.entity.SysUser;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.framework.shiro.util.AuthorizationUtils;
import com.eoa.system.domain.SysUserRole;
import com.eoa.system.service.ISysRoleService;
import com.eoa.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysRoleController
 * @Description TODO
 * @Date 2021/10/6 0006 20:46
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/role")
public class SysRoleController extends BaseController {
    
    private String prefix = "system/role";
    
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysUserService userService;
    
    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role(){
        return prefix + "/role";
    }
    
    @RequiresPermissions("system:role:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysRole role){
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }
    
    @GetMapping(value = "/add")
    public String add(){
        return prefix + "/add";
    }
    
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:role:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysRole role){
        if(UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))){
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        if(UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))){
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getLoginName());
        AuthorizationUtils.clearAllCachedAuthorizationInfo();
        return toAjax(roleService.insertRole(role));
    }
    
    @GetMapping(value = "/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap){
        roleService.checkRoleDataScope(roleId);
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/edit";
    }
    
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:role:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysRole role){
        roleService.checkRoleAllowed(role);
        if(UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))){
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        if(UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))){
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(getLoginName());
        AuthorizationUtils.clearAllCachedAuthorizationInfo();
        return toAjax(roleService.updateRole(role));
    }
    
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:role:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        return toAjax(roleService.deleteRoleByIds(ids));
    }
    
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysRole role){
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }
    
    @PostMapping(value = "/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(SysRole role){
        return roleService.checkRoleNameUnique(role);
    }
    
    @PostMapping(value = "/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(SysRole role){
        return roleService.checkRoleKeyUnique(role);
    }
    
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:role:edit")
    @PostMapping(value = "/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysRole role){
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.changeStatus(role));
    }
    
    @GetMapping(value = "/authDataScope/{roleId}")
    public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap){
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/dataScope";
    }
    
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:role:eidt")
    @PostMapping(value = "/authDataScope")
    @ResponseBody
    public AjaxResult authDataScopeSave(SysRole role){
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(getLoginName());
        if(roleService.authDataScope(role) > 0){
            setSysUser(userService.selectUserById(getUserId()));
            return success();
        }
        return error();
    }
    
    @RequiresPermissions("system:role:eidt")
    @GetMapping(value = "/authUser/{roleId}")
    public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap){
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authUser";
    }
    
    @RequiresPermissions("system:role:list")
    @PostMapping(value = "/authUser/allocatedList")
    @ResponseBody
    public TableDataInfo allocatedList(SysUser user){
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }
    
    @GetMapping(value = "/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap){
        SysRole role = roleService.selectRoleById(roleId);
        mmap.put("role", role);
        return prefix + "/selectUser";
    }
    
    @RequiresPermissions("system:role:list")
    @PostMapping(value = "/authUser/unallocatedList")
    @ResponseBody
    public TableDataInfo unallocatedList(SysUser user){
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }
    
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @RequiresPermissions("system:role:edit")
    @PostMapping(value = "/authUser/selectAll")
    @ResponseBody
    public AjaxResult selectAll(Long roleId, String userIds){
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }
    
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @RequiresPermissions("system:role:edit")
    @PostMapping("/authUser/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(SysUserRole userRole){
        return toAjax(roleService.deleteAuthUser(userRole));
    }
    
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @RequiresPermissions("system:role:edit")
    @PostMapping("/authUser/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds){
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }
}
