package top.ccyy.Discovery;

import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.loadbalancer.LoadBalancer;
import top.ccyy.loadbalancer.RoundRobinLoadBalancer;

import java.net.InetSocketAddress;

public class NacosServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);
    private static final LoadBalancer DEFAULT_LOADBALANCER = new RoundRobinLoadBalancer();
    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery() {
        this.loadBalancer = DEFAULT_LOADBALANCER;
    }

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
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
