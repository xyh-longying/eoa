package com.eoa.framework.shiro.util;

import com.eoa.framework.shiro.realm.UserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;

/**
 * @author ChengLong
 * @ClassName: AuthorizationUtils
 * @Description 用户授权信息
 * @Date 2021/9/9 0009 18:29
 * @Version 1.0.0
 */
public class AuthorizationUtils {
    /**
     * 清理所有用户授权信息缓存
     */
    public static void clearAllCachedAuthorizationInfo()
    {
        getUserRealm().clearAllCachedAuthorizationInfo();
    }

    /**
     * 获取自定义Realm
     */
    public static UserRealm getUserRealm()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        return (UserRealm) rsm.getRealms().iterator().next();
    }
}
