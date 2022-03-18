package top.ccyy.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.entity.RpcRequest;
import top.ccyy.entity.RpcResponse;
import top.ccyy.enumeration.RpcError;
import top.ccyy.provider.ServiceProvider;
import top.ccyy.provider.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 过程调用的处理类
 *
 * @author FriskKiddo
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = RequestHandler.serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeServiceMethod(rpcRequest, service);
    }

    private Object invokeServiceMethod(RpcRequest rpcRequest, Object service) {
        try {
            Class<?> clazz = service.getClass();
            Method method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            logger.info("服务：{} 成功调用方法：{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return method.invoke(service, rpcRequest.getParameters());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error(RpcError.METHOD_NOT_FOUND.getContent());
            return RpcResponse.fail();
        }
    }
}
