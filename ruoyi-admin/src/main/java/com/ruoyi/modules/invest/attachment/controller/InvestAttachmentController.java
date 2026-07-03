package com.ruoyi.modules.invest.attachment.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.attachment.domain.InvestAttachment;
import com.ruoyi.modules.invest.attachment.service.IInvestAttachmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/invest/attachment")
public class InvestAttachmentController extends BaseController {
    @Autowired private IInvestAttachmentService attachmentService;

    @RequiresPermissions("invest:attachment:list")
    @GetMapping("/list") public TableDataInfo list(InvestAttachment att) { startPage(); return getDataTable(attachmentService.selectList(att)); }

    @RequiresPermissions("invest:attachment:list")
    @GetMapping("/business/{businessType}/{businessId}") public AjaxResult byBusiness(@PathVariable String businessType, @PathVariable Long businessId) { return success(attachmentService.selectByBusiness(businessType, businessId)); }

    @RequiresPermissions("invest:attachment:add")
    @Log(title = "附件上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload") public AjaxResult upload(@RequestParam("file") MultipartFile file, @RequestParam String businessType, @RequestParam(required = false) Long businessId) { return success(attachmentService.uploadFile(file, businessType, businessId)); }

    @RequiresPermissions("invest:attachment:remove")
    @Log(title = "附件删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{attachmentIds}") public AjaxResult remove(@PathVariable Long[] attachmentIds) { return toAjax(attachmentService.deleteByIds(attachmentIds)); }
}
