package com.scu.lcw.common.response;

public enum RspEnum {

    error_400(400, "错误请求"),

    error_401(401, "未授权"),

    error_404(404, "未找到资源"),

    error_no_user(9501, "该账户不存在"),

    error_status(9503, "该账户已被禁用"),

    error_form(9504, "请将表单其余项目填写完整"),

    error_same_user(9505, "该用户名已存在"),

    error_validate_email(9506, "邮箱验证码错误"),

    error_time_not_enough(9507, "请60s后再尝试"),

    error_validate_time_out(9508, "验证码已过期"),

    error_password(9502, "密码错误");

    private int code;
    private String msg;

    RspEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
