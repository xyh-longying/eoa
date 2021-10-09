package com.eoa.web.controller.common;

import com.eoa.common.config.EoaConfig;
import com.eoa.common.config.ServerConfig;
import com.eoa.common.constant.Constants;
import com.eoa.common.core.domain.AjaxResult;
import com.eoa.common.utils.StringUtils;
import com.eoa.common.utils.file.FileUploadUtils;
import com.eoa.common.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengLong
 * @ClassName: CommonController
 * @Description 通用请求处理
 * @Date 2021/9/8 0008 9:36
 * @Version 1.0.0
 */
@Controller
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     * @param fileName 文件名
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request){
        try {
            if(!FileUtils.checkAllowDownload(fileName)){
                throw new Exception(StringUtils.format("文件名称（{}）非法，不允许下载。",fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = EoaConfig.getDownloadPath() + fileName;
            
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete){
               FileUtils.deleteFile(filePath);
            }
        } catch (Exception e){
            log.error("下载文件失败",e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/common/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception{
        try {
            // 上传文件路径
            String filePath = EoaConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/common/uploads")
    @ResponseBody
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception{
        try {
            String filePath = EoaConfig.getUploadPath();
            List<String> fileNames = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            for(MultipartFile file : files){
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                fileNames.add(fileName);
                urls.add(url);
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            return ajax;
        } catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     * @param resource
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception{
        try {
            if(!FileUtils.checkAllowDownload(resource)){
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。", resource));
            }
            //本地资源路径
            String localpath = EoaConfig.getProfile();
            //数据库资源地址
            String downloadPath = localpath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            //下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e){
            log.error("下载文件失败", e);
        }
    }
}
