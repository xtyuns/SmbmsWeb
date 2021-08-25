package com.xtyuns.service.provider;

import com.xtyuns.dao.provider.ProviderDao;
import com.xtyuns.dao.provider.ProviderDaoImpl;
import com.xtyuns.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {
    ProviderDao providerDao = null;

    public ProviderServiceImpl() {
        providerDao = new ProviderDaoImpl();
    }

    @Override
    public int getProviderCount(String proCode, String proName) {
        int providerCount = 0;

        try {
            providerCount = providerDao.getProviderCount(proCode, proName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return providerCount;
    }

    @Override
    public List<Provider> getProviderList(String proCode, String proName, int currentPageNo, int pageSize) {
        List<Provider> providerList = null;

        try {
            providerList = providerDao.getProviderList(proCode, proName, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return providerList;
    }

    @Override
    public Provider getProviderById(Long id) {
        Provider provider = null;

        try {
            provider = providerDao.getProviderById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return provider;
    }

    @Override
    public boolean deleteProviderById(Long id) {
        boolean flag = false;

        try {
            flag = providerDao.deleteProviderById(id) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    @Override
    public boolean modifyProvider(Provider provider) {
        boolean flag = false;

        try {
            flag = providerDao.modify(provider) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    @Override
    public boolean addProvider(Provider provider) {
        boolean flag = false;

        try {
            flag = providerDao.add(provider) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }
}
