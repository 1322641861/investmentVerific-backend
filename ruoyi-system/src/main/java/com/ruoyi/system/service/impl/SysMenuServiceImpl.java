package com.ruoyi.system.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.service.ISysMenuService;

/**
 * 菜单 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService
{
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 根据用户查询菜单
     * 
     * @param user 用户信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenusByUser(SysUser user)
    {
        List<SysMenu> menus = new LinkedList<SysMenu>();
        // 管理员显示所有菜单信息
        if (user.isAdmin())
        {
            menus = menuMapper.selectMenuNormalAll();
        }
        else
        {
            menus = menuMapper.selectMenusByUserId(user.getUserId());
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 查询菜单集合
     * 
     * @return 所有菜单信息
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId)
    {
        List<SysMenu> menuList = null;
        if (ShiroUtils.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 查询菜单集合
     * 
     * @return 所有菜单信息
     */
    @Override
    public List<SysMenu> selectMenuAll(Long userId)
    {
        List<SysMenu> menuList = null;
        if (ShiroUtils.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuAll();
        }
        else
        {
            menuList = menuMapper.selectMenuAllByUserId(userId);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectPermsByUserId(Long userId)
    {
        List<String> perms = menuMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectPermsByRoleId(Long roleId)
    {
        List<String> perms = menuMapper.selectPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询菜单
     * 
     * @param role 角色对象
     * @return 菜单列表
     */
    @Override
    public List<Ztree> roleMenuTreeData(SysRole role, Long userId)
    {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysMenu> menuList = selectMenuAll(userId);
        if (StringUtils.isNotNull(roleId))
        {
            List<String> roleMenuList = menuMapper.selectMenuTree(roleId);
            ztrees = initZtree(menuList, roleMenuList, true);
        }
        else
        {
            ztrees = initZtree(menuList, null, true);
        }
        return ztrees;
    }

    /**
     * 查询所有菜单
     * 
     * @return 菜单列表
     */
    @Override
    public List<Ztree> menuTreeData(Long userId)
    {
        List<SysMenu> menuList = selectMenuAll(userId);
        List<Ztree> ztrees = initZtree(menuList);
        return ztrees;
    }

    /**
     * 查询系统所有权限
     * 
     * @return 权限列表
     */
    @Override
    public LinkedHashMap<String, String> selectPermsAll(Long userId)
    {
        LinkedHashMap<String, String> section = new LinkedHashMap<>();
        List<SysMenu> permissions = selectMenuAll(userId);
        if (StringUtils.isNotEmpty(permissions))
        {
            for (SysMenu menu : permissions)
            {
                section.put(menu.getUrl(), MessageFormat.format(PREMISSION_STRING, menu.getPerms()));
            }
        }
        return section;
    }

    /**
     * 对象转菜单树
     * 
     * @param menuList 菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList)
    {
        return initZtree(menuList, null, false);
    }

    /**
     * 对象转菜单树
     * 
     * @param menuList 菜单列表
     * @param roleMenuList 角色已存在菜单列表
     * @param permsFlag 是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList, List<String> roleMenuList, boolean permsFlag)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleMenuList);
        for (SysMenu menu : menuList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(menu.getMenuId());
            ztree.setpId(menu.getParentId());
            ztree.setName(transMenuName(menu, permsFlag));
            ztree.setTitle(menu.getMenuName());
            if (isCheck)
            {
                ztree.setChecked(roleMenuList.contains(menu.getMenuId() + menu.getPerms()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public String transMenuName(SysMenu menu, boolean permsFlag)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getMenuName());
        if (permsFlag)
        {
            sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
        }
        return sb.toString();
    }

    /**
     * 删除菜单管理信息
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId)
    {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 根据菜单ID查询信息
     * 
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId)
    {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 查询子菜单数量
     * 
     * @param parentId 父级菜单ID
     * @return 结果
     */
    @Override
    public int selectCountMenuByParentId(Long parentId)
    {
        return menuMapper.selectCountMenuByParentId(parentId);
    }

    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int selectCountRoleMenuByMenuId(Long menuId)
    {
        return roleMenuMapper.selectCountRoleMenuByMenuId(menuId);
    }

    /**
     * 新增保存菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu)
    {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu)
    {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 保存菜单排序
     * 
     * @param menuIds 菜单ID
     * @param orderNums 排序ID
     */
    @Override
    @Transactional
    public void updateMenuSort(String[] menuIds, String[] orderNums)
    {
        try
        {
            for (int i = 0; i < menuIds.length; i++)
            {
                SysMenu menu = new SysMenu();
                menu.setMenuId(Convert.toLong(menuIds[i]));
                menu.setOrderNum(orderNums[i]);
                menuMapper.updateMenuSort(menu);
            }
        }
        catch (Exception e)
        {
            throw new ServiceException("保存排序异常，请联系管理员");
        }
    }

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysMenu menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据父节点的ID获取所有子节点
     * 
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * 
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0;
    }

    // ==================== 前端动态路由构建（DB驱动） ====================

    /**
     * 构建前端路由所需的菜单树（DB驱动）
     * 从 sys_menu 表读取数据，构建前端 vue-router 所需的路由树格式
     *
     * @param userId 用户ID
     * @return 路由树列表
     */
    @Override
    public List<Map<String, Object>> buildRouters(Long userId)
    {
        List<SysMenu> menus;
        if (ShiroUtils.isAdmin(userId))
        {
            menus = menuMapper.selectMenuNormalAll();
        }
        else
        {
            menus = menuMapper.selectMenusByUserId(userId);
        }
        // 构建树结构（从根节点 parent_id=0 开始）
        List<SysMenu> menuTree = getChildPerms(menus, 0);
        List<Map<String, Object>> routes = new ArrayList<>();
        for (SysMenu menu : menuTree)
        {
            if (!"F".equals(menu.getMenuType()))
            {
                routes.add(menuToRoute(menu, ""));
            }
        }
        return routes;
    }

    /**
     * 将 SysMenu 转换为前端路由 Map
     *
     * @param menu 菜单对象
     * @param parentPath 父级路由路径
     * @return 路由 Map
     */
    private Map<String, Object> menuToRoute(SysMenu menu, String parentPath)
    {
        Map<String, Object> route = new LinkedHashMap<>();
        String menuType = menu.getMenuType();
        String url = menu.getUrl();

        // 放入 menuId，前端可用作菜单的唯一标识
        route.put("menuId", menu.getMenuId());
        route.put("id", menu.getMenuId());

        // ===== 处理 path =====
        // 修复：当父级目录 url='#' 时，子项的 parentPath 为空字符串，
        // 但子项并非顶层菜单（parentId != 0），如果走 resolvePath 的
        // "顶层节点"逻辑会只取 URL 第一段作为路径，导致同级菜单路径重复。
        // 此时应使用完整 URL（去除前导 /）确保每个菜单有唯一路由路径。
        String path;
        if (StringUtils.isEmpty(parentPath)
                && menu.getParentId() != null && menu.getParentId() != 0L
                && StringUtils.isNotEmpty(url) && !"#".equals(url))
        {
            path = url.startsWith("/") ? url.substring(1) : url;
        }
        else
        {
            path = resolvePath(url, parentPath, menu);
        }
        route.put("path", path);

        // ===== 处理 component =====
        if ("M".equals(menuType))
        {
            // 顶级目录用 Layout，子目录用 ParentView
            if (menu.getParentId() != null && menu.getParentId() == 0L)
            {
                route.put("component", "Layout");
            }
            else
            {
                route.put("component", "ParentView");
            }
        }
        else if ("C".equals(menuType))
        {
            // 外链处理
            if ("0".equals(menu.getIsFrame()) && StringUtils.isNotEmpty(url) && (url.startsWith("http") || url.startsWith("https")))
            {
                route.put("component", "InnerLink");
            }
            else
            {
                String component = menu.getComponent();
                if (StringUtils.isEmpty(component))
                {
                    // 兼容老数据：组件路径为空时使用 Layout 占位
                    component = "Layout";
                }
                route.put("component", component);
            }
        }

        // ===== 处理 name =====
        if (StringUtils.isNotEmpty(menu.getRouteName()))
        {
            route.put("name", menu.getRouteName());
        }
        else
        {
            // 自动生成 name：从 path 最后一段转换
            String name = path;
            if (name.startsWith("/"))
            {
                name = name.substring(1);
            }
            // 取最后一段
            int lastSlash = name.lastIndexOf('/');
            if (lastSlash >= 0)
            {
                name = name.substring(lastSlash + 1);
            }
            // 驼峰命名
            if (name.length() > 0)
            {
                name = Character.toUpperCase(name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
            }
            route.put("name", name);
        }

        // ===== meta =====
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("title", menu.getMenuName());
        if (StringUtils.isNotEmpty(menu.getIcon()))
        {
            meta.put("icon", menu.getIcon());
        }
        if ("1".equals(menu.getIsCache()))
        {
            meta.put("noCache", true);
        }
        route.put("meta", meta);

        // ===== hidden =====
        if ("1".equals(menu.getVisible()))
        {
            route.put("hidden", true);
        }

        // ===== query =====
        if (StringUtils.isNotEmpty(menu.getQuery()))
        {
            route.put("query", menu.getQuery());
        }

        // ===== 递归处理子节点 =====
        List<SysMenu> children = menu.getChildren();
        if (children != null && !children.isEmpty())
        {
            List<Map<String, Object>> childRoutes = new ArrayList<>();
            // 顶级目录的 redirect 指向第一个子节点
            String firstChildPath = null;
            for (SysMenu child : children)
            {
                if (!"F".equals(child.getMenuType()))
                {
                    Map<String, Object> childRoute = menuToRoute(child, path);
                    if (firstChildPath == null)
                    {
                        firstChildPath = (String) childRoute.get("path");
                    }
                    childRoutes.add(childRoute);
                }
            }
            if (!childRoutes.isEmpty())
            {
                route.put("children", childRoutes);
                // redirect 指向第一个子路由
                route.put("redirect", firstChildPath);
            }
            else
            {
                route.put("children", new ArrayList<>());
            }
        }
        else if ("M".equals(menuType))
        {
            // 没有子节点的目录给空 children
            route.put("children", new ArrayList<>());
        }

        return route;
    }

    /**
     * 解析路由路径
     */
    private String resolvePath(String url, String parentPath, SysMenu menu)
    {
        if (StringUtils.isEmpty(url) || "#".equals(url))
        {
            return "";
        }

        // 外链直接返回完整 URL
        if ("0".equals(menu.getIsFrame()) && (url.startsWith("http") || url.startsWith("https")))
        {
            return url;
        }

        if (StringUtils.isEmpty(parentPath))
        {
            // 顶层节点：直接返回完整 URL 作为路径，保证唯一性
            return url.startsWith("/") ? url : "/" + url;
        }
        else
        {
            // 子节点：计算相对父节点的路径
            if (url.startsWith(parentPath))
            {
                String relative = url.substring(parentPath.length());
                if (relative.startsWith("/"))
                {
                    relative = relative.substring(1);
                }
                if (StringUtils.isNotEmpty(relative))
                {
                    return relative;
                }
            }
            // 如果无法计算相对路径，取最后一段
            if (url.contains("/"))
            {
                return url.substring(url.lastIndexOf('/') + 1);
            }
            return url;
        }
    }

}
