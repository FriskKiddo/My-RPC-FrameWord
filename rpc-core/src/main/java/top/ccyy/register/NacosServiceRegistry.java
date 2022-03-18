package top.ccyy.register;

import top.ccyy.util.NacosUtil;

import java.net.InetSocketAddress;

public class NacosServiceRegistry implements ServiceRegistry {

    @Override
    public void registry(String serviceName, InetSocketAddress address) {
        NacosUtil.registry(serviceName, address);
    }

}
