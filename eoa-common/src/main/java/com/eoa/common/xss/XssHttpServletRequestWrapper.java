package com.eoa.common.xss;

import com.eoa.common.utils.html.EscapeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author ChengLong
 * @ClassName: XssHttpServletRequestWrapper
 * @Description XSS过滤处理
 * @Date 2021/9/9 0009 12:22
 * @Version 1.0.0
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * @param request
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request)
    {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name)
    {
        String[] values = super.getParameterValues(name);
        if (values != null)
        {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++)
            {
                // 防xss攻击和过滤前后空格
                escapseValues[i] = EscapeUtil.clean(values[i]).trim();
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }
}
