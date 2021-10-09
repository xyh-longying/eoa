package com.eoa.system.mapper;

import com.eoa.system.domain.SysUserOnline;

import java.util.List;

/**
 * @author ChengLong
 * @ClassName: SysUserOnlineMapper
 * @Description 在线用户 数据层
 * @Date 2021/9/9 0009 16:07
 * @Version 1.0.0
 */
public interface SysUserOnlineMapper {
    /**
     * 通过会话序号查询信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineById(String sessionId);

    /**
     * 通过会话序号删除信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public int deleteOnlineById(String sessionId);

    /**
     * 保存会话信息
     *
     * @param online 会话信息
     * @return 结果
     */
    public int saveOnline(SysUserOnline online);

    /**
     * 查询会话集合
     *
     * @param userOnline 会话参数
     * @return 会话集合
     */
    public List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline);

    /**
     * 查询过期会话集合
     *
     * @param lastAccessTime 过期时间
     * @return 会话集合
     */
    public List<SysUserOnline> selectOnlineByExpired(String lastAccessTime);
}
