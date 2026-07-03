package com.ruoyi.modules.invest.project.service;

import com.ruoyi.modules.invest.project.domain.InvestProject;

import java.util.List;
import java.util.Map;

/**
 * 投资项目Service接口
 *
 * @author investvf
 */
public interface IInvestProjectService {

    /**
     * 查询投资项目
     *
     * @param projectId 投资项目ID
     * @return 投资项目
     */
    InvestProject selectInvestProjectById(Long projectId);

    /**
     * 查询投资项目列表
     *
     * @param project 投资项目
     * @return 投资项目集合
     */
    List<InvestProject> selectInvestProjectList(InvestProject project);

    /**
     * 新增投资项目
     *
     * @param project 投资项目
     * @return 结果
     */
    int insertInvestProject(InvestProject project);

    /**
     * 修改投资项目
     *
     * @param project 投资项目
     * @return 结果
     */
    int updateInvestProject(InvestProject project);

    /**
     * 批量删除投资项目
     *
     * @param projectIds 需要删除的投资项目ID
     * @return 结果
     */
    int deleteInvestProjectByIds(Long[] projectIds);

    /**
     * 删除投资项目信息
     *
     * @param projectId 投资项目ID
     * @return 结果
     */
    int deleteInvestProjectById(Long projectId);

    /**
     * 根据规划方案ID查询项目列表
     *
     * @param schemeId 规划方案ID
     * @return 投资项目集合
     */
    List<InvestProject> selectInvestProjectBySchemeId(Long schemeId);

    /**
     * 根据流程实例ID查询项目
     *
     * @param processInstanceId 流程实例ID
     * @return 投资项目
     */
    InvestProject selectProjectByProcessInstanceId(String processInstanceId);

    /**
     * 提交项目审批流程
     *
     * @param projectId 项目ID
     * @param userId 当前用户ID
     * @return 结果
     */
    boolean submitProject(Long projectId, Long userId);

    Map<String, Object> validateCompliance(Long projectId);

    Map<String, Object> transitionStatus(Long projectId, String targetStatus);
}
