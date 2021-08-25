package com.xtyuns.dao.role;

import com.xtyuns.dao.BaseDao;
import com.xtyuns.pojo.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    @Override
    public List<Role> getRoleList() throws SQLException {
        List<Role> roleList = new ArrayList<>();

        String sql = "SELECT id, roleName FROM smbms_role";
        Object[] params = {};
        ResultSet rs = BaseDao.executeQuery(sql, params);
        while (rs.next()) {
            Role _role = new Role();
            _role.setId(rs.getLong("id"));
            _role.setRoleName(rs.getString("roleName"));
            roleList.add(_role);
        }
        BaseDao.closeResultSet(rs);

        return roleList;
    }
}
