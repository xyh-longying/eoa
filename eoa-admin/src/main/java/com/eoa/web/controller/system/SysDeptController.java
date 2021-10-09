package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.Ztree;
import com.eoa.common.core.domain.entity.SysDept;
import com.eoa.common.core.domain.entity.SysRole;
import com.eoa.common.core.domain.entity.SysUser;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.StringUtils;
import com.eoa.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysDeptController
 * @Description 部门信息
 * @Date 2021/10/4 0004 11:13
 * @Version 1.0.0
 */
@RequestMapping(value = "/system/dept")
@Controller
public class SysDeptController extends BaseController {
    
    private String prefix = "system/dept";
    
    @Autowired
    private ISysDeptService deptService;

    /**
     * 加载部门列表树
     * @return
     */
    @GetMapping(value = "/treeData")
    @ResponseBody
    public List<Ztree> treeData(){
        List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
        return ztrees;
    }

    /**
     * 加载部门列表（排除下级）
     * @param excludeId
     * @param mmap
     * @return
     */
    @GetMapping(value = "/treeData/{excludeId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable("excludeId") Long excludeId, ModelMap mmap){
        SysDept dept = new SysDept();
        dept.setExcludeId(excludeId);
        List<Ztree> ztrees = deptService.selectDeptTreeExcludeChild(dept);
        return ztrees;
    }
    
    @RequiresPermissions("system:dept:view")
    @GetMapping()
    public String dept(){
        return prefix + "/dept";
    }
    
    @RequiresPermissions("system:dept:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public List<SysDept> list(SysDept dept){
        List<SysDept> list = deptService.selectDeptList(dept);
        return list;
    }
    
    @GetMapping(value = "/add/{parentId}")
    public String add(@PathVariable Long parentId, ModelMap mmap){
        SysUser user = getSysUser();
        if(!user.isAdmin()){
            parentId = user.getDeptId();
        }
        mmap.put("dept", deptService.selectDeptById(parentId));
        return prefix + "/add";
    }
    
    @GetMapping(value = "/edit/{deptId}")
    public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap){
        deptService.checkDeptDataScope(deptId);
        SysDept dept = deptService.selectDeptById(deptId);    
        if(StringUtils.isNotNull(dept) && 100L == deptId){
            dept.setParentName("无");
        }
        mmap.put("dept", dept);
        return prefix + "/edit";
    }
    
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dept:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDept dept){
        if(UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))){
            return error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(getLoginName());
        return toAjax(deptService.insertDept(dept));
    }
    
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dept:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDept dept){
        if(UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))){
            return error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        if(dept.getParentId().equals(dept.getDeptId())){
            return error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        if(StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) 
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0){
            return error("该部门包含未停用的子部门");
        }
        dept.setUpdateBy(getLoginName());
        return toAjax(deptService.updateDept(dept));
    }
    
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dept:remove")
    @GetMapping(value = "/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") Long deptId){
        if(deptService.selectDeptCount(deptId) > 0){
            return AjaxResult.warn("存在下级部门，不允许删除");
        }
        if(deptService.checkDeptExistUser(deptId)){
            return AjaxResult.warn("部门存在用户，不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));    
    }
    
    @PostMapping(value = "/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(SysDept dept){
        return deptService.checkDeptNameUnique(dept);
    }
    
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") Long deptId, 
                                 @PathVariable(value = "excludeId", required = false) Long excludeId, ModelMap mmap){
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/tree";
    }
    
    @GetMapping(value = "/roleDeptTreeData")
    @ResponseBody
    public List<Ztree> roleDeptTreeData(SysRole role){
        return deptService.roleDeptTreeData(role);
    }
    
}
