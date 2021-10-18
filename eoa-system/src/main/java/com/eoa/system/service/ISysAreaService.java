package com.eoa.system.service;

import com.eoa.common.core.domain.Ztree;
import com.eoa.system.domain.SysArea;

import java.util.List;

/**
 * 行政区Service接口
 * 
 * @author ChengLong
 * @date 2021-10-10
 */
public interface ISysAreaService 
{
    /**
     * 查询行政区
     * 
     * @param areaId 行政区主键
     * @return 行政区
     */
    public SysArea selectSysAreaByAreaId(Long areaId);

    /**
     * 查询行政区列表
     * 
     * @param sysArea 行政区
     * @return 行政区集合
     */
    public List<SysArea> selectSysAreaList(SysArea sysArea);

    /**
     * 新增行政区
     * 
     * @param sysArea 行政区
     * @return 结果
     */
    public int insertSysArea(SysArea sysArea);

    /**
     * 修改行政区
     * 
     * @param sysArea 行政区
     * @return 结果
     */
    public int updateSysArea(SysArea sysArea);

    /**
     * 批量删除行政区
     * 
     * @param areaIds 需要删除的行政区主键集合
     * @return 结果
     */
    public int deleteSysAreaByAreaIds(String areaIds);

    /**
     * 删除行政区信息
     * 
     * @param areaId 行政区主键
     * @return 结果
     */
    public int deleteSysAreaByAreaId(Long areaId);

    /**
     * 查询行政区树列表
     * 
     * @return 所有行政区信息
     */
    public List<Ztree> selectSysAreaTree();

    /**
     * 导入行政区数据
     *
     * @param areaList 行政区数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importArea(List<SysArea> areaList, Boolean isUpdateSupport, String operName);
}
