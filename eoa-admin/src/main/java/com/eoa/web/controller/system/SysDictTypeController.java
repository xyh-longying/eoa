package com.eoa.web.controller.system;

import com.eoa.common.annotation.Log;
import com.eoa.common.constant.UserConstants;
import com.eoa.common.core.controller.BaseController;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.Ztree;
import com.eoa.common.core.domain.entity.SysDictType;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.enums.BusinessType;
import com.eoa.common.utils.poi.ExcelUtil;
import com.eoa.system.service.ISysDictTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SystemDictTypeController
 * @Description 数据字典信息
 * @Date 2021/10/8 0008 18:06
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/system/dict")
public class SysDictTypeController extends BaseController {
    
    private String prefix = "system/dict/type";
    
    @Autowired
    private ISysDictTypeService dictTypeService;
    
    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictType(){
        return prefix + "/type";
    }    
    
    @RequiresPermissions("system:dict:list")
    @PostMapping(value = "/list")
    @ResponseBody
    public TableDataInfo list(SysDictType dictType){
        startPage();
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(dictTypes);
    }    
    
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping(value = "/export")
    @ResponseBody
    public AjaxResult export(SysDictType dictType){
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }
    
    @GetMapping(value = "/add")
    public String add(){
        return prefix + "/add";
    }
    
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping(value = "add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDictType dictType){
        if(UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dictType))){
            return error("新增字典'" + dictType.getDictName() + "'失败，字典类型已存在");
        }
        dictType.setCreateBy(getLoginName());
        return toAjax(dictTypeService.insertDictType(dictType));
    }
    
    @GetMapping(value = "/edit/{dictId}")
    public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap){
        mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
        return prefix + "/edit";
    }
    
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping(value = "/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDictType dict){
        
        if(UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))){
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(getLoginName());
        return toAjax(dictTypeService.updateDictType(dict));
    }
    
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping(value = "remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        dictTypeService.deleteDictTypeByIds(ids);
        return success();
    }
    
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @RequiresPermissions("system:dict:remove")
    @GetMapping(value = "/refreshCache")
    @ResponseBody
    public AjaxResult refreshCache(){
        dictTypeService.resetDictCache();
        return success();
    }
    
    @RequiresPermissions("system:dict:list")
    @GetMapping(value = "/detail/{dictId}")
    public String detail(@PathVariable("dictId") Long dictId, ModelMap mmap){
        mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
        mmap.put("dictList", dictTypeService.selectDictTypeAll());
        return "/system/dict/data/data";
    }
    
    @PostMapping(value = "/checkDictTypeUnique")
    @ResponseBody
    public String checkDictTypeUnique(SysDictType dictType){
        return dictTypeService.checkDictTypeUnique(dictType);
    }

    /**
     * 选择字典树
     */
    @GetMapping("/selectDictTree/{columnId}/{dictType}")
    public String selectDeptTree(@PathVariable("columnId") Long columnId, @PathVariable("dictType") String dictType,
                                 ModelMap mmap)
    {
        mmap.put("columnId", columnId);
        mmap.put("dict", dictTypeService.selectDictTypeByType(dictType));
        return prefix + "/tree";
    }

    /**
     * 加载字典列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = dictTypeService.selectDictTree(new SysDictType());
        return ztrees;
    }
}
