package com.xtyuns.service.bill;

import com.xtyuns.pojo.Bill;

import java.util.List;

public interface BillService {
    int getBillCount(String productName, Long providerId, Integer isPayment);

    List<Bill> getBillList(String productName, Long providerId, Integer isPayment, int currentPageNo, int pageSize);

    Bill getBillById(Long id);

    boolean deleteBillById(Long id);

    boolean modifyBill(Bill bill);

    boolean addBill(Bill bill);
}
