package com.xtyuns.dao.bill;

import com.xtyuns.pojo.Bill;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class BillDaoImplTest {
    BillDao billDao = null;
    public BillDaoImplTest() {
        billDao = new BillDaoImpl();
    }

    @Test
    public void getBillList() throws SQLException {
        List<Bill> billList = billDao.getBillList("", 0L, 0, 1, 5);
        System.out.println(billList);
    }

    @Test
    public void getBillById() throws SQLException {
        Bill bill = billDao.getBillById(2L);
        System.out.println(bill);
    }
}