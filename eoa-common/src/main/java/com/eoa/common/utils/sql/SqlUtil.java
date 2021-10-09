package com.eoa.common.utils.sql;

import com.eoa.common.exception.base.BaseException;
import com.eoa.common.utils.StringUtils;

/**
 * @author ChengLong
 * @ClassName: SqlUtil
 * @Description sql操作工具类
 * @Date 2021/9/8 0008 20:08
 * @Version 1.0.0
 */
public class SqlUtil {

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     * @param value
     * @return
     */
    public static String escapeOrderBySql(String value){
        if(StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)){
            throw new BaseException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     * @param value
     * @return
     */
    public static Boolean isValidOrderBySql(String value){
        return value.matches(SQL_PATTERN);
    }
}
