package com.xtyuns.service.provider;

import com.xtyuns.pojo.Provider;

import java.util.List;

public interface ProviderService {
    int getProviderCount(String proCode, String proName);

    List<Provider> getProviderList(String proCode, String proName, int currentPageNo, int pageSize);

    Provider getProviderById(Long id);

    boolean deleteProviderById(Long id);

    boolean modifyProvider(Provider provider);

    boolean addProvider(Provider provider);
}
