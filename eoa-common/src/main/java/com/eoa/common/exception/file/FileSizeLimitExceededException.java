package com.eoa.common.exception.file;

/**
 * @author ChengLong
 * @ClassName: FileSizeLimitExceededException
 * @Description 文件名大小限制异常类
 * @Date 2021/9/8 0008 12:43
 * @Version 1.0.0
 */
public class FileSizeLimitExceededException extends FileException{
    private static final long serialVersionUID = 1L;
    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[] { defaultMaxSize });
    }
}
