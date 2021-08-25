package com.xtyuns.dao.user;

import com.xtyuns.dao.BaseDao;
import com.xtyuns.pojo.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User getUserById(Long id) throws SQLException {
        User user = null;

        ResultSet rs = null;
        String sql = "SELECT u.*,r.roleName as userRoleName FROM smbms_user u,smbms_role r WHERE u.userRole = r.id AND u.id = ?";
        Object[] param = {id};

        rs = BaseDao.executeQuery(sql, param);
        if (rs.next()) {
            user = new User();
            user.setId(rs.getLong("id"));
            user.setUserCode(rs.getString("userCode"));
            user.setUserName(rs.getString("userName"));
            user.setGender(rs.getInt("gender"));
            user.setBirthday(rs.getDate("birthday"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            user.setUserRole(rs.getLong("userRole"));
            user.setUserRoleName(rs.getString("userRoleName"));
        }
        BaseDao.closeResultSet(rs);

        return user;
    }

    @Override
    public User getUserByCode(String userCode) throws SQLException {
        User user = null;

        ResultSet rs = null;
        String sql = "SELECT * FROM smbms_user WHERE userCode = ?";
        Object[] params = {userCode};

        rs = BaseDao.executeQuery(sql, params);
        if (rs.next()) {
            user = new User();
            user.setId(rs.getLong("id"));
            user.setUserCode(rs.getString("userCode"));
            user.setUserName(rs.getString("userName"));
            user.setUserPassword(rs.getString("userPassword"));
            user.setGender(rs.getInt("gender"));
            user.setBirthday(rs.getDate("birthday"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            user.setUserRole(rs.getLong("userRole"));
            user.setCreatedBy(rs.getLong("createdBy"));
            user.setCreationDate(rs.getTimestamp("creationDate"));
            user.setModifyBy(rs.getLong("modifyBy"));
            user.setModifyDate(rs.getTimestamp("modifyDate"));
        }
        BaseDao.closeResultSet(rs);

        return user;
    }

    @Override
    public int changeUserPwd(Long id, String newPassword) throws SQLException {
        int rows = 0;

        String sql = "UPDATE smbms_user SET userPassword=? WHERE id=?";
        Object[] params = {newPassword, id};
        rows = BaseDao.executeUpdate(sql, params);

        return rows;
    }

    @Override
    public int getUserCount(String userName, Long userRoleId) throws SQLException {
        int userCount = 0;

        StringBuilder sql = new StringBuilder();
        List<Object> list = new ArrayList<>();
        Object[] params = null;
        // 使用内连接去除那些还没有分配角色的用户
        sql.append("SELECT count(1) AS count FROM smbms_user u, smbms_role r WHERE u.userRole = r.id");
        if (!"".equals(userName)) {
            sql.append(" AND u.userName LIKE ?");
            list.add("%" + userName + "%");
        }
        if (userRoleId != 0) {
            sql.append(" AND u.userRole = ?");
            list.add(userRoleId);
        }
        params = list.toArray();

        ResultSet rs = BaseDao.executeQuery(sql.toString(), params);
        userCount = rs.next() ? rs.getInt("count") : 0;
        BaseDao.closeResultSet(rs);

        return userCount;
    }

    @Override
    public List<User> getUserList(String userName, Long userRoleId, int currentPageNo, int pageSize) throws SQLException {
        List<User> userList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        List<Object> list = new ArrayList<>();
        Object[] params = null;
        sql.append("SELECT u.*, r.roleName FROM smbms_user u, smbms_role r WHERE u.userRole = r.id");
        if (!"".equals(userName)) {
            sql.append(" AND u.userName LIKE ?");
            list.add("%" + userName + "%");
        }
        if (userRoleId != 0) {
            sql.append(" AND r.id = ?");
            list.add(userRoleId);
        }
        sql.append(" ORDER BY u.id LIMIT ?, ?");
        list.add((currentPageNo - 1) * pageSize);
        list.add(pageSize);
        params = list.toArray();

        ResultSet rs = BaseDao.executeQuery(sql.toString(), params);
        while (rs.next()) {
            User _user = new User();
            _user.setId(rs.getLong("id"));
            _user.setUserCode(rs.getString("userCode"));
            _user.setUserName(rs.getString("userName"));
            _user.setGender(rs.getInt("gender"));
            _user.setBirthday(rs.getDate("birthday"));
            _user.setPhone(rs.getString("phone"));
            _user.setUserRole(rs.getLong("userRole"));
            _user.setUserRoleName(rs.getString("roleName"));
            userList.add(_user);
        }
        BaseDao.closeResultSet(rs);

        return userList;
    }

    @Override
    public int modify(User user) throws SQLException {
        int rows = 0;

        String sql = "UPDATE smbms_user SET userName=?, gender=?, birthday=?, phone=?, address=?, userRole=?, modifyBy=?, modifyDate=? WHERE id=?";
        Object[] params = {user.getUserName(), user.getGender(), user.getBirthday(), user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(), user.getModifyDate(), user.getId()};
        rows = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return rows;
    }

    @Override
    public int deleteUserById(Long id) throws SQLException {
        int rows = 0;

        String sql = "DELETE FROM smbms_user WHERE id = ?";
        Object[] params = {id};
        rows = BaseDao.executeUpdate(sql, params);
        BaseDao.closeResultSet(null);

        return rows;
    }

    @Override
    public int addUser(User user) throws SQLException {
        int rows = 0;

        String sql = "INSERT INTO smbms_user(usercode, username, userpassword, gender, birthday, phone, address, userrole, createdby, creationdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(), user.getGender(), user.getBirthday(), user.getPhone(), user.getAddress(), user.getUserRole(), user.getCreatedBy(), user.getCreationDate()};

        rows = BaseDao.executeUpdate(sql, params);

        return rows;
    }
}
