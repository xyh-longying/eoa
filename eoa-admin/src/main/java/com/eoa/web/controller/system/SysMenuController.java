package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.Ztree;
import com.eoa.common.core.domain.entity.SysMenu;
import com.eoa.common.core.domain.entity.SysRole;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.ShiroUtils;
import com.eoa.framework.shiro.util.AuthorizationUtils;
import com.eoa.system.service.ISysMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysMenuController
 * @Description TODO
 * @Date 2021/10/6 0006 21:01
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/menu")
public class SysMenuController extends BaseController {
    
    private String prefix = "system/menu";
    
    @Autowired
    private ISysMenuService menuService;
    
    @RequiresPermissions("system:menu:view")
    @GetMapping()
    public String menu(){
        return prefix + "/menu";
    }
    
    @RequiresPermissions("system:menu:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public List<SysMenu> list(SysMenu menu){
        Long userId = ShiroUtils.getUserId();
        List<SysMenu> list = menuService.selectMenuList(menu, userId);
        return list;
    }
    
    @GetMapping(value = "/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap){
        SysMenu menu = null;
        if(0L != parentId){
            menu = menuService.selectMenuById(parentId);
        }else {
            menu = new SysMenu();
            menu.setMenuId(0L);
            menu.setMenuName("主目录");
        }
        mmap.put("menu", menu);
        return prefix + "/add";
    }
    
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:menu:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysMenu menu){
        if(UserConstants.MENU_NAME_NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))){
            return error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        menu.setCreateBy(getLoginName());    
        AuthorizationUtils.clearAllCachedAuthorizationInfo();
        return toAjax(menuService.insertMenu(menu));
    } 
    
    @GetMapping(value = "/edit/{menuId}")
    public String edit(@PathVariable("menuId") Long menuId, ModelMap mmap){
        mmap.put("menu", menuService.selectMenuById(menuId));
        return prefix + "/edit";
    }
    
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:menu:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysMenu menu){
        if(UserConstants.MENU_NAME_NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))){
            return error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        menu.setUpdateBy(getLoginName());
        AuthorizationUtils.clearAllCachedAuthorizationInfo();;
        return toAjax(menuService.updateMenu(menu));
    }
    
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:menu:remove")
    @GetMapping(value = "/remove/{menuId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("menuId") Long menuId){
        if(menuService.selectCountMenuByParentId(menuId) > 0){
            return AjaxResult.warn("存在子菜单，不允许删除");
        }
        if(menuService.selectCountRoleMenuByMenuId(menuId) > 0){
            return AjaxResult.warn("菜单已分配，不允许删除");
        }
        AuthorizationUtils.clearAllCachedAuthorizationInfo();
        return toAjax(menuService.deleteMenuById(menuId));
    }
    
    @GetMapping(value = "/roleMenuTreeData")
    @ResponseBody
    public List<Ztree> roleMenuTreeData(SysRole role){
        Long userId = ShiroUtils.getUserId();
        List<Ztree> ztrees = menuService.roleMenuTreeData(role, userId);
        return ztrees;
    }
    
    @PostMapping(value = "/checkMenuNameUnique")
    @ResponseBody
    public String checkMenuNameUnique(SysMenu menu){
        return menuService.checkMenuNameUnique(menu);
    }
    
    @GetMapping(value = "/selectMenuTree/{menuId}")
    public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap mmap){
        mmap.put("menu", menuService.selectMenuById(menuId));
        return prefix + "/tree";
    }
    
    @GetMapping(value = "/menuTreeData")
    @ResponseBody
    public List<Ztree> menuTreeData(){
        Long userId = ShiroUtils.getUserId();
        List<Ztree> ztrees = menuService.menuTreeData(userId);
        return ztrees;
    }
}
