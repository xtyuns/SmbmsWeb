package com.xtyuns.dao.provider;

import com.xtyuns.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {
    int getProviderCount(String proCode, String proName) throws SQLException;

    List<Provider> getProviderList(String proCode, String proName, int currentPageNo, int pageSize) throws SQLException;

    Provider getProviderById(Long id) throws SQLException;

    int deleteProviderById(Long id) throws SQLException;

    int modify(Provider provider) throws SQLException;

    int add(Provider provider) throws SQLException;
}
