package com.xtyuns.servlet;

import com.alibaba.fastjson.JSON;
import com.xtyuns.pojo.User;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AjaxServlet", urlPatterns = "/ajax.do")
public class AjaxServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if ("checkUserPwd".equals(method)) {
            checkUserPwd(request, response);
        } else if ("checkUserPwdFormat".equals(method)) {
            checkUserPwdFormat(request, response);
        } else if ("ucexist".equals(method)) {  // ajax
            checkUserCode(request, response);
        } else {
            response.setContentType("application/json");
            Map<String, String> result = new HashMap<>();
            result.put("msg", "request not found");
            result.put("code", "404");
            response.getWriter().write(JSON.toJSONString(result));
        }
    }

    /**
     * 检查用户当前密码是否正确
     * @param request 包含指定参数 oldPassword 的请求
     * json.msg: loginError, pwdError, success
     */
    private void checkUserPwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        Map<String, String> result = new HashMap<>();
        String userPwd = request.getParameter("oldPassword");

        if (user == null) {
            result.put("msg", "loginError");
        } else if (!(Funcs.isUserPwd(userPwd) && userPwd.equals(user.getUserPassword()))) {
            result.put("msg", "pwdError");
        } else {
            result.put("msg", "success");
        }

        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(result));
    }

    /**
     * 检查用户新密码格式是否正确
     * @param request 包含指定参数 newPassword 的请求
     * json.msg: yes, no
     */
    private void checkUserPwdFormat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userPwd = request.getParameter("newPassword");
        Map<String, String> result = new HashMap<>();

        if (Funcs.isUserPwd(userPwd)) {
            result.put("msg", "yes");
        } else {
            result.put("msg", "no");
        }

        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(result));
    }

    private void checkUserCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s_userCode = request.getParameter("userCode");
        boolean isExist = true;
        Map<String, String> result = new HashMap<>();

        if (s_userCode != null && s_userCode.length() > 0) {
            UserService userService = new UserServiceImpl();
            isExist = userService.getUserByCode(s_userCode)!=null;
        }

        if (isExist) {
            result.put("userCode", "exist");
        } else {
            result.put("userCode", "notExist");
        }
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
