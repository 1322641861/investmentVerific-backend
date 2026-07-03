package com.ruoyi.modules.invest.benefit.calculation.service.impl;

import com.ruoyi.modules.invest.benefit.calculation.service.IFinancialAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class FinancialAnalysisServiceImpl implements IFinancialAnalysisService {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Map<String, Object> calculateNpvIrr(Long calcId, Double discountRate) {
        Map<String, Object> result = new LinkedHashMap<>();

        // 查询测算数据
        List<Map<String, Object>> rows = jdbc.queryForList(
            "SELECT calc_id, invest_amount, expected_income, expected_profit, payback_period, roi " +
            "FROM invest_benefit_calculation WHERE calc_id=? AND del_flag='0'", calcId);

        if (rows.isEmpty()) {
            result.put("error", "测算数据不存在");
            return result;
        }

        Map<String, Object> calc = rows.get(0);
        double invest = toDouble(calc.get("invest_amount"));
        double profit = toDouble(calc.get("expected_profit"));
        double rate = discountRate != null && discountRate > 0 ? discountRate : 8.0;
        double period = toDouble(calc.get("payback_period"));
        if (period <= 0) period = 5; // 默认5年

        // 计算NPV: Σ(profit / (1+r)^t) - invest, 假设每年利润相同
        double npv = -invest;
        for (int t = 1; t <= (int) Math.ceil(period); t++) {
            npv += profit / Math.pow(1 + rate / 100, t);
        }
        // 残值（剩余月份按比例）
        double remainder = period - Math.floor(period);
        if (remainder > 0) {
            npv += profit * remainder / Math.pow(1 + rate / 100, (int) Math.ceil(period));
        }

        // 计算IRR（使用牛顿法迭代）
        double irr = calculateIrr(invest, profit, (int) Math.ceil(period));

        result.put("calcId", calcId);
        result.put("investAmount", invest);
        result.put("annualProfit", profit);
        result.put("discountRate", rate);
        result.put("period", period);
        result.put("npv", BigDecimal.valueOf(npv).setScale(2, RoundingMode.HALF_UP).doubleValue());
        result.put("irr", BigDecimal.valueOf(irr * 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
        result.put("roi", calc.get("roi"));

        // 动态回收期 = 投资额 / 年利润
        if (profit > 0) {
            double dynamicPayback = invest / profit;
            result.put("dynamicPaybackPeriod", BigDecimal.valueOf(dynamicPayback).setScale(2, RoundingMode.HALF_UP).doubleValue());
        } else {
            result.put("dynamicPaybackPeriod", null);
        }

        return result;
    }

    @Override
    public Map<String, Object> batchCalculate(Double discountRate) {
        List<Map<String, Object>> calcs = jdbc.queryForList(
            "SELECT calc_id FROM invest_benefit_calculation WHERE del_flag='0' AND calc_status='2'");

        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> c : calcs) {
            Long id = ((Number) c.get("calc_id")).longValue();
            results.add(calculateNpvIrr(id, discountRate));
        }

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", results.size());
        summary.put("avgNpv", results.stream().filter(r -> r.get("npv") != null)
                .mapToDouble(r -> (double) r.get("npv")).average().orElse(0));
        summary.put("avgIrr", results.stream().filter(r -> r.get("irr") != null)
                .mapToDouble(r -> (double) r.get("irr")).average().orElse(0));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summary", summary);
        result.put("details", results);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectFinancialList(Integer planYear) {
        String sql = "SELECT c.calc_id, c.calc_code, p.project_name, c.calc_year, " +
            "c.invest_amount, c.expected_profit, c.roi, c.payback_period, c.benefit_score, c.calc_status " +
            "FROM invest_benefit_calculation c " +
            "LEFT JOIN invest_project p ON c.project_id = p.project_id " +
            "WHERE c.del_flag='0'";
        if (planYear != null) sql += " AND c.calc_year=" + planYear;
        sql += " ORDER BY c.calc_year DESC, c.roi DESC";
        return jdbc.queryForList(sql);
    }

    /**
     * 计算IRR（内部收益率）- 使用牛顿法
     * NPV = -invest + Σ(profit / (1+r)^t) = 0
     */
    private double calculateIrr(double invest, double annualProfit, int years) {
        if (invest <= 0 || annualProfit <= 0) return 0;

        double guess = 0.1; // 初始猜测10%
        for (int i = 0; i < 100; i++) {
            double npv = -invest;
            double dnpv = 0; // NPV对r的导数
            for (int t = 1; t <= years; t++) {
                double denom = Math.pow(1 + guess, t);
                npv += annualProfit / denom;
                dnpv -= t * annualProfit / Math.pow(1 + guess, t + 1);
            }
            if (Math.abs(dnpv) < 1e-10) break;
            double newGuess = guess - npv / dnpv;
            if (Math.abs(newGuess - guess) < 1e-7) return newGuess;
            guess = newGuess;
            if (guess < -0.99) guess = -0.99; // 避免发散
        }
        return Math.max(guess, 0);
    }

    private double toDouble(Object v) {
        if (v == null) return 0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        return Double.parseDouble(v.toString());
    }
}
