package com.xtyuns.service.bill;

import com.xtyuns.dao.bill.BillDao;
import com.xtyuns.dao.bill.BillDaoImpl;
import com.xtyuns.pojo.Bill;

import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService {
    private BillDao billDao = null;

    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    @Override
    public int getBillCount(String productName, Long providerId, Integer isPayment) {
        int billCount = 0;

        try {
            billCount = billDao.getBillCount(productName, providerId, isPayment);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return billCount;
    }

    @Override
    public List<Bill> getBillList(String productName, Long providerId, Integer isPayment, int currentPageNo, int pageSize) {
        List<Bill> billList = null;

        try {
            billList = billDao.getBillList(productName, providerId, isPayment, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return billList;
    }

    @Override
    public Bill getBillById(Long id) {
        Bill bill = null;

        try {
            bill = billDao.getBillById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return bill;
    }

    @Override
    public boolean deleteBillById(Long id) {
        boolean flag = false;

        try {
            flag = billDao.deleteBillById(id) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    @Override
    public boolean modifyBill(Bill bill) {
        boolean flag = false;

        try {
            flag = billDao.modify(bill) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    @Override
    public boolean addBill(Bill bill) {
        boolean flag = false;

        try {
            flag = billDao.add(bill) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }
}
