package com.ruoyi.modules.invest.postinvest.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.postinvest.domain.PostInvestTracking;
import com.ruoyi.modules.invest.postinvest.service.IPostInvestTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投后跟踪Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/postinvest")
public class PostInvestTrackingController extends BaseController {

    @Autowired
    private IPostInvestTrackingService trackingService;

    @GetMapping("/list")
    public TableDataInfo list(PostInvestTracking tracking) {
        startPage();
        List<PostInvestTracking> list = trackingService.selectPostInvestTrackingList(tracking);
        return getDataTable(list);
    }

    @GetMapping("/{trackingId}")
    public AjaxResult getInfo(@PathVariable Long trackingId) {
        PostInvestTracking tracking = trackingService.selectPostInvestTrackingById(trackingId);
        return success(tracking);
    }

    @GetMapping("/byProject/{projectId}")
    public AjaxResult getByProject(@PathVariable Long projectId) {
        List<PostInvestTracking> list = trackingService.selectByProjectId(projectId);
        return success(list);
    }

    @Log(title = "投后跟踪", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PostInvestTracking tracking) {
        return toAjax(trackingService.insertPostInvestTracking(tracking));
    }

    @Log(title = "投后跟踪", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PostInvestTracking tracking) {
        return toAjax(trackingService.updatePostInvestTracking(tracking));
    }

    @Log(title = "投后跟踪", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(trackingService.deletePostInvestTrackingByIds(ids));
    }
}
