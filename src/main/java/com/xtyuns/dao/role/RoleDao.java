package com.xtyuns.dao.role;

import com.xtyuns.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    /**
     * 获取所有用户角色对象的列表
     * @return 返回一个包含 (id, roleName) 属性的用户角色对象列表
     * @throws SQLException sql语句执行异常
     */
    List<Role> getRoleList() throws SQLException;
}
