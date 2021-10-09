package com.eoa.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * @author ChengLong
 * @ClassName: TableDataInfo
 * @Description 表格分页数据对象
 * @Date 2021/9/8 0008 20:20
 * @Version 1.0.0
 */
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<?> rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    public TableDataInfo() {
    }
    
    public TableDataInfo(List<?> list, long total){
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
