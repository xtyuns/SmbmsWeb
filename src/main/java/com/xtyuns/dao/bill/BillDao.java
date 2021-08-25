package com.xtyuns.dao.bill;

import com.xtyuns.pojo.Bill;

import java.sql.SQLException;
import java.util.List;

public interface BillDao {
    int getBillCount(String productName, Long providerId, Integer isPayment) throws SQLException;

    List<Bill> getBillList(String productName, Long providerId, Integer isPayment, int currentPageNo, int pageSize) throws SQLException;

    Bill getBillById(Long id) throws SQLException;

    int deleteBillById(Long id) throws SQLException;

    int modify(Bill bill) throws SQLException;

    int add(Bill bill) throws SQLException;
}
