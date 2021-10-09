package com.eoa.common.core.page;

import com.eoa.common.constant.Constants;
import com.eoa.common.utils.ServletUtils;

/**
 * @author ChengLong
 * @ClassName: TableSupport
 * @Description 表格数据处理
 * @Date 2021/9/8 0008 19:13
 * @Version 1.0.0
 */
public class TableSupport {

    /**
     * 封装分页对象
     * @return
     */
    public static PageDomain getPageDomain(){
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtils.getParameterToInt(Constants.PAGE_NUM));
        pageDomain.setPageSize(ServletUtils.getParameterToInt(Constants.PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(Constants.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(Constants.IS_ASC));
        return pageDomain;
    }
    
    public static PageDomain buildPageRequest(){
        return getPageDomain();
    }
}
