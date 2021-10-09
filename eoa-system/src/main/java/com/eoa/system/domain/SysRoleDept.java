package com.eoa.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author ChengLong
 * @ClassName: SysRoleDept
 * @Description 角色和部门关联 sys_role_dept
 * @Date 2021/9/9 0009 15:57
 * @Version 1.0.0
 */
public class SysRoleDept {
    /** 角色ID */
    private Long roleId;

    /** 部门ID */
    private Long deptId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("deptId", getDeptId())
                .toString();
    }
}
