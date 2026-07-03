package com.ruoyi.modules.invest.project.mapper;

import com.ruoyi.modules.invest.project.domain.InvestProject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投资项目Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface InvestProjectMapper {

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
     * 删除投资项目
     *
     * @param projectId 投资项目ID
     * @return 结果
     */
    int deleteInvestProjectById(Long projectId);

    /**
     * 批量删除投资项目
     *
     * @param projectIds 需要删除的数据ID
     * @return 结果
     */
    int deleteInvestProjectByIds(Long[] projectIds);

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
}
