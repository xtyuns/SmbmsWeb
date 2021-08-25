package com.xtyuns.servlet.role;

import com.alibaba.fastjson.JSON;
import com.xtyuns.pojo.Role;
import com.xtyuns.service.role.RoleService;
import com.xtyuns.service.role.RoleServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RoleServlet", urlPatterns = "/admin/role.do")
public class RoleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if ("getRoleList".equals(method)) {
            getRoleList(request, response);
        } else {
            request.getRequestDispatcher("/404.404").forward(request, response);
        }
    }

    private void getRoleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(roleList));
    }
}
