package top.ccyy.transport.client;

import top.ccyy.entity.RpcRequest;
import top.ccyy.serializer.CommonSerializer;

/**
 * @author FriskKiddo
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.DEFAULT_SERIALIZER;

    /**
     * send request
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);
}
