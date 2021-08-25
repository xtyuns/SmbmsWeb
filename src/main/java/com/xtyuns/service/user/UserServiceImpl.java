package com.xtyuns.service.user;

import com.xtyuns.dao.user.UserDao;
import com.xtyuns.dao.user.UserDaoImpl;
import com.xtyuns.pojo.User;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public User getUserById(Long id) {
        User user = null;

        try {
            user = userDao.getUserById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUserByCode(String userCode) {
        User user = null;

        try {
            user = userDao.getUserByCode(userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public User login(String userCode, String userPassword) {
        User user = null;

        try {
            user = userDao.getUserByCode(userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 检查用户密码是否正确
        if (user != null && !user.getUserPassword().equals(userPassword)) {
            user = null;
        }

        return user;
    }

    @Override
    public boolean changePwd(Long id, String newPassword) {
        boolean flag = false;

        try {
            flag = userDao.changeUserPwd(id, newPassword) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    @Override
    public int getUserCount(String userName, Long userRole) {
        int userCount = 0;

        try {
            userCount = userDao.getUserCount(userName, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userCount;
    }

    @Override
    public List<User> getUserList(String userName, Long userRoleId, int currentPageNo, int pageSize) {
        List<User> userList = null;

        try {
            userList = userDao.getUserList(userName, userRoleId, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userList;
    }

    @Override
    public boolean modify(User user) {
        boolean flag = false;

        try {
            flag = userDao.modify(user) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    @Override
    public boolean deleteUserById(Long id) {
        boolean flag = false;

        try {
            flag = userDao.deleteUserById(id) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    @Override
    public boolean addUser(User user) {
        boolean flag = false;

        try {
            flag = userDao.addUser(user) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }
}
