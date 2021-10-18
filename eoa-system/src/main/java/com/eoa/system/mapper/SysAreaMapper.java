package com.eoa.system.mapper;

import com.eoa.system.domain.SysArea;

import java.util.List;

/**
 * 行政区Mapper接口
 * 
 * @author ChengLong
 * @date 2021-10-10
 */
public interface SysAreaMapper 
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
     * 删除行政区
     * 
     * @param areaId 行政区主键
     * @return 结果
     */
    public int deleteSysAreaByAreaId(Long areaId);

    /**
     * 批量删除行政区
     * 
     * @param areaIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAreaByAreaIds(String[] areaIds);
}
