package com.ruoyi.modules.invest.benefit.summary.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.benefit.calculation.domain.InvestBenefitCalculation;
import com.ruoyi.modules.invest.benefit.calculation.mapper.InvestBenefitCalculationMapper;
import com.ruoyi.modules.invest.benefit.evaluation.domain.InvestBenefitEvaluation;
import com.ruoyi.modules.invest.benefit.evaluation.mapper.InvestBenefitEvaluationMapper;
import com.ruoyi.modules.invest.benefit.summary.service.IBenefitSummaryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/invest/benefit/summary")
public class BenefitSummaryController extends BaseController {

    @Autowired
    private IBenefitSummaryService service;

    @Autowired
    private InvestBenefitCalculationMapper calculationMapper;

    @Autowired
    private InvestBenefitEvaluationMapper evaluationMapper;

    @RequiresPermissions("invest:benefit:summary:view")
    @GetMapping
    public AjaxResult summary() {
        return success(service.getSummary());
    }

    @RequiresPermissions("invest:benefit:summary:view")
    @GetMapping("/list")
    public TableDataInfo list(String projectName) {
        startPage();

        InvestBenefitCalculation query = new InvestBenefitCalculation();
        List<InvestBenefitCalculation> calculations = calculationMapper.selectList(query);

        List<Map<String, Object>> result = new ArrayList<>();

        for (InvestBenefitCalculation c : calculations) {
            if (projectName != null && !projectName.isEmpty()
                    && (c.getProjectName() == null || !c.getProjectName().contains(projectName))) {
                continue;
            }

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("projectId", c.getProjectId());
            map.put("projectCode", c.getProjectCode());
            map.put("projectName", c.getProjectName());
            map.put("investAmount", c.getInvestAmount());
            map.put("expectedIncome", c.getExpectedIncome());
            map.put("expectedProfit", c.getExpectedProfit());
            map.put("roi", c.getRoi());
            map.put("benefitScore", c.getBenefitScore());

            BigDecimal actualRoi = BigDecimal.ZERO;
            BigDecimal actualScore = BigDecimal.ZERO;
            String deviationNote = "";

            InvestBenefitEvaluation evalQuery = new InvestBenefitEvaluation();
            List<InvestBenefitEvaluation> evals = evaluationMapper.selectList(evalQuery);
            for (InvestBenefitEvaluation e : evals) {
                if (e.getProjectId() != null && e.getProjectId().equals(c.getProjectId())) {
                    actualRoi = e.getActualRoi() != null ? e.getActualRoi() : BigDecimal.ZERO;
                    actualScore = e.getActualScore() != null ? e.getActualScore() : BigDecimal.ZERO;
                    deviationNote = e.getDeviationNote();
                    break;
                }
            }

            map.put("actualRoi", actualRoi);
            map.put("actualScore", actualScore);
            map.put("deviationNote", deviationNote);

            result.add(map);
        }

        return getDataTable(result);
    }

    @RequiresPermissions("invest:benefit:summary:view")
    @GetMapping("/project/{projectId}")
    public AjaxResult project(@PathVariable Long projectId) {
        return success(service.project(projectId));
    }

    @RequiresPermissions("invest:benefit:summary:view")
    @GetMapping("/scheme/{schemeId}")
    public AjaxResult scheme(@PathVariable Long schemeId) {
        return success(service.scheme(schemeId));
    }
}
