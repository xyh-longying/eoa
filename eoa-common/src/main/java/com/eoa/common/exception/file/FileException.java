package com.eoa.common.exception.file;

import com.eoa.common.exception.base.BaseException;

/**
 * @author ChengLong
 * @ClassName: FileException
 * @Description 文件信息异常类
 * @Date 2021/9/8 0008 12:11
 * @Version 1.0.0
 */
public class FileException extends BaseException {

    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }
}
