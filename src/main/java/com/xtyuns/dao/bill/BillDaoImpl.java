package com.xtyuns.dao.bill;

import com.xtyuns.dao.BaseDao;
import com.xtyuns.pojo.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillDaoImpl implements BillDao {
    @Override
    public int getBillCount(String productName, Long providerId, Integer isPayment) throws SQLException {
        StringBuilder sql = new StringBuilder();
        List<Object> param = new ArrayList<>();
        sql.append("SELECT count(id) AS count FROM smbms_bill WHERE 1=1");

        if (productName != null && productName.length() != 0) {
            sql.append(" AND productName LIKE ?");
            param.add("%" + productName + "%");
        }
        if (providerId != null && providerId != 0) {
            sql.append(" AND providerId=?");
            param.add(providerId);
        }
        if (isPayment != null && (isPayment == 1 || isPayment == 2)) {
            sql.append(" AND isPayment=?");
            param.add(isPayment);
        }

        ResultSet rs = BaseDao.executeQuery(sql.toString(), param.toArray());
        int billCount = rs.next() ? rs.getInt("count") : 0;
        BaseDao.closeResultSet(rs);

        return billCount;
    }

    @Override
    public List<Bill> getBillList(String productName, Long providerId, Integer isPayment, int currentPageNo, int pageSize) throws SQLException {
        List<Bill> billList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        List<Object> param = new ArrayList<>();
        sql.append("SELECT b.*, p.proName FROM smbms_bill b, smbms_provider p WHERE b.providerId=p.id");
        if (productName != null && productName.length() > 0) {
            sql.append(" AND b.productName LIKE ?");
            param.add("%" + productName + "%");
        }
        if (providerId != null && providerId > 0) {
            sql.append(" AND b.providerId=?");
            param.add(providerId);
        }
        if (isPayment != null && (isPayment == 1 || isPayment == 2)) {
            sql.append(" AND b.isPayment=?");
            param.add(isPayment);
        }
        sql.append(" ORDER BY b.id LIMIT ?, ?");
        param.add((currentPageNo - 1) * pageSize);
        param.add(pageSize);

        ResultSet rs = BaseDao.executeQuery(sql.toString(), param.toArray());
        while (rs.next()) {
            billList.add(toBill(rs));
        }
        BaseDao.closeResultSet(rs);

        return billList;
    }

    @Override
    public Bill getBillById(Long id) throws SQLException {
        Bill bill = null;

        String sql = "SELECT b.*, p.proName FROM smbms_bill b, smbms_provider p WHERE b.providerId=p.id AND b.id=?";
        Object[] params = {id};

        ResultSet rs = BaseDao.executeQuery(sql, params);
        if (rs.next()) {
            bill = toBill(rs);
        }
        BaseDao.closeResultSet(rs);

        return bill;
    }

    @Override
    public int deleteBillById(Long id) throws SQLException {
        String sql = "DELETE FROM smbms_bill WHERE id=?";
        Object[] params = {id};

        int row = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return row;
    }

    @Override
    public int modify(Bill bill) throws SQLException {
        String sql = "UPDATE smbms_bill SET billCode=?, productName=?, productUnit=?, productCount=?, totalPrice=?, providerId=?, isPayment=?, modifyBy=?, modifyDate=? WHERE id=?";
        Object[] param = toParams(bill);
        Object[] params = Arrays.copyOf(param, param.length + 3);
        params[param.length] = bill.getModifyBy();
        params[param.length+1] = bill.getModifyDate();
        params[param.length+2] = bill.getId();


        int row = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return row;
    }

    @Override
    public int add(Bill bill) throws SQLException {
        String sql = "INSERT INTO smbms_bill(billCode, productName, productUnit, productCount, totalPrice, providerId, isPayment, createdBy, creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] param = toParams(bill);
        Object[] params = Arrays.copyOf(param, param.length + 2);
        params[param.length] = bill.getCreatedBy();
        params[param.length + 1] = bill.getCreationDate();

        int row = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return row;
    }

    private Bill toBill(ResultSet resultSet) throws SQLException {
        Bill _bill = new Bill();
        _bill.setId(resultSet.getLong("id"));
        _bill.setBillCode(resultSet.getString("billCode"));
        _bill.setProductName(resultSet.getString("productName"));
        _bill.setProductDesc(resultSet.getString("productDesc"));
        _bill.setProductUnit(resultSet.getString("productUnit"));
        _bill.setProductCount(resultSet.getInt("productCount"));
        _bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
        _bill.setIsPayment(resultSet.getInt("isPayment"));
        _bill.setCreationDate(resultSet.getDate("creationDate"));
        _bill.setProviderId(resultSet.getLong("providerId"));
        _bill.setProviderName(resultSet.getString("proName"));

        return _bill;
    }

    private Object[] toParams(Bill bill) {
        List<Object> param = new ArrayList<>();

        param.add(bill.getBillCode());
        param.add(bill.getProductName());
        param.add(bill.getProductUnit());
        param.add(bill.getProductCount());
        param.add(bill.getTotalPrice());
        param.add(bill.getProviderId());
        param.add(bill.getIsPayment());

        return param.toArray();
    }
}
