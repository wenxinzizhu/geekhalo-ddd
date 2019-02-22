//package com.whkj.infra.ddd.query.dto;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//@ApiModel
//@Data
//public class ResultVo<T> {
//
//    @ApiModelProperty(value = "返回状态码 0 成功，-1失败")
//    private Integer code;
//
//    @ApiModelProperty(value = "返回数据")
//    private T data;
//
//    @ApiModelProperty(value = "返回信息")
//    private String msg;
//
//    @ApiModelProperty(value = "异常信息")
//    private String errorMsg;
//
//    public static <T> ResultVo<T> success() {
//        return success(null);
//    }
//
//    public static <T> ResultVo<T> success(T data) {
//        ResultVo<T> resultVo = new ResultVo<>();
//        resultVo.setCode(0);
//        resultVo.setData(data);
//        resultVo.setMsg("SUCCESS");
//        return resultVo;
//    }
//
//    public static <T> ResultVo<T> error(String message) {
//        ResultVo<T> resultVo = new ResultVo<>();
//        resultVo.setCode(-1);
//        resultVo.setMsg(message);
//        resultVo.setErrorMsg(message);
//        return resultVo;
//    }
//
//    public static <T> ResultVo<T> error(Exception e) {
//        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw, true));
//
//        ResultVo<T> resultVo = new ResultVo<>();
//        resultVo.setCode(-1);
//        resultVo.setErrorMsg(sw.getBuffer().toString());
//        return resultVo;
//    }
//
//}
