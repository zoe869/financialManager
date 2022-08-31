package ie.tus.financialmanager.controller;

import com.wf.captcha.utils.CaptchaUtil;
import ie.tus.financialmanager.entity.Privilege;
import ie.tus.financialmanager.entity.Role;
import ie.tus.financialmanager.entity.UserInfo;
import ie.tus.financialmanager.service.UserInfoService;
import ie.tus.financialmanager.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping(value = {"/", "login.html"})
    public String toLogin(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        if(session.getAttribute(Config.CURRENT_USERNAME)==null){
            return "login";
        }else {
            try {
                response.sendRedirect("/pages/index");
            } catch (IOException e) {
                e.printStackTrace();
                return "login";
            }
            return null;
        }

    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public Result getUserInfo(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {

        if (!CaptchaUtil.ver(userInfo.getVerity(),request)){
            CaptchaUtil.clear(request);  // 清除session中的验证码
            return ResultUtil.unSuccess("Wrong captcha!");
        }

        boolean userIsExisted = userInfoService.userIsExisted(userInfo);
        System.out.println(userIsExisted + " - " + request.getHeader("token"));
        UserInfo user = getUserInfo(userInfo);
        if("client".equals(request.getHeader("token")) || !userIsExisted){
            //用户不存在
            return ResultUtil.unSuccess("user does not exist");
        }
        if (userIsExisted && !user.getPassword().equals(MD5Util.md5(userInfo.getPassword()))){
            return ResultUtil.unSuccess("Wrong username or password！");
        }else {
            //将用户信息存入session
            userInfo = setSessionUserInfo(user,request.getSession());
            //将当前用户信息存入cookie
            setCookieUser(request,response);
            return ResultUtil.success("Login successfully", userInfo);
        }
    }

    private UserInfo getUserInfo(UserInfo userInfo) {
        return userInfoService.getUserInfo(userInfo);
    }

    // 登录时将用户信息存到Cookie中
    private void setCookieUser(HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = getSessionUser(request.getSession());
        Cookie cookie = new Cookie(Config.CURRENT_USERNAME,userInfo.getUsername()+"_"+userInfo.getId());
        cookie.setMaxAge(60*60*24*7);   // 设置cookie的有效期为7天
        response.addCookie(cookie);
    }

    @RequestMapping("/getSessionUser")
    @ResponseBody
    private UserInfo getSessionUser(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(Config.CURRENT_USERNAME);
        userInfo.setPassword(null);
        return userInfo;
    }

    // 通过用户信息获取用户权限，并将其存入到session中
    private UserInfo setSessionUserInfo(UserInfo userInfo, HttpSession session) {
//        List<Privilege> privileges = privilegeService.getPrivilegeByRoleid(userInfo.getRoleid());
//        userInfo.setPrivileges(privileges);
        session.setAttribute(Config.CURRENT_USERNAME,userInfo);
        return userInfo;
    }

    // 添加用户
    @RequestMapping("/user/add")
    @ResponseBody
    public Result addUser(UserInfo userInfo){
        int result = userInfoService.add(userInfo);
        if (result>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }
    }

    // 修改用户
    @RequestMapping("/user/update")
    @ResponseBody
    public Result updateUser(UserInfo userInfo){

        UserInfo update = userInfoService.update(userInfo);
//


        if (update!=null){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/getAllRoles")
    @ResponseBody
    public Result<Role> getAllRoles(){
        List<Role> roles = userInfoService.getAllRoles();
        if (roles.size()>0){
            return ResultUtil.success(roles);
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/getRoles")
    @ResponseBody
    public Result<Role> getRoles(){
        List<Role> roles = userInfoService.getRoles();
        if (roles.size()>0){
            return ResultUtil.success(roles);
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/role/add")
    @ResponseBody
    public Result addRole(Role role){
        Set<Privilege> defaultprivilege= new HashSet<>();
        //新增角色 默认的privilege
        defaultprivilege.add(new Privilege());
        role.setPrivileges(defaultprivilege);
        int result = userInfoService.addRole(role);
        if (result>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }
    }


    @PostMapping("/users/getUsersByWhere/{pageNo}/{pageSize}")
    @ResponseBody
    public Result getUsersByWhere(UserInfo userInfo, @PathVariable int pageNo, @PathVariable int pageSize, int roleid, HttpSession session) {

        pageNo=pageNo-1;

//        if (1 == roleid){
////            userInfo.setHouseid("");
//            userInfo.setHouse(null);
//        }

        Utils.log(userInfo.toString());

        Pageable page = PageRequest.of(pageNo, pageSize);
        return ResultUtil.success(userInfoService.getUsersByWhere(userInfo,page));
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        delCookieUser(request,response);
        request.getSession().removeAttribute(Config.CURRENT_USERNAME);

        return "login";
    }

    @RequestMapping("/user/password/change")
    @ResponseBody
    public Result updateUserPassword(UserInfo userInfo,String password,String newPassword,String reNewPassword,HttpServletRequest request){
        UserInfo user=getUserInfo(userInfo);//缓存中的数据
        if(!user.getPassword().equals(MD5Util.md5(password))){
            return ResultUtil.unSuccess("wrong original password!");
        }else if(!newPassword.equals(reNewPassword)){
            return ResultUtil.unSuccess("Confirm password error！");
        }else {
            UserInfo result=userInfoService.updateUserPassword(userInfo,newPassword);
            if (result!=null){
                return ResultUtil.success();
            }else {
                return ResultUtil.unSuccess();
            }

        }
    }

    private void delCookieUser(HttpServletRequest request,HttpServletResponse response){
        UserInfo user=getSessionUser(request.getSession());
        Cookie cookie=new Cookie(Config.CURRENT_USERNAME,user.getUsername()+"_"+user.getId());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    @RequestMapping("/user/del/{id}")
    @ResponseBody
    public Result deleteUser(UserInfo userInfo){
        userInfoService.delete(userInfo);
        return ResultUtil.success();
    }


    @RequestMapping("/register.html")
    public String toRegister(HttpServletRequest request,HttpServletResponse response){
        return "register";
    }

    @RequestMapping("/register")
    @ResponseBody
    public Result register(UserInfo userInfo,HttpServletRequest request,HttpServletResponse response){
        boolean userIsExisted = userInfoService.userIsExisted(userInfo);
        System.out.println(userIsExisted);
        if(userIsExisted){
            return ResultUtil.unSuccess("Username already been used!");
        }
        userInfo.setPassword(MD5Util.md5(userInfo.getPassword()));
        int result = userInfoService.add(userInfo);
        if(result>0){
            return ResultUtil.success();
        }
        return ResultUtil.unSuccess();
    }

}
