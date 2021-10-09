package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.system.domain.SysNotice;
import com.eoa.system.service.ISysNoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysNoticeController
 * @Description TODO
 * @Date 2021/10/9 0009 8:58
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/notice")
public class SysNoticeController extends BaseController {
    
    private String prefix = "system/notice";
    
    @Autowired
    private ISysNoticeService noticeService;
    
    @RequiresPermissions("system:notice:view")
    @GetMapping
    public String notice(){
        return prefix + "/notice";
    }
    
    @RequiresPermissions("system:notice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice){
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }
    
    @GetMapping(value = "/add")
    public String add(){
        return prefix + "/add";
    }
    
    @Log(title = "公告管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:notice:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult add(@Validated SysNotice notice){
        notice.setCreateBy(getLoginName());
        return toAjax(noticeService.insertNotice(notice));
    }
    
    @GetMapping(value = "/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap){
        mmap.put("notice", noticeService.selectNoticeById(noticeId));
        return prefix + "/edit";
    }
    
    @Log(title = "公告管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:notice:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult edit(@Validated SysNotice notice){
        notice.setUpdateBy(getLoginName());
        return toAjax(noticeService.updateNotice(notice));
    }
    
    @Log(title = "公告管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:notice:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        noticeService.deleteNoticeByIds(ids);
        return success();
    }
    
    
}
