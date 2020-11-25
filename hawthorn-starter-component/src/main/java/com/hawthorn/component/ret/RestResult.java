package com.hawthorn.component.ret;

import com.alibaba.fastjson.JSONObject;
import com.hawthorn.component.exception.BizCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Copyright: Copyright (c) 2020 andyten

 * @remark: 基础返回结果类
 * @author:andy.ten@tom.com
 * @date:2020/8/11 5:26 下午
 * @version v1.0.1
 */
@ApiModel(value = "结果返回类", description = "通用结果返回类对象")
public class RestResult
{
  @ApiModelProperty(value = "代码", name = "code")
  public int code;
  @ApiModelProperty(value = "结果状态", name = "status")
  public String status;
  @ApiModelProperty(value = "信息", name = "msg")
  private String msg;
  @ApiModelProperty(value = "数据", name = "data")
  private Object data;

  public RestResult()
  {
    this.code = 200;
    this.status = "";
    this.msg = "";
    this.data = (new JSONObject());
  }

  public RestResult setCode(RetCode retCode)
  {
    this.code = retCode.code;
    return this;
  }

  public int getCode()
  {
    return code;
  }

  public RestResult setCode(int code)
  {
    this.code = code;
    return this;
  }

  public String getStatus()
  {
    return status;
  }

  public RestResult setStatus(String status)
  {
    this.status = status;
    return this;
  }

  public String getMsg()
  {
    return msg;
  }

  public RestResult setMsg(String msg)
  {
    this.msg = msg;
    return this;
  }

  public Object getData()
  {
    return data;
  }

  public RestResult setData(Object data)
  {
    this.data = data;
    return this;
  }

  @Override
  public String toString()
  {
    return "Result->{" +
        "code=" + code +
        ", status=" + status +
        ", msg='" + msg + '\'' +
        ", data=" + data +
        '}';
  }

  private final static String SUCCESS = "success";
  private final static String OK_STATUS = "ok";
  private final static String FAIL_STATUS = "fail";

  public static RestResult success()
  {
    return new RestResult().setCode(RetCode.SUCCESS).setMsg(SUCCESS).setStatus(OK_STATUS);
  }

  public static RestResult success(Object data)
  {
    return new RestResult().setCode(RetCode.SUCCESS).setMsg(SUCCESS).setData(data).setStatus(OK_STATUS);
  }

  public static RestResult success(int code, String msg)
  {
    return new RestResult().setCode(code).setMsg(msg).setStatus(OK_STATUS);
  }

  public static RestResult fail(BizCode bizCode)
  {
    return new RestResult().setCode(bizCode.getCode()).setMsg(bizCode.getMsg()).setStatus(FAIL_STATUS);
  }

  public static RestResult fail(BizCode bizCode, Object data)
  {
    return new RestResult().setCode(bizCode.getCode()).setMsg(bizCode.getMsg()).setData(data).setStatus(FAIL_STATUS);
  }

  public static RestResult fail(String message)
  {
    return new RestResult().setCode(RetCode.FAIL).setMsg(message).setStatus(FAIL_STATUS);
  }

  public static RestResult fail(int code, String msg)
  {
    return new RestResult().setCode(code).setMsg(msg).setStatus(FAIL_STATUS);
  }

  public static RestResult fail(int code, String msg, Object data)
  {
    return new RestResult().setCode(code).setMsg(msg).setData(data).setStatus(FAIL_STATUS);
  }
}
