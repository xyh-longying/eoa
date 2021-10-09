package com.eoa.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ChengLong
 * @ClassName: MapDataUtil
 * @Description Map通用处理方法
 * @Date 2021/9/9 0009 12:03
 * @Version 1.0.0
 */
public class MapDataUtil {
    public static Map<String, Object> convertDataMap(HttpServletRequest request)
    {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<?> entries = properties.entrySet().iterator();
        Map.Entry<?, ?> entry;
        String name = "";
        String value = "";
        while (entries.hasNext())
        {
            entry = (Map.Entry<?, ?>) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj)
            {
                value = "";
            }
            else if (valueObj instanceof String[])
            {
                String[] values = (String[]) valueObj;
                value = "";
                for (int i = 0; i < values.length; i++)
                {
                    value += values[i] + ",";
                }
                if (value.length() > 0)
                {
                    value = value.substring(0, value.length() - 1);
                }
            }
            else
            {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
}
