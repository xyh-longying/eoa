package com.eoa.common.exception.file;


/**
 * @author ChengLong
 * @ClassName: FileNameLengthLimitExceededException
 * @Description 文件名称超长限制异常类
 * @Date 2021/9/8 0008 12:09
 * @Version 1.0.0
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[] { defaultFileNameLength });
    }
}
