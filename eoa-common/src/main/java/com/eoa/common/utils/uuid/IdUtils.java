package com.eoa.common.utils.uuid;

/**
 * @author ChengLong
 * @ClassName: IdUtils
 * @Description ID生成器工具类
 * @Date 2021/9/8 0008 11:21
 * @Version 1.0.0
 */
public class IdUtils {

    /**
     * 获取随机UUID
     * @return
     */
    public static String randomUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     * @return
     */
    public static String simpleUUID(){
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }
}
