package com.xtyuns.servlet.bill;

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
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BillServlet", urlPatterns = "/admin/bill.do")
public class BillServlet extends HttpServlet {
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
        String s_billCode = request.getParameter("billCode");
        String s_productName = request.getParameter("productName");
        String s_productUnit = request.getParameter("productUnit");
        String s_productCount = request.getParameter("productCount");
        String s_totalPrice = request.getParameter("totalPrice");
        String s_providerId = request.getParameter("providerId");
        String s_isPayment = request.getParameter("isPayment");

        int productCount = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        long providerId = 0;
        int isPayment = 0;
        try {
            productCount = Integer.parseInt(s_productCount);
            totalPrice = new BigDecimal(s_totalPrice);
            providerId = Long.parseLong(s_providerId);
            isPayment = Integer.parseInt(s_isPayment);
        } catch (NumberFormatException ignored) {
            // 业务逻辑应该在Service层进行, 所以理论来说数据校验不应该在Servlet层进行
            // 应该向Service层传递String类型的参数, 而在Service层再进行校验
            // 这里先留个坑, 以后重写的时候再填吧
        }


        Bill bill = new Bill();
        bill.setBillCode(s_billCode);
        bill.setProductName(s_productName);
        bill.setProductUnit(s_productUnit);
        bill.setProductCount(productCount);
        bill.setTotalPrice(totalPrice);
        bill.setProviderId(providerId);
        bill.setIsPayment(isPayment);

        BillService billService = new BillServiceImpl();
        if ("add".equals(op)) {
            bill.setCreatedBy(admin.getId());
            bill.setCreationDate(new Date());
            if (!billService.addBill(bill)) {
                // 这里理论来说可以使用重定向的, 但是为了方便向页面传递错误信息, 所以添加/修改失败都应该使用转发(借助request传递参数)
                request.getRequestDispatcher("./billadd.jsp").forward(request, response);
                return;
            }
        } else if ("modify".equals(op)) {
            String s_billid = request.getParameter("billid");
            bill.setId(Long.parseLong(s_billid));
            bill.setModifyBy(admin.getId());
            bill.setModifyDate(new Date());
            if (!billService.modifyBill(bill)) {
                request.getRequestDispatcher("./bill.do?method=modify&billid=" + s_billid);
                return;
            }
        }

        response.sendRedirect("./bill.do?method=query");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if ("query".equals(method)) {
            queryBills(request, response);
        } else if ("view".equals(method)) {
            getBillById(request, response, "./forward/billview.jsp");
        } else if ("modify".equals(method)) {
            getBillById(request, response, "./forward/billmodify.jsp");
        } else if ("delete".equals(method)) {
            deleteBillById(request, response);
        } else {
            request.getRequestDispatcher("/404.404").forward(request, response);
        }
    }

    private void deleteBillById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s_billid = request.getParameter("billid");
        long id = 0;
        Map<String, String> result = new HashMap<>();

        try {
            id = Long.parseLong(s_billid);
        } catch (NumberFormatException ignored) {
        }

        if (id == 0) {
            result.put("delResult", "notExist");
        } else {
            BillService billService = new BillServiceImpl();
            if (billService.deleteBillById(id)) {
                result.put("delResult", "success");
            } else{
                result.put("delResult", "failed");
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(result));
    }

    private void getBillById(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        String s_billid = request.getParameter("billid");
        long id = 0;
        Bill bill = null;

        try {
            id = Long.parseLong(s_billid);
        } catch (NumberFormatException ignored) {
        }

        if (id != 0) {
            BillService billService = new BillServiceImpl();
            bill = billService.getBillById(id);
        }

        request.setAttribute("bill", bill);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void queryBills(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s_productName = request.getParameter("queryProductName");
        String s_providerId = request.getParameter("queryProviderId");
        String s_isPayment = request.getParameter("queryIsPayment");
        String s_pageIndex = request.getParameter("pageIndex");
        long providerId = 0;
        int isPayment = 0;
        int pageIndex = 1;


        try {
            providerId = Long.parseLong(s_providerId);
        } catch (NumberFormatException ignored) {
        }
        try {
            isPayment = Integer.parseInt(s_isPayment);
        } catch (NumberFormatException ignored) {
        }
        try {
            pageIndex = Integer.parseInt(s_pageIndex);
        } catch (NumberFormatException ignored) {
        }
        if (pageIndex < 1) {
            pageIndex = 1;
        }

        ProviderService providerService = new ProviderServiceImpl();
        int totalCount = providerService.getProviderCount("", "");
        List<Provider> providerList = providerService.getProviderList("", "", 1, totalCount);
        BillService billService = new BillServiceImpl();
        totalCount = billService.getBillCount(s_productName, providerId, isPayment);
        List<Bill> billList = billService.getBillList(s_productName, providerId, isPayment, pageIndex, Constants.PAGE_SIZE);
        int totalPageCount = (int) Math.ceil(totalCount * 1.0 / Constants.PAGE_SIZE);
        pageIndex = totalPageCount == 0 ? 0 : pageIndex;

        request.setAttribute("queryProductName", s_productName);
        request.setAttribute("providerList", providerList);
        request.setAttribute("queryProviderId", s_providerId);
        request.setAttribute("queryIsPayment", s_isPayment);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("billList", billList);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPageCount", totalPageCount);
        request.getRequestDispatcher("./forward/billlist.jsp").forward(request, response);
    }
}
