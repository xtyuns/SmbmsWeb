package com.xtyuns.servlet.user;

import com.xtyuns.pojo.User;
import com.xtyuns.service.user.UserService;
import com.xtyuns.service.user.UserServiceImpl;
import com.xtyuns.tools.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login.do")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userCode = request.getParameter("userCode");
        String pwd = request.getParameter("userPassword");
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, pwd);

        if (user == null) {
            request.setAttribute("error", "账号或密码错误");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if (user.getUserRole() != 1) {
            request.setAttribute("error", "非管理员用户禁止登录");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute(Constants.USER_SESSION, user);
            response.sendRedirect(request.getContextPath() + "/admin/home.jsp");
        }
    }
}
