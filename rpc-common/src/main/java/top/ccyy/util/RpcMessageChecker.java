package top.ccyy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.entity.RpcResponse;
import top.ccyy.enumeration.ResponseCode;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;

public class RpcMessageChecker {

    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);
    private static final String INTERFACE_NAME = "interfaceName";

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            logger.error("调用服务 {} 失败", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME +
                    ":" + rpcRequest.getInterfaceName());
        } else {
            if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
                throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME +
                        ":" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS)) {
                logger.error("调用服务失败,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
            }
        }
    }


}
