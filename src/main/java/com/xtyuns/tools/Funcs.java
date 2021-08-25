package com.xtyuns.tools;

public class Funcs {

    // 判断用户密码格式是否正确
    public static boolean isUserPwd(String pwd) {
        boolean flag = false;

        if (pwd != null && pwd.length()>5) {
            flag = true;
        }
        return flag;
    }
}
