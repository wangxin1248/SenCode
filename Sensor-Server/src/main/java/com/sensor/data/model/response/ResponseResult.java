package com.sensor.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 15:14.
 * @Description: 请求响应对象
 */
@Data
@ToString
@NoArgsConstructor
public class ResponseResult implements Response {

    //操作是否成功
    boolean success = SUCCESS;

    //操作代码
    int code = SUCCESS_CODE;

    //提示信息
    String message;

    public ResponseResult(ResultCode resultCode){
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(CommonCode.SUCCESS);
    }
    public static ResponseResult FAIL(){
        return new ResponseResult(CommonCode.FAIL);
    }

}
