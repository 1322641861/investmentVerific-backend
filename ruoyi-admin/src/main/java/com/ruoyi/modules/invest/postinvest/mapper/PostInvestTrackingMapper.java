package com.ruoyi.modules.invest.postinvest.mapper;

import com.ruoyi.modules.invest.postinvest.domain.PostInvestTracking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投后跟踪Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface PostInvestTrackingMapper {

    PostInvestTracking selectPostInvestTrackingById(Long trackingId);

    List<PostInvestTracking> selectPostInvestTrackingList(PostInvestTracking tracking);

    int insertPostInvestTracking(PostInvestTracking tracking);

    int updatePostInvestTracking(PostInvestTracking tracking);

    int deletePostInvestTrackingByIds(Long[] ids);

    List<PostInvestTracking> selectByProjectId(Long projectId);
}
