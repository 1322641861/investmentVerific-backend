package com.ruoyi.modules.invest.report.data.service.impl;

import com.ruoyi.modules.invest.report.data.service.IReportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 标准报表数据服务实现
 * 使用JdbcTemplate直接查询，避免创建大量Mapper
 *
 * @author investvf
 */
@Service
public class ReportDataServiceImpl implements IReportDataService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getSchemeReport(Integer planYear) {
        Map<String, Object> result = new LinkedHashMap<>();

        // 统计卡片
        String sql = "SELECT COUNT(*) total, COALESCE(SUM(total_amount),0) totalAmount, " +
                "AVG(total_amount) avgAmount FROM plan_scheme WHERE del_flag='0'";
        if (planYear != null) sql += " AND plan_year=" + planYear;
        List<Map<String, Object>> stats = jdbcTemplate.queryForList(sql);
        result.put("stats", stats.isEmpty() ? new HashMap<>() : stats.get(0));

        // 按年度分组
        String yearSql = "SELECT plan_year, COUNT(*) cnt, COALESCE(SUM(total_amount),0) amt " +
                "FROM plan_scheme WHERE del_flag='0' GROUP BY plan_year ORDER BY plan_year DESC";
        result.put("byYear", jdbcTemplate.queryForList(yearSql));

        // 按状态分组
        String statusSql = "SELECT scheme_status, COUNT(*) cnt, COALESCE(SUM(total_amount),0) amt " +
                "FROM plan_scheme WHERE del_flag='0' GROUP BY scheme_status ORDER BY scheme_status";
        result.put("byStatus", jdbcTemplate.queryForList(statusSql));

        // 按类型分组
        String typeSql = "SELECT scheme_type, COUNT(*) cnt, COALESCE(SUM(total_amount),0) amt " +
                "FROM plan_scheme WHERE del_flag='0' GROUP BY scheme_type ORDER BY scheme_type";
        result.put("byType", jdbcTemplate.queryForList(typeSql));

        // 近期方案列表
        String listSql = "SELECT scheme_code, scheme_name, scheme_type, plan_year, total_amount, scheme_status, leader_name " +
                "FROM plan_scheme WHERE del_flag='0' ORDER BY create_time DESC LIMIT 10";
        result.put("list", jdbcTemplate.queryForList(listSql));

        return result;
    }

    @Override
    public Map<String, Object> getProjectReport(String projectStatus) {
        Map<String, Object> result = new LinkedHashMap<>();

        String where = " WHERE p.del_flag='0'";
        if (projectStatus != null && !projectStatus.isEmpty()) where += " AND p.project_status='" + projectStatus + "'";

        // 统计卡片
        String statsSql = "SELECT COUNT(*) total, COALESCE(SUM(p.proposed_amount),0) totalAmount, " +
                "COALESCE(AVG(p.proposed_amount),0) avgAmount FROM invest_project p" + where;
        List<Map<String, Object>> stats = jdbcTemplate.queryForList(statsSql);
        result.put("stats", stats.isEmpty() ? new HashMap<>() : stats.get(0));

        // 按状态分组
        String statusSql = "SELECT p.project_status, COUNT(*) cnt, COALESCE(SUM(p.proposed_amount),0) amt " +
                "FROM invest_project p WHERE p.del_flag='0' GROUP BY p.project_status ORDER BY p.project_status";
        result.put("byStatus", jdbcTemplate.queryForList(statusSql));

        // 按年度分组
        String yearSql = "SELECT p.plan_year, COUNT(*) cnt, COALESCE(SUM(p.proposed_amount),0) amt " +
                "FROM invest_project p WHERE p.del_flag='0' GROUP BY p.plan_year ORDER BY p.plan_year DESC";
        result.put("byYear", jdbcTemplate.queryForList(yearSql));

        // 项目列表
        String listSql = "SELECT p.project_code, p.project_name, p.proposed_amount, p.invested_amount, " +
                "p.project_status, p.plan_year, p.leader_name, d.dept_name FROM invest_project p " +
                "LEFT JOIN sys_dept d ON p.dept_id = d.dept_id" + where + " ORDER BY p.create_time DESC LIMIT 10";
        result.put("list", jdbcTemplate.queryForList(listSql));

        return result;
    }

    @Override
    public Map<String, Object> getFundReport(Integer planYear) {
        Map<String, Object> result = new LinkedHashMap<>();

        String where = " WHERE fp.del_flag='0'";
        if (planYear != null) where += " AND fp.plan_year=" + planYear;

        // 统计卡片
        String statsSql = "SELECT COUNT(*) planCount, COALESCE(SUM(fp.plan_total),0) totalPlan, " +
                "COALESCE(SUM(fp.arrived_total),0) totalArrived, COALESCE(SUM(fp.paid_total),0) totalPaid " +
                "FROM invest_fund_plan fp" + where;
        List<Map<String, Object>> stats = jdbcTemplate.queryForList(statsSql);
        result.put("stats", stats.isEmpty() ? new HashMap<>() : stats.get(0));

        // 按来源统计
        String sourceSql = "SELECT s.source_type, s.source_name, " +
                "COALESCE(SUM(fpi.planned_amount),0) planned, COALESCE(SUM(fpi.arrived_amount),0) arrived " +
                "FROM invest_fund_plan_item fpi " +
                "LEFT JOIN invest_fund_source s ON fpi.source_id = s.source_id " +
                "GROUP BY s.source_id ORDER BY planned DESC";
        result.put("bySource", jdbcTemplate.queryForList(sourceSql));

        // 配套率分布
        String rateSql = "SELECT CASE WHEN fp.match_rate >= 90 THEN '优(≥90%)' WHEN fp.match_rate >= 80 THEN '良(80-90%)' " +
                "ELSE '差(<80%)' END AS rate_level, COUNT(*) cnt " +
                "FROM invest_fund_plan fp" + where + " GROUP BY rate_level ORDER BY rate_level";
        result.put("byRate", jdbcTemplate.queryForList(rateSql));

        // 近期配套计划
        String listSql = "SELECT fp.plan_code, p.project_name, fp.plan_total, fp.arrived_total, fp.paid_total, fp.match_rate, fp.plan_status " +
                "FROM invest_fund_plan fp LEFT JOIN invest_project p ON fp.project_id = p.project_id" + where + " ORDER BY fp.create_time DESC LIMIT 10";
        result.put("list", jdbcTemplate.queryForList(listSql));

        return result;
    }

    @Override
    public Map<String, Object> getBenefitReport(Integer planYear) {
        Map<String, Object> result = new LinkedHashMap<>();

        String where = " WHERE c.del_flag='0'";
        if (planYear != null) where += " AND c.calc_year=" + planYear;

        // 统计卡片
        String statsSql = "SELECT COUNT(*) total, COALESCE(SUM(c.expected_profit),0) totalProfit, " +
                "COALESCE(AVG(c.roi),0) avgRoi, COALESCE(AVG(c.benefit_score),0) avgScore " +
                "FROM invest_benefit_calculation c" + where;
        List<Map<String, Object>> stats = jdbcTemplate.queryForList(statsSql);
        result.put("stats", stats.isEmpty() ? new HashMap<>() : stats.get(0));

        // 按年度分组
        String yearSql = "SELECT c.calc_year, COUNT(*) cnt, COALESCE(AVG(c.roi),0) avgRoi, " +
                "COALESCE(AVG(c.benefit_score),0) avgScore FROM invest_benefit_calculation c" + where +
                " GROUP BY c.calc_year ORDER BY c.calc_year DESC";
        result.put("byYear", jdbcTemplate.queryForList(yearSql));

        // 按状态分组
        String statusSql = "SELECT c.calc_status, COUNT(*) cnt FROM invest_benefit_calculation c" + where +
                " GROUP BY c.calc_status ORDER BY c.calc_status";
        result.put("byStatus", jdbcTemplate.queryForList(statusSql));

        // 效益评分TOP10
        String topSql = "SELECT c.calc_code, p.project_name, c.roi, c.benefit_score, c.expected_profit, c.calc_year " +
                "FROM invest_benefit_calculation c LEFT JOIN invest_project p ON c.project_id = p.project_id" +
                where + " ORDER BY c.benefit_score DESC LIMIT 10";
        result.put("topList", jdbcTemplate.queryForList(topSql));

        return result;
    }

    @Override
    public Map<String, Object> getProgressReport(Integer planYear) {
        Map<String, Object> result = new LinkedHashMap<>();

        // 统计卡片
        String statsSql = "SELECT COUNT(*) total, COALESCE(AVG(progress_rate),0) avgRate, " +
                "SUM(CASE WHEN progress_rate >= 100 THEN 1 ELSE 0 END) completed " +
                "FROM invest_project_milestone WHERE del_flag='0'";
        if (planYear != null) statsSql += " AND plan_year=" + planYear;
        List<Map<String, Object>> stats = jdbcTemplate.queryForList(statsSql);
        result.put("stats", stats.isEmpty() ? new HashMap<>() : stats.get(0));

        // 按状态分组
        String statusSql = "SELECT m.status, COUNT(*) cnt FROM invest_project_milestone m WHERE m.del_flag='0' " +
                (planYear != null ? " AND m.plan_year=" + planYear : "") + " GROUP BY m.status";
        result.put("byStatus", jdbcTemplate.queryForList(statusSql));

        // 预警统计
        String warnSql = "SELECT w.warning_type, w.warning_level, COUNT(*) cnt, " +
                "SUM(CASE WHEN w.status='0' THEN 1 ELSE 0 END) unresolved " +
                "FROM invest_progress_warning w GROUP BY w.warning_type, w.warning_level ORDER BY w.warning_type";
        result.put("warningStats", jdbcTemplate.queryForList(warnSql));

        // 近期里程碑
        String listSql = "SELECT m.milestone_code, m.milestone_name, p.project_name, m.progress_rate, m.status, " +
                "m.plan_end_date FROM invest_project_milestone m LEFT JOIN invest_project p ON m.project_id = p.project_id " +
                "WHERE m.del_flag='0' ORDER BY m.update_time DESC LIMIT 10";
        result.put("list", jdbcTemplate.queryForList(listSql));

        return result;
    }
}
