package top.ccyy.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NacosUtil {

    private static final String SERVICE_ADDR = "127.0.0.1:8848";
    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);
    private static final NamingService namingService;
    private static final Set<String> serviceNames = new HashSet<>();
    private static InetSocketAddress address;


    static {
        try {
            namingService = NamingFactory.createNamingService(SERVICE_ADDR);
        } catch (NacosException e) {
            logger.error(RpcError.CANNOT_CONNECT_REGISTRY.getContent(), e);
            throw new RpcException(RpcError.CANNOT_CONNECT_REGISTRY);
        }
    }

    public static void registry(String serviceName, InetSocketAddress address) {
        try {
            namingService.registerInstance(serviceName, address.getHostName(),address.getPort());
            NacosUtil.address = address;
            serviceNames.add(serviceName);
        } catch (NacosException e) {
            logger.error(RpcError.REGISTER_SERVICE_FAILED.getContent());
        }
    }

    public static InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> allInstances = namingService.getAllInstances(serviceName);
            if (allInstances.size() == 0) {
                logger.error("找不到对应服务" + serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            Instance instance = allInstances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error(RpcError.SERVICE_NOT_FOUND.getContent(), e);
        }
        return null;
    }

    public static List<Instance> getAllInstances(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    public static void deregister() {
        if (!serviceNames.isEmpty() && address != null) {
            Iterator<String> it = serviceNames.iterator();
            while (it.hasNext()) {
                String serviceName = it.next();
                try {
                    namingService.deregisterInstance(serviceName, address.getHostName(), address.getPort());
                } catch (NacosException e) {
                    logger.error("注销服务 {} 出错", serviceName, e);
                }
            }
        }
    }
}
