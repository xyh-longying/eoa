package com.eoa.common.utils.security;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author ChengLong
 * @ClassName: CipherUtils
 * @Description 对称密钥密码算法工具类
 * @Date 2021/9/9 0009 11:51
 * @Version 1.0.0
 */
public class CipherUtils {
    /**
     * 生成随机秘钥
     *
     * @param keyBitSize 字节大小
     * @param algorithmName 算法名称
     * @return 创建密匙
     */
    public static Key generateNewKey(int keyBitSize, String algorithmName)
    {
        KeyGenerator kg;
        try
        {
            kg = KeyGenerator.getInstance(algorithmName);
        }
        catch (NoSuchAlgorithmException e)
        {
            String msg = "Unable to acquire " + algorithmName + " algorithm.  This is required to function.";
            throw new IllegalStateException(msg, e);
        }
        kg.init(keyBitSize);
        return kg.generateKey();
    }
}
