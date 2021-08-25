package com.xtyuns.dao.provider;

import com.xtyuns.dao.BaseDao;
import com.xtyuns.pojo.Provider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao {
    @Override
    public int getProviderCount(String proCode, String proName) throws SQLException {
        StringBuilder sql = new StringBuilder();
        List<Object> param = new ArrayList<>();
        sql.append("SELECT count(id) AS count FROM smbms_provider WHERE 1=1");
        if (proCode != null && proCode.length() != 0) {
            sql.append(" AND proCode LIKE ?");
            param.add("%" + proCode + "%");
        }
        if (proName != null && proName.length() != 0) {
            sql.append(" AND proName LIKE ?");
            param.add("%" + proName + "%");
        }

        ResultSet rs = BaseDao.executeQuery(sql.toString(), param.toArray());
        int providerCount = rs.next() ? rs.getInt("count") : 0;
        BaseDao.closeResultSet(rs);

        return providerCount;
    }

    @Override
    public List<Provider> getProviderList(String proCode, String proName, int currentPageNo, int pageSize) throws SQLException {
        List<Provider> providerList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        List<Object> param = new ArrayList<>();
        sql.append("SELECT * FROM smbms_provider WHERE 1=1");
        if (proCode != null && proCode.length() > 0) {
            sql.append(" AND proCode LIKE ?");
            param.add("%" + proCode + "%");
        }
        if (proName != null && proName.length() > 0) {
            sql.append(" AND proName LIKE ?");
            param.add("%" + proName + "%");
        }
        sql.append(" ORDER BY `id` LIMIT ?, ?");
        param.add((currentPageNo - 1) * pageSize);
        param.add(pageSize);
        Object[] params = param.toArray();

        ResultSet rs = BaseDao.executeQuery(sql.toString(), params);
        while (rs.next()) {
            providerList.add(toProvider(rs));
        }
        BaseDao.closeResultSet(rs);

        return providerList;
    }

    @Override
    public Provider getProviderById(Long id) throws SQLException {
        Provider provider = null;
        String sql = "SELECT * FROM smbms_provider WHERE id = ?";
        Object[] params = {id};

        ResultSet rs = BaseDao.executeQuery(sql, params);
        if (rs.next()) {
            provider = toProvider(rs);
        }
        BaseDao.closeResultSet(rs);

        return provider;
    }

    @Override
    public int deleteProviderById(Long id) throws SQLException {
        String sql = "DELETE FROM smbms_provider WHERE id = ?";
        Object[] params = {id};

        int row = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return row;
    }

    @Override
    public int modify(Provider provider) throws SQLException {
        String sql = "UPDATE smbms_provider SET proCode=?, proName=?, proContact=?, proPhone=?, proAddress=?, proFax=?, proDesc=?, modifyBy=?, modifyDate=? WHERE id=?";
        Object[] param = toParams(provider);
        Object[] params = Arrays.copyOf(param, param.length + 3);
        params[param.length] = provider.getModifyBy();
        params[param.length + 1] = provider.getModifyDate();
        params[param.length + 2] = provider.getId();

        int row = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return row;
    }

    @Override
    public int add(Provider provider) throws SQLException {
        String sql = "INSERT INTO smbms_provider (proCode, proName, proContact, proPhone, proAddress, proFax, proDesc, createdBy, creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] param = toParams(provider);
        Object[] params = Arrays.copyOf(param, param.length + 2);
        params[param.length] = provider.getCreatedBy();
        params[param.length + 1] = provider.getCreationDate();

        int row = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return row;
    }

    private Provider toProvider(ResultSet resultSet) throws SQLException {
        Provider _provider = new Provider();
        _provider.setId(resultSet.getLong("id"));
        _provider.setProCode(resultSet.getString("proCode"));
        _provider.setProName(resultSet.getString("proName"));
        _provider.setProDesc(resultSet.getString("proDesc"));
        _provider.setProContact(resultSet.getString("proContact"));
        _provider.setProPhone(resultSet.getString("proPhone"));
        _provider.setProAddress(resultSet.getString("proAddress"));
        _provider.setProFax(resultSet.getString("proFax"));
        _provider.setCreationDate(resultSet.getDate("creationDate"));

        return _provider;
    }

    private Object[] toParams(Provider provider) {
        List<Object> param = new ArrayList<>();

        param.add(provider.getProCode());
        param.add(provider.getProName());
        param.add(provider.getProContact());
        param.add(provider.getProPhone());
        param.add(provider.getProAddress());
        param.add(provider.getProFax());
        param.add(provider.getProDesc());

        return param.toArray();
    }
}
