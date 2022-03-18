package top.ccyy.entity;

import lombok.Data;
import top.ccyy.enumeration.ResponseCode;

import java.io.Serializable;

@Data
public class RpcResponse<T> implements Serializable {

    /**
     * 响应对应的请求号
     */
    private String requestId;
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public static <T> RpcResponse success(T data, String requestId){
        RpcResponse<T> rpcResponse = new RpcResponse<T>();
        rpcResponse.setRequestId(requestId);
        rpcResponse.setStatusCode(ResponseCode.SUCCESS.getCode());
        rpcResponse.setMessage(ResponseCode.SUCCESS.getMessage());
        rpcResponse.setData(data);
        return rpcResponse;
    }

    public static <T> RpcResponse fail() {
        RpcResponse<T> rpcResponse = new RpcResponse<T>();
        rpcResponse.setStatusCode(ResponseCode.SUCCESS.getCode());
        rpcResponse.setMessage(ResponseCode.SUCCESS.getMessage());
        rpcResponse.setData(null);
        return rpcResponse;
    }


}
