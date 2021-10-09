package com.eoa.common.core.controller;

import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.core.domain.AjaxResult.Type;
import com.eoa.common.core.domain.entity.SysUser;
import com.eoa.common.core.page.PageDomain;
import com.eoa.common.core.page.TableDataInfo;
import com.eoa.common.core.page.TableSupport;
import com.eoa.common.utils.DateUtils;
import com.eoa.common.utils.ServletUtils;
import com.eoa.common.utils.ShiroUtils;
import com.eoa.common.utils.StringUtils;
import com.eoa.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * @author ChengLong
 * @ClassName: BaseController
 * @Description web层通用数据处理
 * @Date 2021/9/8 0008 16:21
 * @Version 1.0.0
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text){
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(){
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if(StringUtils.isNotEmpty(pageDomain.getOrderBy())){
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy(){
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if(StringUtils.isNotEmpty(pageDomain.getOrderBy())){
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 获取request
     * @return
     */
    public HttpServletRequest getRequest(){
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     * @return
     */
    public HttpServletResponse getResponse(){
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     * @return
     */
    public HttpSession getSession(){
        return ServletUtils.getSession();
    }

    /**
     * 响应请求分页数据
     * @param list
     * @return
     */
    protected TableDataInfo getDataTable(List<?> list){
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     * @param rows
     * @return
     */
    protected AjaxResult toAjax(int rows){
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     * @param result
     * @return
     */
    protected AjaxResult toAjax(boolean result){
        return result ? success() : error();
    }

    /**
     * 返回成功
     * @return
     */
    public AjaxResult success(){
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     * @return
     */
    public AjaxResult error(){
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     * @param message
     * @return
     */
    public AjaxResult success(String message){
        return AjaxResult.success(message);
    }

    /**
     * 返回成功数据
     * @param data
     * @return
     */
    public AjaxResult success(Object data){
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回失败消息
     * @param message
     * @return
     */
    public AjaxResult error(String message){
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     * @param type
     * @param message
     * @return
     */
    public AjaxResult error(Type type, String message){
        return new AjaxResult(type, message);
    }

    /**
     * 页面跳转
     * @param url
     * @return
     */
    public String redirect(String url){
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     * @return
     */
    public SysUser getSysUser(){
        return ShiroUtils.getSysUser();
    }

    /**
     * 设置用户缓存信息
     * @param user
     */
    public void setSysUser(SysUser user){
        ShiroUtils.setSysUser(user);
    }

    /**
     * 获取登录用户id
     * @return
     */
    public Long getUserId(){
        return getSysUser().getUserId();
    }

    /**
     * 获取登录用户名
     * @return
     */
    public String getLoginName(){
        return getSysUser().getLoginName();
    }
}
