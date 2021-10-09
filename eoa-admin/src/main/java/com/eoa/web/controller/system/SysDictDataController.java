package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.entity.SysDictData;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.system.service.ISysDictDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysDictDataController
 * @Description TODO
 * @Date 2021/10/8 0008 20:43
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/dict/data")
public class SysDictDataController extends BaseController {
    
    private String prefix = "/system/dict/data";
    
    @Autowired
    private ISysDictDataService dictDataService;
    
    @RequiresPermissions("system:dict:view")
    @GetMapping
    public String view(){
        return prefix + "/data";
    }
    
    @RequiresPermissions("system:dict:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysDictData dictData){
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }
    
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysDictData dictData){
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }
    
    @GetMapping(value = "/add/{dictType}")
    public String add(@PathVariable("dictType") String dictType, ModelMap mmap){
        mmap.put("dictType", dictType);
        return prefix + "/add";
    }
    
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDictData dictData){
        dictData.setCreateBy(getLoginName());
        return toAjax(dictDataService.insertDictData(dictData));
    }
    
    @GetMapping(value = "/edit/{dictCode}")
    public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap){
        mmap.put("dict", dictDataService.selectDictDataById(dictCode));
        return prefix + "/edit";
    }
    
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDictData dictData){
        dictData.setUpdateBy(getLoginName());
        return toAjax(dictDataService.updateDictData(dictData));
    }
    
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping(value = "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        dictDataService.deleteDictDataByIds(ids);
        return success();
    }
}
