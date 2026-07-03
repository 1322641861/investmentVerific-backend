package com.ruoyi.web.controller.system;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.system.service.ISysMenuService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Vue前端兼容登录接口。
 * 当前后端基于RuoYi Shiro版，前端基于RuoYi-Vue3接口风格，
 * 这里补齐 /login、/getInfo、/getRouters、/captchaImage 等JSON接口。
 *
 * @author investvf
 */
@RestController
public class VueLoginController {

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private ISysMenuService menuService;

    @Value("${shiro.user.captchaEnabled:true}")
    private boolean captchaEnabled;

    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String username = Objects.toString(body.get("username"), "");
        String password = Objects.toString(body.get("password"), "");
        String code = Objects.toString(body.get("code"), "");
        Boolean rememberMe = Boolean.valueOf(Objects.toString(body.get("rememberMe"), "false"));

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return AjaxResult.error("用户名或密码不能为空");
        }

        if (captchaEnabled) {
            Object sessionCode = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (sessionCode == null || !StringUtils.equalsIgnoreCase(code, sessionCode.toString())) {
                return AjaxResult.error("验证码错误");
            }
            request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            AjaxResult ajax = AjaxResult.success();
            // 当前Shiro版使用Session认证；前端只需要有一个Admin-Token通过路由守卫。
            ajax.put("token", request.getSession().getId());
            return ajax;
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return AjaxResult.error(msg);
        }
    }

    @PostMapping("/logout")
    public AjaxResult logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return AjaxResult.success();
    }

    @GetMapping("/unauth-json")
    public AjaxResult unauthJson() {
        AjaxResult ajax = AjaxResult.error("未登录或登录超时，请重新登录");
        ajax.put("code", 401);
        return ajax;
    }

    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        SysUser user = ShiroUtils.getSysUser();
        if (user == null) {
            return AjaxResult.error("用户未登录");
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);

        Set<String> permissions;
        if (user.isAdmin()) {
            // admin 用户拥有所有权限，与 UserRealm.doGetAuthorizationInfo() 保持一致
            permissions = Collections.singleton("*:*:*");
        } else {
            permissions = menuService.selectPermsByUserId(user.getUserId());
        }
        ajax.put("permissions", permissions == null ? Collections.emptySet() : permissions);
        ajax.put("roles", Collections.singletonList(user.isAdmin() ? "admin" : "common"));
        ajax.put("isDefaultModifyPwd", false);
        ajax.put("isPasswordExpired", false);
        ajax.put("pwdChrtype", "");
        return ajax;
    }

    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        SysUser user = ShiroUtils.getSysUser();
        if (user == null) {
            return AjaxResult.error("用户未登录");
        }
        // DB 驱动路由：从 sys_menu 表读取，按用户权限过滤
        List<Map<String, Object>> routers = menuService.buildRouters(user.getUserId());
        return AjaxResult.success(routers);
    }

    @GetMapping("/captchaImage")
    public AjaxResult captchaImage(HttpSession session) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return ajax;
        }

        try {
            String capText = captchaProducerMath.createText();
            String capStr = capText.substring(0, capText.lastIndexOf("@"));
            String code = capText.substring(capText.lastIndexOf("@") + 1);
            BufferedImage image = captchaProducerMath.createImage(capStr);
            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, code);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            ajax.put("uuid", session.getId());
            ajax.put("img", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error("生成验证码失败：" + e.getMessage());
        }
    }

}
