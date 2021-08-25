package com.xtyuns.servlet.user;

import com.alibaba.fastjson.JSON;
import com.xtyuns.pojo.Role;
import com.xtyuns.pojo.User;
import com.xtyuns.service.role.RoleService;
import com.xtyuns.service.role.RoleServiceImpl;
import com.xtyuns.service.user.UserService;
import com.xtyuns.service.user.UserServiceImpl;
import com.xtyuns.tools.Constants;
import com.xtyuns.tools.Funcs;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "UserServlet", urlPatterns = "/admin/user.do")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if ("changeUserPwd".equals(method)) {
            changeUserPwd(request, response);
        } else if ("modifyexe".equals(method)) {
            modify(request, response);
        } else if ("add".equals(method)) {
            addUser(request, response);
        } else {
            request.getRequestDispatcher("/404.404").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if ("query".equals(method)) {
            queryUsers(request, response);
        } else if ("view".equals(method)) {
            getUserById(request, response, "./forward/userview.jsp");
        } else if ("modify".equals(method)) {
            getUserById(request, response, "./forward/usermodify.jsp");
        } else if ("delete".equals(method)) {
            deleteUser(request, response);
        } else {
            request.getRequestDispatcher("/404.404").forward(request, response);
            //response.sendRedirect(request.getContextPath() + "/admin/home.jsp");
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean flag = false;
        String msg = "";
        String s_userCode = request.getParameter("userCode");
        String s_userName = request.getParameter("userName");
        String s_userPassword = request.getParameter("userPassword");
        String s_ruserPassword = request.getParameter("ruserPassword");
        String s_gender = request.getParameter("gender");
        String s_birthday = request.getParameter("birthday");
        String s_phone = request.getParameter("phone");
        String s_address = request.getParameter("address");
        String s_userRole = request.getParameter("userRole");


        Date birthday = null;
        try {
            birthday = new SimpleDateFormat("yyyy-MM-dd").parse(s_birthday);
        } catch (ParseException e) {
            birthday = new Date();
        }
        if (s_phone == null) {
            s_phone = "";
        }
        if (s_address == null) {
            s_address = "";
        }
        if (s_userRole == null) {
            s_userRole = "0";
        }

        UserService userService = new UserServiceImpl();
        if (s_userCode == null || s_userCode.length() <= 0) {
            msg = "用户编码格式错误";
        } else if (userService.getUserByCode(s_userCode)!=null) {
            msg = "用户编码已存在";
        } else if (s_userPassword == null || !s_userPassword.equals(s_ruserPassword)) {
            msg = "两次输入的密码不一致";
        } else if (!"1".equals(s_gender) && !"2".equals(s_gender)) {
            msg = "性别只能为: 男/女";
        } else if ("0".equals(s_userRole)) {
            msg = "用户角色不存在";
        } else {
            User user = new User();
            user.setUserCode(s_userCode);
            user.setUserName(s_userName);
            user.setUserPassword(s_userPassword);
            user.setGender(Integer.valueOf(s_gender));
            user.setBirthday(birthday);
            user.setPhone(s_phone);
            user.setAddress(s_address);
            user.setUserRole(Long.parseLong(s_userRole));
            user.setCreationDate(new Date());
            user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());

            flag = userService.addUser(user);
        }

        if (flag) {
            response.sendRedirect("./user.do?method=query");
        } else {
            msg = msg.length() < 1 ? "用户添加失败" : msg;
            request.setAttribute(Constants.MESSAGR, msg);
            request.getRequestDispatcher("./useradd.jsp").forward(request, response);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uid = request.getParameter("uid");
        Long id = 0L;
        try {
            id = Long.parseLong(uid);
        } catch (NumberFormatException ignore) {
        }

        Map<String, String> result = new HashMap<>();
        if (id.equals(0L)) {
            result.put("delResult", "notExist");
        } else {
            UserService userService = new UserServiceImpl();
            boolean flag = userService.deleteUserById(id);
            result.put("delResult", flag ? "success" : "failed");
        }

        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(result));
    }

    private void modify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean flag = false;
        String msg = "";
        //UPDATE smbms_user SET userName=?, gender=?, birthday=?, phone=?, address=?, userRole=?, modifyBy=?, modifyDate=? WHERE id=?
        User admin = (User) request.getSession().getAttribute(Constants.USER_SESSION);

        String s_id = request.getParameter("uid");
        String s_userName = request.getParameter("userName");
        String s_gender = request.getParameter("gender");
        String s_birthday = request.getParameter("birthday");
        String s_phone = request.getParameter("phone");
        String s_address = request.getParameter("address");
        String s_userRole = request.getParameter("userRole");

        Long modifyBy = admin.getId();
        Date modifyDate = new Date();

        Long id = null;
        Integer gender = null;
        Date birthday = null;
        Long userRole = null;
        try {
            id = Long.parseLong(s_id);
            gender = Integer.valueOf(s_gender);
            birthday = new SimpleDateFormat("yyyy-MM-dd").parse(s_birthday);
            userRole = Long.parseLong(s_userRole);
        } catch (NumberFormatException | ParseException e) {
            id = 0L;
        }
        if (!id.equals(0L)) {
            if (s_userName == null || s_userName.length() < 2) {
                s_userName = "默认用户名";
            }
            if (gender !=1 && gender != 2) {
                gender = 1;
            }
            if (s_phone == null || s_phone.length() != 11) {
                s_phone = "";
            }
            if (s_address == null) {
                s_address = "";
            }

            User user = new User();
            user.setId(id);
            user.setUserName(s_userName);
            user.setGender(gender);
            user.setBirthday(birthday);
            user.setPhone(s_phone);
            user.setAddress(s_address);
            user.setUserRole(userRole);
            user.setModifyBy(modifyBy);
            user.setModifyDate(modifyDate);

            UserService userService = new UserServiceImpl();
            flag = userService.modify(user);
        }

        if (flag) {
            response.sendRedirect("./user.do?method=query");
        } else {
            msg = msg.length() < 1 ? "用户信息修改失败" : msg;
            response.sendRedirect("./user.do?method=modify&uid="+s_id+"&"+Constants.MESSAGR+"="+ URLEncoder.encode(msg));
        }
    }


    // POST 修改用户密码
    private void changeUserPwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        if (user != null) {
            String oldPwd = request.getParameter("oldPassword");
            String newPwd = request.getParameter("newPassword");
            if (!(Funcs.isUserPwd(oldPwd) && oldPwd.equals(user.getUserPassword()))) {
                // 旧密码错误
                request.setAttribute(Constants.MESSAGR, "旧密码不正确");
            } else if (!Funcs.isUserPwd(newPwd)) {
                // 新密码格式错误
                request.setAttribute(Constants.MESSAGR, "新密码格式不正确");
            } else if (newPwd.equals(oldPwd)) {
                // 新密码不能与旧密码一致
                request.setAttribute(Constants.MESSAGR, "新密码不能与旧密码一致");
            } else {
                UserService userService = new UserServiceImpl();
                if (userService.changePwd(user.getId(), newPwd)) {
                    // 修改密码成功
                    request.getSession().removeAttribute(Constants.USER_SESSION);
                    request.setAttribute(Constants.MESSAGR, "密码修改成功, 请使用新密码重新登录");
                } else {
                    // 修改密码失败
                    request.setAttribute(Constants.MESSAGR, "修改密码失败, 请刷新页面后重试");
                }
            }
        } else {
            // 登录状态异常
            request.setAttribute(Constants.MESSAGR, "修改密码失败, 请刷新页面后重试");
        }

        request.getRequestDispatcher("./pwdmodify.jsp").forward(request, response);
    }


    // GET 查询用户
    private void queryUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryname = request.getParameter("queryname");
        String userRole = request.getParameter("queryUserRole");
        String pageIndex = request.getParameter("pageIndex");
        long queryUserRoleId = 0;  // 默认查询所有角色列表
        int currentPageNo = 1;  // 默认获取第一页用户列表

        // -> 构造 queryname 参数
        if (queryname == null || (queryname = queryname.trim()).length() == 0) {
            queryname = "";
        }
        // -> 构造 queryUserRoleId 参数
        try {
            queryUserRoleId = Long.parseLong(userRole);
        } catch (NumberFormatException ignored) {
        }
        queryUserRoleId = queryUserRoleId < 0 ? 0 : queryUserRoleId;
        // -> 构造 currentPageNo 参数
        try {
            currentPageNo = Integer.parseInt(pageIndex);
        } catch (NumberFormatException ignored) {
        }
        currentPageNo = currentPageNo < 1 ? 1 : currentPageNo;
        // -> 构造 pageSize 参数
        int pageSize = Constants.PAGE_SIZE;
        // <- 获取页面所需数据 totalUserCount, userList
        UserService userService = new UserServiceImpl();
        int totalUserCount = userService.getUserCount(queryname, queryUserRoleId);
        List<User> userList = userService.getUserList(queryname, queryUserRoleId, currentPageNo, pageSize);
        // <- 获取页面所需数据 roleList
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        // <- 获取页面所需数据 totalPageCount
        int totalPageCount = (int) Math.ceil(totalUserCount * 1.0 / Constants.PAGE_SIZE);
        // <- 获取页面显示的当前页数, 避免出现以下情况: 共0条记录 1/0页
        currentPageNo = totalPageCount == 0 ? 0 : currentPageNo;

        // req<- 通过request向页面传递数据
        request.setAttribute("totalCount", totalUserCount);
        request.setAttribute("userList", userList);
        request.setAttribute("roleList", roleList);
        request.setAttribute("queryUserName", queryname);
        request.setAttribute("queryUserRole", queryUserRoleId);
        request.setAttribute("currentPageNo", currentPageNo);
        request.setAttribute("totalPageCount", totalPageCount);
        // 转发到指定页面, 进行数据渲染
        request.getRequestDispatcher("./forward/userlist.jsp").forward(request, response);
    }

    private void getUserById(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        User user = null;
        String uid = request.getParameter("uid");

        try {
            Long id = Long.parseLong(uid);
            UserService userService = new UserServiceImpl();
            user = userService.getUserById(id);
            request.setAttribute("user", user);
        } catch (Exception ignored) {
            // uid空指针异常
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

}
