package com.xtyuns.service.user;

import com.xtyuns.pojo.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    User getUserByCode(String userCode);
    User login(String userCode, String userPassword);
    boolean changePwd(Long id, String newPassword);
    int getUserCount(String userName, Long userRole);
    List<User> getUserList(String userName, Long userRoleId, int currentPageNo, int pageSize);
    boolean modify(User user);
    boolean deleteUserById(Long id);
    boolean addUser(User user);
}
