package com.ruoyi.modules.invest.postinvest.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.postinvest.domain.PostInvestTracking;
import com.ruoyi.modules.invest.postinvest.mapper.PostInvestTrackingMapper;
import com.ruoyi.modules.invest.postinvest.service.IPostInvestTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投后跟踪Service业务层
 *
 * @author investvf
 */
@Service
public class PostInvestTrackingServiceImpl implements IPostInvestTrackingService {

    @Autowired
    private PostInvestTrackingMapper trackingMapper;

    @Override
    public PostInvestTracking selectPostInvestTrackingById(Long trackingId) {
        return trackingMapper.selectPostInvestTrackingById(trackingId);
    }

    @Override
    public List<PostInvestTracking> selectPostInvestTrackingList(PostInvestTracking tracking) {
        return trackingMapper.selectPostInvestTrackingList(tracking);
    }

    @Override
    public int insertPostInvestTracking(PostInvestTracking tracking) {
        tracking.setCreateBy(ShiroUtils.getLoginName());
        tracking.setCreateTime(DateUtils.getNowDate());
        tracking.setDelFlag("0");
        if (tracking.getTrackingStatus() == null) {
            tracking.setTrackingStatus("0");
        }
        // 自动判断风险等级
        if (tracking.getDebtRatio() != null && tracking.getDebtRatio().compareTo(new java.math.BigDecimal("70")) > 0) {
            tracking.setRiskLevel("红色风险");
        } else if (tracking.getDebtRatio() != null && tracking.getDebtRatio().compareTo(new java.math.BigDecimal("50")) > 0) {
            tracking.setRiskLevel("黄色预警");
        } else {
            tracking.setRiskLevel("绿色正常");
        }
        return trackingMapper.insertPostInvestTracking(tracking);
    }

    @Override
    public int updatePostInvestTracking(PostInvestTracking tracking) {
        tracking.setUpdateBy(ShiroUtils.getLoginName());
        tracking.setUpdateTime(DateUtils.getNowDate());
        // 重新判断风险等级
        if (tracking.getDebtRatio() != null && tracking.getDebtRatio().compareTo(new java.math.BigDecimal("70")) > 0) {
            tracking.setRiskLevel("红色风险");
        } else if (tracking.getDebtRatio() != null && tracking.getDebtRatio().compareTo(new java.math.BigDecimal("50")) > 0) {
            tracking.setRiskLevel("黄色预警");
        } else {
            tracking.setRiskLevel("绿色正常");
        }
        return trackingMapper.updatePostInvestTracking(tracking);
    }

    @Override
    public int deletePostInvestTrackingByIds(Long[] ids) {
        return trackingMapper.deletePostInvestTrackingByIds(ids);
    }

    @Override
    public List<PostInvestTracking> selectByProjectId(Long projectId) {
        return trackingMapper.selectByProjectId(projectId);
    }
}
