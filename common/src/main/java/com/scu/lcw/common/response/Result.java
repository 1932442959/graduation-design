package com.scu.lcw.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result {

    @JSONField(name = "code")
    private int code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "data")
    private Object data;

    public static Result ok() {
        return new Result(200, "", "success");
    }

    public static Result data(Object result) {

        return new Result(200, "", result);
    }

    public static Result fail(RspEnum rspEnum) {
        return new Result(rspEnum.getCode(), rspEnum.getMsg(), null);
    }
}
