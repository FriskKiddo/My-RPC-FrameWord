package top.ccyy.register;

import java.net.InetSocketAddress;

public interface ServiceRegistry {

    /**
     * 向注册中心注册服务
     * @param serviceName 服务名
     * @param address 注册主机地址
     */
    void registry(String serviceName, InetSocketAddress address);

}
