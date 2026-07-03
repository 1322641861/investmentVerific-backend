package com.ruoyi.modules.invest.report.dashboard.service.impl;

import com.ruoyi.modules.invest.report.dashboard.domain.dto.DashboardDataVO;
import com.ruoyi.modules.invest.report.dashboard.domain.dto.StatCardVO;
import com.ruoyi.modules.invest.report.dashboard.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements IDashboardService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DashboardDataVO getDashboardData() {
        DashboardDataVO vo = new DashboardDataVO();

        StatCardVO totalProject = new StatCardVO();
        totalProject.setTitle("项目总数");
        totalProject.setIcon("fa fa-project-diagram");
        totalProject.setColor("#409EFF");
        totalProject.setValue(getCount("SELECT COUNT(*) FROM invest_project WHERE del_flag='0'"));
        totalProject.setUnit("个");
        vo.setTotalProject(totalProject);

        StatCardVO totalInvest = new StatCardVO();
        totalInvest.setTitle("投资总额");
        totalInvest.setIcon("fa fa-money");
        totalInvest.setColor("#67C23A");
        totalInvest.setValue(getBigDecimal("SELECT COALESCE(SUM(invested_amount),0) FROM invest_project WHERE del_flag='0'"));
        totalInvest.setUnit("万元");
        vo.setTotalInvest(totalInvest);

        StatCardVO inProgress = new StatCardVO();
        inProgress.setTitle("进行中项目");
        inProgress.setIcon("fa fa-spinner");
        inProgress.setColor("#E6A23C");
        inProgress.setValue(getCount("SELECT COUNT(*) FROM invest_project WHERE project_status IN ('2','3') AND del_flag='0'"));
        inProgress.setUnit("个");
        vo.setInProgress(inProgress);

        StatCardVO avgRoi = new StatCardVO();
        avgRoi.setTitle("平均ROI");
        avgRoi.setIcon("fa fa-chart-line");
        avgRoi.setColor("#F56C6C");
        avgRoi.setValue(getBigDecimal("SELECT COALESCE(AVG(roi),0) FROM invest_benefit_calculation WHERE del_flag='0'"));
        avgRoi.setUnit("%");
        vo.setAvgRoi(avgRoi);

        vo.setProjectStatusChart(getProjectStatusChart());
        vo.setInvestTrendChart(getInvestTrendChart());
        vo.setFundSourceChart(getFundSourceChart());
        vo.setRecentProjects(getRecentProjects());

        return vo;
    }

    private BigDecimal getCount(String sql) {
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count != null ? BigDecimal.valueOf(count) : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getBigDecimal(String sql) {
        try {
            BigDecimal val = jdbcTemplate.queryForObject(sql, BigDecimal.class);
            return val != null ? val : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private List<Map<String, Object>> getProjectStatusChart() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String[] labels = {"草稿","审批中","尽调中","风险评估中","已审批","已投资","已终止"};
            String[] statuses = {"0","1","2","3","4","5","6"};
            String[] colors = {"#909399","#409EFF","#67C23A","#E6A23C","#F56C6C","#06b604","#909399"};
            for(int i=0;i<labels.length;i++) {
                Map<String, Object> m = new HashMap<>();
                m.put("name", labels[i]);
                m.put("value", getCount("SELECT COUNT(*) FROM invest_project WHERE project_status='"+statuses[i]+"' AND del_flag='0'"));
                m.put("color", colors[i]);
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Map<String, Object>> getInvestTrendChart() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            for(int i=5;i>=0;i--) {
                Map<String, Object> m = new HashMap<>();
                String month = java.time.LocalDate.now().minusMonths(i).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                m.put("month", month);
                m.put("amount", BigDecimal.ZERO);
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Map<String, Object>> getFundSourceChart() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Map<String, Object> m1 = new HashMap<>();
            m1.put("name", "自有资金");
            m1.put("value", BigDecimal.valueOf(50));
            m1.put("color", "#409EFF");
            list.add(m1);
            Map<String, Object> m2 = new HashMap<>();
            m2.put("name", "银行贷款");
            m2.put("value", BigDecimal.valueOf(30));
            m2.put("color", "#67C23A");
            list.add(m2);
            Map<String, Object> m3 = new HashMap<>();
            m3.put("name", "其他");
            m3.put("value", BigDecimal.valueOf(20));
            m3.put("color", "#E6A23C");
            list.add(m3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Map<String, Object>> getRecentProjects() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String sql = "SELECT p.project_id,p.project_code,p.project_name,p.invested_amount,p.project_status,p.create_time,d.dept_name " +
                    "FROM invest_project p LEFT JOIN sys_dept d ON p.dept_id = d.dept_id WHERE p.del_flag='0' ORDER BY p.create_time DESC LIMIT 10";
            list = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
