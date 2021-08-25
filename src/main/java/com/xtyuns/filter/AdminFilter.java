package com.xtyuns.filter;

import com.xtyuns.pojo.User;
import com.xtyuns.tools.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户访问权限过滤器
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = "/*")
public class AdminFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String servletPath = request.getServletPath();
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);

        if (servletPath.startsWith("/admin/") && user == null) {
            response.sendRedirect(request.getContextPath()+"/error.jsp");
        } else if ("/login.jsp".equals(servletPath) && user != null) {
            response.sendRedirect(request.getContextPath()+"/admin/home.jsp");
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
    }

}
