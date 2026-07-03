package com.ruoyi.modules.invest.plan.fund.tracking.service.impl;

import com.ruoyi.modules.invest.plan.fund.tracking.service.IFundTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FundTrackingServiceImpl implements IFundTrackingService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> selectPaymentLedger(String projectName, String payeeName,
                                                          String sourceName, String startDate,
                                                          String endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.payment_id, p.payment_code, p.project_name, p.payee_name, ")
           .append("p.payee_bank, p.payee_account, p.apply_amount, p.paid_amount, ")
           .append("p.purpose, p.pay_status, p.apply_date, p.pay_date, ")
           .append("ps.source_name AS split_source_name, ps.split_amount ")
           .append("FROM invest_fund_payment p ")
           .append("LEFT JOIN invest_fund_payment_split ps ON p.payment_id = ps.payment_id ")
           .append("WHERE p.del_flag = '0' ");

        List<Object> params = new ArrayList<>();
        if (projectName != null && !projectName.isEmpty()) {
            sql.append("AND p.project_name LIKE ? "); params.add("%" + projectName + "%");
        }
        if (payeeName != null && !payeeName.isEmpty()) {
            sql.append("AND p.payee_name LIKE ? "); params.add("%" + payeeName + "%");
        }
        if (sourceName != null && !sourceName.isEmpty()) {
            sql.append("AND ps.source_name LIKE ? "); params.add("%" + sourceName + "%");
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND p.pay_date >= ? "); params.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND p.pay_date <= ? "); params.add(endDate);
        }
        sql.append("ORDER BY p.pay_date DESC, p.payment_id DESC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    @Override
    public Map<String, Object> getDashboardStats(String startDate, String endDate) {
        StringBuilder where = new StringBuilder(" WHERE p.del_flag='0' AND p.pay_status='3'");
        List<Object> params = new ArrayList<>();
        if (startDate != null && !startDate.isEmpty()) {
            where.append(" AND p.pay_date >= ?"); params.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            where.append(" AND p.pay_date <= ?"); params.add(endDate);
        }

        String sql = "SELECT COUNT(*) totalPayments, COALESCE(SUM(p.paid_amount),0) totalPaid, "
                + "COUNT(DISTINCT p.project_id) projectCount, COUNT(DISTINCT p.payee_name) payeeCount "
                + "FROM invest_fund_payment p" + where.toString();
        List<Map<String, Object>> stats = jdbcTemplate.queryForList(sql, params.toArray());

        String topSql = "SELECT p.payee_name, SUM(p.paid_amount) total FROM invest_fund_payment p"
                + where.toString() + " GROUP BY p.payee_name ORDER BY total DESC LIMIT 5";
        List<Map<String, Object>> topPayees = jdbcTemplate.queryForList(topSql, params.toArray());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("stats", stats.isEmpty() ? new HashMap<>() : stats.get(0));
        result.put("topPayees", topPayees);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectPaymentSplits(Long paymentId) {
        return jdbcTemplate.queryForList(
                "SELECT ps.split_id, ps.source_name, ps.split_amount, s.source_type "
                + "FROM invest_fund_payment_split ps "
                + "LEFT JOIN invest_fund_source s ON ps.source_id = s.source_id "
                + "WHERE ps.payment_id = ? ORDER BY ps.split_id", paymentId);
    }
}
