package com.eoa.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author ChengLong
 * @ClassName: SysUserPost
 * @Description 用户和岗位关联 sys_user_post
 * @Date 2021/9/9 0009 15:59
 * @Version 1.0.0
 */
public class SysUserPost {
    /** 用户ID */
    private Long userId;

    /** 岗位ID */
    private Long postId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("postId", getPostId())
                .toString();
    }
}
