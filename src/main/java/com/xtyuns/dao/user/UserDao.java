package com.xtyuns.dao.user;

import com.xtyuns.pojo.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    /**
     * 获取指定用户对象 (id)
     * @param id 指定用户的 id
     * @return 返回指定用户对象
     * @throws SQLException sql语句执行异常
     */
    User getUserById(Long id) throws SQLException;

    /**
     * 获取指定用户对象 (userCode)
     * @param userCode 指定用户的 userCode
     * @return 返回指定用户对象
     * @throws SQLException sql语句执行异常
     */
    User getUserByCode(String userCode) throws SQLException;

    /**
     * 修改指定用户的密码
     * @param id 指定用户的 id
     * @param newPassword 用户的新密码
     * @return 返回密码修改操作所影响的行数
     * @throws SQLException sql语句执行异常
     */
    int changeUserPwd(Long id, String newPassword) throws SQLException;

    /**
     * 获取所有已分配用户角色的用户数量
     * 如果用户还未被分配角色 或 用户所分配的角色已经不存在了 那么将不会统计他们
     *
     * @param userName 只统计名称中含有指定关键字的用户, 如果为空则统计所有用户
     * @param userRoleId 只统计指定角色的用户, 0则表示统计所有角色列表的用户
     * @return 返回用户数量
     * @throws SQLException sql语句执行异常
     */
    int getUserCount(String userName, Long userRoleId) throws SQLException;

    /**
     * 获取用户列表
     * @param userName 只取名称中含有指定关键字的用户, 如果为空则获取所有用户
     * @param userRoleId 只取指定角色的用户, 0则表示获取所有角色列表的用户
     * @param currentPageNo 当前页码
     * @param pageSize 分页大小
     * @return 返回指定参数的用户列表
     * @throws SQLException sql语句执行异常
     */
    List<User> getUserList(String userName, Long userRoleId, int currentPageNo, int pageSize) throws SQLException;

    int modify(User user) throws SQLException;

    int deleteUserById(Long id) throws SQLException;

    int addUser(User user) throws SQLException;
}
