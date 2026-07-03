package com.ruoyi.modules.invest.contract.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.contract.domain.InvestContract;
import com.ruoyi.modules.invest.contract.service.IInvestContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投资合同Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/contract")
public class InvestContractController extends BaseController {

    @Autowired
    private IInvestContractService investContractService;

    @GetMapping("/list")
    public TableDataInfo list(InvestContract contract) {
        startPage();
        List<InvestContract> list = investContractService.selectInvestContractList(contract);
        return getDataTable(list);
    }

    @GetMapping("/{contractId}")
    public AjaxResult getInfo(@PathVariable Long contractId) {
        InvestContract contract = investContractService.selectInvestContractById(contractId);
        return success(contract);
    }

    @GetMapping("/byProject/{projectId}")
    public AjaxResult getByProject(@PathVariable Long projectId) {
        List<InvestContract> list = investContractService.selectByProjectId(projectId);
        return success(list);
    }

    @Log(title = "投资合同", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestContract contract) {
        return toAjax(investContractService.insertInvestContract(contract));
    }

    @Log(title = "投资合同", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestContract contract) {
        return toAjax(investContractService.updateInvestContract(contract));
    }

    @Log(title = "投资合同", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(investContractService.deleteInvestContractByIds(ids));
    }
}
