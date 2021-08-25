package com.xtyuns.service.role;

import com.xtyuns.dao.role.RoleDao;
import com.xtyuns.dao.role.RoleDaoImpl;
import com.xtyuns.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl() {
        this.roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        List<Role> roleList = null;

        try {
            roleList = roleDao.getRoleList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return roleList;
    }
}
