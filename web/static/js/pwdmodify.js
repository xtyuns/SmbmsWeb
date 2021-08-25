var oldpassword = null;
var newpassword = null;
var rnewpassword = null;
var saveBtn = null;

$(function () {
    oldpassword = $("#oldpassword");
    newpassword = $("#newpassword");
    rnewpassword = $("#rnewpassword");
    saveBtn = $("#save");

    oldpassword.next().html("*");
    newpassword.next().html("*");
    rnewpassword.next().html("*");

    oldpassword.on("blur", function () {
        $.ajax({
            type: "POST",
            url: path + "/ajax.do",
            data: {method: "checkUserPwd", oldPassword: oldpassword.val()},
            dataType: "json",
            success: function (data) {
                if (data.msg === "success") {//旧密码正确
                    validateTip(oldpassword.next(), {"color": "green"}, imgYes, true);
                } else if (data.msg === "pwdError") {//旧密码输入不正确
                    validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 原密码输入不正确", false);
                } else if (data.msg === "loginError") {//当前用户session过期，请重新登录
                    validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 用户登录状态异常, 请刷新页面后重试", false);
                } else {
                    validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 请求出错了", false);
                }
            },
            error: function () {
                //请求出错
                validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 请求失败", false);
            }
        });
    }).on("focus", function () {
        validateTip(oldpassword.next(), {"color": "#666666"}, "* 请输入原密码", false);
    });

    newpassword.on("focus", function () {
        validateTip(newpassword.next(), {"color": "#666666"}, "* 密码长度必须是6-20位", false);
    }).on("blur", function () {
        $.ajax({
            type: "POST",
            url: path + "/ajax.do",
            data: {method: "checkUserPwdFormat", newPassword: newpassword.val()},
            dataType: "json",
            success: function (data) {
                if (data.msg === "yes") {//旧密码正确
                    validateTip(newpassword.next(), {"color": "green"}, imgYes, true);
                } else if (data.msg === "no") {
                    validateTip(newpassword.next(), {"color": "red"}, imgNo + " 密码长度必须是6-20位", false);
                } else {
                    validateTip(newpassword.next(), {"color": "red"}, imgNo + " 请求出错了", false);
                }
            },
            error: function () {
                //请求出错
                validateTip(newpassword.next(), {"color": "red"}, imgNo + " 请求失败", false);
            }
        });
    });


    rnewpassword.on("focus", function () {
        validateTip(rnewpassword.next(), {"color": "#666666"}, "* 请输入与上面一致的密码", false);
    }).on("blur", function () {
        if (rnewpassword.val() != null && newpassword.val() === rnewpassword.val()) {
            validateTip(rnewpassword.next(), {"color": "green"}, imgYes, true);
        } else {
            validateTip(rnewpassword.next(), {"color": "red"}, imgNo + " 两次密码输入不一致, 请重新输入", false);
        }
    });


    saveBtn.on("click", function () {
        oldpassword.blur();
        newpassword.blur();
        rnewpassword.blur();
        if (oldpassword.attr("validateStatus") === "true"
            && newpassword.attr("validateStatus") === "true"
            && rnewpassword.attr("validateStatus") === "true") {
            if (confirm("确定要修改密码？")) {
                $("#userForm").submit();
            }
        }
    });
});