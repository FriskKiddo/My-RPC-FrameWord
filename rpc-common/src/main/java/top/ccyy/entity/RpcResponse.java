package top.ccyy.entity;

import lombok.Data;
import top.ccyy.enumeration.ResponseCode;

import java.io.Serializable;

@Data
public class RpcResponse implements Serializable {

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
    private Object data;

    public static RpcResponse success(Object data, String requestId) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(requestId);
        rpcResponse.setStatusCode(ResponseCode.SUCCESS.getCode());
        rpcResponse.setMessage(ResponseCode.SUCCESS.getMessage());
        rpcResponse.setData(data);
        return rpcResponse;
    }

    public static RpcResponse fail() {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setStatusCode(ResponseCode.SUCCESS.getCode());
        rpcResponse.setMessage(ResponseCode.SUCCESS.getMessage());
        rpcResponse.setData(null);
        return rpcResponse;
    }


}
