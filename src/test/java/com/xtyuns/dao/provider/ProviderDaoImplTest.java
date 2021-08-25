package com.xtyuns.dao.provider;

import com.xtyuns.pojo.Provider;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class ProviderDaoImplTest {
    ProviderDao providerDao = null;
    public ProviderDaoImplTest() {
        providerDao = new ProviderDaoImpl();
    }

    @Test
    public void getProviderList() {
    }

    @Test
    public void getProviderById() throws SQLException {
        Provider provider = providerDao.getProviderById(1L);
        System.out.println(provider);
    }

    @Test
    public void getProviderCount() throws SQLException {
        int providerCount = providerDao.getProviderCount("", "");
        System.out.println(providerCount);
    }

    @Test
    public void test() {
        Long l = 0L;
        System.out.println(l == 0);
    }
}