package com.xtyuns.servlet.provider;

import com.alibaba.fastjson.JSON;
import com.xtyuns.pojo.Bill;
import com.xtyuns.pojo.Provider;
import com.xtyuns.pojo.User;
import com.xtyuns.service.bill.BillService;
import com.xtyuns.service.bill.BillServiceImpl;
import com.xtyuns.service.provider.ProviderService;
import com.xtyuns.service.provider.ProviderServiceImpl;
import com.xtyuns.tools.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProviderServlet", urlPatterns = "/admin/provider.do")
public class ProviderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if ("modifysave".equals(method)) {
            addOrModify(request, response, "modify");
        } else if ("add".equals(method)) {
            addOrModify(request, response, "add");
        } else {
            request.getRequestDispatcher("/404.404").forward(request, response);
        }
    }

    private void addOrModify(HttpServletRequest request, HttpServletResponse response, String op) throws IOException, ServletException {
        User admin = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        String s_proCode = request.getParameter("proCode");
        String s_proName = request.getParameter("proName");
        String s_proContact = request.getParameter("proContact");
        String s_proPhone = request.getParameter("proPhone");
        String s_proAddress = request.getParameter("proAddress");
        String s_proFax = request.getParameter("proFax");
        String s_proDesc = request.getParameter("proDesc");


        Provider provider = new Provider();
        provider.setProCode(s_proCode);
        provider.setProName(s_proName);
        provider.setProContact(s_proContact);
        provider.setProPhone(s_proPhone);
        provider.setProAddress(s_proAddress);
        provider.setProFax(s_proFax);
        provider.setProDesc(s_proDesc);

        ProviderService providerService = new ProviderServiceImpl();
        if ("add".equals(op)) {
            provider.setCreatedBy(admin.getId());
            provider.setCreationDate(new Date());
            if (!providerService.addProvider(provider)) {
                request.getRequestDispatcher("./provideradd.jsp").forward(request, response);
                return;
            }
        } else if ("modify".equals(op)) {
            String s_proid = request.getParameter("proid");
            provider.setId(Long.parseLong(s_proid));
            provider.setModifyBy(admin.getId());
            provider.setModifyDate(new Date());
            if (!providerService.modifyProvider(provider)) {
                request.getRequestDispatcher("./provider.do?method=modify&proid=" + s_proid);
                return;
            }
        }

        response.sendRedirect("./provider.do?method=query");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if ("query".equals(method)) {
            queryProviders(request, response);
        } else if ("view".equals(method)) {
            getProviderById(request, response, "./forward/providerview.jsp");
        } else if ("modify".equals(method)) {
            getProviderById(request, response, "./forward/providermodify.jsp");
        } else if ("delete".equals(method)) {
            deleteProviderById(request, response);
        } else if ("getProviderlist".equals(method)) {
            getProviderlist(request, response);
        } else {
            request.getRequestDispatcher("/404.404").forward(request, response);
        }
    }

    private void getProviderlist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProviderService providerService = new ProviderServiceImpl();
        int totalCount = providerService.getProviderCount("", "");
        List<Provider> providerList = providerService.getProviderList("", "", 1, totalCount);

        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(providerList));
    }

    private void deleteProviderById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String proid = request.getParameter("proid");
        boolean flag = false;
        Map<String, String> result = new HashMap<>();
        long id = 0;
        try {
            id = Long.parseLong(proid);
        } catch (NumberFormatException ignored) {
        }

        if (id == 0) {  // 是否为有效的供应商id
            result.put("delResult", "notExist");
        } else {
            BillService billService = new BillServiceImpl();
            List<Bill> billList = billService.getBillList("", id, 0, 1, 1);
            if (billList != null && billList.size() > 0) {  // 该供应商是否还存在订单关系
                result.put("delResult", "hasBills");
            } else {
                ProviderService providerService = new ProviderServiceImpl();
                flag = providerService.deleteProviderById(id);
                if (flag) {  // 供应商删除成功
                    result.put("delResult", "success");
                } else {
                    result.put("delResult", "failed");
                }
            }

        }

        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(result));
    }

    private void getProviderById(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        String proid = request.getParameter("proid");
        Provider provider = null;
        long id = 0;
        try {
            id = Long.parseLong(proid);
        } catch (NumberFormatException ignored) {
        }
        if (id != 0) {
            ProviderService providerService = new ProviderServiceImpl();
            provider = providerService.getProviderById(id);
        }
        request.setAttribute("provider", provider);

        request.getRequestDispatcher(url).forward(request, response);
    }

    private void queryProviders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s_proCode = request.getParameter("queryProCode");
        String s_proName = request.getParameter("queryProName");
        String s_pageIndex = request.getParameter("pageIndex");
        int pageIndex = 1;

        if (s_proCode == null || (s_proCode = s_proCode.trim()).length() == 0) {
            s_proCode = "";
        }
        if (s_proName == null || (s_proName = s_proName.trim()).length() == 0) {
            s_proName = "";
        }
        if (s_pageIndex != null) {
            try {
                pageIndex = Integer.parseInt(s_pageIndex);
            } catch (NumberFormatException ignored) {
            }
        }
        pageIndex = pageIndex < 1 ? 1 : pageIndex;

        ProviderService providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderList(s_proCode, s_proName, pageIndex, Constants.PAGE_SIZE);
        int totalCount = providerService.getProviderCount(s_proCode, s_proName);
        int totalPageCount = (int) Math.ceil(totalCount * 1.0 / Constants.PAGE_SIZE);
        pageIndex = totalPageCount == 0 ? 0 : pageIndex;

        request.setAttribute("queryProCode", s_proCode);
        request.setAttribute("queryProName", s_proName);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("providerList", providerList);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPageCount", totalPageCount);

        request.getRequestDispatcher("./forward/providerlist.jsp").forward(request, response);
    }
}
