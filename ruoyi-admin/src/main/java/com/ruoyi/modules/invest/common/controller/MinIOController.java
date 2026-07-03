package com.ruoyi.modules.invest.common.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.modules.invest.common.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * MinIO文件上传Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/file")
public class MinIOController extends BaseController {

    @Autowired
    private MinIOService minIOService;

    @Value("${minio.bucket:investvf}")
    private String defaultBucket;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String ext = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectName = UUID.randomUUID().toString().replace("-", "") + ext;
            String url = minIOService.uploadFile(file, defaultBucket, objectName);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", originalFilename);
            ajax.put("objectName", objectName);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @PostMapping("/delete")
    public AjaxResult delete(String objectName) {
        try {
            minIOService.deleteFile(defaultBucket, objectName);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error("删除失败：" + e.getMessage());
        }
    }
}
