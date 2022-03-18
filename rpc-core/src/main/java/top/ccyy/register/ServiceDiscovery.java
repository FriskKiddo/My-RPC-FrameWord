package top.ccyy.register;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    /**
     * 根据服务名发现服务实体
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
