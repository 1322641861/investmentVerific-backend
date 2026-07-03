package com.ruoyi.modules.invest.plan.fund.roi.service.impl;

import com.ruoyi.modules.invest.plan.fund.roi.service.IFundRoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FundRoiServiceImpl implements IFundRoiService {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Map<String, Object> getDashboard() {
        Map<String, Object> r = new LinkedHashMap<>();

        // 总ROI统计
        r.put("totalRoi", jdbc.queryForList(
            "SELECT COUNT(*) total, COALESCE(AVG(roi),0) avgRoi, COALESCE(AVG(benefit_score),0) avgScore, " +
            "COALESCE(SUM(expected_profit),0) totalProfit FROM invest_benefit_calculation WHERE del_flag='0'"));

        // 资金周转率（总支付/总预算）
        r.put("turnover", jdbc.queryForList(
            "SELECT COALESCE(SUM(fp.paid_total),0) totalPaid, COALESCE(SUM(fp.plan_total),0) totalPlan, " +
            "CASE WHEN COALESCE(SUM(fp.plan_total),0)>0 THEN ROUND(SUM(fp.paid_total)/SUM(fp.plan_total)*100,2) ELSE 0 END turnoverRate " +
            "FROM invest_fund_plan fp WHERE fp.del_flag='0'"));

        // 按年度ROI趋势
        r.put("roiByYear", jdbc.queryForList(
            "SELECT calc_year, COUNT(*) cnt, ROUND(AVG(roi),2) avgRoi, ROUND(AVG(benefit_score),2) avgScore " +
            "FROM invest_benefit_calculation WHERE del_flag='0' GROUP BY calc_year ORDER BY calc_year DESC"));

        return r;
    }

    @Override
    public List<Map<String, Object>> selectProjectRoiList(Integer planYear, String orderBy) {
        String sql = "SELECT c.calc_id, c.calc_code, p.project_name, c.calc_year, " +
            "c.invest_amount, c.expected_income, c.expected_profit, c.roi, c.benefit_score, c.calc_status, " +
            "COALESCE(fp.paid_total,0) actualPaid, COALESCE(fp.plan_total,0) fundPlan " +
            "FROM invest_benefit_calculation c " +
            "LEFT JOIN invest_project p ON c.project_id = p.project_id " +
            "LEFT JOIN invest_fund_plan fp ON fp.project_id = c.project_id " +
            "WHERE c.del_flag='0'";

        List<Object> params = new ArrayList<>();
        if (planYear != null) { sql += " AND c.calc_year=?"; params.add(planYear); }

        if ("roi".equals(orderBy)) sql += " ORDER BY c.roi DESC";
        else if ("score".equals(orderBy)) sql += " ORDER BY c.benefit_score DESC";
        else if ("profit".equals(orderBy)) sql += " ORDER BY c.expected_profit DESC";
        else sql += " ORDER BY c.calc_year DESC, c.roi DESC";

        return jdbc.queryForList(sql, params.toArray());
    }

    @Override
    public List<Map<String, Object>> selectMonthlyTrend(Integer year) {
        String sql = "SELECT DATE_FORMAT(p.pay_date,'%Y-%m') month, COUNT(*) payCount, " +
            "COALESCE(SUM(p.paid_amount),0) totalPaid FROM invest_fund_payment p " +
            "WHERE p.pay_status='3' AND YEAR(p.pay_date)=? " +
            "GROUP BY DATE_FORMAT(p.pay_date,'%Y-%m') ORDER BY month";
        return jdbc.queryForList(sql, year);
    }
}
