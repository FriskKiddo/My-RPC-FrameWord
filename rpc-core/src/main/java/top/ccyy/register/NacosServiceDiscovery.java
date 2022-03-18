package top.ccyy.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import top.ccyy.loadbalancer.LoadBalancer;
import top.ccyy.loadbalancer.RoundRobinLoadBalancer;
import top.ccyy.util.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        if (loadBalancer == null) {
            this.loadBalancer = new RoundRobinLoadBalancer();
        } else {
            this.loadBalancer = loadBalancer;
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        Instance selectedInstance = loadBalancer.select(serviceName);
        return new InetSocketAddress(selectedInstance.getIp(), selectedInstance.getPort());
//        try {
//            List<Instance> allInstances = NacosUtil.getAllInstances(serviceName);
//            if (allInstances.size() == 0) {
//                logger.error("找不到对应的服务: " + serviceName);
//                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
//            }
//            Instance instance = loadBalancer.select(allInstances);
//            return new InetSocketAddress(instance.getIp(), instance.getPort());
//        } catch (NacosException e) {
//            logger.error("获取服务时有错误发生:", e);
//        }
//        return null;
    }
}
