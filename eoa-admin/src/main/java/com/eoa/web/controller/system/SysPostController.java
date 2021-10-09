package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.system.domain.SysPost;
import com.eoa.system.service.ISysPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysPostController
 * @Description 岗位管理
 * @Date 2021/10/5 0005 20:58
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/post")
public class SysPostController extends BaseController {
    
    private String prefix = "system/post";
    
    @Autowired
    private ISysPostService postService;
    
    @RequiresPermissions("system:post:view")
    @GetMapping
    public String operLog(){
        return prefix + "/post";
    }
    
    @RequiresPermissions("system:post:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysPost post){
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    } 
    
    @GetMapping(value = "/add")
    public String add(){
        return prefix + "/add";
    }
    
    @PostMapping(value = "/checkPostNameUnique")
    @ResponseBody
    public String checkPostNameUnique(SysPost post){
        return postService.checkPostNameUnique(post);
    }
    
    @PostMapping(value = "/checkPostCodeUnique")
    @ResponseBody
    public String checkPostCodeUnique(SysPost post){
        return postService.checkPostCodeUnique(post);
    }
    
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:post:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysPost post){
        if(UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))){
            return error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        if(UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))){
            return error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(getLoginName());
        return toAjax(postService.insertPost(post));
    }
    
    @GetMapping(value = "/edit/{postId}")
    public String edit(@PathVariable("postId")Long postId, ModelMap mmap){
        mmap.put("post", postService.selectPostById(postId));
        return prefix + "/edit";
    }
    
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:post:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysPost post){
        if(UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))){
            return error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        if(UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))){
            return error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(getLoginName());
        return toAjax(postService.updatePost(post));
    }
    
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:post:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        try {
            return toAjax(postService.deletePostByIds(ids));
        } catch (Exception e){
            return error(e.getMessage());
        }
    }
    
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:post:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysPost post){
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<>(SysPost.class);
        return util.exportExcel(list, "岗位管理");
    }
}
