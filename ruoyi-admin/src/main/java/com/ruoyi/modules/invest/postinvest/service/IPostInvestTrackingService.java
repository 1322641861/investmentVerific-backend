package com.ruoyi.modules.invest.postinvest.service;

import com.ruoyi.modules.invest.postinvest.domain.PostInvestTracking;

import java.util.List;

/**
 * 投后跟踪Service接口
 *
 * @author investvf
 */
public interface IPostInvestTrackingService {

    PostInvestTracking selectPostInvestTrackingById(Long trackingId);

    List<PostInvestTracking> selectPostInvestTrackingList(PostInvestTracking tracking);

    int insertPostInvestTracking(PostInvestTracking tracking);

    int updatePostInvestTracking(PostInvestTracking tracking);

    int deletePostInvestTrackingByIds(Long[] ids);

    List<PostInvestTracking> selectByProjectId(Long projectId);
}
