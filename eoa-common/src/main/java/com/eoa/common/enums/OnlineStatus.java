package com.eoa.common.enums;

/**
 * @author ChengLong
 * @ClassName: OnlineStatus
 * @Description 用户会话
 * @Date 2021/9/9 0009 10:57
 * @Version 1.0.0
 */
public enum OnlineStatus {

    /** 用户状态 */
    on_line("在线"), off_line("离线");

    private final String info;

    private OnlineStatus(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
    }
}
