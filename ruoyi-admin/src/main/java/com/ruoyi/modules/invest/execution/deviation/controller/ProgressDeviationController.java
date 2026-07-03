package com.ruoyi.modules.invest.execution.deviation.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.modules.invest.execution.deviation.service.IProgressDeviationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/execution/deviation")
public class ProgressDeviationController extends BaseController {
 @Autowired private IProgressDeviationService service;
 @RequiresPermissions("invest:execution:deviation:view") @GetMapping("/project/{projectId}") public AjaxResult project(@PathVariable Long projectId){return success(service.getProjectDeviation(projectId));}
}
