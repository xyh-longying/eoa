package com.eoa.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.eoa.common.annotation.Excel;
import com.eoa.common.core.domain.TreeEntity;

/**
 * 行政区对象 sys_area
 * 
 * @author ChengLong
 * @date 2021-10-10
 */
public class SysArea extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 行政区ID */
    @Excel(name = "行政区ID")
    private Long areaId;

    @Excel(name = "父级ID")
    private Long parentId;

    /** 行政区名称 */
    @Excel(name = "行政区名称")
    private String areaName;

    /** 简称 */
    @Excel(name = "简称")
    private String shortName;

    /** 经度 */
    @Excel(name = "经度")
    private Double longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private Double latitude;

    /** 等级 */
    @Excel(name = "等级")
    private Integer level;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    public void setAreaId(Long areaId) 
    {
        this.areaId = areaId;
    }

    public Long getAreaId() 
    {
        return areaId;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setAreaName(String areaName) 
    {
        this.areaName = areaName;
    }

    public String getAreaName() 
    {
        return areaName;
    }
    public void setShortName(String shortName) 
    {
        this.shortName = shortName;
    }

    public String getShortName() 
    {
        return shortName;
    }
    public void setLongitude(Double longitude) 
    {
        this.longitude = longitude;
    }

    public Double getLongitude() 
    {
        return longitude;
    }
    public void setLatitude(Double latitude) 
    {
        this.latitude = latitude;
    }

    public Double getLatitude() 
    {
        return latitude;
    }
    public void setLevel(Integer level) 
    {
        this.level = level;
    }

    public Integer getLevel() 
    {
        return level;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("areaId", getAreaId())
            .append("parentId", getParentId())
            .append("areaName", getAreaName())
            .append("shortName", getShortName())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("level", getLevel())
            .append("sort", getSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
