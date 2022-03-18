package top.ccyy.Discovery;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    /**
     * 根据服务名发现服务实体
     *
     * @param serviceName 调用服务接口名
     * @return 服务实体地址
     */
    InetSocketAddress lookupService(String serviceName);
}
