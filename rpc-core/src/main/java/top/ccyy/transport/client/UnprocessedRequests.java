package top.ccyy.transport.client;

import top.ccyy.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存通过发送request获得的ResponseFuture对象
 * 并对future进行管理，收到response后会对future执行complete方法
 * @author FriskKiddo
 */
public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>> unprocessedResponseFutureMap
            = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        unprocessedResponseFutureMap.put(requestId, future);
    }

    public void remove(String requestId) {
        unprocessedResponseFutureMap.remove(requestId);
    }

    public void complete(RpcResponse response) {
        CompletableFuture<RpcResponse> future = unprocessedResponseFutureMap.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }
}
