package top.ccyy.transport.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.provider.ServiceProvider;
import top.ccyy.register.ServiceRegistry;

import java.net.InetSocketAddress;

public abstract class AbstractRpcServer implements RpcServer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;
    protected ServiceProvider serviceProvider;
    protected ServiceRegistry serviceRegistry;

    //todo 根据注解注册服务
    public void scanService() {

    }

    @Override
    public abstract void start();

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.registry(serviceName, new InetSocketAddress(host, port));
    }

}
