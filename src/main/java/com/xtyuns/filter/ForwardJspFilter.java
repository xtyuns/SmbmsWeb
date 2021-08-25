package com.xtyuns.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 禁止用户直接访问数据转发页面
 */
@WebFilter(filterName = "ForwardJspFilter", urlPatterns = "/admin/forward/*")
public class ForwardJspFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.getRequestDispatcher("/404.404").forward(req, resp);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
