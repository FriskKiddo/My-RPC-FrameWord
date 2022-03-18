package top.ccyy.loadbalancer;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import top.ccyy.enumeration.RpcError;
import top.ccyy.exception.RpcException;
import top.ccyy.util.NacosUtil;
import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {

    private static Integer index = 0;
    public static List<Instance> instanceList;

//    @Override
//    public Instance select(List<Instance> instanceList) {
//        synchronized (index) {
//            index++;
//            index %= instanceList.size();
//        }
//        return instanceList.get(index);
//    }

    @Override
    public Instance select(String serviceName) {
        try {
            instanceList = NacosUtil.getAllInstances(serviceName);
        } catch (NacosException e) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        if (instanceList.isEmpty()) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        synchronized (index) {
            index++;
            index %= instanceList.size();
        }
        return instanceList.get(index);
    }
}
