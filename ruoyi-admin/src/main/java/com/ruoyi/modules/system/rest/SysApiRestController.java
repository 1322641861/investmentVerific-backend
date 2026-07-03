package com.ruoyi.modules.system.rest;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
public class SysApiRestController extends BaseController {

    @Autowired private ISysUserService userService;
    @Autowired private ISysRoleService roleService;
    @Autowired private ISysMenuService menuService;
    @Autowired private ISysDeptService deptService;
    @Autowired private ISysPostService postService;
    @Autowired private ISysDictTypeService dictTypeService;
    @Autowired private ISysDictDataService dictDataService;
    @Autowired private ISysConfigService configService;
    @Autowired private ISysNoticeService noticeService;

    // ==================== 用户管理 ====================

    @GetMapping("/user/list")
    public TableDataInfo userList(SysUser user) { startPage(); return getDataTable(userService.selectUserList(user)); }

    @GetMapping("/user/{userId}")
    public AjaxResult userGet(@PathVariable Long userId) { return success(userService.selectUserById(userId)); }

    @PostMapping("/user")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    public AjaxResult userAdd(@RequestBody SysUser user) { user.setCreateBy(ShiroUtils.getLoginName()); user.setCreateTime(DateUtils.getNowDate()); return toAjax(userService.insertUser(user)); }

    @PutMapping("/user")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public AjaxResult userEdit(@RequestBody SysUser user) { user.setUpdateBy(ShiroUtils.getLoginName()); user.setUpdateTime(DateUtils.getNowDate()); return toAjax(userService.updateUser(user)); }

    @DeleteMapping("/user/{userIds}")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    public AjaxResult userDel(@PathVariable String userIds) { return toAjax(userService.deleteUserByIds(userIds)); }

    @PutMapping("/user/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) { return toAjax(userService.resetUserPwd(user)); }

    @PutMapping("/user/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) { return toAjax(userService.updateUser(user)); }

    @GetMapping("/user/deptTree")
    public AjaxResult deptTree() { return success(deptService.selectDeptTree(new SysDept())); }

    // ==================== 角色管理 ====================

    @GetMapping("/role/list")
    public TableDataInfo roleList(SysRole role) { startPage(); return getDataTable(roleService.selectRoleList(role)); }

    @GetMapping("/role/{roleId}")
    public AjaxResult roleGet(@PathVariable Long roleId) { return success(roleService.selectRoleById(roleId)); }

    @PostMapping("/role")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    public AjaxResult roleAdd(@RequestBody SysRole role) { return toAjax(roleService.insertRole(role)); }

    @PutMapping("/role")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    public AjaxResult roleEdit(@RequestBody SysRole role) { return toAjax(roleService.updateRole(role)); }

    @DeleteMapping("/role/{roleIds}")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    public AjaxResult roleDel(@PathVariable String roleIds) { return toAjax(roleService.deleteRoleByIds(roleIds)); }

    @PutMapping("/role/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) { return toAjax(roleService.updateRole(role)); }

    @PutMapping("/role/changeStatus")
    public AjaxResult roleChangeStatus(@RequestBody SysRole role) { return toAjax(roleService.updateRole(role)); }

    @GetMapping("/role/optionselect")
    public AjaxResult roleOptionSelect() { return success(roleService.selectRoleAll()); }

    // ==================== 菜单管理 ====================

    @GetMapping("/menu/list")
    public AjaxResult menuList(SysMenu menu) { return success(menuService.selectMenuList(menu, ShiroUtils.getUserId())); }

    @GetMapping("/menu/{menuId}")
    public AjaxResult menuGet(@PathVariable Long menuId) { return success(menuService.selectMenuById(menuId)); }

    @PostMapping("/menu")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    public AjaxResult menuAdd(@RequestBody SysMenu menu) { return toAjax(menuService.insertMenu(menu)); }

    @PutMapping("/menu")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public AjaxResult menuEdit(@RequestBody SysMenu menu) { return toAjax(menuService.updateMenu(menu)); }

    @DeleteMapping("/menu/{menuId}")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    public AjaxResult menuDel(@PathVariable Long menuId) { return toAjax(menuService.deleteMenuById(menuId)); }

    @PutMapping("/menu/updateSort")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public AjaxResult menuUpdateSort(String menuIds, String orderNums) {
        String[] mids = menuIds.split(",");
        String[] orders = orderNums.split(",");
        menuService.updateMenuSort(mids, orders);
        return success();
    }

    @GetMapping("/menu/treeselect")
    public AjaxResult menuTreeSelect() { return success(menuService.selectMenuAll(ShiroUtils.getUserId())); }

    @GetMapping("/menu/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeSelect(@PathVariable Long roleId) {
        AjaxResult r = success();
        r.put("menus", menuService.selectMenuAll(ShiroUtils.getUserId()));
        r.put("checkedKeys", java.util.Collections.emptyList());
        return r;
    }

    @GetMapping("/role/deptTree/{roleId}")
    public AjaxResult roleDeptTree(@PathVariable Long roleId) {
        AjaxResult r = success();
        r.put("depts", deptService.selectDeptList(new SysDept()));
        return r;
    }

    // ==================== 部门管理 ====================

    @GetMapping("/dept/list")
    public AjaxResult deptList(SysDept dept) { return success(deptService.selectDeptList(dept)); }

    @GetMapping("/dept/{deptId}")
    public AjaxResult deptGet(@PathVariable Long deptId) { return success(deptService.selectDeptById(deptId)); }

    @PostMapping("/dept")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    public AjaxResult deptAdd(@RequestBody SysDept dept) { dept.setCreateBy(ShiroUtils.getLoginName()); dept.setCreateTime(DateUtils.getNowDate()); return toAjax(deptService.insertDept(dept)); }

    @PutMapping("/dept")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    public AjaxResult deptEdit(@RequestBody SysDept dept) { dept.setUpdateBy(ShiroUtils.getLoginName()); dept.setUpdateTime(DateUtils.getNowDate()); return toAjax(deptService.updateDept(dept)); }

    @DeleteMapping("/dept/{deptId}")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    public AjaxResult deptDel(@PathVariable Long deptId) { return toAjax(deptService.deleteDeptById(deptId)); }

    @GetMapping("/dept/treeselect")
    public AjaxResult deptTreeSelect() { return success(deptService.selectDeptList(new SysDept())); }

    // ==================== 岗位管理 ====================

    @GetMapping("/post/list")
    public TableDataInfo postList(SysPost post) { startPage(); return getDataTable(postService.selectPostList(post)); }

    @GetMapping("/post/{postId}")
    public AjaxResult postGet(@PathVariable Long postId) { return success(postService.selectPostById(postId)); }

    @PostMapping("/post")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    public AjaxResult postAdd(@RequestBody SysPost post) { return toAjax(postService.insertPost(post)); }

    @PutMapping("/post")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    public AjaxResult postEdit(@RequestBody SysPost post) { return toAjax(postService.updatePost(post)); }

    @DeleteMapping("/post/{postIds}")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    public AjaxResult postDel(@PathVariable String postIds) { return toAjax(postService.deletePostByIds(postIds)); }

    @GetMapping("/post/optionselect")
    public AjaxResult postOptionSelect() { return success(postService.selectPostAll()); }

    // ==================== 字典管理 ====================

    @GetMapping("/dict/type/list")
    public TableDataInfo dictTypeList(SysDictType dictType) { startPage(); return getDataTable(dictTypeService.selectDictTypeList(dictType)); }

    @GetMapping("/dict/type/{dictId}")
    public AjaxResult dictTypeGet(@PathVariable Long dictId) { return success(dictTypeService.selectDictTypeById(dictId)); }

    @PostMapping("/dict/type")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    public AjaxResult dictTypeAdd(@RequestBody SysDictType dictType) { return toAjax(dictTypeService.insertDictType(dictType)); }

    @PutMapping("/dict/type")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    public AjaxResult dictTypeEdit(@RequestBody SysDictType dictType) { return toAjax(dictTypeService.updateDictType(dictType)); }

    @DeleteMapping("/dict/type/{dictIds}")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    public AjaxResult dictTypeDel(@PathVariable String dictIds) { dictTypeService.deleteDictTypeByIds(dictIds); return success(); }

    @GetMapping("/dict/type/optionselect")
    public AjaxResult dictOptionSelect() { return success(dictTypeService.selectDictTypeAll()); }

    @DeleteMapping("/dict/type/refreshCache")
    public AjaxResult refreshDictCache() { dictTypeService.resetDictCache(); return success(); }

    // ==================== 字典数据（兼容前端 GET 查询） ====================

    @GetMapping("/dict/data/list")
    public TableDataInfo dictDataList(SysDictData dictData) { startPage(); return getDataTable(dictDataService.selectDictDataList(dictData)); }

    @GetMapping("/dict/data/list/{dictType}")
    public TableDataInfo dictDataListByType(@PathVariable String dictType, SysDictData dictData) { dictData.setDictType(dictType); startPage(); return getDataTable(dictDataService.selectDictDataList(dictData)); }

    @Anonymous
    @GetMapping("/dict/data/type/{dictType}")
    public AjaxResult dictDataByType(@PathVariable String dictType) { return success(dictDataService.selectDictDataByType(dictType)); }

    @GetMapping("/dict/data/{dictCode}")
    public AjaxResult dictDataGet(@PathVariable Long dictCode) { return success(dictDataService.selectDictDataById(dictCode)); }

    @PostMapping("/dict/data")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    public AjaxResult dictDataAdd(@RequestBody SysDictData dictData) { return toAjax(dictDataService.insertDictData(dictData)); }

    @PutMapping("/dict/data")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    public AjaxResult dictDataEdit(@RequestBody SysDictData dictData) { return toAjax(dictDataService.updateDictData(dictData)); }

    @DeleteMapping("/dict/data/{dictCodes}")
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    public AjaxResult dictDataDel(@PathVariable String dictCodes) { dictDataService.deleteDictDataByIds(dictCodes); return success(); }

    // ==================== 参数配置 ====================

    @GetMapping("/config/list")
    public TableDataInfo configList(SysConfig config) { startPage(); return getDataTable(configService.selectConfigList(config)); }

    @GetMapping("/config/{configId}")
    public AjaxResult configGet(@PathVariable Long configId) { return success(configService.selectConfigById(configId)); }

    @GetMapping("/config/configKey/{configKey}")
    public AjaxResult configByKey(@PathVariable String configKey) { return success(configService.selectConfigByKey(configKey)); }

    @PostMapping("/config")
    @Log(title = "参数配置", businessType = BusinessType.INSERT)
    public AjaxResult configAdd(@RequestBody SysConfig config) { return toAjax(configService.insertConfig(config)); }

    @PutMapping("/config")
    @Log(title = "参数配置", businessType = BusinessType.UPDATE)
    public AjaxResult configEdit(@RequestBody SysConfig config) { return toAjax(configService.updateConfig(config)); }

    @DeleteMapping("/config/{configIds}")
    @Log(title = "参数配置", businessType = BusinessType.DELETE)
    public AjaxResult configDel(@PathVariable String configIds) { configService.deleteConfigByIds(configIds); return success(); }

    @DeleteMapping("/config/refreshCache")
    public AjaxResult refreshConfigCache() { configService.resetConfigCache(); return success(); }

    // ==================== 通知公告 ====================

    @GetMapping("/notice/list")
    public TableDataInfo noticeList(SysNotice notice) { startPage(); return getDataTable(noticeService.selectNoticeList(notice)); }

    @GetMapping("/notice/{noticeId}")
    public AjaxResult noticeGet(@PathVariable Long noticeId) { return success(noticeService.selectNoticeById(noticeId)); }

    @PostMapping("/notice")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    public AjaxResult noticeAdd(@RequestBody SysNotice notice) { return toAjax(noticeService.insertNotice(notice)); }

    @PutMapping("/notice")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    public AjaxResult noticeEdit(@RequestBody SysNotice body) { return toAjax(noticeService.updateNotice(body)); }

    @DeleteMapping("/notice/{noticeIds}")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    public AjaxResult noticeDel(@PathVariable String noticeIds) { return toAjax(noticeService.deleteNoticeByIds(noticeIds)); }
}
