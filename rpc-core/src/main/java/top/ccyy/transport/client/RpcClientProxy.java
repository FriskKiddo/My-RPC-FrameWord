package top.ccyy.transport.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.entity.RpcResponse;
import top.ccyy.util.RpcMessageChecker;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author FriskKiddo
 */
public class RpcClientProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient rpcClient;

    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        logger.info("调用方法：{}#{}", method.getDeclaringClass().getName(), method.getName());
        String requestId = UUID.randomUUID().toString();
        RpcRequest rpcRequest = new RpcRequest(requestId, method.getDeclaringClass().getCanonicalName()
                , method.getName(), args, method.getParameterTypes(), false);
        RpcResponse rpcResponse = null;
        try {
            CompletableFuture<RpcResponse> completableFuture
                    = (CompletableFuture<RpcResponse>) rpcClient.sendRequest(rpcRequest);
            rpcResponse = completableFuture.get();
        } catch (Exception e) {
            logger.error("方法调用请求发送失败", e);
            return null;
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
