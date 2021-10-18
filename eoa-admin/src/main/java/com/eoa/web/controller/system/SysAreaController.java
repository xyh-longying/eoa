package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.Ztree;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.StringUtils;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.system.domain.SysArea;
import com.eoa.system.service.ISysAreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 行政区Controller
 * 
 * @author ChengLong
 * @date 2021-10-10
 */
@Controller
@RequestMapping("/system/area")
public class SysAreaController extends BaseController
{
    private String prefix = "system/area";

    @Autowired
    private ISysAreaService sysAreaService;

    @RequiresPermissions("system:area:view")
    @GetMapping()
    public String area()
    {
        return prefix + "/area";
    }

    /**
     * 查询行政区树列表
     */
    @RequiresPermissions("system:area:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysArea sysArea)
    {
        if(StringUtils.isNull(sysArea.getLevel())){
            sysArea.setLevel(1);
        }
        startPage();
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        return getDataTable(list);
    }

    /**
     * 导出行政区列表
     */
    @RequiresPermissions("system:area:export")
    @Log(title = "行政区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysArea sysArea)
    {
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        ExcelUtil<SysArea> util = new ExcelUtil<SysArea>(SysArea.class);
        return util.exportExcel(list, "行政区数据");
    }

    /**
     * 新增行政区
     */
    @GetMapping(value = { "/add/{areaId}", "/add/" })
    public String add(@PathVariable(value = "areaId", required = false) Long areaId, ModelMap mmap)
    {
        if (StringUtils.isNotNull(areaId))
        {
            SysArea sysArea = sysAreaService.selectSysAreaByAreaId(areaId);
            mmap.put("selectLevel", sysArea.getLevel()+1);
            mmap.put("sysArea", sysArea);
        }else {
            mmap.put("selectLevel", 1);
        }
        return prefix + "/add";
    }

    /**
     * 新增保存行政区
     */
    @RequiresPermissions("system:area:add")
    @Log(title = "行政区", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysArea sysArea)
    {
        sysArea.setCreateBy(getLoginName());
        return toAjax(sysAreaService.insertSysArea(sysArea));
    }

    /**
     * 修改行政区
     */
    @GetMapping("/edit/{areaId}")
    public String edit(@PathVariable("areaId") Long areaId, ModelMap mmap)
    {
        SysArea sysArea = sysAreaService.selectSysAreaByAreaId(areaId);
        mmap.put("sysArea", sysArea);
        return prefix + "/edit";
    }

    /**
     * 修改保存行政区
     */
    @RequiresPermissions("system:area:edit")
    @Log(title = "行政区", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysArea sysArea)
    {
        sysArea.setUpdateBy(getLoginName());
        return toAjax(sysAreaService.updateSysArea(sysArea));
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:area:remove")
    @Log(title = "行政区", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{areaId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("areaId") Long areaId)
    {
        return toAjax(sysAreaService.deleteSysAreaByAreaId(areaId));
    }

    /**
     * 选择行政区树
     */
    @GetMapping(value = { "/selectAreaTree/{areaId}", "/selectAreaTree/" })
    public String selectAreaTree(@PathVariable(value = "areaId", required = false) Long areaId, ModelMap mmap)
    {
        if (StringUtils.isNotNull(areaId))
        {
            mmap.put("sysArea", sysAreaService.selectSysAreaByAreaId(areaId));
        }
        return prefix + "/tree";
    }

    /**
     * 加载行政区树列表
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = sysAreaService.selectSysAreaTree();
        return ztrees;
    }

    @RequiresPermissions("system:area:view")
    @GetMapping(value = "/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate(){
        ExcelUtil<SysArea> util = new ExcelUtil<>(SysArea.class);
        return util.importTemplateExcel("行政区数据");
    }

    @Log(title = "行政区管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:area:import")
    @PostMapping(value = "/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysArea> util = new ExcelUtil<>(SysArea.class);
        List<SysArea> areaList = util.importExcel(file.getInputStream());
        String message = sysAreaService.importArea(areaList, updateSupport, getLoginName());
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:area:list")
    @GetMapping(value = "/detail/{areaId}")
    public String detail(@PathVariable("areaId") Long areaId, ModelMap mmap){
        SysArea parentArea = sysAreaService.selectSysAreaByAreaId(areaId);
        mmap.put("level", parentArea.getLevel()+1);
        mmap.put("parentId", parentArea.getAreaId());
        return prefix + "/detail";
    }
}
