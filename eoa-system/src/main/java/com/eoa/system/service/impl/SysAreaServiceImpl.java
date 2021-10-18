package com.eoa.system.service.impl;

import com.eoa.common.core.domain.Ztree;
import com.eoa.common.core.text.Convert;
import com.eoa.common.exception.ServiceException;
import com.eoa.common.utils.DateUtils;
import com.eoa.common.utils.StringUtils;
import com.eoa.system.domain.SysArea;
import com.eoa.system.mapper.SysAreaMapper;
import com.eoa.system.service.ISysAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 行政区Service业务层处理
 * 
 * @author ChengLong
 * @date 2021-10-10
 */
@Service
public class SysAreaServiceImpl implements ISysAreaService 
{
    private static final Logger log = LoggerFactory.getLogger(SysAreaServiceImpl.class);
    @Autowired
    private SysAreaMapper sysAreaMapper;

    /**
     * 查询行政区
     * 
     * @param areaId 行政区主键
     * @return 行政区
     */
    @Override
    public SysArea selectSysAreaByAreaId(Long areaId)
    {
        return sysAreaMapper.selectSysAreaByAreaId(areaId);
    }

    /**
     * 查询行政区列表
     * 
     * @param sysArea 行政区
     * @return 行政区
     */
    @Override
    public List<SysArea> selectSysAreaList(SysArea sysArea)
    {
        return sysAreaMapper.selectSysAreaList(sysArea);
    }

    /**
     * 新增行政区
     * 
     * @param sysArea 行政区
     * @return 结果
     */
    @Override
    public int insertSysArea(SysArea sysArea)
    {
        sysArea.setCreateTime(DateUtils.getNowDate());
        return sysAreaMapper.insertSysArea(sysArea);
    }

    /**
     * 修改行政区
     * 
     * @param sysArea 行政区
     * @return 结果
     */
    @Override
    public int updateSysArea(SysArea sysArea)
    {
        sysArea.setUpdateTime(DateUtils.getNowDate());
        return sysAreaMapper.updateSysArea(sysArea);
    }

    /**
     * 批量删除行政区
     * 
     * @param areaIds 需要删除的行政区主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaByAreaIds(String areaIds)
    {
        return sysAreaMapper.deleteSysAreaByAreaIds(Convert.toStrArray(areaIds));
    }

    /**
     * 删除行政区信息
     * 
     * @param areaId 行政区主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaByAreaId(Long areaId)
    {
        return sysAreaMapper.deleteSysAreaByAreaId(areaId);
    }

    /**
     * 查询行政区树列表
     * 
     * @return 所有行政区信息
     */
    @Override
    public List<Ztree> selectSysAreaTree()
    {
        List<SysArea> sysAreaList = sysAreaMapper.selectSysAreaList(new SysArea());
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (SysArea sysArea : sysAreaList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(sysArea.getAreaId());
            ztree.setpId(sysArea.getParentId());
            ztree.setName(sysArea.getAreaName());
            ztree.setTitle(sysArea.getAreaName());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    /**
     * 导入行政区数据
     *
     * @param areaList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importArea(List<SysArea> areaList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(areaList) || areaList.size() == 0)
        {
            throw new ServiceException("导入行政区数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysArea area : areaList)
        {
            try
            {
                // 验证是否存在这个行政区
                SysArea a = sysAreaMapper.selectSysAreaByAreaId(area.getAreaId());
                if (StringUtils.isNull(a))
                {
                    area.setAreaId(area.getAreaId());
                    area.setCreateBy(operName);
                    this.insertSysArea(area);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、地区 " + area.getShortName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    area.setUpdateBy(operName);
                    this.updateSysArea(area);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、地区 " + area.getShortName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、地区 " + area.getShortName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、地区 " + area.getShortName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
